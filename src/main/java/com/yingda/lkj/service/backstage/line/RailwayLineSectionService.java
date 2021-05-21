package com.yingda.lkj.service.backstage.line;

import com.yingda.lkj.beans.entity.backstage.line.RailwayLineSection;

import java.util.List;

public interface RailwayLineSectionService {

    List<RailwayLineSection> getByIds(List<String> ids);

    List<RailwayLineSection> getByStationId(String stationId) throws Exception;

    List<RailwayLineSection> getByStationIds(List<String> stationIds) throws Exception;

    List<RailwayLineSection> getByRailwayLineId(String railwayLineId);

    RailwayLineSection getByStationIdAndDownriver(String leftStationId, String rightStationId, byte downriver);

    void saveOrUpdate(RailwayLineSection railwayLineSection);
}
