package com.yingda.lkj.service.backstage.constructioncontrolplan;

import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionControlPlan;
import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionFormalPlan;
import com.yingda.lkj.beans.exception.CustomException;
import com.yingda.lkj.beans.pojo.utils.ExcelSheetInfo;

import java.util.List;

public interface ConstructionFormalPlanService {

    ConstructionFormalPlan getById(String id);

    List<ConstructionFormalPlan> getByConstructionControlPlanId(String constructionControlPlanId);

    void addByConstructionControlPlan(ConstructionControlPlan constructionControlPlan);

    void relevancePlan(String constructionControlPlanId, String constructionFormalPlanId);

    void importByExcel(List<ExcelSheetInfo> excelSheetInfos) throws CustomException;

    void delete(String id);

    /**
     * 批量删除
     * @param ids
     * @return
     */
    boolean deleteByIds(String[] ids);
}
