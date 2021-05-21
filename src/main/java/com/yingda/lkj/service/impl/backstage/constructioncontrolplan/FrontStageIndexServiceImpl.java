package com.yingda.lkj.service.impl.backstage.constructioncontrolplan;

import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionControlPlan;
import com.yingda.lkj.dao.BaseDao;
import com.yingda.lkj.service.backstage.constructioncontrolplan.FrontStageIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("frontStageIndexService")
//用于首页上半部分各状态方案数量展示
public class FrontStageIndexServiceImpl implements FrontStageIndexService {

    @Autowired
    BaseDao<ConstructionControlPlan> constructionControlPlanBaseDao;

    /**
     * 待关联
     * SELECT COUNT(id) AS pendingRelevancePlans FROM construction_control_plan WHERE process_status = 4;
     * @return
     */
    @Override
    public Long getPendingRelevancePlans() {
        return constructionControlPlanBaseDao.getCount(
                "SELECT COUNT(1) from ConstructionControlPlan where process_status = :processStatus",
                Map.of("processStatus", ConstructionControlPlan.PENDING_RELEVANCE)
        );
    }

    /**
     * 未开始施工
     * SELECT COUNT(id) AS pendingStartPlans FROM construction_control_plan WHERE process_status = 5 AND NOW() < start_time;
     * @return
     */
    @Override
    public Long getPendingStartPlans() {
        return constructionControlPlanBaseDao.getCount(
                "SELECT COUNT(1) from ConstructionControlPlan where process_status = :processStatus AND NOW() < start_time",
                Map.of("processStatus", ConstructionControlPlan.FORMAL_START)
        );
    }

    /**
     * 正在施工
     * SELECT COUNT(id) AS constructingPlans FROM construction_control_plan WHERE process_status = 5 AND NOW() > start_time AND NOW() < end_time;
     * @return
     */
    @Override
    public Long getConstructingPlans() {
        return constructionControlPlanBaseDao.getCount(
                "SELECT COUNT(1) from ConstructionControlPlan where process_status = :processStatus AND NOW() > start_time AND NOW() < end_time",
                Map.of("processStatus", ConstructionControlPlan.FORMAL_START)
        );
    }
}
