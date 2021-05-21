package com.yingda.lkj.controller.jmreport;

import com.alibaba.fastjson.JSONObject;
import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionControlPlan;
import com.yingda.lkj.beans.pojo.jmreport.StatisticsDailyPlan;
import com.yingda.lkj.beans.system.Json;
import com.yingda.lkj.beans.system.JsonMessage;
import com.yingda.lkj.service.backstage.statisticanalysis.StatisticAnalysisService;
import com.yingda.lkj.service.base.BaseService;
import com.yingda.lkj.utils.date.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/jmreport/statisticsDailyPlan")
public class StatisticsDailyPlanController {
    @Autowired
    private BaseService<StatisticsDailyPlan> statisticsDailyPlanBaseService;
    @Autowired
    private StatisticAnalysisService statisticAnalysisService;

    @RequestMapping("/countNormalConstructionDailyPlan")
    public Json countNormalConstructionDailyPlan() throws Exception {
        Map<String, Object> attributes = new HashMap<>();
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();

        String sql = """
                SELECT
                	COUNT( dailyPlan.id ) AS `value`,
                	CASE dailyPlan.finished_status
                	WHEN 0 THEN '未开始'
                	WHEN 1 THEN '进行中'
                	WHEN 2 THEN '进行中'
                	ELSE '其他'
                END AS type
                FROM
                	construction_daily_plan AS dailyPlan
                	LEFT JOIN construction_control_plan AS plan ON dailyPlan.construction_control_plan_id = plan.id\s
                WHERE
                	plan.construction_status = :NORMAL_CONSTRUCTION
                	AND DATE_FORMAT( dailyPlan.start_time, '%Y-%m-%d' )= :startDate
                """;

        params.put("NORMAL_CONSTRUCTION", ConstructionControlPlan.NORMAL_CONSTRUCTION);

        Timestamp startTime = new Timestamp(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000);
        Timestamp endTime = new Timestamp(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
        List<String> daysList = statisticAnalysisService.getDaysByStartTimeAndEndTime(DateUtil.format(startTime, "yyyy-MM-dd"), DateUtil.format(endTime, "yyyy-MM-dd"));

        List<StatisticsDailyPlan> statisticsDailyPlanList = new ArrayList<>();
        daysList.forEach(day -> {
            params.put("startDate",day);
            try {
                List<StatisticsDailyPlan> statisticsDailyPlans = statisticsDailyPlanBaseService.findSQL(sql,params,StatisticsDailyPlan.class);
                if (statisticsDailyPlans.size() > 0) {
                    statisticsDailyPlans.forEach(statisticsDailyPlan -> {
                        statisticsDailyPlan.setName(day);
                        statisticsDailyPlanList.add(statisticsDailyPlan);
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        attributes.put("data",statisticsDailyPlanList);
        return new Json(JsonMessage.SUCCESS,attributes);

    }

    @RequestMapping("/countBCConstructionDailyPlan")
    public Json countBCConstructionDailyPlan() throws Exception {
        Map<String, Object> attributes = new HashMap<>();
        Map<String, Object> params = new HashMap<>();

        String sql = """
                SELECT
                	COUNT( dailyPlan.id ) AS `value`,
                	CASE dailyPlan.finished_status
                	WHEN 0 THEN '未开始'
                	WHEN 1 THEN '进行中'
                	WHEN 2 THEN '进行中'
                	ELSE '其他'
                END AS type
                FROM
                	construction_daily_plan AS dailyPlan
                	LEFT JOIN construction_control_plan AS plan ON dailyPlan.construction_control_plan_id = plan.id\s
                WHERE
                	plan.construction_status = :BC_CONSTRUCTION
                	AND DATE_FORMAT( dailyPlan.start_time, '%Y-%m-%d' )= :startDate
                """;

        params.put("BC_CONSTRUCTION", ConstructionControlPlan.BC_CONSTRUCTION);

        Timestamp startTime = new Timestamp(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000);
        Timestamp endTime = new Timestamp(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
        List<String> daysList = statisticAnalysisService.getDaysByStartTimeAndEndTime(DateUtil.format(startTime, "yyyy-MM-dd"), DateUtil.format(endTime, "yyyy-MM-dd"));

        List<StatisticsDailyPlan> statisticsDailyPlanList = new ArrayList<>();
        daysList.forEach(day -> {
            params.put("startDate",day);
            try {
                List<StatisticsDailyPlan> statisticsDailyPlans = statisticsDailyPlanBaseService.findSQL(sql,params,StatisticsDailyPlan.class);
                if (statisticsDailyPlans.size() > 0) {
                    statisticsDailyPlans.forEach(statisticsDailyPlan -> {
                        statisticsDailyPlan.setName(day);
                        statisticsDailyPlanList.add(statisticsDailyPlan);
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        attributes.put("data",statisticsDailyPlanList);
        return new Json(JsonMessage.SUCCESS,attributes);
    }

}
