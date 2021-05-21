package com.yingda.lkj.service.backstage.equipment;

import com.yingda.lkj.beans.entity.backstage.equipment.Equipment;
import com.yingda.lkj.beans.system.Json;

import java.util.List;

public interface EquipmentService {

    List<Equipment> showDown();

    List<String> imeis();

    Equipment getById(String id);

    List<Equipment> getByIds(List<String> ids);

    Equipment getByImei(String imei);

    void fillExtendInfo(Equipment equipment);

    void saveOrUpdate(Equipment pageEquipment);

    Json checkImei(String imei);

    void recordLocation(String imei, double longitude, double latitude) throws Exception;

    List<Equipment> getByConstructionControlPlanId(String constructionControlPlanId);

}
