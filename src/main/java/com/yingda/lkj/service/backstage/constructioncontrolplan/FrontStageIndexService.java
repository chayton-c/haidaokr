package com.yingda.lkj.service.backstage.constructioncontrolplan;

public interface FrontStageIndexService {
    //待关联计划：processStatus=ConstructionControlPlan.PENDING_RELEVANCE
    Long getPendingRelevancePlans();

    //未开始施工：processStatus=ConstructionControlPlan.FORMAL_START && now() < startTime(开始施工时间)
    Long getPendingStartPlans();

    //正在施工：processStatus=ConstructionControlPlan.FORMAL_START && startTime < now() < endTime
    Long getConstructingPlans();
}
