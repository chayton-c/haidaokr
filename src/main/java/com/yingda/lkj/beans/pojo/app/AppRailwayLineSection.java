package com.yingda.lkj.beans.pojo.app;

import com.yingda.lkj.beans.entity.backstage.line.RailwayLineSection;

import java.sql.Timestamp;

public class AppRailwayLineSection {
    private String key;
    private String railwayLineId;
    private String leftStationId;
    private String rightStationId;

    public static RailwayLineSection transform2RailwayLineSection(AppRailwayLineSection appRailwayLineSection) {
        RailwayLineSection railwayLineSection = new RailwayLineSection();
        railwayLineSection.setId(appRailwayLineSection.getKey());
        railwayLineSection.setRailwayLineId(appRailwayLineSection.getRailwayLineId());
        railwayLineSection.setLeftStationId(appRailwayLineSection.getLeftStationId());
        railwayLineSection.setRightStationId(appRailwayLineSection.getRightStationId());
        railwayLineSection.setDownriver(RailwayLineSection.SINGLE_LINE);
        railwayLineSection.setAddTime(new Timestamp(System.currentTimeMillis()));

        return railwayLineSection;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getRailwayLineId() {
        return railwayLineId;
    }

    public void setRailwayLineId(String railwayLineId) {
        this.railwayLineId = railwayLineId;
    }

    public String getLeftStationId() {
        return leftStationId;
    }

    public void setLeftStationId(String leftStationId) {
        this.leftStationId = leftStationId;
    }

    public String getRightStationId() {
        return rightStationId;
    }

    public void setRightStationId(String rightStationId) {
        this.rightStationId = rightStationId;
    }
}
