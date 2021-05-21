package com.yingda.lkj.controller.backstage.constructioncontrolplan;

import com.yingda.lkj.beans.Constant;
import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionControlPlan;
import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionControlPlanPoint;
import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionFormalPlan;
import com.yingda.lkj.beans.entity.backstage.equipment.Equipment;
import com.yingda.lkj.beans.entity.backstage.line.RailwayLine;
import com.yingda.lkj.beans.entity.backstage.line.Station;
import com.yingda.lkj.beans.entity.backstage.line.StationRailwayLine;
import com.yingda.lkj.beans.entity.backstage.location.Location;
import com.yingda.lkj.beans.entity.backstage.opc.OpcMarkType;
import com.yingda.lkj.beans.entity.backstage.organization.Organization;
import com.yingda.lkj.beans.entity.system.Role;
import com.yingda.lkj.beans.enums.constructioncontrolplan.WarnLevel;
import com.yingda.lkj.beans.exception.CustomException;
import com.yingda.lkj.beans.pojo.constructioncontrolplan.AppConstructionControlPlanPoint;
import com.yingda.lkj.beans.pojo.constructioncontrolplan.AppConstructionControlPlanPointInfos;
import com.yingda.lkj.beans.system.Json;
import com.yingda.lkj.beans.system.JsonMessage;
import com.yingda.lkj.controller.BaseController;
import com.yingda.lkj.service.backstage.constructioncontrolplan.ConstructionControlPlanService;
import com.yingda.lkj.service.backstage.constructioncontrolplan.ConstructionCoordinatePlanUploadService;
import com.yingda.lkj.service.backstage.constructioncontrolplan.ConstructionFormalPlanService;
import com.yingda.lkj.service.backstage.equipment.EquipmentService;
import com.yingda.lkj.service.backstage.line.RailwayLineService;
import com.yingda.lkj.service.backstage.line.StationService;
import com.yingda.lkj.service.backstage.opc.OpcMarkTypeService;
import com.yingda.lkj.service.backstage.opc.OpcTypeService;
import com.yingda.lkj.service.backstage.organization.OrganizationClientService;
import com.yingda.lkj.service.base.BaseService;
import com.yingda.lkj.service.system.UserService;
import com.yingda.lkj.utils.JsonUtils;
import com.yingda.lkj.utils.StreamUtil;
import com.yingda.lkj.utils.StringUtils;
import com.yingda.lkj.utils.position.CoordinateTransform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/backstage/constructionControlPlan")
@RestController
public class ConstructionControlPlanController extends BaseController {

    @Autowired
    private BaseService<ConstructionControlPlan> constructionControlPlanBaseService;
    @Autowired
    private ConstructionControlPlanService constructionControlPlanService;
    @Autowired
    private UserService userService;
    @Autowired
    private BaseService<Location> locationBaseService;
    @Autowired
    private BaseService<ConstructionControlPlanPoint> constructionControlPlanPointBaseService;
    @Autowired
    private OrganizationClientService organizationClientService;
    @Autowired
    private StationService stationService;
    @Autowired
    private RailwayLineService railwayLineService;
    @Autowired
    private BaseService<StationRailwayLine> stationRailwayLineBaseService;
    @Autowired
    private OpcMarkTypeService opcMarkTypeService;
    @Autowired
    private OpcTypeService opcTypeService;
    @Autowired
    private EquipmentService equipmentService;
    @Autowired
    private ConstructionFormalPlanService constructionFormalPlanService;
    @Autowired
    private ConstructionCoordinatePlanUploadService constructionCoordinatePlanUploadService;

    private ConstructionControlPlan pageConstructionControlPlan;

