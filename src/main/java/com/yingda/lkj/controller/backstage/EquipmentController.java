package com.yingda.lkj.controller.backstage;

import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionControlPlan;
import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionControlPlanEquipmentHistory;
import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionControlPlanKilometerMark;
import com.yingda.lkj.beans.entity.backstage.equipment.Equipment;
import com.yingda.lkj.beans.entity.backstage.location.Location;
import com.yingda.lkj.beans.entity.backstage.opc.Opc;
import com.yingda.lkj.beans.entity.backstage.organization.Organization;
import com.yingda.lkj.beans.exception.CustomException;
import com.yingda.lkj.beans.system.Json;
import com.yingda.lkj.beans.system.JsonMessage;
import com.yingda.lkj.controller.BaseController;
import com.yingda.lkj.service.backstage.constructioncontrolplan.ConstructionControlPlanEquipmentHistoryService;
import com.yingda.lkj.service.backstage.constructioncontrolplan.ConstructionControlPlanKilometerMarkService;
import com.yingda.lkj.service.backstage.constructioncontrolplan.ConstructionControlPlanService;
import com.yingda.lkj.service.backstage.constructioncontrolplan.ConstructionDailyPlanService;
import com.yingda.lkj.service.backstage.equipment.EquipmentService;
import com.yingda.lkj.service.backstage.location.LocationService;
import com.yingda.lkj.service.backstage.opc.OpcService;
import com.yingda.lkj.service.backstage.organization.OrganizationClientService;
import com.yingda.lkj.service.base.BaseService;
import com.yingda.lkj.utils.StringUtils;
import com.yingda.lkj.utils.location.LocationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/backstage/equipment")
@RestController
public class EquipmentController extends BaseController {

    @Autowired
    private BaseService<Equipment> equipmentBaseService;
    @Autowired
    private EquipmentService equipmentService;
    @Autowired
    private OrganizationClientService organizationClientService;
    @Autowired
    private ConstructionControlPlanKilometerMarkService constructionControlPlanKilometerMarkService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private OpcService opcService;
    @Autowired
    private ConstructionDailyPlanService constructionDailyPlanService;
    @Autowired
    private ConstructionControlPlanService constructionControlPlanService;
    @Autowired
    private ConstructionControlPlanEquipmentHistoryService constructionControlPlanEquipmentHistoryService;

    private Equipment pageEquipment;

    @RequestMapping("/getList")
    public Json getList() throws Exception {
        Map<String, Object> attributes = new HashMap<>();

        String workshopId = req.getParameter("workshopId");
        String nameOrCode = req.getParameter("nameOrCode");

        String sql = """
                SELECT
                	equipment.id,
                	equipment.imei,
                	equipment.equipment_type as equipmentType,
                	equipment.workshop_id AS workshopId,
                	equipment.`name`,
                	equipment.`status`,
                	equipment.add_time AS addTime,
                	equipment.update_time AS updateTime,
                	equipment.linkman_phone_number AS linkmanPhoneNumber,
                	workshop.`name` AS workshopName\s
                FROM
                	equipment AS equipment
                	LEFT JOIN organization AS workshop ON equipment.workshop_id = workshop.id
                WHERE
                    1 = 1
                """;
        Map<String, Object> params = new HashMap<>();
        sql += createDataAuthSql("equipment", Organization.WORKSHOP, params);
        if (StringUtils.isNotEmpty(workshopId)) {
            sql += "AND equipment.workshop_id = :workshopId\n";
            params.put("workshopId", workshopId);
        }
        if (StringUtils.isNotEmpty(nameOrCode)) {
            sql += "AND (equipment.name like :nameOrCode OR equipment.imei like :nameOrCode)\n";
            params.put("nameOrCode", "%" + nameOrCode + "%");
        }

        sql += "ORDER BY equipment.add_time desc";
        List<Equipment> equipments = equipmentBaseService.findSQL(
                sql, params, Equipment.class, page.getCurrentPage(), page.getPageSize()
        );

        attributes.put("equipments", equipments);
        setObjectNum(sql, params);
        attributes.put("page", page);
        attributes.put("workshops", organizationClientService.getWorkshopsByUser(getUser()));

        return new Json(JsonMessage.SUCCESS, attributes);
    }

