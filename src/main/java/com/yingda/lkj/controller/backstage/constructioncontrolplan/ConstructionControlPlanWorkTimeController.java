package com.yingda.lkj.controller.backstage.constructioncontrolplan;

import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionControlPlanWorkTime;
import com.yingda.lkj.beans.system.Json;
import com.yingda.lkj.beans.system.JsonMessage;
import com.yingda.lkj.controller.BaseController;
import com.yingda.lkj.service.backstage.constructioncontrolplan.ConstructionControlPlanWorkTimeService;
import com.yingda.lkj.service.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/backstage/constructionControlPlanWorkTime")
@RestController
public class ConstructionControlPlanWorkTimeController extends BaseController {

    @Autowired
    private BaseService<ConstructionControlPlanWorkTime> constructionControlPlanWorkTimeBaseService;
    @Autowired
    private ConstructionControlPlanWorkTimeService constructionControlPlanWorkTimeService;

    private ConstructionControlPlanWorkTime pageConstructionControlPlanWorkTime;

    @RequestMapping("/getList")
    public Json getList() throws Exception {
        Map<String, Object> attributes = new HashMap<>();

        String constructionControlPlanId = req.getParameter("constructionControlPlanId");

        String sql = """
                SELECT
                    constructionControlPlanWorkTime.*
                FROM
                    construction_control_plan_work_time constructionControlPlanWorkTime
                WHERE
                    constructionControlPlanWorkTime.construction_control_plan_id = :constructionControlPlanId
                """;
        Map<String, Object> params = new HashMap<>();
        params.put("constructionControlPlanId", constructionControlPlanId);

        List<ConstructionControlPlanWorkTime> constructionControlPlanWorkTimes = constructionControlPlanWorkTimeBaseService.findSQL(
                sql, params, ConstructionControlPlanWorkTime.class
        );

        attributes.put("constructionControlPlanWorkTimes", constructionControlPlanWorkTimes);
        return new Json(JsonMessage.SUCCESS, attributes);
    }

    @RequestMapping("/saveOrUpdate")
    public Json saveOrUpdate() {
        constructionControlPlanWorkTimeService.saveOrUpdate(pageConstructionControlPlanWorkTime);
        return new Json(JsonMessage.SUCCESS);
    }

    @RequestMapping("/delete")
    public Json delete() {
        String id = req.getParameter("id");
        constructionControlPlanWorkTimeService.delete(id);
        return new Json(JsonMessage.SUCCESS);
    }

    /**
     * 关闭计划详情页时，检查无效的workTime中间表
     */
    @RequestMapping("/checkDeleteWhenDestropPage")
    public Json checkDeleteWhenDestropPage() {
        String constructionControlPlanId = req.getParameter("constructionControlPlanId");
        constructionControlPlanWorkTimeService.checkDeleteWhenDestropPage(constructionControlPlanId);
        return new Json(JsonMessage.SUCCESS);
    }

    @ModelAttribute
    public void setPageConstructionControlPlanWorkTime(ConstructionControlPlanWorkTime pageConstructionControlPlanWorkTime) {
        this.pageConstructionControlPlanWorkTime = pageConstructionControlPlanWorkTime;
    }
}
