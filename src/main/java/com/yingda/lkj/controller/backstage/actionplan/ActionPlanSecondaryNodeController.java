package com.yingda.lkj.controller.backstage.actionplan;

import com.yingda.lkj.beans.entity.backstage.actionplan.ActionPlanPrimaryNode;
import com.yingda.lkj.beans.entity.backstage.actionplan.ActionPlanSecondaryNode;
import com.yingda.lkj.beans.entity.backstage.organization.Organization;
import com.yingda.lkj.beans.entity.system.User;
import com.yingda.lkj.beans.exception.CustomException;
import com.yingda.lkj.beans.system.Json;
import com.yingda.lkj.beans.system.JsonMessage;
import com.yingda.lkj.controller.BaseController;
import com.yingda.lkj.service.backstage.actionplan.ActionPlanPrimaryNodeService;
import com.yingda.lkj.service.backstage.actionplan.ActionPlanSecondaryNodeService;
import com.yingda.lkj.service.backstage.organization.OrganizationClientService;
import com.yingda.lkj.service.base.BaseService;
import com.yingda.lkj.service.system.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/backstage/actionPlanSecondaryNode")
@RestController
public class ActionPlanSecondaryNodeController extends BaseController {
    @Autowired
    private ActionPlanSecondaryNodeService actionPlanSecondaryNodeService;
    @Autowired
    private BaseService<ActionPlanSecondaryNode> actionPlanSecondaryNodeBaseService;
    @Autowired
    private ActionPlanPrimaryNodeService actionPlanPrimaryNodeService;
    @Autowired
    private OrganizationClientService organizationClientService;
    @Autowired
    private UserService userService;

    private ActionPlanSecondaryNode pageActionPlanSecondaryNode;

    @RequestMapping("/detail")
    public Json detail() throws CustomException {
        Map<String, Object> attributes = new HashMap<>();

        String id = pageActionPlanSecondaryNode.getId();
        ActionPlanSecondaryNode actionPlanSecondaryNode = actionPlanSecondaryNodeService.getById(id);

        if (actionPlanSecondaryNode == null) {
            actionPlanSecondaryNode = new ActionPlanSecondaryNode();
            String actionPlanPrimaryNodeId = pageActionPlanSecondaryNode.getActionPlanPrimaryNodeId();
            ActionPlanPrimaryNode actionPlanPrimaryNode = actionPlanPrimaryNodeService.getById(actionPlanPrimaryNodeId);

            actionPlanSecondaryNode.setActionPlanId(actionPlanPrimaryNode.getActionPlanId());
            actionPlanSecondaryNode.setActionPlanPrimaryNodeId(actionPlanPrimaryNode.getId());
        }

        attributes.put("actionPlanSecondaryNode", actionPlanSecondaryNode);

        // 查询可以调用的用户（下级）
        String userLowestLevelOrganizationId = getUserLowestLevelOrganizationId();
        List<Organization> organizations = organizationClientService.getByParentId(userLowestLevelOrganizationId);
        List<User> slaves = new ArrayList<>();
        for (Organization organization : organizations)
            slaves.addAll(userService.getByOrganizationId(organization.getId()));
        slaves.add(getUser());
        attributes.put("slaves", slaves);

        return new Json(JsonMessage.SUCCESS, attributes);
    }

    @RequestMapping("/saveOrUpdate")
    public Json saveOrUpdate() {
        actionPlanSecondaryNodeService.saveOrUpdate(pageActionPlanSecondaryNode);
        return new Json(JsonMessage.SUCCESS);
    }

    @RequestMapping("/cancelFinishedActionPlanSecondaryNode")
    public Json cancelFinishedActionPlanSecondaryNode() {
        String actionPlanSecondaryNodeId = req.getParameter("actionPlanSecondaryNodeId");
        actionPlanSecondaryNodeService.cancelfinishedActionPlanSecondaryNode(actionPlanSecondaryNodeId);
        return new Json(JsonMessage.SUCCESS);
    }

    @RequestMapping("/finishedActionPlanSecondaryNode")
    public Json finishedActionPlanSecondaryNode() {
        String actionPlanSecondaryNodeId = req.getParameter("actionPlanSecondaryNodeId");
        actionPlanSecondaryNodeService.finishedActionPlanSecondaryNode(actionPlanSecondaryNodeId);
        return new Json(JsonMessage.SUCCESS);
    }

    @ModelAttribute
    public void setPageActionPlanSecondaryNode(ActionPlanSecondaryNode pageActionPlanSecondaryNode) {
        this.pageActionPlanSecondaryNode = pageActionPlanSecondaryNode;
    }
}
