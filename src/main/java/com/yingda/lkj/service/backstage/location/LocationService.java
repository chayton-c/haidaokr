package com.yingda.lkj.service.backstage.location;

import com.alibaba.fastjson.JSONObject;
import com.yingda.lkj.beans.entity.backstage.line.KilometerMark;
import com.yingda.lkj.beans.entity.backstage.line.RailwayLineSection;
import com.yingda.lkj.beans.entity.backstage.location.Location;
import com.yingda.lkj.beans.exception.CustomException;
import com.yingda.lkj.beans.pojo.location.ContainsLocation;
import com.yingda.lkj.beans.system.Json;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author hood  2020/12/15
 */
public interface LocationService {
    Location create(double latitude, double longitude, String dataId, byte type);

    Location getById(String id);

    List<Location> getByDataId(String dataId);

    void delete(String id);

    /**
     * 获取与containLocationDatas关联的locations，并添加到containLocationData.locations中
     */
    List<? extends ContainsLocation> fillLocations(List<? extends ContainsLocation> containLocationDatas);

    /**
     * 获取与containLocationDatas关联的locations，并添加到containLocationData.locations中
     */
    List<? extends ContainsLocation> fillLocations(List<? extends ContainsLocation> containLocationDatas, Timestamp startTime, Timestamp endTime);

    /**
     * 获取与containLocationDatas关联的locations，并添加到containLocationData.locations中
     */
    List<? extends ContainsLocation> fillLocations(List<? extends ContainsLocation> containLocationDatas, Byte type);

    /**
     * 获取与containLocationDatas关联的locations，并添加到containLocationData.locations中
     */
    List<? extends ContainsLocation> fillLocations(List<? extends ContainsLocation> containLocationDatas, Timestamp startTime, Timestamp endTime, Byte type);

    /**
     * 获取与containLocationData关联的最新的位置信息
     */
    Location getLastLocation(ContainsLocation containsLocation) throws Exception;

    /**
     * 获取与containLocationData关联的最新的count条位置信息
     */
    List<Location> getLastLocation(ContainsLocation containsLocation, int count) throws Exception;

    void deleteByDataId(String dataId);

    /**
     * 根据计划的公里标截取线路，把截取后的线路放到railwayLineSections.locations中
     */
    List<KilometerMark> fillRailwayLineSectionsWithLocationsByPlan(List<RailwayLineSection> railwayLineSections, double startKilometer, double endKilometer) throws CustomException;

    void saveOrUpdate(Location location);

    void bulkInsert(List<Location> locations) throws SQLException;

    Location saveOrUpdate(String dataId, double longitude, double latitude, byte dataType);

    default boolean samePoint(Location location1, Location location2) {
        return location1.getLatitude() == location2.getLatitude() && location1.getLongitude() == location2.getLongitude();
    }

    /**
     * 获取所有线路location，用于线路地图
     * @return
     * @throws Exception
     */
    JSONObject getAllRailwayLineLocations() throws Exception;
    Json getRailwayLineLocationsByStationIds(String stationIds) throws Exception;
}
