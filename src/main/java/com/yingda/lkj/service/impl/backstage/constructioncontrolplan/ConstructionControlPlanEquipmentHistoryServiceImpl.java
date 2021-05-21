package com.yingda.lkj.service.impl.backstage.constructioncontrolplan;

import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionControlPlanEquipmentHistory;
import com.yingda.lkj.dao.BaseDao;
import com.yingda.lkj.service.backstage.constructioncontrolplan.ConstructionControlPlanEquipmentHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Service("constructionControlPlanEquipmentHistoryService")
public class ConstructionControlPlanEquipmentHistoryServiceImpl implements ConstructionControlPlanEquipmentHistoryService {

    @Autowired
    private BaseDao<ConstructionControlPlanEquipmentHistory> constructionControlPlanEquipmentHistoryBaseDao;

    @Override
    public void bindEquipment(String constructionControlPlanId, String equipmentId) {
        ConstructionControlPlanEquipmentHistory constructionControlPlanEquipmentHistory = new ConstructionControlPlanEquipmentHistory(equipmentId, constructionControlPlanId);
        constructionControlPlanEquipmentHistoryBaseDao.saveOrUpdate(constructionControlPlanEquipmentHistory);
    }

    @Override
    public void unbindEquipment(String constructionControlPlanId, String equipmentId) {
        ConstructionControlPlanEquipmentHistory constructionControlPlanEquipmentHistory = getByConstructionControlPlanIdAndEquipmentId(constructionControlPlanId, equipmentId);
        constructionControlPlanEquipmentHistory.setEndTime(new Timestamp(System.currentTimeMillis()));
        constructionControlPlanEquipmentHistoryBaseDao.saveOrUpdate(constructionControlPlanEquipmentHistory);
    }

    @Override
    public List<ConstructionControlPlanEquipmentHistory> getByConstructionControlPlanId(String constructionControlPlanId) {
        return constructionControlPlanEquipmentHistoryBaseDao.find(
                "from ConstructionControlPlanEquipmentHistory where constructionControlPlanId = :constructionControlPlanId",
                Map.of("constructionControlPlanId", constructionControlPlanId)
        );
    }

    @Override
    public ConstructionControlPlanEquipmentHistory getByConstructionControlPlanIdAndEquipmentId(String constructionControlPlanId, String equipmentId) {
        return constructionControlPlanEquipmentHistoryBaseDao.get(
                "from ConstructionControlPlanEquipmentHistory where constructionControlPlanId = :constructionControlPlanId AND equipmentId = :equipmentId",
                Map.of("constructionControlPlanId", constructionControlPlanId, "equipmentId", equipmentId)
        );
    }
}
