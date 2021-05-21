package com.yingda.lkj.controller.app.init;

import com.yingda.lkj.beans.entity.backstage.line.StationRailwayLine;
import com.yingda.lkj.beans.system.Json;
import com.yingda.lkj.beans.system.JsonMessage;
import com.yingda.lkj.controller.BaseController;
import com.yingda.lkj.service.backstage.line.RailwayLineService;
import com.yingda.lkj.service.backstage.line.StationService;
import com.yingda.lkj.service.backstage.opc.OpcMarkTypeService;
import com.yingda.lkj.service.backstage.opc.OpcTypeService;
import com.yingda.lkj.service.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/app/init/baseData")
public class InitBaseDataController extends BaseController {
    @Autowired
    private StationService stationService;
    @Autowired
    private BaseService<StationRailwayLine> stationRailwayLineBaseService;
    @Autowired
    private RailwayLineService railwayLineService;
    @Autowired
    private OpcMarkTypeService opcMarkTypeService;
    @Autowired
    private OpcTypeService opcTypeService;

    @RequestMapping("")
    @ResponseBody
    public Json getStations() throws Exception {
        Map<String, Object> attributes = new HashMap<>();

        attributes.put("railwayLines", railwayLineService.getAll());
        attributes.put("stations", stationService.getAll());
        attributes.put("stationRailwayLines", stationRailwayLineBaseService.find("from StationRailwayLine"));
        attributes.put("opcTypes", opcTypeService.getAll());
        attributes.put("opcMarkTypes", opcMarkTypeService.getAll());

        return new Json(JsonMessage.SUCCESS, attributes);
    }
}
