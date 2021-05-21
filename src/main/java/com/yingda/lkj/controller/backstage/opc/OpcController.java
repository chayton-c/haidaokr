package com.yingda.lkj.controller.backstage.opc;

import com.yingda.lkj.beans.entity.backstage.line.RailwayLineSection;
import com.yingda.lkj.beans.entity.backstage.location.Location;
import com.yingda.lkj.beans.entity.backstage.opc.Opc;
import com.yingda.lkj.beans.entity.backstage.opc.OpcMark;
import com.yingda.lkj.beans.exception.CustomException;
import com.yingda.lkj.beans.pojo.opc.AppOpcJson;
import com.yingda.lkj.beans.pojo.opc.OpcStatistics;
import com.yingda.lkj.beans.system.Json;
import com.yingda.lkj.beans.system.JsonMessage;
import com.yingda.lkj.controller.BaseController;
import com.yingda.lkj.service.backstage.location.LocationService;
import com.yingda.lkj.service.backstage.opc.OpcMarkService;
import com.yingda.lkj.service.backstage.opc.OpcMarkTypeService;
import com.yingda.lkj.service.backstage.opc.OpcService;
import com.yingda.lkj.service.backstage.opc.OpcTypeService;
import com.yingda.lkj.service.base.BaseService;
import com.yingda.lkj.utils.JsonUtils;
import com.yingda.lkj.utils.StringUtils;
import com.yingda.lkj.utils.hql.HqlUtils;
import com.yingda.lkj.utils.position.CoordinateTransform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author hood  2020/12/15
 */
@RequestMapping("/backstage/opc")
@ResponseBody
@Controller
public class OpcController extends BaseController {

    @Autowired
    private BaseService<Opc> opcBaseService;
    @Autowired
    private BaseService<OpcMark> opcMarkBaseService;
    @Autowired
    private BaseService<Location> locationBaseService;
    @Autowired
    private OpcService opcService;
    @Autowired
    private OpcMarkService opcMarkService;
    @Autowired
    private OpcTypeService opcTypeService;
    @Autowired
    private OpcMarkTypeService opcMarkTypeService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private BaseService<OpcStatistics> opcStatisticsBaseService;
    @Autowired
    private BaseService<RailwayLineSection> railwayLineSectionBaseService;

    @RequestMapping("/initLocationsByStations")
    public Json initLocationsByStations() throws CustomException {
        Map<String, Object> attributes = new HashMap<>();
        String leftStationId = req.getParameter("leftStationId");
        String rightStationId = req.getParameter("rightStationId");

        String opcId = req.getParameter("opcId");
        String opcMarkTypeId = req.getParameter("opcMarkTypeId");
        String opcMarkName = req.getParameter("opcMarkName");

        List<Opc> opcs = StringUtils.isNotEmpty(leftStationId, rightStationId) ?
                opcService.getByStations(leftStationId, rightStationId) : List.of(opcService.getById(opcId));
        List<Opc> selectOpcs = opcs;
        if (opcs == null)
            throw new CustomException(JsonMessage.SUCCESS, "该区间尚未上传数据");
        if (StringUtils.isNotEmpty(opcId))
            opcs = opcs.stream().filter(x -> x.getId().equals(opcId)).collect(Collectors.toList());

        locationService.fillLocations(opcs);

        List<OpcMark> opcMarks = new ArrayList<>();
        for (Opc opc : opcs) {
            List<OpcMark> opcMarkList = opcMarkService.getByOpc(opc.getId());
            if (StringUtils.isNotEmpty(opcMarkTypeId))
                opcMarkList = opcMarkList.stream().filter(x -> x.getOpcMarkTypeId().equals(opcMarkTypeId)).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(opcMarkName))
                opcMarkList = opcMarkList.stream().filter(x -> x.getName().contains(opcMarkName)).collect(Collectors.toList());
            opcMarks.addAll(opcMarkList);
        }
        locationService.fillLocations(opcMarks);

        attributes.put("opcMarkTypes", opcMarkTypeService.getAll());
        attributes.put("opcTypes", opcTypeService.getAll());
        attributes.put("opcMarks", opcMarks);
        attributes.put("opcs", opcs);
        attributes.put("selectOpcs", selectOpcs);

