package com.yingda.lkj.service.impl.backstage.location;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yingda.lkj.beans.entity.backstage.line.KilometerMark;
import com.yingda.lkj.beans.entity.backstage.line.RailwayLineSection;
import com.yingda.lkj.beans.entity.backstage.location.Location;
import com.yingda.lkj.beans.exception.CustomException;
import com.yingda.lkj.beans.pojo.location.ContainsLocation;
import com.yingda.lkj.beans.system.Json;
import com.yingda.lkj.beans.system.JsonMessage;
import com.yingda.lkj.dao.BaseDao;
import com.yingda.lkj.service.backstage.line.KilometerMarkService;
import com.yingda.lkj.service.backstage.location.LocationService;
import com.yingda.lkj.service.base.BaseService;
import com.yingda.lkj.utils.StreamUtil;
import com.yingda.lkj.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hood  2020/12/15
 */
@Service("locationService")
public class LocationServiceImpl implements LocationService {

    @Autowired
    private BaseDao<Location> locationBaseDao;
    @Autowired
    private BaseService<Location> locationBaseService;
    @Autowired
    private KilometerMarkService kilometerMarkService;
    @Autowired
    private BaseService<RailwayLineSection> railwayLineSectionBaseService;


    @Override
    public Location create(double latitude, double longitude, String dataId, byte type) {
        Location location = new Location();
        location.setId(UUID.randomUUID().toString());
        location.setDataId(dataId);
        location.setType(type);
        location.setLongitude(longitude);
        location.setLatitude(latitude);
        location.setAddTime(new Timestamp(System.currentTimeMillis()));
        location.setSeq(0);
        locationBaseDao.saveOrUpdate(location);

        return location;
    }

    @Override
    public Location getById(String id) {
        return locationBaseDao.get(Location.class, id);
    }

    @Override
    public List<Location> getByDataId(String dataId) {
        return locationBaseDao.find(
                "FROM Location WHERE dataId = :dataId ORDER BY seq",
                Map.of("dataId", dataId)
        );
    }

    @Override
    public void delete(String id) {
        locationBaseDao.executeHql(
                "delete from Location where id = :id",
                Map.of("id", id)
        );
    }

    @Override
    public List<? extends ContainsLocation> fillLocations(List<? extends ContainsLocation> containLocationDatas) {
        return fillLocations(containLocationDatas, null, null);
    }

    @Override
    public List<? extends ContainsLocation> fillLocations(List<? extends ContainsLocation> containLocationDatas, Timestamp startTime, Timestamp endTime) {
        return fillLocations(containLocationDatas, startTime, endTime, null);
    }

    @Override
    public List<? extends ContainsLocation> fillLocations(List<? extends ContainsLocation> containLocationDatas, Byte type) {
        return fillLocations(containLocationDatas, null, null, type);
    }

    @Override
    public List<? extends ContainsLocation> fillLocations(List<? extends ContainsLocation> containLocationDatas, Timestamp startTime, Timestamp endTime, Byte type) {
        if (containLocationDatas.isEmpty())
            return containLocationDatas;

        List<String> dataIds = StreamUtil.getList(containLocationDatas, ContainsLocation::getId);

        Map<String, Object> params = new HashMap<>(Map.of("dataIds", dataIds));
        String hql = "FROM Location WHERE dataId IN :dataIds\n";

        if (startTime != null) {
            hql += "AND addTime >= :startTime\n";
            params.put("startTime", startTime);
        }
        if (endTime != null) {
            hql += "AND addTime <= :endTime\n";
            params.put("endTime", endTime);
        }
        if (type != null) {
            hql += "AND type = :type\n";
            params.put("type", type);
        }
        hql += "ORDER BY seq, addTime";
        List<Location> locations = locationBaseDao.find(hql, params);

        Map<String, List<Location>> locationMap = locations.stream().collect(Collectors.groupingBy(Location::getDataId));

        for (ContainsLocation containLocationData : containLocationDatas) {
            List<Location> locationList = locationMap.get(containLocationData.getId());
            if (locationList != null)
                containLocationData.setLocations(locationList);
        }

        return containLocationDatas;
    }

