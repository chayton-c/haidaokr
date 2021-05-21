package com.yingda.lkj.controller.backstage;

import com.yingda.lkj.beans.entity.backstage.line.Station;
import com.yingda.lkj.beans.entity.backstage.organization.Organization;
import com.yingda.lkj.beans.pojo.line.LineSelectTreeNode;
import com.yingda.lkj.beans.system.Json;
import com.yingda.lkj.beans.system.JsonMessage;
import com.yingda.lkj.controller.BaseController;
import com.yingda.lkj.service.backstage.line.RailwayLineService;
import com.yingda.lkj.service.backstage.line.StationRailwayLineService;
import com.yingda.lkj.service.backstage.line.StationService;
import com.yingda.lkj.service.backstage.organization.OrganizationClientService;
import com.yingda.lkj.service.base.BaseService;
import com.yingda.lkj.utils.StringUtils;
import com.yingda.lkj.utils.hql.HqlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hood  2020/1/2
 */
@RequestMapping("/backstage/station")
@Controller
public class StationController extends BaseController {

    @Autowired
    private StationRailwayLineService stationRailwayLineService;
    @Autowired
    private BaseService<Station> stationBaseService;
    @Autowired
    private OrganizationClientService organizationClientService;
    @Autowired
    private RailwayLineService railwayLineService;
    @Autowired
    private StationService stationService;
    private Station pageStation;

    @ModelAttribute
    public void setPageStation(Station pageStation) {
        this.pageStation = pageStation;
    }


    @RequestMapping("")
    @ResponseBody
    public Json list() throws Exception {
        Map<String, Object> attributes = new HashMap<>();

        String nameOrCode = req.getParameter("nameOrCode");

        String sql = """
                SELECT
                	station.id,
                	station.`name`,
                	station.`code`,
                	station.kilometer_mark AS kilometerMark,
                	railwayLines.railwayLineCodes\s,
                	railwayLines.railwayLineNames\s,
                	workshop.name AS workshopName,
                	section.name AS sectionName
                FROM
                	station
                	# 查询中间表关联对应的线路
                	LEFT JOIN (
                        SELECT
                            GROUP_CONCAT( railway_line.id ) AS railwayLineIds,
                            GROUP_CONCAT( railway_line.`name` ) AS railwayLineNames,
                            GROUP_CONCAT( railway_line.`code` ) AS railwayLineCodes,
                            station_railway_line.station_id AS stationId\s
                        FROM
                            station_railway_line
                            LEFT JOIN railway_line ON railway_line.id = station_railway_line.railway_line_id\s
                        GROUP BY
                        station_railway_line.station_id\s
                	) railwayLines ON railwayLines.stationId = station.id
                	LEFT JOIN organization workshop on workshop.id = station.workshop_id
                	LEFT JOIN organization section on section.id = workshop.parent_id
                WHERE
                    1 = 1
                """;
        Map<String, Object> params = new HashMap<>();

        if (StringUtils.isNotEmpty(nameOrCode)) {
            sql += "AND (station.name like :nameOrCode or station.code like :nameOrCode)";
            params.put("nameOrCode", "%" + nameOrCode + "%");
        }

        List<Station> stations = stationBaseService.findSQL(sql, params, Station.class, page.getCurrentPage(), page.getPageSize());
        stations.forEach(station -> {
            if(station.getKilometerMark() != 0){
                String kilometerMark = String.format("%.3f", station.getKilometerMark());
                String[] kilometerMarkArray = kilometerMark.split("\\.");
                String kilometerMarkText = kilometerMarkArray[0] + "K+" + kilometerMarkArray[1];
                station.setKilometerMarkText(kilometerMarkText);
            }else {
                station.setKilometerMarkText("0K+000");
            }
        });
        attributes.put("stations", stations);

        setObjectNum(sql, params);
        attributes.put("page", page);

        return new Json(JsonMessage.SUCCESS, attributes);
    }

    @RequestMapping("/opcInfo")
    @ResponseBody
    public Json opcInfo() {
        Map<String, Object> attributes = new HashMap<>();

        String stationId = pageStation.getId();
        List<Station> stations = stationService.getAll();
        Station currentStation = null, leftStation = null, rightStation = null;
        for (int i = 0, stationsSize = stations.size(); i < stationsSize; i++) {
            Station station = stations.get(i);
            if (station.getId().equals(stationId)) {
                currentStation = station;
                if (i != 0) leftStation = stations.get(i - 1);
                if (i != stationsSize - 1) rightStation = stations.get(i + 1);
            }
        }
        if (leftStation != null)  attributes.put("leftStation", leftStation);
        if (currentStation != null)  attributes.put("station", currentStation);
        if (rightStation != null)  attributes.put("rightStation", rightStation);

        return new Json(JsonMessage.SUCCESS, attributes);
    }

    @RequestMapping("/info")
    @ResponseBody
    public Json info() throws Exception {
        String stationId = pageStation.getId();
        Station station = StringUtils.isNotEmpty(stationId) ? stationBaseService.get(Station.class, stationId) : new Station();
        if (StringUtils.isNotEmpty(stationId)) {
           String workshopId = station.getWorkshopId();
            Organization workshop = organizationClientService.getById(workshopId);
            Organization section = organizationClientService.getParent(workshop.getId());
            Organization bureau = organizationClientService.getParent(section.getId());
            station.setWorkshopId(workshop.getId());
            station.setSectionId(section.getId());
            station.setBureauId(bureau.getId());

            List<String> railwayLineIds = stationRailwayLineService.getRailwayLineIdsByStationIds(stationId);
            if (!railwayLineIds.isEmpty())
                station.setRailwayLineId(railwayLineIds.get(0));
        }

        // 备选局
        List<Organization> bereaus = organizationClientService.getBureaus();
        // 备选站段
        List<Organization> sections = organizationClientService.getSections();
        // 备选车间
        List<Organization> workshops = organizationClientService.getWorkshops();

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("station", station);
        attributes.put("bureaus", bereaus);
        attributes.put("sections", sections);
        attributes.put("workshops", workshops);

        return new Json(JsonMessage.SUCCESS, attributes);
    }

