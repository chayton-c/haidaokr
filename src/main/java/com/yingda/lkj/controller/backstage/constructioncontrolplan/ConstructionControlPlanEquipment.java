package com.yingda.lkj.controller.backstage.constructioncontrolplan;

import com.yingda.lkj.beans.system.Json;
import com.yingda.lkj.beans.system.JsonMessage;
import com.yingda.lkj.controller.BaseController;
import com.yingda.lkj.service.backstage.constructioncontrolplan.ConstructionControlPlanEquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/backstage/constructionConstrolPlanEquipment")
public class ConstructionControlPlanEquipment extends BaseController {
    @Autowired
    private ConstructionControlPlanEquipmentService constructionControlPlanEquipmentService;

    @RequestMapping("/addEquipment")
    public Json addEquipment() {
        String constructionControlPlanId = req.getParameter("constructionControlPlanId");
        String equipmentId = req.getParameter("equipmentId");
        String influenceRadius = req.getParameter("influenceRadius");

        constructionControlPlanEquipmentService.save(constructionControlPlanId, equipmentId, Double.parseDouble(influenceRadius));
        return new Json(JsonMessage.SUCCESS);
    }

    @RequestMapping("/removeEquipment")
    public Json removeEquipment() {
        String constructionControlPlanId = req.getParameter("constructionControlPlanId");
        String equipmentId = req.getParameter("equipmentId");

        constructionControlPlanEquipmentService.delete(constructionControlPlanId, equipmentId);
        return new Json(JsonMessage.SUCCESS);
    }

    @RequestMapping("/updateEquipmentInfluenceRadius")
    public Json updateEquipmentInfluenceRadius() {
        String constructionControlPlanId = req.getParameter("constructionControlPlanId");
        String equipmentId = req.getParameter("equipmentId");
        String influenceRadius = req.getParameter("influenceRadius");

        constructionControlPlanEquipmentService.updateEquipmentInfluenceRadius(constructionControlPlanId, equipmentId, Double.parseDouble(influenceRadius));

        return new Json(JsonMessage.SUCCESS);
    }
}
