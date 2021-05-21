package com.yingda.lkj.service.impl.backstage.constructioncontrolplan;

import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionControlPlanPoint;
import com.yingda.lkj.beans.entity.backstage.location.Location;
import com.yingda.lkj.beans.entity.backstage.opc.Opc;
import com.yingda.lkj.dao.BaseDao;
import com.yingda.lkj.service.backstage.constructioncontrolplan.ConstructionControlPlanPointService;
import com.yingda.lkj.service.backstage.constructioncontrolplan.ConstructionControlPlanService;
import com.yingda.lkj.service.backstage.location.LocationService;
import com.yingda.lkj.service.backstage.opc.OpcService;
import com.yingda.lkj.utils.location.LocationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("constructionControlPlanPointService")
public class ConstructionControlPlanPointServiceImpl implements ConstructionControlPlanPointService {

    @Autowired
    private BaseDao<ConstructionControlPlanPoint> constructionControlPlanPointBaseDao;
    @Autowired
    private ConstructionControlPlanService constructionControlPlanService;
    @Autowired
    private OpcService opcService;
    @Autowired
    private LocationService locationService;

    @Override
    public List<ConstructionControlPlanPoint> getByPlanId(String constructionControlPlanId) {
        return constructionControlPlanPointBaseDao.find(
                "from ConstructionControlPlanPoint where constructionControlPlanId = :constructionControlPlanId",
                Map.of("constructionControlPlanId", constructionControlPlanId)
        );
    }

    @Override
    public double calculationShortestDistance(ConstructionControlPlanPoint constructionControlPlanPoint, List<Opc> opcsWithLocations) {
        locationService.fillLocations(List.of(constructionControlPlanPoint));

        double shortestDistance = Double.MAX_VALUE;
        // 测定点到opcs包含的所有路径的最近距离(米)
        List<Location> locations = constructionControlPlanPoint.getLocations();

        byte collectType = constructionControlPlanPoint.getCollectType();
        if (collectType == ConstructionControlPlanPoint.POINT) {
            double currentShortestDistance = LocationUtil.pointToMultiLineShortestDistance(locations.get(0), opcsWithLocations);
            shortestDistance = Math.min(shortestDistance, currentShortestDistance);
        }
        if (collectType == ConstructionControlPlanPoint.LINE) {
            for (int i = 0; i < locations.size(); i++) {
                if (i == locations.size() - 1) continue;

                Location location = locations.get(i);
                Location next = locations.get(i + 1);

                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                double nextLongitude = next.getLongitude();
                double nextLatitude = next.getLatitude();

                for (int j = 0; j < 100; j++) {
                    double splitLongitude = longitude + (longitude - nextLongitude) / 100 * j;
                    double splitLatitude = latitude + (latitude - nextLatitude) / 100 * j;
                    Location splitLocation = new Location();
                    splitLocation.setLongitude(splitLongitude);
                    splitLocation.setLatitude(splitLatitude);
                    double currentShortestDistance = LocationUtil.pointToMultiLineShortestDistance(splitLocation, opcsWithLocations);
                    shortestDistance = Math.min(shortestDistance, currentShortestDistance);
                }

            }
        }
        if (collectType == ConstructionControlPlanPoint.AREA) {
            for (int i = 0; i < locations.size(); i++) {

                Location location = locations.get(i);
                Location next = i == locations.size() - 2 ? locations.get(i + 1) : locations.get(0);

                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                double nextLongitude = next.getLongitude();
                double nextLatitude = next.getLatitude();

                for (int j = 0; j < 100; j++) {
                    double splitLongitude = longitude + (longitude - nextLongitude) / 100 * j;
                    double splitLatitude = latitude + (latitude - nextLatitude) / 100 * j;
                    Location splitLocation = new Location();
                    splitLocation.setLongitude(splitLongitude);
                    splitLocation.setLatitude(splitLatitude);
                    double currentShortestDistance = LocationUtil.pointToMultiLineShortestDistance(splitLocation, opcsWithLocations);
                    shortestDistance = Math.min(shortestDistance, currentShortestDistance);
                }

            }
        }
        return shortestDistance;
    }
}
