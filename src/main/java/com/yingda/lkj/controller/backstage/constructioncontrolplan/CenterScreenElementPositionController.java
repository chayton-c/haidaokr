package com.yingda.lkj.controller.backstage.constructioncontrolplan;

import com.yingda.lkj.beans.entity.backstage.centerscreen.CenterScreenElementPosition;
import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionControlPlan;
import com.yingda.lkj.beans.system.Json;
import com.yingda.lkj.beans.system.JsonMessage;
import com.yingda.lkj.service.backstage.centerscreen.CenterScreenElementPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("backstage/centerScreenElementPosition")
@RestController
public class CenterScreenElementPositionController {

    @Autowired
    private CenterScreenElementPositionService centerScreenElementPositionService;

    private CenterScreenElementPosition pageCenterScreenElementPosition;

    @RequestMapping("/save")
    public Json save(){
        centerScreenElementPositionService.saveOrUpdate(pageCenterScreenElementPosition);
        return new Json(JsonMessage.SUCCESS);
    }

    @ModelAttribute
    public void setPageCenterScreenElementPosition(CenterScreenElementPosition pageCenterScreenElementPosition) {
        this.pageCenterScreenElementPosition = pageCenterScreenElementPosition;
    }
}
