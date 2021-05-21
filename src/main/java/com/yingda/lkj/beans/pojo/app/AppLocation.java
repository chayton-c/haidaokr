package com.yingda.lkj.beans.pojo.app;

import com.yingda.lkj.beans.entity.backstage.location.Location;

public class AppLocation {
    private String key;
    private String dataId;
    private double latitude;
    private double longitude;
    private double altitude;
    private byte type;
    private int seq;

    public static Location createLocation(AppLocation appLocation) {
        Location location = new Location(appLocation.getDataId(), appLocation.getLongitude(), appLocation.getLatitude(),
                appLocation.getSeq(), appLocation.getType());
        location.setId(appLocation.getKey());
        return location;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }
}
