package com.yingda.lkj.controller.backstage;

import com.alibaba.fastjson.JSON;
import com.yingda.lkj.beans.entity.backstage.line.RailwayLineSection;
import com.yingda.lkj.beans.entity.backstage.location.Location;
import com.yingda.lkj.beans.entity.backstage.opc.Opc;
import com.yingda.lkj.beans.system.Json;
import com.yingda.lkj.beans.system.JsonMessage;
import com.yingda.lkj.controller.BaseController;
import com.yingda.lkj.service.backstage.line.RailwayLineSectionService;
import com.yingda.lkj.service.backstage.location.LocationService;
import com.yingda.lkj.service.backstage.opc.OpcService;
import com.yingda.lkj.service.base.BaseService;
import com.yingda.lkj.utils.StringUtils;
import com.yingda.lkj.utils.hql.HqlUtils;
import com.yingda.lkj.utils.location.LocationUtil;
import com.yingda.lkj.utils.map.GeoJsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/backstage/location")
@ResponseBody
@Controller
public class LocationController extends BaseController {

    @Autowired
    private BaseService<Location> locationBaseService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private OpcService opcService;
    @Autowired
    private RailwayLineSectionService railwayLineSectionService;

    @RequestMapping("/getByDataId")
    public Json getByDataId() throws Exception {
        Map<String, Object> attributes = new HashMap<>();
        String dataId = req.getParameter("dataId");

        Map<String, Object> params = new HashMap<>();
        Map<String, String> conditions = new HashMap<>();

        params.put("dataId", dataId);
        conditions.put("dataId", "=");

        List<Location> locations = locationBaseService.getObjcetPagination(
                Location.class, params, conditions, page.getCurrentPage(), page.getPageSize(), "order by seq"
        );
        Long count = locationBaseService.getObjectNum(Location.class, params, conditions);
        page.setDataTotal(count);

        attributes.put("locations", locations);
        attributes.put("page", page);

        return new Json(JsonMessage.SUCCESS, attributes);
    }

    @RequestMapping("delete")
    public Json delete() {
        String id = req.getParameter("id");

        locationService.delete(id);

        return new Json(JsonMessage.SUCCESS);
    }

    @RequestMapping("getDistanceFromOpcOrRailwayLine")
    public Json getDistanceFromOpcOrRailwayLine() throws Exception {
        String locationId = req.getParameter("locationId");
        String areOpc = req.getParameter("areOpc");
        String areRailwayLineSection = req.getParameter("areRailwayLineSection");
        String rawOpcIds = req.getParameter("rawOpcIds");
        String rawRailwayLineSectionIds = req.getParameter("rawRailwayLineSectionIds");

        Location location = locationService.getById(locationId);

        // 光电缆上的点到铁路线的距离
        if (Boolean.parseBoolean(areOpc)) {
            List<RailwayLineSection> railwayLineSections = railwayLineSectionService.getByIds(Arrays.asList(rawRailwayLineSectionIds.split(",")));
            locationService.fillLocations(railwayLineSections);
            double shortestDistance = LocationUtil.pointToMultiLineShortestDistance(location, railwayLineSections);
            return new Json(JsonMessage.SUCCESS, String.format("距离铁路%.5f米", shortestDistance));
        }

        // 铁路上的点到光电缆的距离
        if (Boolean.parseBoolean(areRailwayLineSection)) {
            List<Opc> opcs = opcService.getByIds(Arrays.asList(rawOpcIds.split(",")));
            locationService.fillLocations(opcs);
            double shortestDistance = LocationUtil.pointToMultiLineShortestDistance(location, opcs);
            return new Json(JsonMessage.SUCCESS, String.format("距离光电缆%.5f米", shortestDistance));
        }

        return new Json(JsonMessage.SUCCESS);
    }

    @RequestMapping("/getList")
    public Json getList() throws Exception {
        String opcIds = req.getParameter("opcIds");
        String sql = """
                SELECT
                	location.longitude,
                	location.latitude,
                	location.kilometer_mark,
                	location.altitude,
                	location.pos_type,
                	opcMark.`name` AS opcMarkName,
                	opcMarkType.`name` AS opcMarkTypeName 
                FROM
                	location
                	LEFT JOIN opc_mark AS opcMark ON location.data_id = opcMark.id
                	LEFT JOIN opc_mark_type AS opcMarkType ON opcMark.opc_mark_type_id = opcMarkType.id 
                WHERE
                	location.type =:OPC_MARK 
                """;

        Map<String, Object> params = new HashMap<>();

        if (StringUtils.isNotEmpty(opcIds)) {
            String[] opcIdArr = opcIds.split(",");
            sql += "AND opcMark.opc_id in :opcIdArr";
            params.put("OPC_MARK", Location.OPC_MARK);
            params.put("opcIdArr", opcIdArr);
        }
        sql += "ORDER BY location.seq DESC";

        List<Location> locations = locationBaseService.findSQL(
                sql, params, Location.class, page.getCurrentPage(), page.getPageSize()
        );

        List<BigInteger> count = bigIntegerBaseService.findSQL(HqlUtils.getCountSql(sql), params);
        page.setDataTotal(count);

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("locations", locations);
        attributes.put("page", page);

        return new Json(JsonMessage.SUCCESS, attributes);
    }