        return new Json(JsonMessage.SUCCESS, attributes);
    }

    @RequestMapping("/importOpc")
    public Json importOpc(MultipartFile file) throws Exception {
        String jsonStr = new String(file.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        AppOpcJson parse = JsonUtils.parse(jsonStr, AppOpcJson.class);

        List<Opc> opcs = parse.getOpcs();
        opcs.forEach(x -> x.setAddTime(current()));
        opcBaseService.bulkInsert(opcs);
        List<OpcMark> opcMarks = parse.getOpc_marks();
        opcMarks.forEach(x -> x.setAddTime(current()));
        opcMarkBaseService.bulkInsert(opcMarks);

        List<Location> location = parse.getLocations();
        location.forEach(x -> {
            double[] doubles = CoordinateTransform.gpsToBaidu(x.getLongitude(), x.getLatitude());
            x.setLongitude(doubles[0]);
            x.setLatitude(doubles[1]);
            x.setAddTime(current());
        });
        locationBaseService.bulkInsert(location);

        return new Json(JsonMessage.SUCCESS);
    }

    @RequestMapping("/getList")
    public Json getList() throws Exception {
        String stationId = req.getParameter("stationId");
        String opcIds = req.getParameter("opcIds");

        String sql = """
                SELECT
                	opc.id,
                	opc.`name`,
                	opcType.`name` AS opcTypeName,
                	opcMark.`name` AS opcMarkName,
                	opc.left_station_id AS leftStationId,
                	opc.right_station_id AS rightStationId,
                	opc.add_time AS addTime,
                	opcMark.id AS opcMarkId 
                FROM
                	opc
                	LEFT JOIN opc_mark AS opcMark ON opc.id = opcMark.opc_id
                	LEFT JOIN opc_mark_type AS opcMarkType ON opcMark.opc_mark_type_id = opcMarkType.id
                	LEFT JOIN opc_type AS opcType ON opc.opc_type_id = opcType.id
                WHERE 
                    1 = 1\n
                """;

        Map<String, Object> params = new HashMap<>();

        if (StringUtils.isNotEmpty(stationId)) {
            sql += "AND opc.left_station_id = :stationId\n";
            params.put("stationId", stationId);
        }
        if (StringUtils.isNotEmpty(opcIds)) {
            String[] opcIdArr = opcIds.split(",");
            sql += "AND opc.id in :opcIdArr\n";
            params.put("opcIdArr", opcIdArr);
        }

        sql += "ORDER BY opc.add_time desc\n";

        List<Opc> opcs = opcBaseService.findSQL(
                sql, params, Opc.class, page.getCurrentPage(), page.getPageSize()
        );

        List<BigInteger> count = bigIntegerBaseService.findSQL(HqlUtils.getCountSql(sql), params);
        page.setDataTotal(count);

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("opcs", opcs);
        attributes.put("page", page);
        return new Json(JsonMessage.SUCCESS, attributes);
    }

    @RequestMapping("/getOpcCasecadeByStationId")
    public Json getOpcCasecadeByStationId() throws CustomException {
        checkParameters("stationId");
        String stationId = req.getParameter("stationId");

        List<Opc> opcOptions = opcService.getOpcByStationId(stationId);
        Map<String, Object> attributes = new HashMap<>();

        attributes.put("opcOptions", opcOptions);
        return new Json(JsonMessage.SUCCESS, attributes);
    }

    /**
     * 线路/光电缆径路总览统计
     */
    @RequestMapping("/getRailwayLineOrOpcStatistics")
    public Json getRailwayLineOrOpcStatistics() throws Exception {
        Map<String, Object> attributes = new HashMap<>();

        OpcStatistics opcStatistics = new OpcStatistics();
        Double opcMileage = opcBaseService.getObjectSum(Opc.class, "measureDistance", null, null);
        Double railwayLineMileage = railwayLineSectionBaseService.getObjectSum(RailwayLineSection.class, "lineLength", null, null);
        opcStatistics.setOpcMileage(opcMileage);
        opcStatistics.setRailwayLineMileage(railwayLineMileage);

        attributes.put("opcStatistics", opcStatistics);
        return new Json(JsonMessage.SUCCESS, attributes);
    }
}
