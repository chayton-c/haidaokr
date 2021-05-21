package com.yingda.lkj.service.impl.backstage.line;

import com.yingda.lkj.beans.entity.backstage.line.RailwayLineSection;
import com.yingda.lkj.dao.BaseDao;
import com.yingda.lkj.service.backstage.line.RailwayLineSectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("railwayLineSectionService")
public class RailwayLineSectionServiceImpl implements RailwayLineSectionService {

    @Autowired
    private BaseDao<RailwayLineSection> railwayLineSectionBaseDao;

    @Override
    public List<RailwayLineSection> getByIds(List<String> ids) {
        return railwayLineSectionBaseDao.find(
                "from RailwayLineSection where id in :ids",
                Map.of("ids", ids)
        );
    }

    @Override
    public List<RailwayLineSection> getByStationId(String stationId) throws Exception {
        return railwayLineSectionBaseDao.find(
                "from RailwayLineSection where leftStationId = :stationId or rightStationId = :stationId",
                Map.of("stationId", stationId)
        );
    }

    @Override
    public List<RailwayLineSection> getByStationIds(List<String> stationIds) throws Exception {
        return railwayLineSectionBaseDao.find(
                "from RailwayLineSection where leftStationId in :stationIds or rightStationId in :stationIds",
                Map.of("stationIds", stationIds)
        );
    }

    @Override
    public List<RailwayLineSection> getByRailwayLineId(String railwayLineId) {
        return railwayLineSectionBaseDao.find(
                "FROM RailwayLineSection where railwayLineId = :railwayLineId",
                Map.of("railwayLineId", railwayLineId)
        );
    }

    @Override
    public RailwayLineSection getByStationIdAndDownriver(String leftStationId, String rightStationId, byte downriver) {
        return railwayLineSectionBaseDao.get(
                "FROM RailwayLineSection where leftStationId = :leftStationId and rightStationId = :rightStationId and downriver = :downriver",
                Map.of("leftStationId", leftStationId, "rightStationId", rightStationId, "downriver", downriver)
        );
    }

    @Override
    public void saveOrUpdate(RailwayLineSection railwayLineSection) {
        railwayLineSectionBaseDao.saveOrUpdate(railwayLineSection);
    }
}
