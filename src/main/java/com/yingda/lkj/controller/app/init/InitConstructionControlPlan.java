package com.yingda.lkj.controller.app.init;

import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionControlPlan;
import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionControlPlanKilometerMark;
import com.yingda.lkj.beans.entity.system.User;
import com.yingda.lkj.beans.system.Json;
import com.yingda.lkj.beans.system.JsonMessage;
import com.yingda.lkj.controller.BaseController;
import com.yingda.lkj.service.backstage.constructioncontrolplan.ConstructionControlPlanService;
import com.yingda.lkj.service.system.UserService;
import com.yingda.lkj.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/app/init/constructionControlPlan")
public class InitConstructionControlPlan extends BaseController {
    @Autowired
    private ConstructionControlPlanService constructionControlPlanService;
    @Autowired
    private UserService userService;

    @RequestMapping("")
    public Json getList() {
        Map<String, Object> attributes = new HashMap<>();

        List<ConstructionControlPlan> constructionControlPlans = constructionControlPlanService.issuePendingInvestigatePlans2App();
        for (ConstructionControlPlan constructionControlPlan : constructionControlPlans) {
            String workshopId = constructionControlPlan.getWorkshopId();
            if (StringUtils.isNotEmpty(workshopId)) {
                List<User> users = userService.getByOrganizationId(workshopId);
                constructionControlPlan.setExecutors(users);
            }
        }

        constructionControlPlanService.fillAssociatedInfos(constructionControlPlans);

        // 测量位置信息，返回给app逗号隔开
        for (ConstructionControlPlan constructionControlPlan : constructionControlPlans) {
            List<ConstructionControlPlanKilometerMark> constructionControlPlanKilometerMarks = constructionControlPlan.getConstructionControlPlanKilometerMarks();
            if (constructionControlPlanKilometerMarks.isEmpty()) continue;
            constructionControlPlan.setConstructionControlPlanKilometerMarkInfos(
                    constructionControlPlanKilometerMarks.stream().map(ConstructionControlPlanKilometerMark::getDetailInfo).collect(Collectors.joining(","))
            );
        }

        attributes.put("constructionControlPlans", constructionControlPlans);

        return new Json(JsonMessage.SUCCESS, attributes);
    }
}
