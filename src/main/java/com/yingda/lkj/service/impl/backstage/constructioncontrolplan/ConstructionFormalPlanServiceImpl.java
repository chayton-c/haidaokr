package com.yingda.lkj.service.impl.backstage.constructioncontrolplan;

import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionControlPlan;
import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionFormalPlan;
import com.yingda.lkj.beans.exception.CustomException;
import com.yingda.lkj.beans.pojo.utils.ExcelRowInfo;
import com.yingda.lkj.beans.pojo.utils.ExcelSheetInfo;
import com.yingda.lkj.dao.BaseDao;
import com.yingda.lkj.service.backstage.constructioncontrolplan.ConstructionFormalPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("constructionFormalPlanService")
public class ConstructionFormalPlanServiceImpl implements ConstructionFormalPlanService {

    @Autowired
    private BaseDao<ConstructionFormalPlan> constructionFormalPlanBaseDao;

    @Override
    public void addByConstructionControlPlan(ConstructionControlPlan constructionControlPlan) {
    }

    @Override
    public void relevancePlan(String constructionControlPlanId, String constructionFormalPlanId) {
        ConstructionFormalPlan constructionFormalPlan = getById(constructionFormalPlanId);
        constructionFormalPlan.setConstructionControlPlanId(constructionControlPlanId);
        constructionFormalPlanBaseDao.saveOrUpdate(constructionFormalPlan);
    }

    @Override
    public List<ConstructionFormalPlan> getByConstructionControlPlanId(String constructionControlPlanId) {
        return constructionFormalPlanBaseDao.find(
                "from ConstructionFormalPlan where constructionControlPlanId = :constructionControlPlanId",
                Map.of("constructionControlPlanId", constructionControlPlanId)
        );
    }

    @Override
    public ConstructionFormalPlan getById(String id) {
        return constructionFormalPlanBaseDao.get(ConstructionFormalPlan.class, id);
    }

    @Override
    public void importByExcel(List<ExcelSheetInfo> excelSheetInfos) throws CustomException {
        List<ConstructionFormalPlan> constructionFormalPlans = new ArrayList<>();
        for (int sheetIndex = 0; sheetIndex < excelSheetInfos.size(); sheetIndex++) {
            ExcelSheetInfo excelSheetInfo = excelSheetInfos.get(sheetIndex);
            Map<Integer, ExcelRowInfo> rowInfoMap = excelSheetInfo.getRowInfoMap();

            for (Map.Entry<Integer, ExcelRowInfo> entry : rowInfoMap.entrySet()) {
                Integer lineIndex = entry.getKey();
                if (lineIndex < 2)
                    continue;

                List<String> cells = entry.getValue().getCells();
                ConstructionFormalPlan constructionFormalPlan = ConstructionFormalPlan.getInstanceByExcel(cells, (lineIndex + 1), (sheetIndex + 1));
                constructionFormalPlans.add(constructionFormalPlan);
            }
        }
        constructionFormalPlanBaseDao.bulkInsert(constructionFormalPlans);
    }

    @Override
    public void delete(String id) {
        constructionFormalPlanBaseDao.executeHql(
                "delete from ConstructionFormalPlan where id = :id",
                Map.of("id", id)
        );
    }

    /**
     * 只可删除未关联的计划
     * @param ids
     * @return
     */
    @Override
    public boolean deleteByIds(String[] ids) {
        List<ConstructionFormalPlan> constructionFormalPlans = constructionFormalPlanBaseDao.find(
                "from ConstructionFormalPlan where relevanceStatus = :RELEVANCED and id in :ids",
                Map.of("RELEVANCED",ConstructionFormalPlan.RELEVANCED,"ids",ids)
        );
        if(constructionFormalPlans == null || constructionFormalPlans.isEmpty()){
            constructionFormalPlanBaseDao.executeHql("delete from ConstructionFormalPlan where id in :ids",Map.of("ids",ids));
            return true;
        }
        return false;
    }
}
