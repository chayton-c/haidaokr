package com.yingda.lkj.controller.app.init;

import com.yingda.lkj.beans.entity.backstage.equipment.Equipment;
import com.yingda.lkj.beans.system.Json;
import com.yingda.lkj.beans.system.JsonMessage;
import com.yingda.lkj.service.backstage.equipment.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequestMapping("/app/init/equipment")
@RestController
public class InitEquipmentController {

    @Autowired
    private EquipmentService equipmentService;

    @RequestMapping("/imeis")
    public Json imeis() {
        Map<String, Object> attributes = new HashMap<String, Object>();
        List<Equipment> equipments = equipmentService.showDown();
        attributes.put("imeis", equipments.stream().map(Equipment::getImei).collect(Collectors.joining(",")));

        return new Json(JsonMessage.SUCCESS, attributes);
    }
}
