package com.yingda.lkj.service.backstage.actionplan;

import com.yingda.lkj.beans.entity.backstage.actionplan.ActionPlanPrimaryNode;
import com.yingda.lkj.dao.BaseDao;
import com.yingda.lkj.utils.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service("actionPlanPrimaryNodeService")
public class ActionPlanPrimaryNodeService {

    @Autowired
    private BaseDao<ActionPlanPrimaryNode> actionPlanPrimaryNodeBaseDao;

    public ActionPlanPrimaryNode getById(String id) {
        return actionPlanPrimaryNodeBaseDao.get(ActionPlanPrimaryNode.class, id);
    }

    public void saveOrUpdate(ActionPlanPrimaryNode pageActionPlanPrimaryNode) {
        String id = pageActionPlanPrimaryNode.getId();

        ActionPlanPrimaryNode actionPlanPrimaryNode = StringUtils.isEmpty(id) ? new ActionPlanPrimaryNode() : getById(id);
        BeanUtils.copyProperties(pageActionPlanPrimaryNode, actionPlanPrimaryNode, "id", "addTime");
        // 新添加时，自动填充添加时间、id、排序
        if (StringUtils.isEmpty(id)) {
            actionPlanPrimaryNode.setId(UUID.randomUUID().toString());

            actionPlanPrimaryNode.setAddTime(new Timestamp(System.currentTimeMillis()));

            int lastSeq = getLastSeq(actionPlanPrimaryNode.getActionPlanId());
            actionPlanPrimaryNode.setSeq(lastSeq);
        }

        actionPlanPrimaryNode.setUpdateTime(new Timestamp(System.currentTimeMillis()));


        actionPlanPrimaryNodeBaseDao.saveOrUpdate(actionPlanPrimaryNode);
    }

    public List<ActionPlanPrimaryNode> getByActionPlanId(String actionPlanId) {
        return actionPlanPrimaryNodeBaseDao.find(
                "from ActionPlanPrimaryNode where actionPlanId = :actionPlanId",
                Map.of("actionPlanId", actionPlanId)
        );
    }

    public int getLastSeq(String actionPlanId) {
        List<ActionPlanPrimaryNode> actionPlanPrimaryNodes = getByActionPlanId(actionPlanId);
        return actionPlanPrimaryNodes.stream().map(ActionPlanPrimaryNode::getSeq).reduce(0, (x, y) -> x > y ? x : y) + 1;
    }

}
