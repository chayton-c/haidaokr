package com.yingda.lkj.service.impl.backstage.equipment;

import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionControlPlanEquipment;
import com.yingda.lkj.beans.entity.backstage.equipment.Equipment;
import com.yingda.lkj.beans.entity.backstage.location.Location;
import com.yingda.lkj.beans.system.Json;
import com.yingda.lkj.beans.system.JsonMessage;
import com.yingda.lkj.dao.BaseDao;
import com.yingda.lkj.service.backstage.constructioncontrolplan.ConstructionControlPlanEquipmentService;
import com.yingda.lkj.service.backstage.constructioncontrolplan.WarningInfoService;
import com.yingda.lkj.service.backstage.equipment.EquipmentService;
import com.yingda.lkj.service.backstage.location.LocationService;
import com.yingda.lkj.service.backstage.organization.OrganizationClientService;
import com.yingda.lkj.utils.StreamUtil;
import com.yingda.lkj.utils.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service("equipmentService")
public class EquipmentServiceImpl implements EquipmentService {

    @Autowired
    private BaseDao<Equipment> equipmentBaseDao;
    @Autowired
    private OrganizationClientService organizationClientService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private ConstructionControlPlanEquipmentService constructionControlPlanEquipmentService;


    @Override
    public Equipment getById(String id) {
        if (id == null) return null;

        return equipmentBaseDao.get(Equipment.class, id);
    }

    @Override
    public List<Equipment> showDown() {
        return equipmentBaseDao.find("from Equipment");
    }

    @Override
    public List<String> imeis() {
        return showDown().stream().map(Equipment::getImei).collect(Collectors.toList());
    }

    @Override
    public List<Equipment> getByIds(List<String> ids) {
        if (ids == null || ids.isEmpty()) return new ArrayList<>();

        return equipmentBaseDao.find(
                "from Equipment where id in :ids",
                Map.of("ids", ids)
        );
    }

    @Override
    public Equipment getByImei(String imei) {
        return equipmentBaseDao.get(
                "from Equipment where imei = :imei",
                Map.of("imei", imei)
        );
    }

    @Override
    public void fillExtendInfo(Equipment equipment) {
        String workshopId = equipment.getWorkshopId();
        if (StringUtils.isNotEmpty(workshopId))
            equipment.setWorkshopName(organizationClientService.getById(workshopId).getName());
    }

    @Override
    public void saveOrUpdate(Equipment pageEquipment) {
        String id = pageEquipment.getId();

        Equipment equipment = StringUtils.isNotEmpty(id) ? getById(id) : new Equipment();
        BeanUtils.copyProperties(pageEquipment, equipment, "id", "addTime");
        if (StringUtils.isEmpty(equipment.getId()))
            equipment.setId(UUID.randomUUID().toString());
        if (equipment.getAddTime() == null)
            equipment.setAddTime(new Timestamp(System.currentTimeMillis()));

        equipmentBaseDao.saveOrUpdate(equipment);
    }

    @Autowired
    private WarningInfoService warningInfoService;
    @Override
    public Json checkImei(String imei) {
        return new Json(JsonMessage.SUCCESS);
    }

    @Override
    public void recordLocation(String imei, double longitude, double latitude) throws Exception {
        if (longitude == 0 && latitude == 0) return;

        Equipment equipment = getByImei(imei);
        if (equipment == null) return;
        locationService.saveOrUpdate(equipment.getId(), longitude, latitude, Location.EQUIPMENT);

        warningInfoService.checkEquipmentOnContructionControlPlan(equipment.getId());
    }

    @Override
    public List<Equipment> getByConstructionControlPlanId(String constructionControlPlanId) {
        List<ConstructionControlPlanEquipment> constructionDailyPlanEquipments =
                constructionControlPlanEquipmentService.getByConstructionControlPlanId(constructionControlPlanId);

        List<String> equipmentIds = StreamUtil.getList(constructionDailyPlanEquipments, ConstructionControlPlanEquipment::getEquipmentId);

        return getByIds(equipmentIds);
    }
}
