package com.yingda.lkj.service.backstage.actionplan;

import com.yingda.lkj.beans.entity.backstage.actionplan.ActionPlanSecondaryNode;
import com.yingda.lkj.dao.BaseDao;
import com.yingda.lkj.utils.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("actionPlanSecondaryNodeService")
public class ActionPlanSecondaryNodeService {
    @Autowired
    private BaseDao<ActionPlanSecondaryNode> actionPlanSecondaryNodeBaseDao;

    public ActionPlanSecondaryNode getById(String id) {
        return actionPlanSecondaryNodeBaseDao.get(ActionPlanSecondaryNode.class, id);
    }

    public void saveOrUpdate(ActionPlanSecondaryNode pageActionPlanSecondaryNode) {
        String id = pageActionPlanSecondaryNode.getId();

        ActionPlanSecondaryNode actionPlanSecondaryNode = StringUtils.isEmpty(id) ? new ActionPlanSecondaryNode() : getById(id);
        BeanUtils.copyProperties(pageActionPlanSecondaryNode, actionPlanSecondaryNode, "id", "addTime");
    }

    public List<ActionPlanSecondaryNode> getByActionPlanPrimaryNodeId(String actionPlanPrimaryNodeId) {
        return actionPlanSecondaryNodeBaseDao.find(
                "from ActionPlanSecondaryNode where actionPlanPrimaryNodeId = :actionPlanPrimaryNodeId",
                Map.of("actionPlanPrimaryNodeId", actionPlanPrimaryNodeId)
        );
    }

    public List<ActionPlanSecondaryNode> getByActionPlanId(String actionPlanId) {
        return actionPlanSecondaryNodeBaseDao.find(
                "from ActionPlanSecondaryNode where actionPlanId = :actionPlanId",
                Map.of("actionPlanId", actionPlanId)
        );
    }

}
