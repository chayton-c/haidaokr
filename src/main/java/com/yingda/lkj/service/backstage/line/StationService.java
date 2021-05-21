package com.yingda.lkj.service.backstage.line;

import com.yingda.lkj.beans.entity.backstage.line.Station;
import com.yingda.lkj.beans.exception.CustomException;

import java.util.List;
import java.util.Map;

/**
 * @author hood  2020/1/7
 */
public interface StationService {
    List<Station> getAll();

    List<Station> findStationsByNames(List<String> names) throws CustomException;
    Station getStationByName(String name) throws CustomException;
    List<Station> findStationsByWorkshopId(String workshopId);
    List<Station> findStationsByWorkshopIds(List<String> workshopIds);
    List<Station> findStationsBySectionId(String sectionId);
    void deleteByIds(List<String> ids) throws CustomException;

    List<Station> getByIds(List<String> ids);

    Map<String, Station> getByNames(List<String> names);

    Station getById(String id);
    Station getByCode(String code);

    void saveOrUpdate(Station pageStation);

    /**
     * 获取当前最大排序
     */
    int getCurrentSeq();
}
