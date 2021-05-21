package com.yingda.lkj.controller.backstage.constructioncontrolplan;

import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionControlPlan;
import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionFormalPlan;
import com.yingda.lkj.beans.exception.CustomException;
import com.yingda.lkj.beans.pojo.utils.ExcelSheetInfo;
import com.yingda.lkj.beans.system.Json;
import com.yingda.lkj.beans.system.JsonMessage;
import com.yingda.lkj.controller.BaseController;
import com.yingda.lkj.service.backstage.constructioncontrolplan.ConstructionControlPlanService;
import com.yingda.lkj.service.backstage.constructioncontrolplan.ConstructionFormalPlanService;
import com.yingda.lkj.service.backstage.line.RailwayLineService;
import com.yingda.lkj.service.backstage.line.StationService;
import com.yingda.lkj.service.base.BaseService;
import com.yingda.lkj.utils.StringUtils;
import com.yingda.lkj.utils.excel.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/backstage/constructionFormalPlan")
@RestController
public class ConstructionFormalPlanController extends BaseController {

    @Autowired
    private BaseService<ConstructionFormalPlan> constructionFormalPlanBaseService;
    @Autowired
    private ConstructionFormalPlanService constructionFormalPlanService;
    @Autowired
    private ConstructionControlPlanService constructionControlPlanService;
    @Autowired
    private StationService stationService;
    @Autowired
    private RailwayLineService railwayLineService;

    @RequestMapping("/getList")
    public Json getList() throws Exception {
        Map<String, Object> attributes = new HashMap<>();
        String planCode = req.getParameter("planCode");
        String planType = req.getParameter("planType");
        String railwayLine = req.getParameter("railwayLine");
        String workshop = req.getParameter("workshop");

        String relevanceStatusStr = req.getParameter("relevanceStatus");

        String sql = """
                SELECT
                	constructionFormalPlan.id,
                	constructionFormalPlan.construction_control_plan_id AS constructionControlPlanId,
                	constructionFormalPlan.relevance_status AS relevanceStatus,
                	constructionFormalPlan.plan_code AS planCode,
                	constructionFormalPlan.downriver,
                	constructionFormalPlan.construction_department_and_principal_name AS constructionDepartmentAndPrincipalName,
                	constructionFormalPlan.construct_department AS constructDepartment,
                	constructionFormalPlan.construction_machinery AS constructionMachinery,
                	constructionFormalPlan.plan_type AS planType,
                	constructionFormalPlan.railway_line_name AS railwayLineName,
                	constructionFormalPlan.construction_project AS constructionProject,
                	constructionFormalPlan.construction_site AS constructionSite,
                	constructionFormalPlan.construction_content_and_influence_area AS constructionContentAndInfluenceArea,
                	constructionFormalPlan.work_time AS workTime,
                	constructionFormalPlan.supervision_department_and_principal_name AS supervisionDepartmentAndPrincipalName,
                	constructionFormalPlan.equipment_monitoring_department_and_principal_name AS equipmentMonitoringDepartmentAndPrincipalName,
                	constructionFormalPlan.add_time AS addTime,
                	constructionFormalPlan.star_rating AS starRating,
                	constructionFormalPlan.audit_department AS auditDepartment, 
                	constructionFormalPlan.remarks,
                	constructionControlPlan.`code` AS constructionControlPlanCode,
                	constructionControlPlan.construction_project_info AS constructionProjectInfo 
                FROM
                	construction_formal_plan AS constructionFormalPlan
                	LEFT JOIN construction_control_plan AS constructionControlPlan ON constructionFormalPlan.construction_control_plan_id = constructionControlPlan.id
                WHERE
                    1 = 1
                    AND constructionFormalPlan.construction_control_plan_id IS NULL
                """;
        Map<String, Object> params = new HashMap<>();

        if (StringUtils.isNotEmpty(relevanceStatusStr)) {
            params.put("relevanceStatus", Byte.valueOf(relevanceStatusStr));
            sql += "AND constructionFormalPlan.relevance_status = :relevanceStatus\n";
        }
        if (StringUtils.isNotEmpty(planCode)) {
            params.put("planCode", "%" + planCode + "%");
            sql += "AND constructionFormalPlan.plan_code like :planCode\n";
        }
        if (StringUtils.isNotEmpty(planType)) {
            params.put("planType", "%" + planType + "%");
            sql += "AND constructionFormalPlan.plan_type like :planType\n";
        }
        if (StringUtils.isNotEmpty(railwayLine)) {
            params.put("railwayLine", "%" + railwayLine + "%");
            sql += "AND constructionFormalPlan.railway_line like :railwayLine\n";
        }
        if (StringUtils.isNotEmpty(workshop)) {
            params.put("workshop", "%" + workshop + "%");
            sql += "AND constructionFormalPlan.workshop like :workshop\n";
        }

        sql += "order by constructionControlPlan.add_time desc";
        List<ConstructionFormalPlan> constructionFormalPlans = constructionFormalPlanBaseService.findSQL(
                sql, params, ConstructionFormalPlan.class, page.getCurrentPage(), page.getPageSize()
        );

        attributes.put("constructionFormalPlans", constructionFormalPlans);
        setObjectNum(sql, params);
        attributes.put("page", page);

        return new Json(JsonMessage.SUCCESS, attributes);
    }

    @RequestMapping("/info")
    public Json info() {
        Map<String, Object> attributes = new HashMap<>();

        String constructionFormalPlanId = req.getParameter("constructionFormalPlanId");
        String constructionControlPlanId = req.getParameter("constructionControlPlanId");
        ConstructionFormalPlan constructionFormalPlan = constructionFormalPlanService.getById(constructionFormalPlanId);
        ConstructionControlPlan constructionControlPlan = constructionControlPlanService.getById(constructionControlPlanId);
        constructionControlPlanService.fillAssociatedInfo(constructionControlPlan);

        attributes.put("constructionFormalPlan", constructionFormalPlan);
        attributes.put("constructionControlPlan", constructionControlPlan);
        attributes.put("stations", stationService.findStationsBySectionId(getSectionId()));
        attributes.put("railwayLines", railwayLineService.getAll());

        return new Json(JsonMessage.SUCCESS, attributes);
    }

    @RequestMapping("/importByExcel")
    public Json importByExcel(MultipartFile file) throws Exception {
        List<ExcelSheetInfo> excelSheetInfos = ExcelUtil.readExcelFile(file);
        constructionFormalPlanService.importByExcel(excelSheetInfos);
        return new Json(JsonMessage.SUCCESS);
    }

    @RequestMapping("/delete")
    public Json delete() throws CustomException {
        checkParameters("ids");
        String idParams = req.getParameter("ids");
        String[] ids = idParams.split(",");
        if(constructionFormalPlanService.deleteByIds(ids)) return new Json(JsonMessage.SUCCESS);
        return new Json(JsonMessage.PARAM_INVALID,"所选计划中存在已关联计划，不可删除");
    }

}
