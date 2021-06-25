package com.yingda.lkj.service.backstage.actionplan;

import com.yingda.lkj.beans.entity.backstage.actionplan.ActionPlanSecondaryNode;
import com.yingda.lkj.dao.BaseDao;
import com.yingda.lkj.utils.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

        BeanUtils.copyProperties(pageActionPlanSecondaryNode, actionPlanSecondaryNode);
        if (StringUtils.isEmpty(id)) {
            actionPlanSecondaryNode.setId(UUID.randomUUID().toString());
            actionPlanSecondaryNode.setAddTime(new Timestamp(System.currentTimeMillis()));
            actionPlanSecondaryNode.setSeq(getLastSeq(actionPlanSecondaryNode.getActionPlanPrimaryNodeId()));
        }
        actionPlanSecondaryNode.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        actionPlanSecondaryNodeBaseDao.saveOrUpdate(actionPlanSecondaryNode);
    }

    public int getLastSeq(String actionPlanPrimaryNodeId) {
        List<ActionPlanSecondaryNode> actionPlanSecondaryNodes = getByActionPlanPrimaryNodeId(actionPlanPrimaryNodeId);
        return actionPlanSecondaryNodes.stream().map(ActionPlanSecondaryNode::getSeq).reduce(0, (x, y) -> x > y ? x : y) + 1;
    }

    public List<ActionPlanSecondaryNode> getByActionPlanPrimaryNodeId(String actionPlanPrimaryNodeId) {
        return actionPlanSecondaryNodeBaseDao.findSQL(
                """
                SELECT
                    action_plan_secondary_node.*,
                    principal.display_name AS principalName,
                    checker.display_name AS checkerName\s
                FROM
                    action_plan_secondary_node
                    INNER JOIN `user` AS principal ON action_plan_secondary_node.principal_id = principal.id
                    INNER JOIN `user` AS checker ON action_plan_secondary_node.checker_id = checker.id
                WHERE
                    action_plan_secondary_node.action_plan_primary_node_id = :actionPlanPrimaryNodeId
                """, Map.of("actionPlanPrimaryNodeId", actionPlanPrimaryNodeId), ActionPlanSecondaryNode.class
        );
    }

    public List<ActionPlanSecondaryNode> getByActionPlanId(String actionPlanId) {
        return actionPlanSecondaryNodeBaseDao.find(
                "from ActionPlanSecondaryNode where actionPlanId = :actionPlanId order by seq",
                Map.of("actionPlanId", actionPlanId)
        );
    }

}