    @Override
    public Location getLastLocation(ContainsLocation containsLocation) throws Exception {
        Map<String, Object> params = new HashMap<>();
        Map<String, String> conditions = new HashMap<>();
        params.put("dataId", containsLocation.getId());
        conditions.put("dataId", "=");

        List<Location> locations = locationBaseService.getObjcetPagination(Location.class, params, conditions, 1, 1, "order by seq desc, addTime desc");
        return locations.stream().reduce(null, (x, y) -> y);
    }

    @Override
    public List<Location> getLastLocation(ContainsLocation containsLocation, int count) throws Exception {
        Map<String, Object> params = new HashMap<>();
        Map<String, String> conditions = new HashMap<>();
        params.put("dataId", containsLocation.getId());
        conditions.put("dataId", "=");

        return locationBaseService.getObjcetPagination(Location.class, params, conditions, 1, count, "order by seq desc, addTime desc");
    }

    @Override
    public void deleteByDataId(String dataId) {
        locationBaseDao.executeHql(
                "delete from Location where dataId = :dataId",
                Map.of("dataId", dataId)
        );
    }

    @Override
    public List<KilometerMark> fillRailwayLineSectionsWithLocationsByPlan(List<RailwayLineSection> railwayLineSections, double startKilometer, double endKilometer) throws CustomException {
        // 防止有精神病反着填
        if (startKilometer > endKilometer) {
            double cache = startKilometer;
            startKilometer = endKilometer;
            endKilometer = cache;
        }

        List<KilometerMark> resultKilometerMarks = new ArrayList<>();

        for (RailwayLineSection railwayLineSection : railwayLineSections) {
            List<Location> locations = railwayLineSection.getLocations();
            locations = locations.stream()
                    .filter(x -> x.getKilometerMark() != null).collect(Collectors.toList());

            // 只拿在起止公里标范围内的location
            List<Location> cutLocations = new ArrayList<>();
            for (Location location : locations) {
                Double kilometerMark = location.getKilometerMark();
                if (kilometerMark >= startKilometer && kilometerMark <= endKilometer)
                    cutLocations.add(location);
            }
            railwayLineSection.setLocations(cutLocations);

            // 在切割后的线路两端添加公里标
            if (!cutLocations.isEmpty()) {
                KilometerMark startKilometerMark = new KilometerMark(startKilometer, List.of(cutLocations.get(0)));
                KilometerMark endKilometerMark = new KilometerMark(endKilometer, List.of(cutLocations.get(0)));
                // 为什么不合一起写，这不方便你卡断点吗
                resultKilometerMarks.add(startKilometerMark);
                resultKilometerMarks.add(endKilometerMark);
            }
        }

        return resultKilometerMarks;
    }

    @Override
    public void saveOrUpdate(Location location) {
        locationBaseDao.saveOrUpdate(location);
    }

    @Override
    public void bulkInsert(List<Location> locations) throws SQLException {
        if (locations.isEmpty()) return;

        List<String> locationIds = StreamUtil.getList(locations, Location::getId);
        locationBaseDao.executeHql(
                "delete from Location where id in :locationIds",
                Map.of("locationIds", locationIds)
        );


        StringBuilder sqlBuilder = new StringBuilder("""
                INSERT INTO location ( 
                    id, data_id, name, kilometer_mark, pos_type, type, longitude, latitude, altitude, add_time, seq 
                )
                VALUES
                """);
        for (Location location : locations) {
            if (location.getAltitude() == null) location.setAltitude(0d);

            String addSql = String.format(
                    """
                            (
                            '%s', # id
                            '%s', # data_id
                            '%s', # name
                            %f, # kilometerMark
                            %d, # posType
                            %d, # type
                            %f, # longitude
                            %f, # latitude
                            %f, # altitude
                            '%s', # add_time
                            %d # seq
                            ),
                            """,
                    location.getId(),
                    location.getDataId(),
                    location.getName(),
                    location.getKilometerMark(),
                    location.getPosType(),
                    location.getType(),
                    location.getLongitude(),
                    location.getLatitude(),
                    location.getAltitude(),
                    location.getAddTime(),
                    location.getSeq()
            );

            sqlBuilder.append(addSql);
        }
        String substring = sqlBuilder.substring(0, sqlBuilder.length() - 2);
        locationBaseDao.executeSql(substring);
    }

