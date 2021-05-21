package com.yingda.lkj.service.backstage.line;

import com.yingda.lkj.beans.entity.backstage.line.KilometerMark;
import com.yingda.lkj.beans.entity.backstage.line.RailwayLineSection;
import com.yingda.lkj.beans.entity.backstage.location.Location;
import com.yingda.lkj.beans.system.Pair;

import java.util.List;

public interface KilometerMarkService {

    KilometerMark getByRailwaylineSectionIdAndStationId(String railwayLineSectionId, String stationId);

    List<KilometerMark> getByRailwayLineSectionId(String railwayLineSectionId);

    List<KilometerMark> getByRailwayLineSections(List<RailwayLineSection> railwayLineSections);

    Pair<KilometerMark, KilometerMark> createStartEndKilometerMark(RailwayLineSection railwayLineSection, String startStationId, String endStationId, Location startLocation, Location endLocation);

}
