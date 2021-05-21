package com.yingda.lkj.controller.backstage;

import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionControlPlan;
import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionDailyPlan;
import com.yingda.lkj.beans.pojo.statisticanalysis.StatisticsPlanOrDailyPlan;
import com.yingda.lkj.beans.system.Json;
import com.yingda.lkj.beans.system.JsonMessage;
import com.yingda.lkj.controller.BaseController;
import com.yingda.lkj.service.backstage.statisticanalysis.StatisticAnalysisService;
import com.yingda.lkj.service.base.BaseService;
import com.yingda.lkj.utils.date.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.*;

@RequestMapping("/backstage/statisticsPlanOrDailyPlan")
@RestController
public class StatisticsPlanOrDailyPlanController extends BaseController {

    @Autowired
    private BaseService<ConstructionDailyPlan> constructionDailyPlanBaseService;
    @Autowired
    private StatisticAnalysisService statisticAnalysisService;
    private ConstructionDailyPlan pageConstructionDailyPlan;

    @RequestMapping("/countDailyPlanByConstructionStatus")
    public Json countDailyPlanByConstructionStatus() throws Exception {
        Map<String, Object> params = new HashMap<>();
        Map<String, StatisticsPlanOrDailyPlan> statisticsPlanOrDailyPlanMap = new LinkedHashMap<>();
        Map<String, Object> attributes = new HashMap<>();

        Timestamp startTime = pageConstructionDailyPlan.getStartTime();
        Timestamp endTime = pageConstructionDailyPlan.getEndTime();

        String sql = """
                SELECT
                	plan.construction_status AS constructionStatus,
                	dailyPlan.start_time AS startTime,
                	dailyPlan.end_time AS endTime,
                	dailyPlan.finished_status AS finishedStatus,
                	plan.workshop_id AS workshopId
                FROM
                	construction_daily_plan AS dailyPlan
                	LEFT JOIN construction_control_plan AS plan ON dailyPlan.construction_control_plan_id = plan.id
                WHERE
                	dailyPlan.start_time >= :startTime
                	AND dailyPlan.end_time <= :endTime
                """;

        if (startTime != null) {
            String startTimeStr = DateUtil.format(startTime, "yyyy-MM-dd");
            params.put("startTime", startTimeStr);
        }else {
            Timestamp startTimeDefault = new Timestamp(System.currentTimeMillis() - 6 * 24 * 60 * 60 * 1000);
            params.put("endTime",DateUtil.format(startTimeDefault, "yyyy-MM-dd"));
        }
        if (endTime != null) {
            String endTimeStr = DateUtil.format(endTime, "yyyy-MM-dd");
            params.put("endTime", endTimeStr);
        }else {
            Timestamp endTimeDefault = new Timestamp(System.currentTimeMillis());
            params.put("endTime",DateUtil.format(endTimeDefault, "yyyy-MM-dd"));
        }

        List<ConstructionDailyPlan> constructionDailyPlans = constructionDailyPlanBaseService.findSQL(sql,params,ConstructionDailyPlan.class);

        List<String> daysList = statisticAnalysisService.getDaysByStartTimeAndEndTime(DateUtil.format(startTime, "yyyy-MM-dd"), DateUtil.format(endTime, "yyyy-MM-dd"));

        daysList.forEach(days -> {
            StatisticsPlanOrDailyPlan statisticsPlanOrDailyPlan = new StatisticsPlanOrDailyPlan();
            // 施工管控日计划总数
            statisticsPlanOrDailyPlan.setConstructionControlTotal(
                    constructionDailyPlans.stream().filter(x -> DateUtil.format(x.getStartTime()).equals(days)).toArray().length
            );
            // 施工管控日计划完成数
            statisticsPlanOrDailyPlan.setConstructionControlFinished(
                    constructionDailyPlans.stream()
                            .filter(x -> DateUtil.format(x.getStartTime()).equals(days))
                            .filter(x -> x.getFinishedStatus() == ConstructionDailyPlan.CLOSED)
                            .toArray().length
            );
            // BC类施工日计划总数
            statisticsPlanOrDailyPlan.setBcControlTotal(
                    constructionDailyPlans.stream()
                            .filter(x -> DateUtil.format(x.getStartTime()).equals(days))
                            .filter(x -> x.getConstructionStatus() == ConstructionControlPlan.BC_CONSTRUCTION)
                            .toArray().length
            );
            // BC类施工日计划完成数
            statisticsPlanOrDailyPlan.setBcControlFinished(
                    constructionDailyPlans.stream()
                            .filter(x -> DateUtil.format(x.getStartTime()).equals(days))
                            .filter(x -> x.getConstructionStatus() == ConstructionControlPlan.BC_CONSTRUCTION)
                            .filter(x -> x.getFinishedStatus() == ConstructionDailyPlan.CLOSED)
                            .toArray().length
            );

            // 暂无天窗和点外施工类型，传固定值
            // 天窗日计划总数
            statisticsPlanOrDailyPlan.setTianChuangControlTotal(20 - new Random().nextInt(5));
            // 天窗日计划完成数
            statisticsPlanOrDailyPlan.setTianChuangControlFinished(20 - new Random().nextInt(10));
            // 点外管控日计划总数
            statisticsPlanOrDailyPlan.setDianWaiControlTotal(20 - new Random().nextInt(5));
            // 点外管控日计划完成数
            statisticsPlanOrDailyPlan.setDianWaiControlFinished(20 - new Random().nextInt(10));
            statisticsPlanOrDailyPlanMap.put(days,statisticsPlanOrDailyPlan);
        });
        attributes.put("daysList",daysList);
        attributes.put("result",statisticsPlanOrDailyPlanMap);
        return new Json(JsonMessage.SUCCESS,attributes);
    }

    @ModelAttribute
    public void setPageConstructionControlPlan(ConstructionDailyPlan pageConstructionDailyPlan) {
        this.pageConstructionDailyPlan = pageConstructionDailyPlan;
    }
}