    @Override
    public Location saveOrUpdate(String dataId, double longitude, double latitude, byte dataType) {
        Location location = new Location(dataId, longitude, latitude, 0, dataType);
        locationBaseDao.saveOrUpdate(location);
        return location;
    }

    /**
     * 查询完整线路location
     * @return
     * @throws Exception
     */
    @Override
    public JSONObject getAllRailwayLineLocations() throws Exception {
        JSONObject featureCollectionJsonObject = new JSONObject();
        JSONArray featuresJsonArray = new JSONArray();

        String railwayLineSectionSql = "select * from railway_line_section";
        Map<String, Object> railwayLineSectionParams = new HashMap<>();

        // 根据筛选条件查出的RailwayLineSection集合，如果stationIds为空，查所有
        List<RailwayLineSection> railwayLineSections = railwayLineSectionBaseService.findSQL(
                railwayLineSectionSql,
                railwayLineSectionParams,
                RailwayLineSection.class
        );

        if (railwayLineSections.size() > 0) {

            // RailwayLineSection的id集合
            List<String> railwayLineSectionIds = railwayLineSections.stream().map(RailwayLineSection::getId).collect(Collectors.toList());

            String locationSql = "SELECT location.* FROM location WHERE location.type = :RAILWAY_LINE\n";

            Map<String, Object> locationParams = new HashMap<>();
            locationParams.put("RAILWAY_LINE", Location.RAILWAY_LINE);

            // 如果存在railwayLineSectionId，追加查询条件，否则查location所有
            if (railwayLineSectionIds.size() > 0) {
                locationSql += "AND location.data_id in :railwayLineSectionIds\n";
                locationParams.put("railwayLineSectionIds", railwayLineSectionIds);
            }

            locationSql += "ORDER BY location.seq";

            List<Location> locations = locationBaseService.findSQL(locationSql, locationParams, Location.class);

            // 创建分组map，railwayLineSectionId为key，location为value；主要为了减少操作DB的次数；后面的业务逻辑取此map中数据
            Map<String, List<Location>> railwayAndLocationMap = new LinkedHashMap<>();

            // 给railwayAndLocationMap填数据
            for (Location location : locations) {
                List<Location> railwayAndLocationMapValue = railwayAndLocationMap.get(location.getDataId());
                if (railwayAndLocationMapValue == null) {
                    railwayAndLocationMapValue = new ArrayList<>();
                    railwayAndLocationMapValue.add(location);
                    railwayAndLocationMap.put(location.getDataId(), railwayAndLocationMapValue);
                } else {
                    railwayAndLocationMapValue.add(location);
                }
            }

            // 下面开始业务逻辑
            railwayLineSectionIds.parallelStream().forEach(railwayLineSectionId -> {
                // 依次取出locations
                List<Location> locationMapValueList = railwayAndLocationMap.get(railwayLineSectionId);
                JSONObject featureJson = new JSONObject();
                JSONObject geometryJson = new JSONObject();
                JSONObject properties = new JSONObject();
                ArrayList<double[]> coordinatesArray = new ArrayList<>();

                // 取出每个location的longitude-经度，latitude-纬度
                locationMapValueList.forEach(location -> {
                    // Geojson-LineString需要的经纬度数据格式是[[],[],[]]
                    double[] coordinateArray = new double[]{location.getLongitude(), location.getLatitude()};
                    coordinatesArray.add(coordinateArray);
                });

                // 开始拼Geojson的featureCollection子集
                geometryJson.put("type", "LineString");
                geometryJson.put("coordinates", coordinatesArray);

                featureJson.put("type", "Feature");
                featureJson.put("geometry", geometryJson);
                featureJson.put("properties", properties);

                //子集添加到子集数组中
                featuresJsonArray.add(featureJson);
            });
        }

        // 套上父级外套
        featureCollectionJsonObject.put("type", "FeatureCollection");
        featureCollectionJsonObject.put("features", featuresJsonArray);

        return featureCollectionJsonObject;
    }