    @RequestMapping("/getList")
    public Json getList() throws Exception {
        Map<String, Object> attributes = new HashMap<>();

        String nameOrCode = req.getParameter("nameOrCode");
        String signInStatus = req.getParameter("signInStatus");
        String approveStatus = req.getParameter("approveStatus");
        String codeOrConstructionProjectInfo = req.getParameter("codeOrConstructionProjectInfo");
        String processStatusStrArr = req.getParameter("processStatusStr");
        String investigationProgressStatusStr = req.getParameter("investigationProgressStatusStr");
        String planStatusStr = req.getParameter("planStatusStr");
        String warnStatusStr = req.getParameter("warnStatusStr");
        String startTimeStr = req.getParameter("startTime");
        String endTimeStr = req.getParameter("endTime");

        Map<String, Object> params = new HashMap<>();

        String sql = """
                SELECT
                    plan.*,
                    workshop.NAME AS workshopName,
                    workTime.endDate,
                    workTime.startDate\s
                FROM
                    construction_control_plan plan
                    LEFT JOIN organization AS workshop ON plan.workshop_id = workshop.id
                    # 关联时间副表，查询最近的结束时间和最远的开始时间
                    LEFT JOIN ( 
                        SELECT 
                            min( end_date ) AS endDate, 
                            max( start_date ) AS startDate, 
                            construction_control_plan_id AS construction_control_plan_id 
                        FROM construction_control_plan_work_time 
                        GROUP BY construction_control_plan_id 
                    ) AS workTime ON workTime.construction_control_plan_id = plan.id\s
                WHERE
                    1 = 1\s
                """;

        if (StringUtils.isNotEmpty(nameOrCode)) {
            sql += "AND (plan.name like :nameOrCode or plan.code like :nameOrCode)\n";
            params.put("nameOrCode", "%" + nameOrCode + "%");
        }
        if (StringUtils.isNotEmpty(signInStatus)) {
            sql += "AND plan.sign_in_status = :signInStatus\n";
            params.put("signInStatus", Byte.valueOf(signInStatus));
        }
        if (StringUtils.isNotEmpty(approveStatus)) {
            sql += "AND plan.approve_status = :approveStatus\n";
            params.put("approveStatus", Byte.valueOf(approveStatus));
        }
        if (StringUtils.isNotEmpty(codeOrConstructionProjectInfo)) {
            sql += "AND (plan.code like :codeOrConstructionProjectInfo or plan.construction_project_info like " +
                    ":codeOrConstructionProjectInfo)\n";
            params.put("codeOrConstructionProjectInfo", "%" + codeOrConstructionProjectInfo + "%");
        }
        if (StringUtils.isNotEmpty(startTimeStr)) {
            startTimeStr = new Timestamp(Long.parseLong(startTimeStr)).toString();
            sql += "AND workTime.endDate >= :startTime\n";
            params.put("startTime", startTimeStr);
        }
        if (StringUtils.isNotEmpty(endTimeStr)) {
            endTimeStr = new Timestamp(Long.parseLong(endTimeStr)).toString();
            sql += "AND workTime.startDate <= :endTime\n";
            params.put("endTime", endTimeStr);
        }

        if (StringUtils.isNotEmpty(processStatusStrArr)) {
            sql += "AND plan.process_status = :processStatus\n";
            params.put("processStatus", Byte.parseByte(processStatusStrArr));
        }
        if (StringUtils.isNotEmpty(investigationProgressStatusStr)) {
            sql += "AND plan.investigation_progress_status = :investigationProgressStatus\n";
            params.put("investigationProgressStatus", Byte.valueOf(investigationProgressStatusStr));
        }
        if (StringUtils.isNotEmpty(planStatusStr)) {
            sql += "AND plan.plan_status = :planStatus\n";
            params.put("planStatus", Byte.valueOf(planStatusStr));
        }
        if (StringUtils.isNotEmpty(warnStatusStr)) {
            sql += "AND plan.warn_status = :warnStatus\n";
            params.put("warnStatus", Byte.valueOf(warnStatusStr));
        }
        sql += createDataAuthSql("plan", Organization.WORKSHOP, params);

        sql += "ORDER BY plan.add_time desc";
        List<ConstructionControlPlan> constructionControlPlans = constructionControlPlanBaseService.findSQL(
                sql, params, ConstructionControlPlan.class, page.getCurrentPage(), page.getPageSize()
        );
        for (ConstructionControlPlan constructionControlPlan : constructionControlPlans) {
            List<ConstructionFormalPlan> constructionFormalPlans =
                    constructionFormalPlanService.getByConstructionControlPlanId(constructionControlPlan.getId());
            if (constructionFormalPlans.size() > 0)
                constructionControlPlan.setConstructionFormalPlanCodes(constructionFormalPlans.get(constructionFormalPlans.size() - 1).getPlanCode());
            constructionControlPlan.setWarnLevel(WarnLevel.getByLevel(constructionControlPlan.getWarnStatus()));
            constructionControlPlanService.fillAssociatedInfo(constructionControlPlan);
        }

        attributes.put("workshops", organizationClientService.getWorkshops());
        attributes.put("constructionControlPlans", constructionControlPlans);
        setObjectNum(sql, params);
        attributes.put("page", page);
        return new Json(JsonMessage.SUCCESS, attributes);
    }

