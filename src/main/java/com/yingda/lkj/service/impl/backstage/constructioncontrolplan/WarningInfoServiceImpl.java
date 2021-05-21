package com.yingda.lkj.service.impl.backstage.constructioncontrolplan;

import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionControlPlan;
import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionControlPlanEquipment;
import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionControlPlanKilometerMark;
import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.WarningInfo;
import com.yingda.lkj.beans.entity.backstage.equipment.Equipment;
import com.yingda.lkj.beans.entity.backstage.location.Location;
import com.yingda.lkj.beans.entity.backstage.opc.Opc;
import com.yingda.lkj.beans.enums.constructioncontrolplan.WarnLevel;
import com.yingda.lkj.beans.exception.CustomException;
import com.yingda.lkj.beans.system.JsonMessage;
import com.yingda.lkj.dao.BaseDao;
import com.yingda.lkj.service.backstage.constructioncontrolplan.*;
import com.yingda.lkj.service.backstage.equipment.EquipmentService;
import com.yingda.lkj.service.backstage.location.LocationService;
import com.yingda.lkj.service.backstage.opc.OpcService;
import com.yingda.lkj.service.backstage.sms.SmsService;
import com.yingda.lkj.service.base.BaseService;
import com.yingda.lkj.socket.WarningInfoWebSocket;
import com.yingda.lkj.utils.StreamUtil;
import com.yingda.lkj.utils.StringUtils;
import com.yingda.lkj.utils.location.LocationUtil;
import com.yingda.lkj.utils.math.Arithmetic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("warningInfoService")
public class WarningInfoServiceImpl implements WarningInfoService {

    @Autowired
    private BaseDao<WarningInfo> warningInfoBaseDao;
    @Autowired
    private BaseService<WarningInfo> warningInfoBaseService;
    @Autowired
    private EquipmentService equipmentService;
    @Autowired
    private ConstructionDailyPlanService constructionDailyPlanService;
    @Autowired
    private ConstructionControlPlanKilometerMarkService constructionControlPlanKilometerMarkService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private OpcService opcService;
    @Autowired
    private ConstructionControlPlanService constructionControlPlanService;
    @Autowired
    private ConstructionControlPlanEquipmentService constructionControlPlanEquipmentService;
    @Autowired
    private WarningInfoWebSocket warningInfoWebSocket;
    @Autowired
    private SmsService smsService;

    @Override
    public WarningInfo getById(String id) {
        return warningInfoBaseDao.get(WarningInfo.class, id);
    }

    @Override
    public List<WarningInfo> sendWarningInfoToWebsocket() throws Exception {
        Map<String, Object> params = new HashMap<>();
        String sql = """
                SELECT
                	warningInfo.id AS id,
                	MAX( warningInfo.add_time ) AS addTime,
                	warningInfo.process_result AS processResult,
                	warningInfo.process_status AS processStatus,
                	warningInfo.warn_info AS warnInfo,
                	warningInfo.warn_level AS warnLevel,
                	warningInfo.construction_daily_plan_id AS constructionDailyPlanId,
                	warningInfo.equipment_id AS equipmentId,
                	dailyPlan.`code` AS constructionDailyPlanCode,
                	controlPlan.`code` AS constructionControlPlanCode,
                	controlPlan.construction_project_info AS constructionControlPlanProjectInfo,
                	equipment.imei AS equipmentImei\s
                FROM
                	warning_info AS warningInfo
                	LEFT JOIN construction_daily_plan AS dailyPlan ON warningInfo.construction_daily_plan_id = dailyPlan.id
                	LEFT JOIN equipment ON warningInfo.equipment_id = equipment.id
                	LEFT JOIN construction_control_plan AS controlPlan ON dailyPlan.construction_control_plan_id = controlPlan.id\s
                GROUP BY
                	warningInfo.equipment_id,
                	dailyPlan.id\s
                HAVING
                	warningInfo.warn_level <> 4\s
                ORDER BY
                	warningInfo.add_time DESC
                """;

//        params.put("warnLevel", WarningInfo.LEVEL5);
//        params.put("processStatus",WarningInfo.CLOSED);
        return warningInfoBaseDao.findSQL(sql, params, WarningInfo.class);
    }

