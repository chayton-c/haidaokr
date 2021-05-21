package com.yingda.lkj.service.backstage.constructioncontrolplan;

import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionControlPlanPoint;
import com.yingda.lkj.beans.entity.backstage.opc.Opc;

import java.util.List;

public interface ConstructionControlPlanPointService {
    List<ConstructionControlPlanPoint> getByPlanId(String constructionControlPlanId);

    /**
     * 计算施工点到所在站附近的所有电缆的最近距离
     */
    double calculationShortestDistance(ConstructionControlPlanPoint constructionControlPlanPoint, List<Opc> opcsWithLocations);
}
