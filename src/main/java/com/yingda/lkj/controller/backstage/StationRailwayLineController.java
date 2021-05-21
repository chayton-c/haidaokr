package com.yingda.lkj.controller.backstage;

import com.yingda.lkj.beans.system.Json;
import com.yingda.lkj.beans.system.JsonMessage;
import com.yingda.lkj.controller.BaseController;
import com.yingda.lkj.service.backstage.line.StationRailwayLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/backstage/stationRailwayLine")
public class StationRailwayLineController extends BaseController {

    @Autowired
    private StationRailwayLineService stationRailwayLineService;

    @RequestMapping("/updateSeq")
    public Json updateSeq() {
        String stationId = req.getParameter("stationId");
        String railwayLineId = req.getParameter("railwayLineId");
        String seq = req.getParameter("seq");

        stationRailwayLineService.updateSeq(stationId, railwayLineId, Integer.parseInt(seq));

        return new Json(JsonMessage.SUCCESS);
    }

}
