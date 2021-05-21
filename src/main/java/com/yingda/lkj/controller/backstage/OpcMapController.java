package com.yingda.lkj.controller.backstage;

import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionControlPlan;
import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionControlPlanKilometerMark;
import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionControlPlanPoint;
import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionDailyPlan;
import com.yingda.lkj.beans.entity.backstage.line.KilometerMark;
import com.yingda.lkj.beans.entity.backstage.line.RailwayLineSection;
import com.yingda.lkj.beans.entity.backstage.location.Location;
import com.yingda.lkj.beans.entity.backstage.opc.Opc;
import com.yingda.lkj.beans.entity.backstage.opc.OpcMark;
import com.yingda.lkj.beans.exception.CustomException;
import com.yingda.lkj.beans.system.Json;
import com.yingda.lkj.beans.system.JsonMessage;
import com.yingda.lkj.controller.BaseController;
import com.yingda.lkj.service.backstage.constructioncontrolplan.ConstructionControlPlanKilometerMarkService;
import com.yingda.lkj.service.backstage.constructioncontrolplan.ConstructionControlPlanPointService;
import com.yingda.lkj.service.backstage.constructioncontrolplan.ConstructionControlPlanService;
import com.yingda.lkj.service.backstage.constructioncontrolplan.ConstructionDailyPlanService;
import com.yingda.lkj.service.backstage.line.KilometerMarkService;
import com.yingda.lkj.service.backstage.line.RailwayLineSectionService;
import com.yingda.lkj.service.backstage.line.StationRailwayLineService;
import com.yingda.lkj.service.backstage.location.LocationService;
import com.yingda.lkj.service.backstage.opc.OpcMarkService;
import com.yingda.lkj.service.backstage.opc.OpcMarkTypeService;
import com.yingda.lkj.service.backstage.opc.OpcService;
import com.yingda.lkj.service.backstage.opc.OpcTypeService;
import com.yingda.lkj.service.base.BaseService;
import com.yingda.lkj.utils.StringUtils;
import com.yingda.lkj.utils.pojo.PojoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/backstage/opcMap")
public class OpcMapController extends BaseController {

    @Autowired
    private BaseService<ConstructionControlPlan> constructionControlPlanBaseService;
    @Autowired
    private ConstructionDailyPlanService constructionDailyPlanService;
    @Autowired
    private ConstructionControlPlanService constructionControlPlanService;
    @Autowired
    private OpcService opcService;
    @Autowired
    private KilometerMarkService kilometerMarkService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private StationRailwayLineService stationRailwayLineService;
    @Autowired
    private OpcMarkService opcMarkService;
    @Autowired
    private OpcMarkTypeService opcMarkTypeService;
    @Autowired
    private OpcTypeService opcTypeService;
    @Autowired
    private ConstructionControlPlanPointService constructionControlPlanPointService;
    @Autowired
    private RailwayLineSectionService railwayLineSectionService;
    @Autowired
    private ConstructionControlPlanKilometerMarkService constructionControlPlanKilometerMarkService;

    @Autowired
    private BaseService<Location> locationBaseService;

