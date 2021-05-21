package com.yingda.lkj.service.backstage.constructioncontrolplan;

import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionControlPlan;
import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionControlPlanKilometerMark;
import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionControlPlanPoint;
import com.yingda.lkj.beans.entity.backstage.opc.Opc;
import com.yingda.lkj.beans.enums.constructioncontrolplan.WarnLevel;
import com.yingda.lkj.dao.BaseDao;
import com.yingda.lkj.service.backstage.location.LocationService;
import com.yingda.lkj.service.backstage.opc.OpcService;
import com.yingda.lkj.utils.StreamUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class AsyncConstructionControlPlanService {

    @Autowired
    private BaseDao<ConstructionControlPlan> constructionControlPlanBaseDao;
    @Autowired
    private ConstructionControlPlanService constructionControlPlanService;
    @Autowired
    private OpcService opcService;
    @Autowired
    private ConstructionControlPlanPointService constructionControlPlanPointService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private ConstructionControlPlanKilometerMarkService constructionControlPlanKilometerMarkService;

    /**
     * 计算距离，修改任务的风险等级
     */
    @Async
    public void checkWarnStatus(ConstructionControlPlanPoint constructionControlPlanPoint) {
        String constructionControlPlanId = constructionControlPlanPoint.getConstructionControlPlanId();
        ConstructionControlPlan constructionControlPlan = constructionControlPlanService.getById(constructionControlPlanId);

        List<ConstructionControlPlanKilometerMark> constructionControlPlanKilometerMarks =
                constructionControlPlanKilometerMarkService.getByConstructionControlPlanId(constructionControlPlanId);

        List<String> stationIds = StreamUtil.getList(constructionControlPlanKilometerMarks,
                ConstructionControlPlanKilometerMark::getStartStationId);
        List<Opc> opcs = opcService.getLineDataByStationIds(stationIds);
        locationService.fillLocations(opcs);

        double shortestDistance = constructionControlPlanPointService.calculationShortestDistance(constructionControlPlanPoint, opcs);
        shortestDistance = Math.max(shortestDistance - constructionControlPlanPoint.getRadius(), 0);
        constructionControlPlanPoint.setShortestDistance(shortestDistance);
        WarnLevel warnLevel = WarnLevel.getByDistance(shortestDistance);
        if (constructionControlPlan.getWarnStatus() >= warnLevel.level) {
            constructionControlPlan.setWarnStatus((byte) warnLevel.level);
            constructionControlPlan.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            constructionControlPlanBaseDao.saveOrUpdate(constructionControlPlan);
        }
    }
}