    @RequestMapping("/info")
    public Json info() {
        Map<String, Object> attributes = new HashMap<>();

        String id = pageConstructionControlPlan.getId();
        ConstructionControlPlan constructionControlPlan = StringUtils.isEmpty(id) ?
                new ConstructionControlPlan() : constructionControlPlanService.getById(id);

        if (StringUtils.isEmpty(id)) {
            constructionControlPlan.setCode(constructionControlPlanService.getNextCode());
            constructionControlPlan.setWarnStatus((byte) WarnLevel.LEVEL5.level);
        }
        if (StringUtils.isNotEmpty(id)) {
            constructionControlPlanService.fillAssociatedInfo(constructionControlPlan);
        }

        attributes.put("railwayLines", railwayLineService.getAll());
        attributes.put("stations", stationService.getAll());
        attributes.put("users", userService.getAll());
        attributes.put("constructionControlPlan", constructionControlPlan);

        if (StringUtils.isNotEmpty(id)) {
            List<Equipment> equipments = equipmentService.getByConstructionControlPlanId(id);
            attributes.put("equipments", equipments);
        }

        return new Json(JsonMessage.SUCCESS, attributes);
    }

    @RequestMapping("/saveOrUpdate")
    public Json saveOrUpdate() throws CustomException {
        checkParametersWithErrorMsg(
            "signInStationId", "签入站"
        );
        String workshopId = getUser().getWorkshopId();
        // admin调试代码
        if (Role.ADMIN.equals(getRole().getName())) {
            String signInStationId = pageConstructionControlPlan.getSignInStationId();
            Station signInStation = stationService.getById(signInStationId);
            workshopId = signInStation.getWorkshopId();
        }
        if (StringUtils.isEmpty(workshopId))
            throw new CustomException(new Json(JsonMessage.NEED_TO_AUTH, "当前用户没有添加方案的权限，请联系管理员"));

        pageConstructionControlPlan.setExecuteOrganizationId(getUserLowestLevelOrganizationId());
        pageConstructionControlPlan.setWorkshopId(workshopId);
        constructionControlPlanService.saveOrUpdate(pageConstructionControlPlan);

        return new Json(JsonMessage.SUCCESS);
    }

