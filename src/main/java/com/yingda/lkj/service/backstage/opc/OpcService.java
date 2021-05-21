package com.yingda.lkj.service.backstage.opc;

import com.yingda.lkj.beans.entity.backstage.opc.Opc;
import com.yingda.lkj.beans.entity.backstage.opc.OpcMark;

import java.util.List;

/**
 * @author hood  2020/12/15
 */
public interface OpcService {
    Opc getById(String id);
    List<Opc> getByIds(List<String> ids);
    List<Opc> getByStations(String leftStationId, String rightStationId);
    List<Opc> getLineDataByStationId(String stationId);
    List<Opc> getLineDataByStationIds(List<String> stationIds);
    void cutOpcLocationsByDistance(Opc opc, List<OpcMark> opcMarks, double startDistance, double endDistance);
    List<Opc> getOpcByStationId(String stationId);

}