    /**
     * 加载方案地图
     */
    @RequestMapping("/initMapByConstructionControlPlanId")
    public Json initMapByConstructionControlPlanId() throws Exception {
        String stationId = req.getParameter("stationId");
        if (StringUtils.isNotEmpty(stationId))
            return initMapByStationId();

        String opcIds = req.getParameter("opcIds");
        if (StringUtils.isNotEmpty(opcIds))
            return initMapByOpcIds();


        Map<String, Object> attributes = new HashMap<>();
        String constructionControlPlanId = req.getParameter("constructionControlPlanId");
        String constructionDailyPlanId = req.getParameter("constructionDailyPlanId");

        ConstructionDailyPlan constructionDailyPlan = null;
        if (StringUtils.isNotEmpty(constructionDailyPlanId)) {
            constructionDailyPlan = constructionDailyPlanService.getById(constructionControlPlanId);
            constructionControlPlanId = constructionDailyPlan.getConstructionControlPlanId();
        }
        ConstructionControlPlan constructionControlPlan = constructionControlPlanService.getById(constructionControlPlanId);

        // 这几个list都是要给前端的
        // 完整铁路线路
        List<RailwayLineSection> rawRailwayLineSections = new ArrayList<>();
        // 方案用到的铁路线路
        List<RailwayLineSection> controlPlanRailwayLineSections = new ArrayList<>();
        // 公里标
        List<KilometerMark> resultKilometerMarks = new ArrayList<>();
        // 完整光电缆
        List<Opc> rawOpcs = new ArrayList<>();
        // 方案用到的光电缆
        List<Opc> constrolPlanOpcs = new ArrayList<>();
        // 光电缆标识
        List<OpcMark> opcMarks = new ArrayList<>();

        // 方案位置副表，每条相当于一个子方案，记录方案的线路，起始公里标，起始车站
        List<ConstructionControlPlanKilometerMark> constructionControlPlanKilometerMarks =
                constructionControlPlanKilometerMarkService.getByConstructionControlPlanId(constructionControlPlan.getId());

        // 遍历方案的位置副表
        for (ConstructionControlPlanKilometerMark constructionControlPlanKilometerMark : constructionControlPlanKilometerMarks) {
            String railwayLineId = constructionControlPlanKilometerMark.getRailwayLineId();
            // 副表关联的所有线路
            List<RailwayLineSection> subRailwayLineSections = railwayLineSectionService.getByStationIds(List.of(constructionControlPlanKilometerMark.getStartStationId(), constructionControlPlanKilometerMark.getEndStationId()));
            locationService.fillLocations(subRailwayLineSections, Location.RAILWAY_LINE);
            rawRailwayLineSections.addAll(subRailwayLineSections);

            // 筛选有用的线路：如果计划是上行，那么只拿上行线
            List<RailwayLineSection> usingRailwayLineSections = filterByStartStationIdAndEndStationId(
                    constructionControlPlanKilometerMark, subRailwayLineSections
            );
            locationService.fillLocations(usingRailwayLineSections);
            controlPlanRailwayLineSections.addAll(usingRailwayLineSections);

            // 获取公里标
            List<KilometerMark> kilometerMarks = kilometerMarkService.getByRailwayLineSections(usingRailwayLineSections);
            locationService.fillLocations(kilometerMarks, Location.KILOMETER_MARKS);
            // 根据方案的公里标截取线路，并在截取后的线路两端添加公里标
            List<KilometerMark> temporaryKilometerMarks = locationService.fillRailwayLineSectionsWithLocationsByPlan(
                    usingRailwayLineSections, constructionControlPlanKilometerMark.getStartKilometer(), constructionControlPlanKilometerMark.getEndKilometer()
            );
            kilometerMarks.addAll(temporaryKilometerMarks);
            resultKilometerMarks.addAll(kilometerMarks);

            // 副表用到的所有光电缆
            List<Opc> opcs = opcService.getLineDataByStationId(constructionControlPlanKilometerMark.getStartStationId());
            if (opcs.isEmpty()) continue;
            locationService.fillLocations(opcs, Location.OPC);
            rawOpcs.addAll(opcs);

            // 方案截取后的光缆
            List<Opc> planOpcs = PojoUtils.copyPojoList(opcs, Opc.class);

            // 查询光电缆标识
            List<OpcMark> planOpcMarks = opcMarkService.getByOpcs(planOpcs);
            locationService.fillLocations(planOpcMarks);

            // 根据光缆标识和计划截取光缆
            Map<String, List<OpcMark>> opcMarkMap = planOpcMarks.stream().collect(Collectors.groupingBy(OpcMark::getOpcId));
            for (Opc opc : planOpcs) {
                List<OpcMark> opcMarkByOpc = opcMarkMap.get(opc.getId());
                opcService.cutOpcLocationsByDistance(opc, opcMarkByOpc, constructionControlPlanKilometerMark.getStartKilometer(),
                        constructionControlPlanKilometerMark.getEndKilometer());
            }
            constrolPlanOpcs.addAll(planOpcs);
        }
        // 查询施工点
        List<ConstructionControlPlanPoint> constructionControlPlanPoints = constructionControlPlanPointService.getByPlanId(constructionControlPlan.getId());
        for (ConstructionControlPlanPoint constructionControlPlanPoint : constructionControlPlanPoints) {
            double shortestDistance = constructionControlPlanPointService.calculationShortestDistance(constructionControlPlanPoint, rawOpcs);
            constructionControlPlanPoint.setShortestDistance(shortestDistance);
        }
        locationService.fillLocations(constructionControlPlanPoints);

        attributes.put("constructionControlPlanPoints", constructionControlPlanPoints);
        attributes.put("constructionControlPlan", constructionControlPlan);
        attributes.put("opcMarkTypes", opcMarkTypeService.getAll());
        attributes.put("opcTypes", opcTypeService.getAll());
        attributes.put("opcMarks", opcMarks);
        attributes.put("rawOpcs", rawOpcs);
        attributes.put("planOpcs", constrolPlanOpcs);
        attributes.put("rawRailwayLineSections", rawRailwayLineSections);
        attributes.put("controlPlanRailwayLineSections", controlPlanRailwayLineSections);
        attributes.put("kilometerMarks", resultKilometerMarks);
        // 如果有日计划，处理根据日计划再截取一次铁路线路
        if (constructionDailyPlan != null) {
            attributes.put("constructionDailyPlan", constructionDailyPlan);

            // 日计划用到的铁路线路
            List<RailwayLineSection> dailyPlanRailwayLineSections = new ArrayList<>();
            for (RailwayLineSection controlPlanRailwayLineSection : controlPlanRailwayLineSections) {
                String railwayLineId = controlPlanRailwayLineSection.getRailwayLineId();
                if (!constructionDailyPlan.getRailwayLineId().equals(railwayLineId))
                    continue;

                List<RailwayLineSection> temporaryRailwayLineSections = PojoUtils.copyPojoList(List.of(controlPlanRailwayLineSection), RailwayLineSection.class);
                for (RailwayLineSection railwayLineSection : temporaryRailwayLineSections)
                    // 手动深拷贝，目的是使temporaryRailwayLineSections中的location与controlPlanRailwayLineSections中的location指向不同地址
                    railwayLineSection.setLocations(PojoUtils.copyPojoList(railwayLineSection.getLocations(), Location.class));

                // 根据日计划的公里标截取线路，并在截取后的线路两端添加公里标
                List<KilometerMark> temporaryKilometerMarks = locationService.fillRailwayLineSectionsWithLocationsByPlan(
                        temporaryRailwayLineSections, constructionDailyPlan.getStartKilometer(), constructionDailyPlan.getEndKilometer()
                );
                dailyPlanRailwayLineSections.addAll(temporaryRailwayLineSections);
                resultKilometerMarks.addAll(temporaryKilometerMarks);
            }
            attributes.put("dailyPlanRailwayLineSections", dailyPlanRailwayLineSections);
        }
        return new Json(JsonMessage.SUCCESS, attributes);
    }