    @RequestMapping("/getStationsByRailwayLineId")
    @ResponseBody
    public Json getStationsByRailwayLineId() throws Exception {
        String railwayLineId = req.getParameter("railwayLineId");
        String name = req.getParameter("name");

        Map<String, Object> attributes = new HashMap<>();

        Map<String, Object> params = new HashMap<>();
        String sql = """
                SELECT
                	station.id,
                	station.name AS name,
                	station.code AS code,
                	station.kilometer_mark AS kilometerMark,
                	stationRailwayLine.seq AS seq,
                	workshop.name AS workshopName
                FROM
                	station_railway_line AS stationRailwayLine
                	INNER JOIN station ON stationRailwayLine.station_id = station.id
                	INNER JOIN organization AS workshop ON station.workshop_id = workshop.id
                WHERE
                    stationRailwayLine.railway_line_id = :railwayLineId
                    OR station.id = :railwayLineId
                """;
        params.put("railwayLineId", railwayLineId);
        if (StringUtils.isNotEmpty(name)) {
            sql += "AND station.name like :name\n";
            params.put("name", "%" + name + "%");
        }

        sql += "ORDER BY stationRailwayLine.seq\n";
        List<Station> stations = stationBaseService.findSQL(sql, params, Station.class, page.getCurrentPage(), page.getPageSize());
        stations.forEach(station -> {
            if(station.getKilometerMark() != 0){
                String kilometerMark = String.format("%.3f", station.getKilometerMark());
                String[] kilometerMarkArray = kilometerMark.split("\\.");
                String kilometerMarkText = kilometerMarkArray[0] + "K+" + kilometerMarkArray[1];
                station.setKilometerMarkText(kilometerMarkText);
            }else {
                station.setKilometerMarkText("0K+000");
            }
        });
        List<BigInteger> count = bigIntegerBaseService.findSQL(HqlUtils.getCountSql(sql), params);
        page.setDataTotal(count.isEmpty() ? 0 : count.get(0).longValue());

        attributes.put("stations", stations);
        attributes.put("page", page);

        return new Json(JsonMessage.SUCCESS, attributes);
    }


    @RequestMapping("/saveOrUpdate")
    @ResponseBody
    public Json saveOrUpdate() throws Exception {
        stationService.saveOrUpdate(pageStation);
        return new Json(JsonMessage.SUCCESS);
    }

    @RequestMapping("/delete")
    @ResponseBody
    public Json delete(String ids) throws Exception {
        stationService.deleteByIds(Arrays.asList(ids.split(",")));
        return new Json(JsonMessage.SUCCESS);
    }

    @RequestMapping("/linkageStationAndRailwayLine")
    @ResponseBody
    public Json linkageStationAndRailwayLine() {
        String railwayLineId = req.getParameter("railwayLineId");
        String stationId = req.getParameter("stationId");

        stationRailwayLineService.saveOrUpdate(stationId, railwayLineId);

        return new Json(JsonMessage.SUCCESS);
    }

    @RequestMapping("/removeLinkageStationAndRailwayLine")
    @ResponseBody
    public Json removeLinkageStationAndRailwayLine() {
        String railwayLineIds = req.getParameter("railwayLineId");
        String stationIds = req.getParameter("stationIds");

        stationRailwayLineService.remove(Arrays.asList(stationIds.split(",")), railwayLineIds);

        return new Json(JsonMessage.SUCCESS);
    }

    /**
     * 通过车间获取车站
     * 联系车间、工区、车站
     */
    @RequestMapping("/getStations")
    @ResponseBody
    public Json getStations(String workShopId) {
        return new Json(JsonMessage.SUCCESS, stationService.findStationsByWorkshopId(workShopId));
    }


    @RequestMapping("/initSelectTrees")
    @ResponseBody
    public Json initSelectTrees() {
        Map<String, Object> attributes = new HashMap<>();

        List<LineSelectTreeNode> lineSelectTreeNodes = railwayLineService.initLineSelectTreeNode();
        attributes.put("lineSelectTreeNodes", lineSelectTreeNodes);

        return new Json(JsonMessage.SUCCESS, attributes);
    }

    @RequestMapping("/getAllStationByRailwayLineId")
    @ResponseBody
    public Json getAllStationByRailwayLineId() throws Exception {
        String railwayLineId = req.getParameter("railwayLineId");

        String sql = """
                SELECT
                	station.id,
                	station.`name` 
                FROM
                	station
                	LEFT JOIN station_railway_line ON station.id = station_railway_line.station_id
                	LEFT JOIN railway_line ON station_railway_line.railway_line_id = railway_line.id 
                """;
        Map<String, Object> params = new HashMap<>();

        if(StringUtils.isNotEmpty(railwayLineId)){
            sql += "WHERE railway_line.id = :railwayLineId ORDER BY station_railway_line.seq";
            params.put("railwayLineId", railwayLineId);
        }

        List<Station> stations = stationBaseService.findSQL(
                sql, params, Station.class
        );

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("stations", stations);

        return new Json(JsonMessage.SUCCESS, attributes);
    }
}
