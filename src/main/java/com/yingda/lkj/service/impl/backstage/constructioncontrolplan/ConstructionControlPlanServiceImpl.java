package com.yingda.lkj.service.impl.backstage.constructioncontrolplan;

import com.yingda.lkj.beans.Constant;
import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionControlPlan;
import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionControlPlanEquipment;
import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionControlPlanKilometerMark;
import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionControlPlanWorkTime;
import com.yingda.lkj.beans.exception.CustomException;
import com.yingda.lkj.beans.system.JsonMessage;
import com.yingda.lkj.dao.BaseDao;
import com.yingda.lkj.service.backstage.constructioncontrolplan.*;
import com.yingda.lkj.service.backstage.line.RailwayLineService;
import com.yingda.lkj.service.backstage.line.StationService;
import com.yingda.lkj.utils.CacheUtil;
import com.yingda.lkj.utils.StreamUtil;
import com.yingda.lkj.utils.StringUtils;
import com.yingda.lkj.utils.date.DateUtil;
import com.yingda.lkj.utils.math.Arithmetic;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("constructionControlPlanService")
public class ConstructionControlPlanServiceImpl implements ConstructionControlPlanService {

    @Autowired
    private BaseDao<ConstructionControlPlan> constructionControlPlanBaseDao;
    @Autowired
    private ConstructionFormalPlanService constructionFormalPlanService;
    @Autowired
    private ConstructionControlPlanWorkTimeService constructionControlPlanWorkTimeService;
    @Autowired
    private ConstructionControlPlanKilometerMarkService constructionControlPlanKilometerMarkService;
    @Autowired
    private ConstructionControlPlanEquipmentService constructionControlPlanEquipmentService;
    @Autowired
    private StationService stationService;
    @Autowired
    private RailwayLineService railwayLineService;



    @Override
    public ConstructionControlPlan getById(String id) {
        if (StringUtils.isEmpty(id)) return null;

        return constructionControlPlanBaseDao.get(
                "from ConstructionControlPlan where id = :id",
                Map.of("id", id)
        );
    }

    @Override
    public ConstructionControlPlan saveOrUpdate(ConstructionControlPlan pageConstructionControlPlan) {
        Timestamp current = new Timestamp(System.currentTimeMillis());

        ConstructionControlPlan constructionControlPlan = new ConstructionControlPlan();
        BeanUtils.copyProperties(pageConstructionControlPlan, constructionControlPlan, "updateTime", "signInStatus", "approveStatus");
        if (constructionControlPlan.getAddTime() == null)
            constructionControlPlan.setAddTime(new Timestamp(System.currentTimeMillis()));
        constructionControlPlan.setUpdateTime(current);

        constructionControlPlanBaseDao.saveOrUpdate(constructionControlPlan);

        return constructionControlPlan;
    }

    @Deprecated
    @Override
    public List<ConstructionControlPlan> getAll() {
        return constructionControlPlanBaseDao.find(
                "from ConstructionControlPlan"
        );
    }

    @Override
    public void submit(String constructionControlPlanId) throws CustomException {
        ConstructionControlPlan constructionControlPlan = getById(constructionControlPlanId);

//        constructionControlPlan.setProcessStatus(ConstructionControlPlan.SUBMITTED);
        constructionControlPlan.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        constructionControlPlanBaseDao.saveOrUpdate(constructionControlPlan);
    }

    @Override
    public void safeCountersign(String constructionControlPlanId) throws CustomException {
        ConstructionControlPlan constructionControlPlan = getById(constructionControlPlanId);
        if (constructionControlPlan.getHasUploadedSafetyProtocol() == Constant.FALSE)
            throw new CustomException(JsonMessage.DATA_NO_COMPLETE, "未上传安全协议");

        constructionControlPlan.setProcessStatus(ConstructionControlPlan.PENDING_RELEVANCE);
        constructionControlPlan.setPlanStatus(ConstructionControlPlan.COUNTERSIGNED);
        constructionControlPlan.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        constructionControlPlanBaseDao.saveOrUpdate(constructionControlPlan);
    }

    @Override
    public String getNextCode() {
        return CodeService.getACodeOfToday();
    }

    @Override
    public void delete(String constructionControlPlanId) {
        constructionControlPlanBaseDao.executeHql(
                "delete from ConstructionControlPlan where id = :constructionControlPlanId",
                Map.of("constructionControlPlanId", constructionControlPlanId)
        );
    }

    @Override
    public List<ConstructionControlPlan> findByStationId(String stationId) {
        return constructionControlPlanBaseDao.find(
                "from ConstructionControlPlan where startStationId = :stationId or endStationId = :stationId or signInStationId = " +
                        ":stationId",
                Map.of("stationId", stationId)
        );
    }