    /**
     * 获取光电缆 Locations，用于地图渲染
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/getOpcLineStringLocationsByOpcIds")
    public Json getOpcLineStringLocationsByOpcIds() throws Exception {
        String opcIds = req.getParameter("opcIds");

        Map<String, Object> attributes = new HashMap<>();

        String sql = """
                SELECT
                	location.*,
                	opcAttribute.opcColor 
                FROM
                	location 
                	LEFT JOIN (
                        SELECT
                            opc.id AS opcId,
                            opcType.color AS opcColor 
                        FROM 
                            opc
                            INNER JOIN opc_type AS opcType ON opc.opc_type_id = opcType.id
                    ) opcAttribute ON location.data_id = opcAttribute.opcId
                WHERE 
                	location.type = :OPC\n
                	
                """;
        Map<String, Object> params = new HashMap<>();
        params.put("OPC", Location.OPC);

        if (StringUtils.isNotEmpty(opcIds)) {
            sql += "AND location.data_id = :opcId ORDER BY location.seq";
            String[] opcIdArr = opcIds.split(",");

            for (String opcId : opcIdArr) {
                params.put("opcId", opcId);
                List<Location> locations = locationBaseService.findSQL(sql, params, Location.class);
                JSON opcLocations = GeoJsonUtil.formatFeatureCollectionLineString(locations);
                attributes.put(opcId, opcLocations);
            }
        }

        return new Json(JsonMessage.SUCCESS, attributes);
    }

    /**
     * 获取光电缆MarkLocations，用于地图渲染
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/getOpcMarkPointLocationsByOpcIds")
    public Json getOpcMarkPointLocationsByOpcIds() throws Exception {
        String opcIds = req.getParameter("opcIds");

        Map<String, Object> attributes = new HashMap<>();

        String sql = """
                SELECT
                	location.data_id,
                	location.type,
                	location.longitude,
                	location.latitude,
                	opcMark.opc_id AS opcId,
                	opcMark.remark As opcMarkremark,
                	opcMarkType.name AS opcMarkTypeName,
                	opcMarkType.icon_url AS iconUrl,
                	opcMarkType.shadow_url AS shadowUrl  
                FROM
                	location
                	LEFT JOIN opc_mark AS opcMark ON location.data_id = opcMark.id
                	LEFT JOIN opc_mark_type AS opcMarkType ON opcMark.opc_mark_type_id = opcMarkType.id
                WHERE 
                	location.type = :OPC_MARK\n
                	
                """;
        Map<String, Object> params = new HashMap<>();
        params.put("OPC_MARK", Location.OPC_MARK);

        if (StringUtils.isNotEmpty(opcIds)) {
            sql += "AND opcMark.opc_id = :opcId ORDER BY location.seq";
            String[] opcIdArr = opcIds.split(",");

            for (String opcId : opcIdArr) {
                params.put("opcId", opcId);
                List<Location> locations = locationBaseService.findSQL(sql, params, Location.class);
                JSON opcMarkLocations = GeoJsonUtil.formatFeatureCollectionPoint(locations);
                attributes.put(opcId, opcMarkLocations);
            }
        }

        return new Json(JsonMessage.SUCCESS, attributes);
    }

    /**
     * 获取完整铁路线Locations，用于地图渲染
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/getAllRailwayLineStringLocations")
    public Json getAllRailwayLineStringLocations() throws Exception {
        String wasAllLine = req.getParameter("wasAllLine");
        String stationIds = req.getParameter("stationIds");

        // wasAllLine-是否显示所有线路，0-否、1-是；
        if (StringUtils.isNotEmpty(wasAllLine) && wasAllLine.equals("0")) {
            return locationService.getRailwayLineLocationsByStationIds(stationIds);
        } else {
            Map<String, Object> attributes = new HashMap<>();
            JSON railwayLocations = locationService.getAllRailwayLineLocations();
            attributes.put("railwayLocations", railwayLocations);
            return new Json(JsonMessage.SUCCESS, attributes);
        }
    }
}
