package com.yingda.lkj.controller.backstage.actionplan;

import com.yingda.lkj.beans.entity.backstage.actionplan.ActionPlan;
import com.yingda.lkj.beans.entity.backstage.actionplan.ActionPlanPrimaryNode;
import com.yingda.lkj.beans.entity.backstage.actionplan.ActionPlanSecondaryNode;
import com.yingda.lkj.beans.pojo.actionplan.ActionPlanTreeNode;
import com.yingda.lkj.beans.system.Json;
import com.yingda.lkj.beans.system.JsonMessage;
import com.yingda.lkj.controller.BaseController;
import com.yingda.lkj.service.backstage.actionplan.ActionPlanPrimaryNodeService;
import com.yingda.lkj.service.backstage.actionplan.ActionPlanSecondaryNodeService;
import com.yingda.lkj.service.backstage.actionplan.ActionPlanService;
import com.yingda.lkj.service.base.BaseService;
import com.yingda.lkj.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/backstage/actionPlanPrimaryNode")
@RestController
public class ActionPlanPrimaryNodeController extends BaseController {

    @Autowired
    private BaseService<ActionPlanPrimaryNode> actionPlanPrimaryNodeBaseService;
    @Autowired
    private ActionPlanPrimaryNodeService actionPlanPrimaryNodeService;
    @Autowired
    private ActionPlanSecondaryNodeService actionPlanSecondaryNodeService;
    @Autowired
    private ActionPlanService actionPlanService;

    private ActionPlanPrimaryNode pageActionPlanPrimaryNode;

    @RequestMapping("/detail")
    public Json detail() {
        Map<String, Object> attributes = new HashMap<>();

        String id = pageActionPlanPrimaryNode.getId();
        String actionPlanId = pageActionPlanPrimaryNode.getActionPlanId();

        ActionPlanPrimaryNode actionPlanPrimaryNode =
                StringUtils.isEmpty(id) ? new ActionPlanPrimaryNode() : actionPlanPrimaryNodeService.getById(id);
        actionPlanPrimaryNode.setActionPlanId(actionPlanId);

        attributes.put("actionPlanPrimaryNode", actionPlanPrimaryNode);

        return new Json(JsonMessage.SUCCESS, attributes);
    }


    @RequestMapping("/saveOrUpdate")
    public Json saveOrUpdate() {
        actionPlanPrimaryNodeService.saveOrUpdate(pageActionPlanPrimaryNode);
        return new Json(JsonMessage.SUCCESS);
    }

    @RequestMapping("/getNodeTree")
    public Json getNodeTree() {
        Map<String, Object> attributes = new HashMap<>();

        String actionPlanId = req.getParameter("actionPlanId");

        List<ActionPlanPrimaryNode> actionPlanPrimaryNodes = actionPlanPrimaryNodeService.getByActionPlanId(actionPlanId);

        List<ActionPlanTreeNode> actionPlanTreeNodes = new ArrayList<>();
        // 可以改进，但没必要
        for (ActionPlanPrimaryNode actionPlanPrimaryNode : actionPlanPrimaryNodes) {
            List<ActionPlanSecondaryNode> actionPlanSecondaryNodes =
                    actionPlanSecondaryNodeService.getByActionPlanPrimaryNodeId(actionPlanPrimaryNode.getId());

            ActionPlanTreeNode actionPlanTreeNode = new ActionPlanTreeNode(actionPlanPrimaryNode, actionPlanSecondaryNodes);
            actionPlanTreeNodes.add(actionPlanTreeNode);
        }
        attributes.put("actionPlanTreeNodes", actionPlanTreeNodes);

        return new Json(JsonMessage.SUCCESS, attributes);
    }

    @RequestMapping("/initActionPage")
    public Json initActionPage() {
        Map<String, Object> attributes = new HashMap<>();

        String actionPlanId = req.getParameter("actionPlanId");
        ActionPlan actionPlan = actionPlanService.getById(actionPlanId);
        attributes.put("actionPlan", actionPlan);

        List<ActionPlanPrimaryNode> actionPlanPrimaryNodes = actionPlanPrimaryNodeService.getByActionPlanId(actionPlanId);

        for (ActionPlanPrimaryNode actionPlanPrimaryNode : actionPlanPrimaryNodes) {
            List<ActionPlanSecondaryNode> actionPlanSecondaryNodes = actionPlanSecondaryNodeService.getByActionPlanPrimaryNodeId(actionPlanPrimaryNode.getId());
            actionPlanPrimaryNode.setActionPlanSecondaryNodes(actionPlanSecondaryNodes);
        }
        attributes.put("actionPlanPrimaryNodes", actionPlanPrimaryNodes);

        return new Json(JsonMessage.SUCCESS, attributes);
    }

    @ModelAttribute
    public void setPageActionPlanPrimaryNode(ActionPlanPrimaryNode pageActionPlanPrimaryNode) {
        this.pageActionPlanPrimaryNode = pageActionPlanPrimaryNode;
    }
}
