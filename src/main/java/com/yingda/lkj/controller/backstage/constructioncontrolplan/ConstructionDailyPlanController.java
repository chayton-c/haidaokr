package com.yingda.lkj.controller.backstage.constructioncontrolplan;

import com.yingda.lkj.beans.entity.backstage.centerscreen.CenterScreenElementPosition;
import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionControlPlan;
import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionControlPlanKilometerMark;
import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionDailyPlan;
import com.yingda.lkj.beans.entity.backstage.equipment.Equipment;
import com.yingda.lkj.beans.entity.backstage.line.RailwayLine;
import com.yingda.lkj.beans.entity.backstage.organization.Organization;
import com.yingda.lkj.beans.exception.CustomException;
import com.yingda.lkj.beans.pojo.constructioncontrolplan.ConstructionControlPlanOrDailyPlan;
import com.yingda.lkj.beans.system.Json;
import com.yingda.lkj.beans.system.JsonMessage;
import com.yingda.lkj.controller.BaseController;
import com.yingda.lkj.service.backstage.centerscreen.CenterScreenElementPositionService;
import com.yingda.lkj.service.backstage.constructioncontrolplan.ConstructionControlPlanService;
import com.yingda.lkj.service.backstage.constructioncontrolplan.ConstructionDailyPlanService;
import com.yingda.lkj.service.backstage.equipment.EquipmentService;
import com.yingda.lkj.service.backstage.line.RailwayLineService;
import com.yingda.lkj.service.backstage.line.StationService;
import com.yingda.lkj.service.backstage.organization.OrganizationClientService;
import com.yingda.lkj.service.base.BaseService;
import com.yingda.lkj.utils.StreamUtil;
import com.yingda.lkj.utils.StringUtils;
import com.yingda.lkj.utils.date.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/backstage/constructionDailyPlan")
public class ConstructionDailyPlanController extends BaseController {

    @Autowired
    private ConstructionDailyPlanService constructionDailyPlanService;
    @Autowired
    private ConstructionControlPlanService constructionControlPlanService;
    @Autowired
    private BaseService<ConstructionDailyPlan> constructionDailyPlanBaseService;
    @Autowired
    private BaseService<ConstructionControlPlanOrDailyPlan> constructionControlPlanOrDailyPlanBaseService;
    @Autowired
    private OrganizationClientService organizationClientService;
    @Autowired
    private StationService stationService;
    @Autowired
    private RailwayLineService railwayLineService;
    @Autowired
    private EquipmentService equipmentService;
    @Autowired
    private CenterScreenElementPositionService centerScreenElementPositionService;

    private ConstructionDailyPlan pageConstructionDailyPlan;

