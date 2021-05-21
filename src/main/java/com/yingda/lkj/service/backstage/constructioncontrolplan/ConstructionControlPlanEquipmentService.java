package com.yingda.lkj.service.backstage.constructioncontrolplan;

import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionControlPlanEquipment;

import java.util.List;

public interface ConstructionControlPlanEquipmentService {

    List<ConstructionControlPlanEquipment> getByConstructionControlPlanId(String constructionControlPlanId);

    List<ConstructionControlPlanEquipment> getByEquipmentId(String equipmentId);

    void save(String constructionControlPlanId, String equipmentId, double influenceRadius);

    void delete(String constructionControlPlanId, String equipmentId);

    void updateEquipmentInfluenceRadius(String constructionControlPlanId, String equipmentId, double influenceRadius);
}
