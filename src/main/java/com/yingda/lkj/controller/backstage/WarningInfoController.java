package com.yingda.lkj.controller.backstage;

import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.WarningInfo;
import com.yingda.lkj.beans.entity.backstage.organization.Organization;
import com.yingda.lkj.beans.system.Json;
import com.yingda.lkj.beans.system.JsonMessage;
import com.yingda.lkj.controller.BaseController;
import com.yingda.lkj.service.backstage.constructioncontrolplan.WarningInfoService;
import com.yingda.lkj.service.base.BaseService;
import com.yingda.lkj.utils.StringUtils;
import com.yingda.lkj.utils.date.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/backstage/warningInfo")
@RestController
public class WarningInfoController extends BaseController {

    @Autowired
    private WarningInfoService warningInfoService;
    @Autowired
    private BaseService<WarningInfo> warningInfoBaseService;

    @RequestMapping("/getList")
    public Json getList() throws Exception {
        Map<String, Object> attributes = new HashMap<>();

        String warnLevelStr = req.getParameter("warnLevelStr");
        String processStatusStr = req.getParameter("processStatusStr");
        String startTime = req.getParameter("startTime");
        String endTime = req.getParameter("endTime");

        Map<String, Object> params = new HashMap<>();
        String sql = """
                SELECT
                	warningInfo.id as id,
                 	warningInfo.add_time as addTime,
                 	warningInfo.process_result as processResult,
                 	warningInfo.process_status as processStatus,
                 	warningInfo.warn_info as warnInfo,
                 	warningInfo.warn_level as warnLevel,
                	controlPlan.`code` AS constructionControlPlanCode,
                	controlPlan.construction_project_info AS constructionControlPlanProjectInfo,
                	equipment.imei AS equipmentImei 
                FROM
                	warning_info AS warningInfo
                	INNER JOIN equipment ON warningInfo.equipment_id = equipment.id
                	INNER JOIN construction_control_plan AS controlPlan ON warningInfo.construction_control_plan_id = controlPlan.id 
                WHERE
                	warningInfo.warn_level <> :LEVEL5
                	AND warningInfo.process_status <> :CLOSED
                """;

        params.put("LEVEL5", WarningInfo.LEVEL5);
        params.put("CLOSED", WarningInfo.CLOSED);
        if (StringUtils.isNotEmpty(warnLevelStr)) {
            sql += "AND warningInfo.warn_level = :warnLevel\n";
            params.put("warnLevel", Byte.parseByte(warnLevelStr));
        }
        if (StringUtils.isNotEmpty(processStatusStr)) {
            sql += "AND warningInfo.process_status = :processStatus\n";
            params.put("processStatus", Byte.parseByte(processStatusStr));
        }
        if (StringUtils.isNotEmpty(startTime)) {
            sql += "AND warningInfo.add_time >= :startTime\n";
            params.put("startTime", DateUtil.format(new Timestamp(Long.parseLong(startTime)), "yyyy-MM-dd"));
        }
        if (StringUtils.isNotEmpty(endTime)) {
            sql += "AND warningInfo.add_time <= :endTime\n";
            params.put("endTime", DateUtil.format(new Timestamp(Long.parseLong(endTime)), "yyyy-MM-dd"));
        }

        sql += "ORDER BY warningInfo.add_time DESC \n";

        List<WarningInfo> warningInfos = warningInfoBaseService.findSQL(sql, params, WarningInfo.class, page.getCurrentPage(), page.getPageSize());
        attributes.put("warningInfos", warningInfos);
        setObjectNum(sql, params);
        attributes.put("page", page);
        return new Json(JsonMessage.SUCCESS, attributes);
    }

    @RequestMapping("/getListToCenterScreen")
    public Json getListToCenterScreen() throws Exception {
        Map<String, Object> attributes = new HashMap<>();

        Map<String, Object> params = new HashMap<>();
        String sql = """
                SELECT
                	warningInfo.id AS id,
                	MAX( warningInfo.add_time ) AS addTime,
                	warningInfo.warn_info AS warnInfo,
                	warningInfo.warn_level AS warnLevel,
                	warningInfo.process_status AS processStatus,
                	constructionControlPlan.`code` AS constructionControlPlanCode,
                	constructionControlPlan.construction_project_info AS constructionControlPlanProjectInfo,
                	equipment.imei AS equipmentImei 
                FROM
                	warning_info AS warningInfo
                	LEFT JOIN construction_control_plan AS constructionControlPlan ON warningInfo.construction_control_plan_id = constructionControlPlan.id
                	LEFT JOIN equipment AS equipment ON warningInfo.equipment_id = equipment.id 
                WHERE
                	warningInfo.warn_level <> :warnLevel  
                	AND warningInfo.process_status = :NOT_PROCESSED
                GROUP BY
                	warningInfo.equipment_id  
                """;

        params.put("warnLevel", WarningInfo.LEVEL5);
        params.put("NOT_PROCESSED", WarningInfo.NOT_PROCESSED);

        List<WarningInfo> warningInfos = warningInfoBaseService.findSQL(sql, params, WarningInfo.class);
        attributes.put("warningInfos", warningInfos);
        return new Json(JsonMessage.SUCCESS, attributes);
    }

    @RequestMapping("/processWarningByWeChat")
    public Json processWarningByWeChat() {
        return null;
    }

    @RequestMapping("/processWarningBySendSms")
    public Json processWarningBySendSms() throws Exception {
        warningInfoService.processWarningBySendSms(req.getParameter("id"));
        return new Json(JsonMessage.SUCCESS);
    }

    @RequestMapping("/processWarningByDoNothing")
    public Json processWarningByDoNothing() {
        warningInfoService.processWarningByDoNothing(req.getParameter("id"));
        return new Json(JsonMessage.SUCCESS);
    }

    /**
     * 获取最近一次一级预警时间
     */
    @RequestMapping("/getLastWarningDate")
    public Json getLastWarningDate() throws Exception {
        Map<String, Object> attributes = new HashMap<>();

        String sql = "SELECT MAX(add_time) AS addTime FROM warning_info WHERE warn_level = :warnLevel LIMIT 1";
        List<WarningInfo> warningInfos = warningInfoBaseService.findSQL(sql, Map.of("warnLevel", WarningInfo.LEVEL1), WarningInfo.class);

        if (StringUtils.isEmpty(warningInfos.get(0).getAddTime()))
            warningInfos.get(0).setAddTime(Timestamp.valueOf("2021-01-01 00:00:00"));
        attributes.put("warningInfos", warningInfos);
        return new Json(JsonMessage.SUCCESS, attributes);
    }

    /**
     * 用于统计各级别、月、年预警数量
     * 返回数据包，前端处理
     */
    @RequestMapping("/getWarningStatisticsResult")
    public Json getWarningStatisticsResult() throws Exception {
        Map<String, Object> attributes = new HashMap<>();
        Map<String, Object> params = new HashMap<>();

        String sql = """
                SELECT
                	warningInfo.warn_level AS warnLevel,
                	warningInfo.add_time AS addTime 
                FROM
                	warning_info AS warningInfo
                	LEFT JOIN construction_control_plan AS plan ON plan.id = warningInfo.construction_control_plan_id
                WHERE
                    1 = 1
                """;

        sql += createDataAuthSql("plan", Organization.WORKSHOP, params);

        List<WarningInfo> warningStatisticsResult = warningInfoBaseService.findSQL(sql, params, WarningInfo.class);

        attributes.put("warningStatisticsResult", warningStatisticsResult);

        return new Json(JsonMessage.SUCCESS, attributes);
    }
}
