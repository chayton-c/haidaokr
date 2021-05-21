package com.yingda.lkj.controller.app.upload;

import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionControlPlan;
import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionControlPlanPoint;
import com.yingda.lkj.beans.entity.backstage.location.Location;
import com.yingda.lkj.beans.enums.constructioncontrolplan.WarnLevel;
import com.yingda.lkj.beans.pojo.constructioncontrolplan.AppConstructionControlPlanPoint;
import com.yingda.lkj.beans.system.Json;
import com.yingda.lkj.beans.system.JsonMessage;
import com.yingda.lkj.controller.BaseController;
import com.yingda.lkj.service.backstage.constructioncontrolplan.AsyncConstructionControlPlanService;
import com.yingda.lkj.service.backstage.constructioncontrolplan.ConstructionControlPlanPointService;
import com.yingda.lkj.service.backstage.constructioncontrolplan.ConstructionControlPlanService;
import com.yingda.lkj.service.backstage.location.LocationService;
import com.yingda.lkj.service.backstage.opc.OpcService;
import com.yingda.lkj.service.base.BaseService;
import com.yingda.lkj.utils.JsonUtils;
import com.yingda.lkj.utils.StreamUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/app/upload/constructionControlPlan")
public class UploadConstructionControlPlanController extends BaseController {

    @Autowired
    private BaseService<Location> locationBaseService;
    @Autowired
    private BaseService<ConstructionControlPlanPoint> constructionControlPlanPointBaseService;
    @Autowired
    private BaseService<ConstructionControlPlan> constructionControlPlanBaseService;
    @Autowired
    private ConstructionControlPlanPointService constructionControlPlanPointService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private OpcService opcService;
    @Autowired
    private ConstructionControlPlanService constructionControlPlanService;
    @Autowired
    private AsyncConstructionControlPlanService asyncConstructionControlPlanService;

    @RequestMapping("")
    public Json importConstructionControlPlanPoint() throws Exception {
        String constructionControlPlanPointsStr = req.getParameter("constructionControlPlanPoints");
        String locationsStr = req.getParameter("locations");

        List<AppConstructionControlPlanPoint> appConstructionControlPlanPoints = JsonUtils.parseList(constructionControlPlanPointsStr,
                AppConstructionControlPlanPoint.class);
        List<ConstructionControlPlanPoint> constructionControlPlanPoints = StreamUtil.getList(appConstructionControlPlanPoints,
                ConstructionControlPlanPoint::new);
        List<String> constructionControlPlanIds = StreamUtil.getList(constructionControlPlanPoints,
                ConstructionControlPlanPoint::getConstructionControlPlanId);
        constructionControlPlanService.finishedInvestigate(constructionControlPlanIds);

        constructionControlPlanPointBaseService.bulkInsert(constructionControlPlanPoints);

        List<Location> locations = JsonUtils.parseList(locationsStr, Location.class);
        locations.forEach(x -> x.setAddTime(current()));
        locationBaseService.bulkInsert(locations);

        // 重置施工点对应的方案的风险等级
        List<ConstructionControlPlan> constructionControlPlans = constructionControlPlanBaseService.find(
                "from ConstructionControlPlan where id in :ids",
                Map.of("ids", constructionControlPlanIds)
        );
        for (ConstructionControlPlan constructionControlPlan : constructionControlPlans) {
            constructionControlPlan.setWarnStatus((byte) WarnLevel.LEVEL5.level);
            constructionControlPlanBaseService.saveOrUpdate(constructionControlPlan);
        }

        // 校验风险等级
        for (ConstructionControlPlanPoint constructionControlPlanPoint : constructionControlPlanPoints) {
            asyncConstructionControlPlanService.checkWarnStatus(constructionControlPlanPoint);
        }

        return new Json(JsonMessage.SUCCESS);
    }


}
