package com.yingda.lkj.controller.app.upload;

import com.yingda.lkj.beans.entity.backstage.line.RailwayLine;
import com.yingda.lkj.beans.entity.backstage.line.RailwayLineSection;
import com.yingda.lkj.beans.entity.backstage.line.Station;
import com.yingda.lkj.beans.entity.backstage.location.Location;
import com.yingda.lkj.beans.exception.CustomException;
import com.yingda.lkj.beans.system.Json;
import com.yingda.lkj.beans.system.JsonMessage;
import com.yingda.lkj.controller.BaseController;
import com.yingda.lkj.service.backstage.line.RailwayLineSectionService;
import com.yingda.lkj.service.backstage.line.RailwayLineService;
import com.yingda.lkj.service.backstage.line.StationService;
import com.yingda.lkj.service.backstage.location.LocationService;
import com.yingda.lkj.utils.JsonUtils;
import com.yingda.lkj.utils.StreamUtil;
import com.yingda.lkj.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequestMapping("/app/uploadLocation")
@RestController
public class UploadLocationController extends BaseController {

    @Autowired
    private LocationService locationService;
    @Autowired
    private StationService stationService;
    @Autowired
    private RailwayLineSectionService railwayLineSectionService;
    @Autowired
    private RailwayLineService railwayLineService;

    @RequestMapping("")
    public Json upload() throws IOException, CustomException, SQLException {
        String data = new String(req.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

        AppLocationJson appLocationJson = JsonUtils.parse(data, AppLocationJson.class);
        List<AppLocation> rawAppLocations = appLocationJson.getLocations();

        List<Station> stations = stationService.getAll();
        Map<String, Station> stationCodeMap = StreamUtil.getMap(stations, Station::getCode, x -> x);

        List<AppLocation> fixedAppLocations = new ArrayList<>();

        String currentStationId = "";
        List<AppLocation> currentAppLocations = new ArrayList<>();
        for (AppLocation appLocation : rawAppLocations) {
            String stationCode = appLocation.getStationCode();

            if (StringUtils.isNotEmpty(stationCode)) {
                Station station = stationCodeMap.get(stationCode);
                if (station == null) throw new CustomException(JsonMessage.PARAM_INVALID, "找不到code = " + stationCode + "的车站");
                currentStationId = station.getId();
                if (!currentAppLocations.isEmpty()) {
                    currentAppLocations.forEach(x -> x.setEndStationId(station.getId()));
                    fixedAppLocations.addAll(currentAppLocations);
                    currentAppLocations = new ArrayList<>();
                }
            }

            appLocation.setStartStationId(currentStationId);
            currentAppLocations.add(appLocation);
        }
        fixedAppLocations = fixedAppLocations.stream().filter(x -> StringUtils.isNotEmpty(x.getEndStationId(), x.getStartStationId())).collect(Collectors.toList());

        Map<String, List<AppLocation>> groupedApplocations =
                fixedAppLocations.stream().collect(Collectors.groupingBy(x -> x.getStartStationId() + x.getEndStationId()));

        List<RailwayLine> railwayLines = railwayLineService.getAll();
        Map<String, RailwayLine> railwayLineCodeMap = StreamUtil.getMap(railwayLines, RailwayLine::getCode, x -> x);

        List<Location> pendingSavedLocation = new ArrayList<>();
        for (List<AppLocation> value : groupedApplocations.values()) {
            AppLocation headAppLocation = value.get(0);
            String startStationId = headAppLocation.getStartStationId();
            String endStationId = headAppLocation.getEndStationId();
            byte downriver = headAppLocation.getDownriver();

            String railwayLineCode = headAppLocation.getRailwayLineCode();
            RailwayLine railwayLine = railwayLineCodeMap.get(railwayLineCode);
            if (railwayLine == null) throw new CustomException(JsonMessage.PARAM_INVALID, "找不到code = " + railwayLineCode + "的线路");

            RailwayLineSection railwayLineSection = railwayLineSectionService.getByStationIdAndDownriver(startStationId, endStationId, downriver);
            if (railwayLineSection == null) {
                railwayLineSection = new RailwayLineSection(railwayLine.getId(), startStationId, endStationId, downriver);
                railwayLineSectionService.saveOrUpdate(railwayLineSection);
            }
            String dataId = railwayLineSection.getId();
            locationService.deleteByDataId(dataId);

            for (AppLocation appLocation : value) {
                Location location = new Location(
                        appLocation.getId(), dataId, appLocation.getName(), appLocation.getKilometerMark() * 1000, Location.RAILWAY_LINE, appLocation.getLongitude(),
                        appLocation.getLatitude(), appLocation.getLatitude(), appLocation.getSeq()
                );
                pendingSavedLocation.add(location);
            }
            locationService.bulkInsert(pendingSavedLocation);
            pendingSavedLocation.clear();
        }

        return new Json(JsonMessage.SUCCESS);
    }

    static class AppLocationJson {
        private List<AppLocation> locations;

        public List<AppLocation> getLocations() {
            return locations;
        }

        public void setLocations(List<AppLocation> locations) {
            this.locations = locations;
        }
    }

    static class AppLocation {
        // downriver字段
        public static final byte UPRIVER = 0; // 上行
        public static final byte DOWNRIVER = 1; // 下行
        public static final byte SINGLE_LINE = 2; // 单线

        private String id;
        private int seq;
        private double longitude;
        private double latitude;
        private Double altitude;
        private byte downriver;
        private String name;
        private Double kilometerMark;
        private String stationCode; // 车站编码
        private String railwayLineCode; // 线路编码

        private String startStationId;
        private String endStationId;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getSeq() {
            return seq;
        }

        public void setSeq(int seq) {
            this.seq = seq;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Double getKilometerMark() {
            return kilometerMark;
        }

        public void setKilometerMark(Double kilometerMark) {
            this.kilometerMark = kilometerMark;
        }

        public String getStationCode() {
            return stationCode;
        }

        public void setStationCode(String stationCode) {
            this.stationCode = stationCode;
        }

        public String getRailwayLineCode() {
            return railwayLineCode;
        }

        public void setRailwayLineCode(String railwayLineCode) {
            this.railwayLineCode = railwayLineCode;
        }

        public String getStartStationId() {
            return startStationId;
        }

        public void setStartStationId(String startStationId) {
            this.startStationId = startStationId;
        }

        public String getEndStationId() {
            return endStationId;
        }

        public void setEndStationId(String endStationId) {
            this.endStationId = endStationId;
        }

        public byte getDownriver() {
            return downriver;
        }

        public void setDownriver(byte downriver) {
            this.downriver = downriver;
        }
    }

}