    @RequestMapping("/getListByConstructionControlPlan")
    public Json getListByConstructionControlPlan() throws Exception {
        Map<String, Object> attributes = new HashMap<>();

        String constructionControlPlanId = req.getParameter("constructionControlPlanId");

        String workshopId = req.getParameter("workshopId");
        String name = req.getParameter("name");
        String using = req.getParameter("using");

        String sql = """
                SELECT
                	equipment.id,
                	equipment.imei,
                	equipment.equipment_type as equipmentType,
                	equipment.workshop_id AS workshopId,
                	equipment.`name`,
                	equipment.`status`,
                	equipment.add_time AS addTime,
                	equipment.update_time AS updateTime,
                	IFNULL(constructionControlPlanEquipment.influence_radius, 0) AS influenceRadius,
                	IFNULL(constructionControlPlanEquipment.construction_control_plan_id, '') AS constructionControlPlanId,
                	workshop.`name` AS workshopName\s
                FROM
                	equipment AS equipment
                	LEFT JOIN organization AS workshop ON equipment.workshop_id = workshop.id
                	LEFT JOIN construction_control_plan_equipment constructionControlPlanEquipment ON equipment.id = constructionControlPlanEquipment.equipment_id AND constructionControlPlanEquipment.construction_control_plan_id = :constructionControlPlanId
                WHERE
                    (constructionControlPlanEquipment.construction_control_plan_id = :constructionControlPlanId OR constructionControlPlanEquipment.id IS NULL)
                """;
        Map<String, Object> params = new HashMap<>();
        params.put("constructionControlPlanId", constructionControlPlanId);
        sql += createDataAuthSql("equipment", Organization.WORKSHOP, params);
        if (StringUtils.isNotEmpty(workshopId)) {
            sql += "AND equipment.workshop_id = :workshopId\n";
            params.put("workshopId", workshopId);
        }
        if (StringUtils.isNotEmpty(name)) {
            sql += "AND equipment.name like :name\n";
            params.put("name", "%" + name + "%");
        }
        if (StringUtils.isNotEmpty(using)) {
            if (Boolean.parseBoolean(using))
                sql += "AND constructionControlPlanEquipment.id IS NULL";
            if (!Boolean.parseBoolean(using))
                sql += "AND constructionControlPlanEquipment.id IS NOT NULL";
        }

        sql += "ORDER BY equipment.add_time desc";
        List<Equipment> equipments = equipmentBaseService.findSQL(
                sql, params, Equipment.class, page.getCurrentPage(), page.getPageSize()
        );
        equipments.forEach(x -> x.setUsing(StringUtils.isNotEmpty(x.getConstructionControlPlanId())));

        attributes.put("equipments", equipments);
        setObjectNum(sql, params);
        attributes.put("page", page);
        attributes.put("workshops", organizationClientService.getWorkshopsByUser(getUser()));

        return new Json(JsonMessage.SUCCESS, attributes);
    }

    @RequestMapping("/info")
    public Json info() throws CustomException {
        Map<String, Object> attributes = new HashMap<>();

        String id = req.getParameter("id");
        Equipment equipment = StringUtils.isNotEmpty(id) ? equipmentService.getById(id) : new Equipment();
        equipmentService.fillExtendInfo(equipment);

        attributes.put("equipment", equipment);
        attributes.put("workshops", organizationClientService.getWorkshopsByUser(getUser()));
        return new Json(JsonMessage.SUCCESS, attributes);
    }

    /**
     * 查询日计划使用的设备的实时位置
     */
    @RequestMapping("/getEquipmentLocationByConstructionControlPlanId") // 长
    public Json getEquipmentLocationByConstructionControlPlanId() {
        Map<String, Object> attributes = new HashMap<>();
        String constructionControlPlanId = req.getParameter("constructionControlPlanId");

        ConstructionControlPlan constructionControlPlan = constructionControlPlanService.getById(constructionControlPlanId);
        List<ConstructionControlPlanKilometerMark> constructionControlPlanKilometerMarks =
                constructionControlPlanKilometerMarkService.getByConstructionControlPlanId(constructionControlPlan.getId());
        List<String> associatedStationIds = new ArrayList<>();
        for (ConstructionControlPlanKilometerMark constructionControlPlanKilometerMark : constructionControlPlanKilometerMarks) {
            associatedStationIds.add(constructionControlPlanKilometerMark.getStartStationId());
            associatedStationIds.add(constructionControlPlanKilometerMark.getEndStationId());
        }

        List<Opc> opcs = opcService.getLineDataByStationIds(associatedStationIds);
        locationService.fillLocations(opcs);

        List<Equipment> equipments = equipmentService.getByConstructionControlPlanId(constructionControlPlanId);
        for (Equipment equipment : equipments) {
            ConstructionControlPlanEquipmentHistory constructionControlPlanEquipmentHistory =
                    constructionControlPlanEquipmentHistoryService.getByConstructionControlPlanIdAndEquipmentId(constructionControlPlanId
                            , equipment.getId());

            Timestamp startTime = constructionControlPlanEquipmentHistory.getStartTime();
            Timestamp endTime = constructionControlPlanEquipmentHistory.getEndTime();

            locationService.fillLocations(List.of(equipment), startTime, endTime);
            if (equipment.getLocations() == null) continue;
            Location lastLocation = equipment.getLocations().stream().reduce(null, (x, y) -> y);
            // 计算到光电缆距离
            double shortestDistance = LocationUtil.pointToMultiLineShortestDistance(lastLocation, opcs);
            equipment.setDistanceToOpc(shortestDistance);
        }
        attributes.put("equipments", equipments);

        return new Json(JsonMessage.SUCCESS, attributes);
    }

    @RequestMapping("/saveOrUpdate")
    public Json saveOrUpdate() {
        equipmentService.saveOrUpdate(pageEquipment);
        return new Json(JsonMessage.SUCCESS);
    }

    @RequestMapping("/checkImei")
    public Json checkImei() {
        return equipmentService.checkImei(req.getParameter("imei"));
    }

    @ModelAttribute
    public void setPageEquipment(Equipment pageEquipment) {
        this.pageEquipment = pageEquipment;
    }
}
