package com.yingda.lkj.service.backstage.constructioncontrolplan;

import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionControlPlan;

public interface ConstructionCoordinatePlanUploadService {
    void deleteDataAndFile(String constructionCoordinatePlanUploadId);

    void getByConstructionControlPlanId(String constructionControlPlanId);

    void deleteByConstructionControlPlan(ConstructionControlPlan constructionControlPlan);
}