    @RequestMapping("/importConstructionControlPlanPoint")
    public Json importConstructionControlPlanPoint(MultipartFile file) throws Exception {
        String jsonStr = new String(file.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        AppConstructionControlPlanPointInfos parse = JsonUtils.parse(jsonStr, AppConstructionControlPlanPointInfos.class);

        List<AppConstructionControlPlanPoint> appConstructionControlPlanPoints = parse.getConstructionControlPlanPoints();
        List<ConstructionControlPlanPoint> constructionControlPlanPoints = StreamUtil.getList(appConstructionControlPlanPoints,
                ConstructionControlPlanPoint::new);
        constructionControlPlanPointBaseService.bulkInsert(constructionControlPlanPoints);

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


    @RequestMapping("/delete")
    public Json delete() {
        String id = req.getParameter("id");
        constructionControlPlanService.delete(id);

        return new Json(JsonMessage.SUCCESS);
    }

    /**
     * 提交方案
     */
    @RequestMapping("/submit")
    public Json submit() throws CustomException {
        constructionControlPlanService.submit(pageConstructionControlPlan.getId());
        return new Json(JsonMessage.SUCCESS);
    }

    /**
     * 安全会签
     */
    @RequestMapping("/safeCountersign")
    public Json approve() throws CustomException {
        constructionControlPlanService.safeCountersign(pageConstructionControlPlan.getId());
        return new Json(JsonMessage.SUCCESS);
    }

    /**
     * 驳回
     */
    @RequestMapping("/rejectPlan")
    public Json rejectPlan() throws Exception {
        String id = req.getParameter("id");
        ConstructionControlPlan constructionControlPlan = constructionControlPlanService.getById(id);

        // 删除配合方案/安全协议：planStatus-技术会签=配合方案；安全会签=安全协议
        constructionCoordinatePlanUploadService.deleteByConstructionControlPlan(constructionControlPlan);

        constructionControlPlan.setProcessStatus(ConstructionControlPlan.PENDING_SUBMIT);
        constructionControlPlan.setPlanStatus(ConstructionControlPlan.FIRST_DRAFT);
        constructionControlPlan.setInvestigationProgressStatus(ConstructionControlPlan.INVESTIGATION_NOT_OPENED);
        constructionControlPlan.setHasUploadedCooperativeScheme(Constant.FALSE);
        constructionControlPlan.setHasUploadedSafetyProtocol(Constant.FALSE);
        constructionControlPlanBaseService.saveOrUpdate(constructionControlPlan);


        return new Json(JsonMessage.SUCCESS);
    }

    /**
     * 确认关联
     */
    @RequestMapping("/confirmConnect")
    public Json confirmConnect() throws CustomException {
        String constructionControlPlanId = req.getParameter("constructionControlPlanId");
        String constructionFormalPlanId = req.getParameter("constructionFormalPlanId");

        constructionControlPlanService.relevanceFormalPlan(constructionControlPlanId, constructionFormalPlanId);

        return new Json(JsonMessage.SUCCESS);
    }

    /**
     * 重新调查
     *
     * @see com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionControlPlan PENDING_INVESTIGATE
     * 调查进度: investigationProgressStatus = 1
     */
    @RequestMapping("/reinvestigationPlan")
    public Json reinvestigationPlan() throws Exception {
        String id = req.getParameter("id");
        ConstructionControlPlan constructionControlPlan = constructionControlPlanService.getById(id);
//        constructionControlPlan.setProcessStatus(ConstructionControlPlan.SURVEYING);
        constructionControlPlan.setInvestigationProgressStatus(ConstructionControlPlan.PENDING_INVESTIGATE);
        constructionControlPlan.setUpdateTime(current());
        constructionControlPlanBaseService.saveOrUpdate(constructionControlPlan);
        return new Json(JsonMessage.SUCCESS);
    }

    /**
     * 补充配合方案
     *
     * @see com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionControlPlan PENDING_INVESTIGATE
     * 调查进度: investigationProgressStatus = 1
     */
    @RequestMapping("/supplementaryPlan")
    public Json supplementaryPlan() throws Exception {
        String id = req.getParameter("id");
        ConstructionControlPlan constructionControlPlan = constructionControlPlanService.getById(id);
        constructionControlPlan.setInvestigationProgressStatus(ConstructionControlPlan.INVESTIGATING);
        constructionControlPlan.setUpdateTime(current());
        constructionControlPlanBaseService.saveOrUpdate(constructionControlPlan);
        return new Json(JsonMessage.SUCCESS);
    }

    /**
     * 技术会签
     */
    @RequestMapping("/techCountersign")
    public Json techCountersign() throws Exception {
        String id = req.getParameter("id");
        ConstructionControlPlan constructionControlPlan = constructionControlPlanService.getById(id);

        constructionControlPlan.setProcessStatus(ConstructionControlPlan.PENDING_COUNTERSIGN);
        constructionControlPlan.setPlanStatus(ConstructionControlPlan.SAFE_COUNTERSIGN);
        constructionControlPlan.setUpdateTime(new Timestamp(System.currentTimeMillis()));
//        constructionControlPlan.setProcessStatus(ConstructionControlPlan.PENDING_APPROVAL);
        constructionControlPlanBaseService.saveOrUpdate(constructionControlPlan);
        return new Json(JsonMessage.SUCCESS);
    }

    /**
     * 提交会签
     */
    @RequestMapping("/submitCountersign")
    public Json submitCountersign() throws Exception {
        String id = req.getParameter("id");
        ConstructionControlPlan constructionControlPlan = constructionControlPlanService.getById(id);
        constructionControlPlan.setProcessStatus(ConstructionControlPlan.PENDING_COUNTERSIGN);
        constructionControlPlan.setPlanStatus(ConstructionControlPlan.TECH_COUNTERSIGN);
        constructionControlPlan.setUpdateTime(current());
        constructionControlPlanBaseService.saveOrUpdate(constructionControlPlan);
        return new Json(JsonMessage.SUCCESS);
    }

    /**
     * 下发调查计划
     */
    @RequestMapping("/issueInvestigationTasks")
    public Json issueInvestigationTasks() {
        String id = req.getParameter("id");
        String investigationOrganizationId = req.getParameter("investigationOrganizationId");
        constructionControlPlanService.startInvestigate(id, investigationOrganizationId);
        return new Json(JsonMessage.SUCCESS);
    }

    /**
     * 关闭计划
     */
    @RequestMapping("/close")
    public Json close() throws Exception {
        String id = req.getParameter("id");
        constructionControlPlanService.close(id);
        return new Json(JsonMessage.SUCCESS);
    }

    /**
     * 重新关联
     */
    @RequestMapping("/reconnect")
    public Json reconnect() throws Exception {
        String id = req.getParameter("id");
        ConstructionControlPlan constructionControlPlan = constructionControlPlanService.getById(id);
        constructionControlPlan.setProcessStatus(ConstructionControlPlan.PENDING_RELEVANCE);
        constructionControlPlan.setUpdateTime(current());
        constructionControlPlanBaseService.saveOrUpdate(constructionControlPlan);
        return new Json(JsonMessage.SUCCESS);
    }

    @RequestMapping("/issuePlanJson")
    public void issuePlanJson() throws Exception {
        Map<String, Object> attributes = new HashMap<>();


        List<RailwayLine> railwayLines = railwayLineService.getAll();
        List<Station> stations = stationService.getAll();
        List<StationRailwayLine> stationRailwayLines = stationRailwayLineBaseService.find("from StationRailwayLine");
        List<OpcMarkType> opcMarkTypes = opcMarkTypeService.getAll();

        attributes.put("constructionControlPlans", constructionControlPlanService.getAll());
        attributes.put("users", userService.getAll());
        attributes.put("railwayLines", railwayLines);
        attributes.put("stations", stations);
        attributes.put("stationRailwayLines", stationRailwayLines);
        attributes.put("opcTypes", opcTypeService.getAll());
        attributes.put("opcMarkTypes", opcMarkTypes);

        MultipartFile file = new MockMultipartFile("线路模板", attributes.toString().getBytes());
        export(file);
//        return new Json(JsonMessage.SUCCESS, attributes);
    }

    /**
     * 统计方案数量
     */
    @RequestMapping("/countPlanTotals")
    public Json countPlanTotals() throws Exception {
        Map<String, Object> attributes = new HashMap<>();

        String sql = """
                SELECT
                	process_status AS processStatus,
                	plan_status AS planStatus,
                	investigation_progress_status AS investigationProgressStatus 
                FROM
                	construction_control_plan 
                WHERE 
                    1 = 1
                """;

        Map<String, Object> params = new HashMap<>();
        sql += createDataAuthSql("construction_control_plan", Organization.WORKSHOP, params);

        List<ConstructionControlPlan> constructionControlPlans = constructionControlPlanBaseService.findSQL(sql, params, ConstructionControlPlan.class);

        attributes.put("constructionControlPlans", constructionControlPlans);
        return new Json(JsonMessage.SUCCESS, attributes);
    }

    /**
     * 用于首页根据方案等级、当前年、上月统计方案数量
     * 仅返回数据包，前端处理结果
     */
    @RequestMapping("/countPlanByWarnStatusAndDate")
    public Json countPlanByWarnStatusAndDate() throws Exception {
        Map<String, Object> attributes = new HashMap<>();
        String sql = """
                SELECT
                	constructionControlPlan.*,
                    workTime.startDate as startTime
                FROM
                	construction_control_plan constructionControlPlan
                	# 关联时间副表，查询最近的结束时间和最远的开始时间
                    LEFT JOIN (\s
                        SELECT\s
                            MAX( end_date ) AS endDate,\s
                            MIN( start_date ) AS startDate,\s
                            construction_control_plan_id AS construction_control_plan_id\s
                        FROM construction_control_plan_work_time\s
                        GROUP BY construction_control_plan_id\s
                    ) AS workTime ON workTime.construction_control_plan_id = constructionControlPlan.id\s
                WHERE
                    1 = 1
                """;

        Map<String, Object> params = new HashMap<>();
        sql += createDataAuthSql("constructionControlPlan", Organization.WORKSHOP, params);

        List<ConstructionControlPlan> countControlPlans = constructionControlPlanBaseService
                .findSQL(sql, params, ConstructionControlPlan.class);

        attributes.put("countControlPlans", countControlPlans);
        return new Json(JsonMessage.SUCCESS, attributes);
    }

    @ModelAttribute
    public void setPageConstructionControlPlan(ConstructionControlPlan pageConstructionControlPlan) {
        this.pageConstructionControlPlan = pageConstructionControlPlan;
    }

}
