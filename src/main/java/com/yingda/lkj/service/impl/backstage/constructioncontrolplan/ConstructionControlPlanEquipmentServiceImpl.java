package com.yingda.lkj.service.impl.backstage.constructioncontrolplan;

import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionControlPlan;
import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionControlPlanEquipment;
import com.yingda.lkj.dao.BaseDao;
import com.yingda.lkj.service.backstage.constructioncontrolplan.ConstructionControlPlanEquipmentHistoryService;
import com.yingda.lkj.service.backstage.constructioncontrolplan.ConstructionControlPlanEquipmentService;
import com.yingda.lkj.service.backstage.constructioncontrolplan.ConstructionControlPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Service("constructionControlPlanEquipmentService")
public class ConstructionControlPlanEquipmentServiceImpl implements ConstructionControlPlanEquipmentService {

    @Autowired
    private BaseDao<ConstructionControlPlanEquipment> constructionControlPlanEquipmentBaseDao;
    @Autowired
    private ConstructionControlPlanEquipmentHistoryService constructionControlPlanEquipmentHistoryService;
    @Autowired
    private ConstructionControlPlanService constructionControlPlanService;

    @Override
    public List<ConstructionControlPlanEquipment> getByConstructionControlPlanId(String constructionControlPlanId) {
        return constructionControlPlanEquipmentBaseDao.find(
                "from ConstructionControlPlanEquipment where constructionControlPlanId = :constructionControlPlanId",
                Map.of("constructionControlPlanId", constructionControlPlanId)
        );
    }

    @Override
    public List<ConstructionControlPlanEquipment> getByEquipmentId(String equipmentId) {
        return constructionControlPlanEquipmentBaseDao.find(
                "from ConstructionControlPlanEquipment where equipmentId = :equipmentId",
                Map.of("equipmentId", equipmentId)
        );
    }

    @Override
    public void save(String constructionControlPlanId, String equipmentId, double influenceRadius) {
        constructionControlPlanEquipmentBaseDao.saveOrUpdate(new ConstructionControlPlanEquipment(constructionControlPlanId, equipmentId));
        constructionControlPlanEquipmentHistoryService.bindEquipment(constructionControlPlanId, equipmentId);

        // 方案设备绑定数 + 1
        ConstructionControlPlan constructionControlPlan = constructionControlPlanService.getById(constructionControlPlanId);
        int equipmentBindCount = constructionControlPlan.getEquipmentBindCount();
        constructionControlPlan.setEquipmentBindCount(equipmentBindCount + 1);
        constructionControlPlan.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        constructionControlPlanService.saveOrUpdate(constructionControlPlan);
    }

    @Override
    public void delete(String constructionControlPlanId, String equipmentId) {
        constructionControlPlanEquipmentBaseDao.executeHql(
                "delete from ConstructionControlPlanEquipment where constructionControlPlanId = :constructionControlPlanId and equipmentId = :equipmentId",
                Map.of("constructionControlPlanId", constructionControlPlanId, "equipmentId", equipmentId)
        );
        constructionControlPlanEquipmentHistoryService.unbindEquipment(constructionControlPlanId, equipmentId);

        // 方案设备解绑数 - 1
        ConstructionControlPlan constructionControlPlan = constructionControlPlanService.getById(constructionControlPlanId);
        int equipmentReleaseCount = constructionControlPlan.getEquipmentReleaseCount();
        constructionControlPlan.setEquipmentReleaseCount(equipmentReleaseCount + 1);
        constructionControlPlan.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        constructionControlPlanService.saveOrUpdate(constructionControlPlan);
    }

    @Override
    public void updateEquipmentInfluenceRadius(String constructionControlPlanId, String equipmentId, double influenceRadius) {
        ConstructionControlPlanEquipment controlPlanEquipment = getByConstructionControlPlanIdAndEquipmentId(constructionControlPlanId, equipmentId);
        controlPlanEquipment.setInfluenceRadius(influenceRadius);
        constructionControlPlanEquipmentBaseDao.saveOrUpdate(controlPlanEquipment);
    }

    private ConstructionControlPlanEquipment getByConstructionControlPlanIdAndEquipmentId(String constructionControlPlanId, String equipmentId) {
        return constructionControlPlanEquipmentBaseDao.get(
                "from ConstructionControlPlanEquipment where constructionControlPlanId = :constructionControlPlanId and equipmentId = :equipmentId",
                Map.of("constructionControlPlanId", constructionControlPlanId, "equipmentId", equipmentId)
        );
    }
}