    @RequestMapping("/getList")
    public Json getList() throws Exception {
        Map<String, Object> attributes = new HashMap<>();

        String constructionStep = req.getParameter("constructionStep"); // 施工阶段
        String requestWorkshopId = req.getParameter("workshopId"); // 前端传的workshopId
        String railwayLineId = req.getParameter("railwayLineId"); // 线路
//        String warnStatus = req.getParameter("warnStatus"); // 风险级别
        Timestamp startTime = DateUtil.toTimestamp(DateUtil.format(current(), "yyyy-MM-dd 00:00:00"), "yyyy-MM-dd HH:mm:ss");
        Timestamp endTime = DateUtil.toTimestamp(DateUtil.format(current(), "yyyy-MM-dd 23:59:59"), "yyyy-MM-dd HH:mm:ss");
        String stationId = req.getParameter("stationId");
        String warnStatusStr = req.getParameter("warnStatus"); // 风险级别

        List<Organization> workshops = organizationClientService.getWorkshopsByUser(getUser());
        String workshopId = requestWorkshopId;
        if (StringUtils.isNotEmpty(requestWorkshopId) && workshops.stream().noneMatch(x -> x.getId().equals(requestWorkshopId)))
            workshopId = Optional.ofNullable(getUser().getWorkshopId()).orElse("");
        attributes.put("workshops", workshops);
        attributes.put("workshopId", workshopId);

        String sql = """
                # 查询待关联的方案
                SELECT
                    constructionControlPlan.id AS id,
                    constructionControlPlan.`code` AS `code`,
                    constructionControlPlan.plan_status AS planStatus,
                    constructionControlPlan.process_status AS processStatus,
                    constructionControlPlan.warn_status AS warnStatus,
                    constructionControlPlan.construction_project_info AS constructionProjectInfo,
                    constructionControlPlan.add_time AS addTime,
                    workTime.startDate AS startTime,
                    workTime.endDate AS endTime
                FROM
                    construction_control_plan AS constructionControlPlan
                    # 关联时间副表，查询最近的结束时间和最远的开始时间
                    LEFT JOIN (
                        SELECT
                            MAX( end_date ) AS endDate,
                            MIN( start_date ) AS startDate,
                            construction_control_plan_id AS construction_control_plan_id
                        FROM construction_control_plan_work_time
                        GROUP BY construction_control_plan_id
                    ) AS workTime ON workTime.construction_control_plan_id = constructionControlPlan.id
                WHERE
                    1 = 1
                    AND constructionControlPlan.workshop_id like :workshopId
                    AND constructionControlPlan.workshop_id in :workshopIds
                """;
        Map<String, Object> params = new HashMap<>();
//        params.put("CONTROL", ConstructionControlPlanOrDailyPlan.CONTROL);
//        params.put("STEP1", ConstructionControlPlanOrDailyPlan.STEP1);
        params.put("workshopId", "%" + workshopId + "%");
        params.put("workshopIds", StreamUtil.getList(workshops, Organization::getId));

        if(StringUtils.isNotEmpty(constructionStep)){
            BigInteger step = BigInteger.valueOf(Long.parseLong(constructionStep));
            if (step.equals(ConstructionControlPlanOrDailyPlan.STEP1)) {
                sql += "AND constructionControlPlan.plan_status = :planStatus AND constructionControlPlan.process_status = :processStatus";
                params.put("planStatus",ConstructionControlPlan.COUNTERSIGNED);
                params.put("processStatus",ConstructionControlPlan.PENDING_RELEVANCE);
            }
            if(step.equals(ConstructionControlPlanOrDailyPlan.STEP2)){
                sql += "AND constructionControlPlan.plan_status in :planStatus AND constructionControlPlan.process_status = :processStatus";
                List<Byte> planStatusList = new ArrayList<>();
                planStatusList.add(ConstructionControlPlan.PENDING_START);
                planStatusList.add(ConstructionControlPlan.FORMAL_START);
                params.put("planStatus",planStatusList);
                params.put("processStatus",ConstructionControlPlan.RELEVANCEED);
            }
            if(step.equals(ConstructionControlPlanOrDailyPlan.STEP3)){
                sql += "AND constructionControlPlan.plan_status = :planStatus AND constructionControlPlan.process_status = :processStatus";
                params.put("planStatus",ConstructionControlPlan.CONSTRUCTING);
                params.put("processStatus",ConstructionControlPlan.RELEVANCEED);
            }
        }else {
            sql += "AND constructionControlPlan.plan_status in :planStatus AND constructionControlPlan.process_status in :processStatus";
            List<Byte> planStatusList = new ArrayList<>();
            planStatusList.add(ConstructionControlPlan.COUNTERSIGNED);
            planStatusList.add(ConstructionControlPlan.PENDING_START);
            planStatusList.add(ConstructionControlPlan.FORMAL_START);
            planStatusList.add(ConstructionControlPlan.CONSTRUCTING);

            List<Byte> processStatusList = new ArrayList<>();
            processStatusList.add(ConstructionControlPlan.PENDING_RELEVANCE);
            processStatusList.add(ConstructionControlPlan.RELEVANCEED);
            params.put("planStatus",planStatusList);
            params.put("processStatus",processStatusList);
        }

        List<ConstructionControlPlanOrDailyPlan> controlPlanOrDailyPlans = constructionControlPlanOrDailyPlanBaseService.findSQL(
                sql, params, ConstructionControlPlanOrDailyPlan.class, 1, 9999999
        );
        attributes.put("rawControlPlanOrDailyPlans", new ArrayList<>(controlPlanOrDailyPlans));

//        if (StringUtils.isNotEmpty(constructionStep))
//            controlPlanOrDailyPlans =
//                    controlPlanOrDailyPlans.stream().filter(x -> x.getConstructionStep().equals(BigInteger.valueOf(Long.parseLong(constructionStep)))).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(warnStatusStr))
            controlPlanOrDailyPlans =
                    controlPlanOrDailyPlans.stream().filter(x -> x.getWarnStatus() == Byte.parseByte(warnStatusStr)).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(stationId))
            controlPlanOrDailyPlans =
                    controlPlanOrDailyPlans.stream().filter(x -> x.getStartStationId().equals(stationId)).collect(Collectors.toList());

        // 根据车站ID获取各站的图标位置信息
        List<String> stationIds = new ArrayList<>();
        controlPlanOrDailyPlans.forEach(c -> {
            if (c.getStartStationId() != null) stationIds.add(c.getStartStationId());
        });
        List<CenterScreenElementPosition> centerScreenElementPositions = centerScreenElementPositionService.getByStationIds(stationIds);

