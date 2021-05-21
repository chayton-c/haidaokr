package com.yingda.lkj.service.impl.backstage.line;

import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionControlPlan;
import com.yingda.lkj.beans.entity.backstage.line.RailwayLine;
import com.yingda.lkj.beans.entity.backstage.line.Station;
import com.yingda.lkj.beans.entity.backstage.organization.Organization;
import com.yingda.lkj.beans.exception.CustomException;
import com.yingda.lkj.beans.system.Json;
import com.yingda.lkj.beans.system.JsonMessage;
import com.yingda.lkj.dao.BaseDao;
import com.yingda.lkj.service.backstage.constructioncontrolplan.ConstructionControlPlanService;
import com.yingda.lkj.service.backstage.line.RailwayLineService;
import com.yingda.lkj.service.backstage.line.StationRailwayLineService;
import com.yingda.lkj.service.backstage.line.StationService;
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

/**
 * @author hood  2020/1/7
 */
@Service("StationService")
public class StationServiceImpl implements StationService {

    @Autowired
    private BaseDao<Station> stationBaseDao;
    @Autowired
    private RailwayLineService railwayLineService;
    @Autowired
    private StationRailwayLineService stationRailwayLineService;
    @Autowired
    private OrganizationClientService organizationClientService;
    @Autowired
    private ConstructionControlPlanService constructionControlPlanService;

    @Override
    public List<Station> getAll() {
        return stationBaseDao.find("from Station order by seq");
    }

    @Override
    public List<Station> findStationsByNames(List<String> names) throws CustomException {
        names = names.stream().distinct().collect(Collectors.toList());
        List<Station> stations = stationBaseDao.find("from Station where name in :stationNames", Map.of("stationNames", names));

        if (stations.size() != names.size()) {
            List<String> acquiredNameList = stations.stream().map(Station::getName).collect(Collectors.toList());
            List<String> notIncluded = names.stream().filter(x -> !acquiredNameList.contains(x)).collect(Collectors.toList());
            System.out.println(notIncluded);
            throw new CustomException(new Json(JsonMessage.SYS_ERROR, "找不到name = " + notIncluded + "的站记录，请前往基础数据 -> 车站管理中添加对应名称的车站"));
        }

        return stations;
    }

    @Override
    public Station getStationByName(String name) {
        return stationBaseDao.get("from Station where name = :name", Map.of("name", name));
    }

    @Override
    public List<Station> findStationsByWorkshopId(String workshopId) {
        return stationBaseDao.find("from Station where workshopId = :workshopId", Map.of("workshopId", workshopId));
    }

    @Override
    public List<Station> findStationsByWorkshopIds(List<String> workshopIds) {
        if (workshopIds.isEmpty()) return new ArrayList<>();
        return stationBaseDao.find("from Station where workshopId in :workshopIds", Map.of("workshopIds", workshopIds));
    }

    @Override
    public List<Station> findStationsBySectionId(String sectionId) {
        List<Organization> workshops = organizationClientService.getSlaves(sectionId);
        if (workshops.isEmpty())
            return new ArrayList<>();

        return findStationsByWorkshopIds(StreamUtil.getList(workshops, Organization::getId));
    }

    @Override
    public void deleteByIds(List<String> ids) throws CustomException {
        for (String id : ids) {
            List<ConstructionControlPlan> constructionControlPlans = constructionControlPlanService.findByStationId(id);
            if (!constructionControlPlans.isEmpty()) {
                String errorMsg = String.format(
                        "名为%s的车站下仍有使用中的方案，请在修改对应方案后再删除，关联的方案编码：%s",
                        getById(id).getName(),
                        constructionControlPlans.stream().map(ConstructionControlPlan::getCode).collect(Collectors.joining(","))
                );
                throw new CustomException(JsonMessage.CONTAINING_ASSOCIATED_DATA, errorMsg);
            }
            List<String> railwayLineIdsByStationIds = stationRailwayLineService.getRailwayLineIdsByStationIds(id);
            List<RailwayLine> railwayLines = railwayLineService.getByIds(railwayLineIdsByStationIds);
            if (!railwayLineIdsByStationIds.isEmpty()) {
                String errorMsg = String.format(
                        "名为%s的车站下仍有使用中的线路，请在修改对应线路后再删除，关联的线路编码：%s",
                        getById(id).getName(),
                        railwayLines.stream().map(RailwayLine::getCode).collect(Collectors.joining(","))
                );
                throw new CustomException(JsonMessage.CONTAINING_ASSOCIATED_DATA, errorMsg);
            }
        }

        stationBaseDao.executeHql(
                "delete from Station where id in :ids",
                Map.of("ids", ids)
        );

        stationBaseDao.executeHql(
                "delete from StationRailwayLine where stationId in :ids",
                Map.of("ids", ids)
        );
    }

    @Override
    public List<Station> getByIds(List<String> ids) {
        return stationBaseDao.find("from Station where id in :ids", Map.of("ids", ids));
    }

    @Override
    public Map<String, Station> getByNames(List<String> names) {
        names = names.stream().distinct().collect(Collectors.toList());
        List<Station> stations = stationBaseDao.find("from Station where name in :names", Map.of("names", names));
        return StreamUtil.getMap(stations, Station::getName, x -> x);
    }

    @Override
    public Station getById(String id) {
        return stationBaseDao.get(Station.class, id);
    }

    @Override
    public Station getByCode(String code) {
        return stationBaseDao.get(
                "from Station where code = :code",
                Map.of("code", code)
        );
    }

    @Override
    public void saveOrUpdate(Station pageStation) {
        String stationId = pageStation.getId();
        Station station = StringUtils.isNotEmpty(stationId) ? getById(stationId) : new Station();
        BeanUtils.copyProperties(pageStation, station, "id", "addTime", "hide");
        if (StringUtils.isEmpty(station.getId()))
            station.setId(UUID.randomUUID().toString());
        station.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        stationBaseDao.saveOrUpdate(station);
    }

    @Override
    public int getCurrentSeq() {
        List<Station> stations = stationBaseDao.find("from Station order by seq desc", 1, 1);

        if (stations.isEmpty())
            return 0;
        return stations.get(0).getSeq();
    }

}
