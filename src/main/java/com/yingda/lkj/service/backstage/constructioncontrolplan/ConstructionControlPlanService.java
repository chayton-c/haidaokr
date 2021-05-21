package com.yingda.lkj.service.backstage.constructioncontrolplan;

import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionControlPlan;
import com.yingda.lkj.beans.exception.CustomException;

import java.util.List;

public interface ConstructionControlPlanService {
    ConstructionControlPlan getById(String id);

    ConstructionControlPlan saveOrUpdate(ConstructionControlPlan pageConstructionControlPlan);

    List<ConstructionControlPlan> getAll();

    void submit(String constructionControlPlanId) throws CustomException;

    void safeCountersign(String constructionControlPlanId) throws CustomException;

    String getNextCode();

    void delete(String constructionControlPlanId);

    List<ConstructionControlPlan> findByStationId(String stationId);

    /**
     * 获取需要下发的计划
     */
    List<ConstructionControlPlan> issuePendingInvestigatePlans2App();

    /**
     * 下发计划
     */
    void startInvestigate(String id, String investigationOrganizationId);

    /**
     * 完成调查
     */
    void finishedInvestigate(List<String> ids);

    void close(String id);

    void relevanceFormalPlan(String constructionControlPlanId, String constructionFormalPlanId) throws CustomException;

    /**
     * 检验是否有超时的方案，有的话关闭
     */
    void checkClose();

    void fillAssociatedInfos(List<ConstructionControlPlan> constructionControlPlans);

    void fillAssociatedInfo(ConstructionControlPlan constructionControlPlan);

    /**
     * 根据设备id获取正在进行中的方案
     */
    List<ConstructionControlPlan> getRelevancedConstructionControlPlanByEquipmentId(String equipmentId);
}
