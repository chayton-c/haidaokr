package com.yingda.lkj.service.backstage.centerscreen;

import com.yingda.lkj.beans.entity.backstage.centerscreen.CenterScreenElementPosition;

import java.util.List;

public interface CenterScreenElementPositionService {

    CenterScreenElementPosition getByStationId(String stationId);

    void saveOrUpdate(CenterScreenElementPosition pageCenterScreenElementPosition);

    void delete(CenterScreenElementPosition centerScreenElementPosition);

    List<CenterScreenElementPosition> getByStationIds(List<String> stationIds);
}
