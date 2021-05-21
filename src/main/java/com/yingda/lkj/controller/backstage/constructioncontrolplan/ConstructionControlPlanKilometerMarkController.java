package com.yingda.lkj.controller.backstage.constructioncontrolplan;

import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionControlPlanKilometerMark;
import com.yingda.lkj.beans.system.Json;
import com.yingda.lkj.beans.system.JsonMessage;
import com.yingda.lkj.controller.BaseController;
import com.yingda.lkj.service.backstage.constructioncontrolplan.ConstructionControlPlanKilometerMarkService;
import com.yingda.lkj.service.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/backstage/constructionControlPlanKilometerMark")
@RestController
public class ConstructionControlPlanKilometerMarkController extends BaseController {

    @Autowired
    private BaseService<ConstructionControlPlanKilometerMark> constructionControlPlanKilometerMarkBaseService;
    @Autowired
    private ConstructionControlPlanKilometerMarkService constructionControlPlanKilometerMarkService;

    private ConstructionControlPlanKilometerMark pageConstructionControlPlanKilometerMark;

    @RequestMapping("/getList")
    public Json getList() throws Exception {
        Map<String, Object> attributes = new HashMap<>();

        String constructionControlPlanId = req.getParameter("constructionControlPlanId");

        String sql = """
                SELECT
                    constructionControlPlanKilometerMark.*,
                    railwayLine.name AS railwayLineName,
                    startStation.name AS startStationName,
                    endStation.name AS endStationName
                FROM
                    construction_control_plan_kilometer_mark constructionControlPlanKilometerMark
                    LEFT JOIN railway_line railwayLine ON railwayLine.id = constructionControlPlanKilometerMark.railway_line_id
                    LEFT JOIN station startStation ON startStation.id = constructionControlPlanKilometerMark.start_station_id
                    LEFT JOIN station endStation ON endStation.id = constructionControlPlanKilometerMark.end_station_id
                WHERE
                    constructionControlPlanKilometerMark.construction_control_plan_id = :constructionControlPlanId
                """;
        Map<String, Object> params = new HashMap<>();
        params.put("constructionControlPlanId", constructionControlPlanId);

        List<ConstructionControlPlanKilometerMark> constructionControlPlanKilometerMarks = constructionControlPlanKilometerMarkBaseService.findSQL(
                sql, params, ConstructionControlPlanKilometerMark.class
        );

        attributes.put("constructionControlPlanKilometerMarks", constructionControlPlanKilometerMarks);
        return new Json(JsonMessage.SUCCESS, attributes);
    }

    @RequestMapping("/saveOrUpdate")
    public Json saveOrUpdate() {
        constructionControlPlanKilometerMarkService.saveOrUpdate(pageConstructionControlPlanKilometerMark);
        return new Json(JsonMessage.SUCCESS);
    }

    @RequestMapping("/delete")
    public Json delete() {
        String id = req.getParameter("id");
        constructionControlPlanKilometerMarkService.delete(id);
        return new Json(JsonMessage.SUCCESS);
    }

    /**
     * 关闭计划详情页时，检查无效的kilometerMark中间表
     */
    @RequestMapping("/checkDeleteWhenDestropPage")
    public Json checkDeleteWhenDestropPage() {
        String constructionControlPlanId = req.getParameter("constructionControlPlanId");
        constructionControlPlanKilometerMarkService.checkDeleteWhenDestropPage(constructionControlPlanId);
        return new Json(JsonMessage.SUCCESS);
    }

    @ModelAttribute
    public void setPageConstructionControlPlanKilometerMark(ConstructionControlPlanKilometerMark pageConstructionControlPlanKilometerMark) {
        this.pageConstructionControlPlanKilometerMark = pageConstructionControlPlanKilometerMark;
    }
}