    @Override
    public List<ConstructionControlPlan> issuePendingInvestigatePlans2App() {
        // 待调查的计划
        List<ConstructionControlPlan> pendingInvestigatePlans = constructionControlPlanBaseDao.find(
                """
                        FROM ConstructionControlPlan 
                        WHERE 
                            (investigationProgressStatus = :pendingInvestigate or investigationProgressStatus = :investigating)
                        AND processStatus != :CLOSED
                        """,
                Map.of(
                        "pendingInvestigate", ConstructionControlPlan.PENDING_INVESTIGATE,
                        "investigating", ConstructionControlPlan.INVESTIGATING,
                        "CLOSED", ConstructionControlPlan.CLOSED
                )
        );
        pendingInvestigatePlans.forEach(x -> x.setInvestigationProgressStatus(ConstructionControlPlan.INVESTIGATING));
        constructionControlPlanBaseDao.bulkInsert(pendingInvestigatePlans);

        return pendingInvestigatePlans;
    }

    @Override
    public void startInvestigate(String id, String investigationOrganizationId) {
        // 待调查的计划
        ConstructionControlPlan constructionControlPlan = constructionControlPlanBaseDao.get(ConstructionControlPlan.class, id);
        constructionControlPlan.setInvestigationProgressStatus(ConstructionControlPlan.INVESTIGATING);
        constructionControlPlan.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        constructionControlPlanBaseDao.saveOrUpdate(constructionControlPlan);
    }

    @Override
    public void finishedInvestigate(List<String> ids) {
        List<ConstructionControlPlan> constructionControlPlans = getByIds(ids);
        for (ConstructionControlPlan constructionControlPlan : constructionControlPlans) {
            constructionControlPlan.setInvestigationProgressStatus(ConstructionControlPlan.INVESTIGATED);
            constructionControlPlan.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        }

        constructionControlPlanBaseDao.bulkInsert(constructionControlPlans);
    }

    @Override
    public void close(String id) {
        ConstructionControlPlan constructionControlPlan = getById(id);
        constructionControlPlan.setPlanStatus(ConstructionControlPlan.MANUALLY_CLOSED);
        constructionControlPlan.setProcessStatus(ConstructionControlPlan.CLOSED);
        constructionControlPlan.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        constructionControlPlanBaseDao.saveOrUpdate(constructionControlPlan);

//        constructionDailyPlanService.closeByConstructionControlPlanId(id);
    }

    @Override
    public void relevanceFormalPlan(String constructionControlPlanId, String constructionFormalPlanId) throws CustomException {
        ConstructionControlPlan constructionControlPlan = getById(constructionControlPlanId);
        if (constructionControlPlan.getInvestigationProgressStatus() == ConstructionControlPlan.INVESTIGATING)
            throw new CustomException(JsonMessage.PARAM_INVALID, "该方案调查中，暂不可关联");

        constructionControlPlan.setProcessStatus(ConstructionControlPlan.RELEVANCEED);
        constructionControlPlan.setPlanStatus(ConstructionControlPlan.PENDING_START);
        constructionControlPlan.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        constructionControlPlanBaseDao.saveOrUpdate(constructionControlPlan);
        constructionFormalPlanService.relevancePlan(constructionControlPlanId, constructionFormalPlanId);
    }

    @Override
    public void checkClose() {
        Timestamp current = new Timestamp(System.currentTimeMillis());
        List<ConstructionControlPlan> constructionControlPlans = getNotClosed();
        for (ConstructionControlPlan constructionControlPlan : constructionControlPlans) {
            List<ConstructionControlPlanWorkTime> constructionControlPlanWorkTimes =
                    constructionControlPlanWorkTimeService.getByConstructionControlPlanId(constructionControlPlan.getId());
            Timestamp latestEndTime = null;
            for (ConstructionControlPlanWorkTime constructionControlPlanWorkTime : constructionControlPlanWorkTimes) {
                Timestamp endDate = constructionControlPlanWorkTime.getEndDate();
                if (latestEndTime == null || endDate.after(latestEndTime)) latestEndTime = endDate;
            }
            if (latestEndTime != null && latestEndTime.before(current))
                close(constructionControlPlan.getId());
        }
    }

    @Override
    public void fillAssociatedInfos(List<ConstructionControlPlan> constructionControlPlans) {
        for (ConstructionControlPlan constructionControlPlan : constructionControlPlans)
            fillAssociatedInfo(constructionControlPlan);
    }