    /**
     * 根据车站ID获取线路location
     * @param stationIds
     * @return
     * @throws Exception
     */
    @Override
    public Json getRailwayLineLocationsByStationIds(String stationIds) throws Exception {
        Map<String, Object> attributes = new HashMap<>();
        JSONObject featureCollectionJsonObject = new JSONObject();
        JSONArray featuresJsonArray = new JSONArray();

        String railwayLineSectionSql = "select * from railway_line_section where 1 = 1\n";
        Map<String, Object> railwayLineSectionParams = new HashMap<>();

        if (StringUtils.isNotEmpty(stationIds)) {
            String[] stationIdArr = stationIds.split(",");
            railwayLineSectionSql += "and left_station_id in :stationIdArr";
            railwayLineSectionParams.put("stationIdArr", stationIdArr);
        }

        // 根据筛选条件查出的RailwayLineSection集合，如果stationIds为空，查所有
        List<RailwayLineSection> railwayLineSections = railwayLineSectionBaseService.findSQL(
                railwayLineSectionSql,
                railwayLineSectionParams,
                RailwayLineSection.class
        );

        if (railwayLineSections.size() > 0) {

            // RailwayLineSection的id集合
            List<String> railwayLineSectionIds = railwayLineSections.stream().map(RailwayLineSection::getId).collect(Collectors.toList());

            String locationSql = "SELECT location.* FROM location WHERE location.type = :RAILWAY_LINE\n";

            Map<String, Object> locationParams = new HashMap<>();
            locationParams.put("RAILWAY_LINE", Location.RAILWAY_LINE);

            // 如果存在railwayLineSectionId，追加查询条件，否则查location所有
            if (railwayLineSectionIds.size() > 0) {
                locationSql += "AND location.data_id in :railwayLineSectionIds\n";
                locationParams.put("railwayLineSectionIds", railwayLineSectionIds);
            }

            locationSql += "ORDER BY location.seq";

            List<Location> locations = locationBaseService.findSQL(locationSql, locationParams, Location.class);

            // 创建分组map，railwayLineSectionId为key，location为value；主要为了减少操作DB的次数；后面的业务逻辑取此map中数据
            Map<String, List<Location>> railwayAndLocationMap = new LinkedHashMap<>();

            // 给railwayAndLocationMap填数据
            for (Location location : locations) {
                List<Location> railwayAndLocationMapValue = railwayAndLocationMap.get(location.getDataId());
                if (railwayAndLocationMapValue == null) {
                    railwayAndLocationMapValue = new ArrayList<>();
                    railwayAndLocationMapValue.add(location);
                    railwayAndLocationMap.put(location.getDataId(), railwayAndLocationMapValue);
                } else {
                    railwayAndLocationMapValue.add(location);
                }
            }

            // 下面开始业务逻辑
            if (StringUtils.isNotEmpty(stationIds)) {
                String[] stationIdArr = stationIds.split(",");

                // 这层循环是为了根据stationId分组，以便前端地图区分出不同图层
                for (String stationId : stationIdArr) {
                    railwayLineSectionIds.parallelStream().forEach(railwayLineSectionId -> {
                        // 依次取出locations
                        List<Location> locationMapValueList = railwayAndLocationMap.get(railwayLineSectionId);
                        JSONObject featureJson = new JSONObject();
                        JSONObject geometryJson = new JSONObject();
                        JSONObject properties = new JSONObject();
                        ArrayList<double[]> coordinatesArray = new ArrayList<>();

                        // 取出每个location的longitude-经度，latitude-纬度
                        locationMapValueList.forEach(location -> {
                            // Geojson-LineString需要的经纬度数据格式是[[],[],[]]
                            double[] coordinateArray = new double[]{location.getLongitude(), location.getLatitude()};
                            coordinatesArray.add(coordinateArray);
                        });

                        // 开始拼Geojson的featureCollection子集
                        geometryJson.put("type", "LineString");
                        geometryJson.put("coordinates", coordinatesArray);

                        featureJson.put("type", "Feature");
                        featureJson.put("geometry", geometryJson);
                        featureJson.put("properties", properties);

                        //子集添加到子集数组中
                        featuresJsonArray.add(featureJson);
                    });

                    // 套上父级外套
                    featureCollectionJsonObject.put("type", "FeatureCollection");
                    featureCollectionJsonObject.put("features", featuresJsonArray);

                    attributes.put(stationId, featureCollectionJsonObject);
                }
            }
        }

        return new Json(JsonMessage.SUCCESS, attributes);
    }
}