    @Override
    public void checkEquipmentOnContructionControlPlan(String equipmentId) throws Exception {
        Equipment equipment = equipmentService.getById(equipmentId);
        // 最新的位置信息(不需要在意并发，因为校验的就是最新的位置，而不是新添加的位置)
        Location lastLocation = locationService.getLastLocation(equipment);

        // 所有使用该设备的进行中的方案
        List<ConstructionControlPlanEquipment> constructionControlPlanEquipments =
                constructionControlPlanEquipmentService.getByEquipmentId(equipmentId);

        for (ConstructionControlPlanEquipment constructionControlPlanEquipment : constructionControlPlanEquipments) {
            double influenceRadius = constructionControlPlanEquipment.getInfluenceRadius();
            ConstructionControlPlan constructionControlPlan =
                    constructionControlPlanService.getById(constructionControlPlanEquipment.getConstructionControlPlanId());
            String constructionControlPlanId = constructionControlPlan.getId();
            List<ConstructionControlPlanKilometerMark> constructionControlPlanKilometerMarks =
                    constructionControlPlanKilometerMarkService.getByConstructionControlPlanId(constructionControlPlanId);
            List<String> stationIds = StreamUtil.getList(constructionControlPlanKilometerMarks, ConstructionControlPlanKilometerMark::getStartStationId);

            List<Opc> opcs = opcService.getLineDataByStationIds(stationIds);
            locationService.fillLocations(opcs);
            // 设备的最新位置到日计划上所有光缆的最近距离
            double shortestDistance = LocationUtil.pointToMultiLineShortestDistance(lastLocation, opcs);
            shortestDistance = shortestDistance - influenceRadius > 0 ? shortestDistance - influenceRadius : 0;

            // 获取最新的预警信息
            WarningInfo warningInfo = getByEquipmentIdAndConstructionControlPlanId(equipmentId, constructionControlPlanId);
            warningInfo = warningInfo == null ? new WarningInfo(constructionControlPlanId, equipmentId, shortestDistance) : warningInfo;

            WarnLevel warnLevel = WarnLevel.getByDistance(shortestDistance);

            // 只有当预警等级发生变化时才添加
            if ((byte) warnLevel.level == warningInfo.getWarnLevel()) continue;

            warningInfo = new WarningInfo(constructionControlPlanId, equipmentId, shortestDistance);
            WarnLevel.fillWarnInfoByDistance(warningInfo, shortestDistance, constructionControlPlan.getConstructionProjectInfo());
            warningInfoBaseDao.saveOrUpdate(warningInfo);

            // 每当有预警产生，通知前端并返回车站ID
            stationIds.forEach(x -> warningInfoWebSocket.sendMessage(x));
        }
    }

    @Override
    public void processWarningBySendSms(String warningInfoId) throws Exception {
        WarningInfo warningInfo = getById(warningInfoId);
        Equipment equipment = equipmentService.getById(warningInfo.getEquipmentId());
        String linkmanPhoneNumber = equipment.getLinkmanPhoneNumber();
        if (StringUtils.isEmpty(linkmanPhoneNumber))
            throw new CustomException(JsonMessage.DATA_NO_COMPLETE, String.format("编号为：%s的设备尚未填写联系人手机号", equipment.getImei()));
        WarnLevel warnLevel = WarnLevel.getByLevel(warningInfo.getWarnLevel());
        smsService.send(linkmanPhoneNumber, warnLevel.color, equipment.getImei(),
                Arithmetic.cheng(warningInfo.getDistanceFromNearestOpc(), 1, 2) + "");
        warningInfo.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        warningInfo.setProcessResult("已发送短息");
        warningInfo.setProcessStatus(WarningInfo.PROCESSED);
    }

    @Override
    public void processWarningByDoNothing(String warningInfoId) {
        WarningInfo warningInfo = getById(warningInfoId);
        warningInfo.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        warningInfo.setProcessResult("已手动处理");
        warningInfo.setProcessStatus(WarningInfo.PROCESSED);
    }

    private WarningInfo getByEquipmentIdAndConstructionControlPlanId(String equipmentId, String constructionControlPlanId) throws Exception {
        Map<String, Object> params = new HashMap<>();
        Map<String, String> conditions = new HashMap<>();
        params.put("equipmentId", equipmentId);
        params.put("constructionControlPlanId", constructionControlPlanId);
        conditions.put("equipmentId", "=");
        conditions.put("constructionControlPlanId", "=");

        // 最新的预警信息
        List<WarningInfo> newestWarningInfos = warningInfoBaseService.getObjcetPagination(
                WarningInfo.class, params, conditions, 1, 1, "order by addTime desc");
        if (newestWarningInfos.size() > 0)
            return newestWarningInfos.get(0);

        return null;
    }
}