    @Override
    public void fillAssociatedInfo(ConstructionControlPlan constructionControlPlan) {

        String constructionControlPlanId = constructionControlPlan.getId();
        List<ConstructionControlPlanKilometerMark> controlPlanKilometerMarks =
                constructionControlPlanKilometerMarkService.getAssociatedInfosByConstructionControlPlanId(constructionControlPlanId);

        // 公里标副表信息
        for (ConstructionControlPlanKilometerMark controlPlanKilometerMark : controlPlanKilometerMarks) {
            String railwayLineName = controlPlanKilometerMark.getRailwayLineName();
            String startStationName = controlPlanKilometerMark.getStartStationName();
            String endStationName = controlPlanKilometerMark.getEndStationName();
            double startKilometer = controlPlanKilometerMark.getStartKilometer();
            double endKilometer = controlPlanKilometerMark.getEndKilometer();

            byte downriver = controlPlanKilometerMark.getDownriver();
            String downriverStr = switch (downriver) {
                case ConstructionControlPlanKilometerMark.DOWNRIVER -> "下行";
                case ConstructionControlPlanKilometerMark.UPRIVER -> "上行";
                case ConstructionControlPlanKilometerMark.SINGLE_LINE -> "单线";
                case ConstructionControlPlanKilometerMark.UP_AND_DOWN -> "上下行";
                default -> "";
            };

            String detailInfo = String.format(
                    "%s %s %s - %s K%.0f + %.0f - K%.0f + %.0f",
                    railwayLineName,
                    downriverStr,
                    startStationName,
                    endStationName,
                    Arithmetic.divide(startKilometer, 1000, 0),
                    startKilometer % 1000,
                    Arithmetic.divide(endKilometer, 1000, 0, RoundingMode.DOWN),
                    endKilometer % 1000
            );
            controlPlanKilometerMark.setDetailInfo(detailInfo);
        }
        constructionControlPlan.setConstructionControlPlanKilometerMarks(controlPlanKilometerMarks);

        // 工作时间副表信息
        List<ConstructionControlPlanWorkTime> controlPlanWorkTimes =
                constructionControlPlanWorkTimeService.getByConstructionControlPlanId(constructionControlPlanId);
        for (ConstructionControlPlanWorkTime controlPlanWorkTime : controlPlanWorkTimes) {
            Timestamp startDate = controlPlanWorkTime.getStartDate();
            Timestamp endDate = controlPlanWorkTime.getEndDate();
            Timestamp startTime = controlPlanWorkTime.getStartTime();
            Timestamp endTime = controlPlanWorkTime.getEndTime();
            String detailInfo = String.format(
                    "%s 至 %s %s - %s\n",
                    DateUtil.format(startDate, "YYYY/MM/dd"),
                    DateUtil.format(endDate, "YYYY/MM/dd"),
                    DateUtil.format(startTime, "HH:mm"),
                    DateUtil.format(endTime, "HH:mm")
            );
            controlPlanWorkTime.setDetailInfo(detailInfo);
        }
        constructionControlPlan.setConstructionControlPlanWorkTimes(controlPlanWorkTimes);
    }

    @Override
    public List<ConstructionControlPlan> getRelevancedConstructionControlPlanByEquipmentId(String equipmentId) {
        List<ConstructionControlPlanEquipment> constructionControlPlanEquipments =
                constructionControlPlanEquipmentService.getByEquipmentId(equipmentId);
        List<String> constructionControlPlanIds = StreamUtil.getList(constructionControlPlanEquipments,
                ConstructionControlPlanEquipment::getConstructionControlPlanId);
        return getByIds(constructionControlPlanIds).stream().filter(x -> x.getProcessStatus() == ConstructionControlPlan.RELEVANCEED).collect(Collectors.toList());
    }

    private List<ConstructionControlPlan> getByIds(List<String> ids) {
        return constructionControlPlanBaseDao.find(
                "from ConstructionControlPlan where id in :ids",
                Map.of("ids", ids)
        );
    }

    /**
     * 获取所有状态为方案提交，现场调查，补充调查，方案审核，方案审批，关联计划，方案开始，方案结束的方案（）
     */
    private List<ConstructionControlPlan> getNotClosed() {
        return constructionControlPlanBaseDao.find(
                "from ConstructionControlPlan where processStatus <> :CLOSED",
                Map.of("CLOSED", ConstructionControlPlan.CLOSED)
        );
    }

    private static class CodeService {
        private static final Map<String, Object> CODES_MAP = CacheUtil.createMap("constructionControlPlanCode");

        private static void initCodesMap(String key) {
            CODES_MAP.clear();
            List<String> codes = new LinkedList<>();
            for (int i = 1; i < 10000; i++) {
                String numberCodes = new DecimalFormat("0000").format(i);
                codes.add(numberCodes);
            }
            CODES_MAP.put(key, codes);
        }

        private static String getACodeOfToday() {
            String currentDate = DateUtil.format(new Timestamp(System.currentTimeMillis()), "yyMMdd");
            if (CODES_MAP.get(currentDate) == null) initCodesMap(currentDate);

            @SuppressWarnings("unchecked")
            List<String> codeOfToday = (List<String>) CODES_MAP.get(currentDate);
            String code = codeOfToday.get(0);
            codeOfToday.remove(0);

            return currentDate + code;
        }
    }
}