    /**
     * 加载车站地图
     */
    @RequestMapping("/initMapByStationId")
    public Json initMapByStationId() throws Exception {
        Map<String, Object> attributes = new HashMap<>();

        String stationId = req.getParameter("stationId");

        // 完整铁路线路
        List<RailwayLineSection> rawRailwayLineSections = railwayLineSectionService.getByStationId(stationId);
        locationService.fillLocations(rawRailwayLineSections);

        // 查询公里标
        List<KilometerMark> kilometerMarks = kilometerMarkService.getByRailwayLineSections(rawRailwayLineSections);
        locationService.fillLocations(kilometerMarks);

        // 完整光电缆
        List<Opc> rawOpcs = opcService.getLineDataByStationId(stationId);

        if (rawOpcs == null)
            throw new CustomException(JsonMessage.SUCCESS, "该区间尚未上传数据");
        locationService.fillLocations(rawOpcs);

        // 查询光电缆标识
        List<OpcMark> opcMarks = opcMarkService.getByOpcs(rawOpcs);
        locationService.fillLocations(opcMarks);


        attributes.put("opcMarkTypes", opcMarkTypeService.getAll());
        attributes.put("opcTypes", opcTypeService.getAll());
        attributes.put("opcMarks", opcMarks);
        attributes.put("rawOpcs", rawOpcs);
        attributes.put("rawRailwayLineSections", rawRailwayLineSections);
        attributes.put("kilometerMarks", kilometerMarks);

        return new Json(JsonMessage.SUCCESS, attributes);
    }

