package com.yingda.lkj.service.backstage.constructioncontrolplan;

import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionDailyPlan;
import com.yingda.lkj.beans.exception.CustomException;

import java.util.List;

public interface ConstructionDailyPlanService {

    String createCode();

    ConstructionDailyPlan getById(String id);

    List<ConstructionDailyPlan> getByConstructionControlPlan(String constructionControlPlanId);

    void saveOrUpdate(ConstructionDailyPlan pageConstructionDailyPlan) throws CustomException;

    /**
     * 判断日计划是否需要开始/关闭
     */
    void checkFinishedStatus();

    void closeByConstructionControlPlanId(String constructionControlPlanId);

    List<ConstructionDailyPlan> getProcessingPlansByIds(List<String> ids);

    /**
     * 实际（手动）开始日计划
     * @param constructionDailyPlan
     */
    void actualStart(ConstructionDailyPlan constructionDailyPlan);

    /**
     * 实际（手动）结束日计划
     * @param constructionDailyPlan
     */
    void actualClose(ConstructionDailyPlan constructionDailyPlan);

}
