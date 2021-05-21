package com.yingda.lkj.controller.backstage;

import com.yingda.lkj.beans.entity.backstage.line.RailwayLineSection;
import com.yingda.lkj.beans.system.Json;
import com.yingda.lkj.beans.system.JsonMessage;
import com.yingda.lkj.controller.BaseController;
import com.yingda.lkj.service.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/backstage/railwayLineSection")
public class RaliwayLineSectionController extends BaseController {

    @Autowired
    private BaseService<RailwayLineSection> railwayLineSectionBaseService;

    @RequestMapping("")
    public Json getList() throws Exception {
        Map<String, Object> attributes = new HashMap<>();

        String sql = """
                 SELECT
                 	railwayLineSection.id AS id,
                 	railwayLineSection.railway_line_id AS railwayLineId,
                 	railwayLineSection.left_station_id AS leftStationId,
                 	railwayLineSection.right_station_id AS rightStationId,
                 	railwayLineSection.downriver AS downriver,
                 	railwayLineSection.add_time AS addTime,
                 	railwayLine.`name` AS railwayLineName,
                 	railwayLine.`code` AS railwayLineCode,
                 	leftStation.`name` AS leftStationName,
                 	leftStation.`code` AS leftStationCode,
                 	rightStation.`name` AS rightStationName,
                 	rightStation.`code` AS rightStationCode\s
                 FROM
                 	railway_line_section AS railwayLineSection
                 	LEFT JOIN station AS leftStation ON railwayLineSection.left_station_id = leftStation.id
                 	LEFT JOIN station AS rightStation ON railwayLineSection.right_station_id = rightStation.id
                 	LEFT JOIN railway_line AS railwayLine ON railwayLineSection.railway_line_id = railwayLine.id
                """;
        Map<String, Object> params = new HashMap<>();

        sql += "ORDER BY railwayLineSection.add_time desc";

        List<RailwayLineSection> railwayLineSections = railwayLineSectionBaseService.findSQL(
                sql, params, RailwayLineSection.class, page.getCurrentPage(), page.getPageSize()
        );

        attributes.put("railwayLineSections", railwayLineSections);

        return new Json(JsonMessage.SUCCESS, attributes);
    }

}
