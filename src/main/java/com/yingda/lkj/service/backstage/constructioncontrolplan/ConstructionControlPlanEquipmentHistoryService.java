package com.yingda.lkj.service.backstage.constructioncontrolplan;

import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionControlPlanEquipmentHistory;

import java.util.List;

public interface ConstructionControlPlanEquipmentHistoryService {

    void bindEquipment(String constructionControlPlanId, String equipmentId);

    void unbindEquipment(String constructionControlPlanId, String equipmentId);

    List<ConstructionControlPlanEquipmentHistory> getByConstructionControlPlanId(String constructionControlPlanId);

    ConstructionControlPlanEquipmentHistory getByConstructionControlPlanIdAndEquipmentId(String constructionControlPlanId, String equipmentId);
}