    /**
     * 加载车站地图
     */
    @RequestMapping("/initMapByOpcIds")
    public Json initMapByOpcIds() throws Exception {
        Map<String, Object> attributes = new HashMap<>();

        String opcIds = req.getParameter("opcIds");
        System.out.println(opcIds);

        // 完整铁路线路
        List<RailwayLineSection> resultRailwayLineSections = new ArrayList<>();
        // 查询公里标
        List<KilometerMark> resultKilometerMarks = new ArrayList<>();
        // 完整光电缆
        List<Opc> resultOpcs = new ArrayList<>();
        // 查询光电缆标识
        List<OpcMark> resultOpcMarks = new ArrayList<>();

        for (String opcId : opcIds.split(",")) {
            Opc opc = opcService.getById(opcId);
            String stationId = opc.getLeftStationId();

            List<RailwayLineSection> railwayLineSections = railwayLineSectionService.getByStationId(stationId);
            resultRailwayLineSections.addAll(railwayLineSections);

            List<KilometerMark> kilometerMarks = kilometerMarkService.getByRailwayLineSections(railwayLineSections);
            resultKilometerMarks.addAll(kilometerMarks);

            List<Opc> opcs = new ArrayList<>(List.of(opc));
            resultOpcs.addAll(opcs);

            List<OpcMark> opcMarks = opcMarkService.getByOpcs(opcs);
            resultOpcMarks.addAll(opcMarks);
        }

        locationService.fillLocations(resultRailwayLineSections);
        locationService.fillLocations(resultKilometerMarks);
        locationService.fillLocations(resultOpcs);
        locationService.fillLocations(resultOpcMarks);


        attributes.put("opcMarkTypes", opcMarkTypeService.getAll());
        attributes.put("opcTypes", opcTypeService.getAll());
        attributes.put("opcMarks", resultOpcMarks);
        attributes.put("rawOpcs", resultOpcs);
        attributes.put("rawRailwayLineSections", resultRailwayLineSections);
        attributes.put("kilometerMarks", resultKilometerMarks);

        return new Json(JsonMessage.SUCCESS, attributes);
    }

    private List<RailwayLineSection> filterByStartStationIdAndEndStationId(
            ConstructionControlPlanKilometerMark constructionControlPlanKilometerMark,
            List<RailwayLineSection> rawRailwayLineSections
    ) throws ReflectiveOperationException {
        // 根据副表的行别(downRiver)筛选使用的线路
        byte downriver = constructionControlPlanKilometerMark.getDownriver();
        List<RailwayLineSection> usingRailwayLineSection = new ArrayList<>();
        for (RailwayLineSection subRailwayLineSection : rawRailwayLineSections) {
            // 副表的计划行别是单线或上下行时
            if (downriver == ConstructionControlPlanKilometerMark.UP_AND_DOWN || downriver == ConstructionControlPlanKilometerMark.SINGLE_LINE)
                usingRailwayLineSection.add(subRailwayLineSection);
            // 线路的行别是单线时
            if (subRailwayLineSection.getDownriver() == RailwayLineSection.SINGLE_LINE)
                usingRailwayLineSection.add(subRailwayLineSection);
            // 副表的行别与线路行别一致时，都添加
            if (subRailwayLineSection.getDownriver() == subRailwayLineSection.getDownriver())
                usingRailwayLineSection.add(subRailwayLineSection);
        }
        usingRailwayLineSection = PojoUtils.copyPojoList(usingRailwayLineSection, RailwayLineSection.class);
        for (RailwayLineSection railwayLineSection : usingRailwayLineSection)
            // 手动深拷贝，目的是使controlPlanRailwayLineSections中的location与rawRailwayLineSections中的location指向不同地址
            railwayLineSection.setLocations(PojoUtils.copyPojoList(railwayLineSection.getLocations(), Location.class));

        return usingRailwayLineSection;
    }

}
