package com.yingda.lkj.service.backstage.constructioncontrolplan;

import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionControlPlan;
import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionControlPlanKilometerMark;
import com.yingda.lkj.dao.BaseDao;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ConstructionControlPlanKilometerMarkService {

    @Autowired
    private BaseDao<ConstructionControlPlanKilometerMark> constructionControlPlanKilometerMarkBaseDao;
    @Autowired
    private ConstructionControlPlanService constructionControlPlanService;

    public ConstructionControlPlanKilometerMark getById(String id) {
        return constructionControlPlanKilometerMarkBaseDao.get(ConstructionControlPlanKilometerMark.class, id);
    }

    public void saveOrUpdate(ConstructionControlPlanKilometerMark pageConstructionControlPlanKilometerMark) {
        ConstructionControlPlanKilometerMark constructionControlPlanKilometerMark =
                Optional.ofNullable(getById(pageConstructionControlPlanKilometerMark.getId())).orElse(new ConstructionControlPlanKilometerMark());

        BeanUtils.copyProperties(pageConstructionControlPlanKilometerMark, constructionControlPlanKilometerMark, "addTime");
        // getById就是为了addTime
        if (constructionControlPlanKilometerMark.getAddTime() == null)
            constructionControlPlanKilometerMark.setAddTime(new Timestamp(System.currentTimeMillis()));
        constructionControlPlanKilometerMark.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        constructionControlPlanKilometerMarkBaseDao.saveOrUpdate(constructionControlPlanKilometerMark);
    }

    public void delete(String id) {
        constructionControlPlanKilometerMarkBaseDao.executeHql(
                "delete from ConstructionControlPlanKilometerMark where id = :id",
                Map.of("id", id)
        );
    }

    public void checkDeleteWhenDestropPage(String constructionControlPlanId) {
        ConstructionControlPlan constructionControlPlan = constructionControlPlanService.getById(constructionControlPlanId);
        if (constructionControlPlan != null) return;

        constructionControlPlanKilometerMarkBaseDao.executeHql(
                "delete from ConstructionControlPlanKilometerMark where constructionControlPlanId = :constructionControlPlanId",
                Map.of("constructionControlPlanId", constructionControlPlanId)
        );
    }

    public List<ConstructionControlPlanKilometerMark> getByConstructionControlPlanId(String constructionControlPlanId) {
        return constructionControlPlanKilometerMarkBaseDao.find(
                "FROM ConstructionControlPlanKilometerMark where constructionControlPlanId = :constructionControlPlanId",
                Map.of("constructionControlPlanId", constructionControlPlanId)
        );
    }

    public List<ConstructionControlPlanKilometerMark> getAssociatedInfosByConstructionControlPlanId(String constructionControlPlanId) {
        String sql = """
                SELECT
                	construction_control_plan_kilometer_mark.*,
                	railway_line.`name` AS railwayLineName,
                	startStation.`name` AS startStationName,
                	endStation.`name` AS endStationName\s
                FROM
                	construction_control_plan_kilometer_mark
                	INNER JOIN station AS startStation ON construction_control_plan_kilometer_mark.start_station_id = startStation.id
                	INNER JOIN station AS endStation ON construction_control_plan_kilometer_mark.end_station_id = endStation.id
                	INNER JOIN railway_line ON construction_control_plan_kilometer_mark.railway_line_id = railway_line.id
                WHERE
                    construction_control_plan_kilometer_mark.construction_control_plan_id = :constructionControlPlanId
                """;
        Map<String, Object> params = new HashMap<>();
        params.put("constructionControlPlanId", constructionControlPlanId);
        return constructionControlPlanKilometerMarkBaseDao.findSQL(
                sql, params, ConstructionControlPlanKilometerMark.class
        );
    }

    public List<ConstructionControlPlanKilometerMark> findByStationId(String stationId) {
        return constructionControlPlanKilometerMarkBaseDao.find(
                "FROM ConstructionControlPlanKilometerMark where startStationId = :stationId OR endStationId = :stationId",
                Map.of("stationId", stationId)
        );
    }
}
