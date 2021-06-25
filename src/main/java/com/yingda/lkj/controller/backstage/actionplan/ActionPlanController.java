package com.yingda.lkj.controller.backstage.actionplan;

import com.yingda.lkj.beans.entity.backstage.actionplan.ActionPlan;
import com.yingda.lkj.beans.entity.backstage.actionplan.ActionPlanUserStatistics;
import com.yingda.lkj.beans.entity.backstage.organization.Organization;
import com.yingda.lkj.beans.entity.system.User;
import com.yingda.lkj.beans.exception.CustomException;
import com.yingda.lkj.beans.system.Json;
import com.yingda.lkj.beans.system.JsonMessage;
import com.yingda.lkj.controller.BaseController;
import com.yingda.lkj.service.backstage.actionplan.ActionPlanService;
import com.yingda.lkj.service.backstage.organization.OrganizationClientService;
import com.yingda.lkj.service.base.BaseService;
import com.yingda.lkj.service.system.UserService;
import com.yingda.lkj.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/backstage/actionPlan")
@RestController
public class ActionPlanController extends BaseController {

    @Autowired
    private BaseService<ActionPlan> actionPlanBaseService;
    @Autowired
    private ActionPlanService actionPlanService;
    @Autowired
    private OrganizationClientService organizationClientService;
    @Autowired
    private UserService userService;

    private ActionPlan pageActionPlan;

    @RequestMapping("/getList")
    public Json getList() throws Exception {
        Map<String, Object> attributes = new HashMap<>();

        String userLowestLevelOrganizationId = getUserLowestLevelOrganizationId();


        String name = req.getParameter("name");
        String executorName = req.getParameter("executorName");

        Map<String, Object> params = new HashMap<>();
        String sql = """
                SELECT
                	action_plan.*,
                	executor.display_name AS executorName,
                	role.`name` AS executorRoleName,
                	organization.`name` AS executorOrganizationName\s
                FROM
                	action_plan
                	INNER JOIN `user` AS executor ON action_plan.execute_user_id = executor.id
                	INNER JOIN organization ON action_plan.organization_id = organization.id
                	INNER JOIN role ON executor.role_id = role.id
                WHERE
                    (
                        # 只查询本部门或本部门下级部门计划任务
                        organization.id = :userLowestLevelOrganizationId
                        OR organization.parent_id = :userLowestLevelOrganizationId
                    )
                """;
        params.put("userLowestLevelOrganizationId", userLowestLevelOrganizationId);
        if (StringUtils.isNotEmpty(name)) {
            sql += "AND action_plan.name like :name\n";
            params.put("name", "%" + name + "%");
        }
        if (StringUtils.isNotEmpty(executorName)) {
            sql += "AND executor.display_name like :executorName\n";
            params.put("executorName", "%" + executorName + "%");
        }

        sql += "ORDER BY action_plan.add_time";

        List<ActionPlan> actionPlans = actionPlanBaseService.findSQL(
                sql, params, ActionPlan.class, page.getCurrentPage(), page.getPageSize()
        );
        attributes.put("actionPlans", actionPlans);

        setObjectNum(sql, params);
        attributes.put("page", page);

        return new Json(JsonMessage.SUCCESS, attributes);
    }

    @RequestMapping("/userActionPlanList")
    public Json userActionPlanList() throws CustomException {
        Map<String, Object> attributes = new HashMap<>();

        // 查询自己及下级组织的用户
        String userLowestLevelOrganizationId = getUserLowestLevelOrganizationId();
        List<Organization> organizations = organizationClientService.getByParentId(userLowestLevelOrganizationId);
        List<ActionPlanUserStatistics> actionPlanUserStatistics = new ArrayList<>();
        actionPlanUserStatistics.add(new ActionPlanUserStatistics(getUser(), organizationClientService.getById(userLowestLevelOrganizationId), new ArrayList<>()));
        // 临时代码
        for (Organization organization : organizations)
            for (User user : userService.getByOrganizationId(organization.getId()))
                actionPlanUserStatistics.add(new ActionPlanUserStatistics(user, organization, new ArrayList<>()));

        attributes.put("actionPlanUserStatistics", actionPlanUserStatistics);

        return new Json(JsonMessage.SUCCESS, attributes);
    }

    @RequestMapping("/detail")
    public Json detail() {
        Map<String, Object> attributes = new HashMap<>();

        String id = pageActionPlan.getId();
        ActionPlan actionPlan = actionPlanService.getById(id);
        if (actionPlan == null) {
            actionPlan = new ActionPlan();
        }

        attributes.put("actionPlan", actionPlan);

        return new Json(JsonMessage.SUCCESS, attributes);
    }

    @RequestMapping("/saveOrUpdate")
    public Json saveOrUpdate() throws CustomException {
        checkParametersWithErrorMsg(
                "name", "行动计划名称",
                "origin", "行动计划来源",
                "client", "行动计划客户",
                "purpose", "行动计划目标",
                "target", "行动计划目标",
                "planStartTime", "开始时间",
                "planEndTime", "完成时间"
        );
        User user = getUser();
        pageActionPlan.setExecuteUserId(user.getId());
        pageActionPlan.setOrganizationId(getUserLowestLevelOrganizationId());
        actionPlanService.saveOrUpdate(pageActionPlan);

        return new Json(JsonMessage.SUCCESS);
    }

    @RequestMapping("/saveOrUpdateThenSubmit")
    public Json saveOrUpdateThenSubmit() throws CustomException {
        checkParametersWithErrorMsg(
                "name", "行动计划名称",
                "origin", "行动计划来源",
                "client", "行动计划客户",
                "purpose", "行动计划目标",
                "target", "行动计划目标",
                "planStartTime", "开始时间",
                "planEndTime", "完成时间"
        );
        User user = getUser();

        pageActionPlan.setExecuteUserId(user.getId());
        pageActionPlan.setOrganizationId(getUserLowestLevelOrganizationId());
        ActionPlan actionPlan = actionPlanService.saveOrUpdate(pageActionPlan);

        actionPlanService.submit(actionPlan.getId());
        return new Json(JsonMessage.SUCCESS);
    }

    @ModelAttribute
    public void setPageActionPlan(ActionPlan pageActionPlan) {
        this.pageActionPlan = pageActionPlan;
    }
}
