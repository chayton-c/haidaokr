package com.yingda.lkj.controller.backstage;

import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionControlPlan;
import com.yingda.lkj.beans.system.Json;
import com.yingda.lkj.beans.system.JsonMessage;
import com.yingda.lkj.controller.BaseController;
import com.yingda.lkj.service.backstage.constructioncontrolplan.FrontStageIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/backstage/frontStageIndex")
@RestController
public class FrontStageIndexController extends BaseController {

    @Autowired
    FrontStageIndexService frontStageIndexService;

    @RequestMapping("")
    public Json getIndexPlanStatusNumber(){
        return new Json(JsonMessage.SUCCESS, Map.of(
                "pendingRelevancePlans",frontStageIndexService.getPendingRelevancePlans(),
                "pendingStartPlans",frontStageIndexService.getPendingStartPlans(),
                "constructingPlans",frontStageIndexService.getConstructingPlans()
        ));
    }
}