        attributes.put("controlPlanOrDailyPlans", controlPlanOrDailyPlans);

        attributes.put("railwayLines", railwayLineService.getAll());
        attributes.put("centerScreenElementPositions", centerScreenElementPositions);

        return new Json(JsonMessage.SUCCESS, attributes);
    }

    @RequestMapping("/dailyList")
    public Json dailyList() throws Exception {
        Map<String, Object> attributes = new HashMap<>();

        String finishedStatus = req.getParameter("finishedStatus");
        String warnStatusStr = req.getParameter("warnStatusStr");

        String sql = """
                    SELECT
                        constructionDailyPlan.id AS id,
                        constructionDailyPlan.construction_control_plan_id AS constructionControlPlanId,
                        constructionDailyPlan.`code` AS `code`,
                        constructionDailyPlan.downriver,
                        constructionDailyPlan.start_time AS startTime,
                        constructionDailyPlan.end_time AS endTime,
                        constructionDailyPlan.start_kilometer AS startKilometer,
                        constructionDailyPlan.end_kilometer AS endKilometer,
                        constructionDailyPlan.add_time AS addTime,
                        constructionDailyPlan.update_time AS updateTime,
                        constructionDailyPlan.finished_status AS finishedStatus,
                        constructionControlPlan.`code` AS constructionControlPlanCode,
                        constructionControlPlan.construction_project_info AS constructionProjectInfo,
                        constructionControlPlan.warn_status AS warnStatus,
                        railwayLine.`name` AS railwayLineName\s
                    FROM
                        construction_daily_plan AS constructionDailyPlan
                        LEFT JOIN construction_control_plan AS constructionControlPlan ON constructionDailyPlan.construction_control_plan_id = constructionControlPlan.id
                        LEFT JOIN railway_line AS railwayLine ON constructionDailyPlan.railway_line_id = railwayLine.id
                    WHERE
                        1 = 1
                """;

        Map<String, Object> params = new HashMap<>();

        if (StringUtils.isNotEmpty(finishedStatus)) {
            sql += "AND constructionDailyPlan.finished_status = :finishedStatus\n";
            params.put("finishedStatus", Byte.valueOf(finishedStatus));
        }
        if(StringUtils.isNotEmpty(warnStatusStr)){
            sql += "AND constructionControlPlan.warn_status = :warnStatus\n";
            params.put("warnStatus",Boolean.valueOf(warnStatusStr));
        }

        sql += "ORDER BY constructionDailyPlan.start_time\n";
        List<ConstructionDailyPlan> constructionDailyPlans = constructionDailyPlanBaseService.findSQL(
                sql, params, ConstructionDailyPlan.class, page.getCurrentPage(), page.getPageSize()
        );
        setObjectNum(sql, params);
        attributes.put("page", page);
        attributes.put("constructionDailyPlans", constructionDailyPlans);

        return new Json(JsonMessage.SUCCESS, attributes);
    }

    @RequestMapping("/getByConstructionControlPlan")
    public Json getByConstructionControlPlan() throws Exception {
        Map<String, Object> attributes = new HashMap<>();
        String constructionControlPlanId = req.getParameter("constructionControlPlanId");
        String finishedStatusStr = req.getParameter("finishedStatusStr");

        Map<String, Object> params = new HashMap<>();
        Map<String, String> conditions = new HashMap<>();

        params.put("constructionControlPlanId", constructionControlPlanId);
        conditions.put("constructionControlPlanId", "=");

        if (StringUtils.isNotEmpty(finishedStatusStr)) {
            params.put("finishedStatus", Byte.valueOf(finishedStatusStr));
            conditions.put("finishedStatus", "=");
        }

        List<ConstructionDailyPlan> constructionDailyPlans = constructionDailyPlanBaseService.getObjcetPagination(
                ConstructionDailyPlan.class, params, conditions, page.getCurrentPage(), page.getPageSize(), "order by addTime desc"
        );
        for (ConstructionDailyPlan constructionDailyPlan : constructionDailyPlans) {
            String railwayLineId = constructionDailyPlan.getRailwayLineId();
            RailwayLine railwayLine = railwayLineService.getById(railwayLineId);
            if (railwayLine != null) constructionDailyPlan.setRailwayLineName(railwayLine.getName());
        }
        page.setDataTotal(constructionDailyPlanBaseService.getObjectNum(ConstructionDailyPlan.class, params, conditions));

        attributes.put("constructionDailyPlans", constructionDailyPlans);
        attributes.put("page", page);

        return new Json(JsonMessage.SUCCESS, attributes);
    }

    @RequestMapping("/info")
    public Json info() throws CustomException {
        checkParameters("constructionControlPlanId");
        Map<String, Object> attributes = new HashMap<>();

        String constructionDailyPlanId = req.getParameter("constructionDailyPlanId");
        String constructionControlPlanId = req.getParameter("constructionControlPlanId");

        ConstructionDailyPlan constructionDailyPlan = StringUtils.isNotEmpty(constructionDailyPlanId) ?
                constructionDailyPlanService.getById(constructionDailyPlanId)
                : new ConstructionDailyPlan();
        ConstructionControlPlan constructionControlPlan = constructionControlPlanService.getById(constructionControlPlanId);
        constructionControlPlanService.fillAssociatedInfo(constructionControlPlan);

        // 公里标副表
        List<ConstructionControlPlanKilometerMark> constructionControlPlanKilometerMarks = constructionControlPlan.getConstructionControlPlanKilometerMarks();
        attributes.put("constructionControlPlanKilometerMarks", constructionControlPlanKilometerMarks);

        String id = constructionDailyPlan.getId();
        if (StringUtils.isEmpty(id))
            constructionDailyPlan.setCode(constructionDailyPlanService.createCode());
        if (StringUtils.isNotEmpty(id)) {
            List<Equipment> equipments = equipmentService.getByConstructionControlPlanId(id);
            String equipmentCodes = equipments.stream().map(Equipment::getImei).collect(Collectors.joining("/"));
            constructionDailyPlan.setEquipmentCodes(equipmentCodes);
        }

        attributes.put("constructionControlPlan", constructionControlPlan);
        attributes.put("constructionDailyPlan", constructionDailyPlan);

        return new Json(JsonMessage.SUCCESS, attributes);
    }

    @RequestMapping("/saveOrUpdate")
    public Json saveOrUpdate() throws CustomException {
        checkParametersWithErrorMsg(
                "startTime", "预计开始时间",
                "endTime", "预计结束时间"
        );
        constructionDailyPlanService.saveOrUpdate(pageConstructionDailyPlan);
        return new Json(JsonMessage.SUCCESS);
    }


    /**
     * 用于首页根据方案等级、当前年、上月统计日计划数量
     * 仅返回数据包，前端处理结果
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/countDailyPlanByConstructionLevelAndDate")
    public Json countDailyPlanByConstructionLevelAndDate() throws Exception {
        Map<String, Object> attributes = new HashMap<>();
        String sql = """
                SELECT
                	dailyPlan.finished_status AS finishedStatus,
                	dailyPlan.start_time AS startTime,
                	controlPlan.warn_status AS warnStatus 
                FROM
                	construction_daily_plan AS dailyPlan
                	INNER JOIN construction_control_plan AS controlPlan ON dailyPlan.construction_control_plan_id = controlPlan.id
                """;

        Map<String, Object> params = new HashMap<>();
        sql += createDataAuthSql("controlPlan", Organization.WORKSHOP, params);

        List<ConstructionDailyPlan> countDailyPlans = constructionDailyPlanBaseService.findSQL(sql, params, ConstructionDailyPlan.class);

        attributes.put("countDailyPlans", countDailyPlans);
        return new Json(JsonMessage.SUCCESS, attributes);
    }

    /**
     * 实际（手动）开始日计划
     *
     * @return
     */
    @RequestMapping("actualStart")
    public Json actualStart() {
        String constructionDailyPlanId = req.getParameter("constructionDailyPlanId");
        ConstructionDailyPlan constructionDailyPlan = constructionDailyPlanService.getById(constructionDailyPlanId);
        if (constructionDailyPlan.getFinishedStatus() != ConstructionDailyPlan.PENDING_START)
            return new Json(JsonMessage.SYS_ERROR, "日计划进行中或已结束，不可重复开始");
        constructionDailyPlanService.actualStart(constructionDailyPlan);
        return new Json(JsonMessage.SUCCESS);
    }

    /**
     * 实际（手动）结束日计划
     *
     * @return
     */
    @RequestMapping("actualClose")
    public Json actualClose() {
        String constructionDailyPlanId = req.getParameter("constructionDailyPlanId");
        ConstructionDailyPlan constructionDailyPlan = constructionDailyPlanService.getById(constructionDailyPlanId);
        if (constructionDailyPlan.getFinishedStatus() == ConstructionDailyPlan.CLOSED) {
            return new Json(JsonMessage.SYS_ERROR, "当前日计划已结束");
        }
        constructionDailyPlanService.actualClose(constructionDailyPlan);
        return new Json(JsonMessage.SUCCESS);
    }

    @ModelAttribute
    public void setPageConstructionDailyPlan(ConstructionDailyPlan pageConstructionDailyPlan) {
        this.pageConstructionDailyPlan = pageConstructionDailyPlan;
    }
}
