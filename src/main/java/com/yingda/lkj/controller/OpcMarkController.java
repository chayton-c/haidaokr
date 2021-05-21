package com.yingda.lkj.controller;

import com.yingda.lkj.beans.entity.backstage.line.RailwayLine;
import com.yingda.lkj.beans.entity.backstage.line.Station;
import com.yingda.lkj.beans.entity.backstage.opc.OpcMark;
import com.yingda.lkj.beans.system.Json;
import com.yingda.lkj.beans.system.JsonMessage;
import com.yingda.lkj.service.backstage.line.RailwayLineService;
import com.yingda.lkj.service.backstage.line.StationService;
import com.yingda.lkj.service.backstage.location.LocationService;
import com.yingda.lkj.service.backstage.opc.OpcMarkService;
import com.yingda.lkj.service.backstage.opc.OpcMarkTypeService;
import com.yingda.lkj.service.base.BaseService;
import com.yingda.lkj.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/backstage/opcMark")
@ResponseBody
@Controller
public class OpcMarkController extends BaseController {
    @Autowired
    private BaseService<OpcMark> opcMarkBaseService;
    @Autowired
    private OpcMarkService opcMarkService;
    @Autowired
    private OpcMarkTypeService opcMarkTypeService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private StationService stationService;
    @Autowired
    private RailwayLineService railwayLineService;

    private OpcMark pageOpcMark;

    @RequestMapping("/getByOpcId")
    public Json getByOpcId() throws Exception {
        Map<String, Object> attributes = new HashMap<>();

        String opcId = req.getParameter("opcId");

        Map<String, Object> params = new HashMap<>();
        Map<String, String> conditions = new HashMap<>();

        params.put("opcId", opcId);
        conditions.put("opcId", "=");

        List<OpcMark> opcMarks = opcMarkBaseService.getObjcetPagination(
                OpcMark.class, params, conditions, page.getCurrentPage(), page.getPageSize(), ""
        );

        Long count = opcMarkBaseService.getObjectNum(OpcMark.class, params, conditions);
        page.setDataTotal(count);

        locationService.fillLocations(opcMarks);
        attributes.put("opcMarks", opcMarks);
        attributes.put("page", page);

        return new Json(JsonMessage.SUCCESS, attributes);
    }

    @RequestMapping("/delete")
    public Json delete() {
        String id = req.getParameter("id");

        opcMarkService.delete(id);

        return new Json(JsonMessage.SUCCESS);
    }

    @RequestMapping("/saveOrUpdate")
    public Json saveOrUpdate() {
        opcMarkService.saveOrUpdate(pageOpcMark);
        return new Json(JsonMessage.SUCCESS);
    }

    @RequestMapping("/initOpcDetailPage")
    public Json initOpcDetailPage() throws Exception {
        Map<String, Object> attributes = new HashMap<>();

        String stationId = req.getParameter("stationId");
        String opcMarkTypeId = req.getParameter("opcMarkTypeId");
        String name = req.getParameter("name");
        String opcMarkId = req.getParameter("opcMarkId");

        List<RailwayLine> railwayLines = railwayLineService.getAll();
        List<Station> stations = stationService.getAll();
        attributes.put("railwayLines", railwayLines);
        attributes.put("stations", stations);

        if (StringUtils.isEmpty(stationId))
            stationId = stations.get(0).getId(); // stations为空直接报错
        attributes.put("stationId", stationId);

        String sql = """
                SELECT
                	opcMark.*,
                	opcMarkType.`name` AS typeName\s
                FROM
                	opc_mark AS opcMark
                	INNER JOIN opc AS opc ON opcMark.opc_id = opc.id
                	INNER JOIN opc_mark_type AS opcMarkType ON opcMark.opc_mark_type_id = opcMarkType.id\s
                WHERE
                	(opc.left_station_id = :stationId\s
                	OR opc.right_station_id = :stationId\s)
                """;
        Map<String, Object> params = new HashMap<>();
        params.put("stationId", stationId);

        if (StringUtils.isNotEmpty(opcMarkTypeId)) {
            sql += "AND opcMark.opc_mark_type_id = :opcMarkTypeId\n";
            params.put("opcMarkTypeId", opcMarkTypeId);
        }
        if (StringUtils.isNotEmpty(name)) {
            sql += "AND opcMark.name LIKE :name\n";
            params.put("name", "%" + name + "%");
        }
        if (StringUtils.isNotEmpty(opcMarkId)) {
            sql += "AND opcMark.id = :opcMarkId\n";
            params.put("opcMarkId", opcMarkId);
        }

        sql += "ORDER BY opcMark.update_time desc, opcMark.seq, opcMark.kilometer_mark\n";


        List<OpcMark> opcMarks = opcMarkBaseService.findSQL(sql, params, OpcMark.class, page.getCurrentPage(), page.getPageSize());
        attributes.put("opcMarks", opcMarks);

        attributes.put("opcMarkTypes", opcMarkTypeService.getAll());

        super.setObjectNum(sql, params);
        attributes.put("page", page);

        return new Json(JsonMessage.SUCCESS, attributes);
    }

    @ModelAttribute
    public void setPageOpcMark(OpcMark pageOpcMark) {
        this.pageOpcMark = pageOpcMark;
    }
}
