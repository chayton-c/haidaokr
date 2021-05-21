package com.yingda.lkj.service.backstage.line;

import com.yingda.lkj.beans.entity.backstage.line.RailwayLine;
import com.yingda.lkj.beans.entity.backstage.line.Station;
import com.yingda.lkj.beans.entity.backstage.line.StationRailwayLine;

import java.util.List;

/**
 * @author hood  2020/5/30
 */
public interface StationRailwayLineService {

    void saveOrUpdate(String stationId, String railwayLineId);

    List<Station> getStationsByRaiwayLineId(String railwayLineId);

    void remove(List<String> stationIds, String railwayLineId);

    void updateSeq(String stationId, String railwayLineId, int seq);

    /**
     * 添加线路车站关联
     */
    void saveOrUpdate(String stationId, List<String> railwayLineIds);

    String getRailwayLineNames(String stationId);

    List<String> getRailwayLineIdsByStationIds(String stationId);

    List<String> getStationIdsByRailwayLineId(String railwayLineId);

    List<StationRailwayLine> getByRailwayLines(List<RailwayLine> railwayLines);

    StationRailwayLine getByRailwayLineIdAndStationId(String railwayLineId, String stationId);
}
