package com.yingda.lkj.beans.entity.backstage.location;

import com.google.gson.annotations.SerializedName;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

/**
 * 位置信息
 * @author hood  2020/12/14
 */
@Entity
@Table(name = "location", schema = "opc_measurement")
public class Location {

    // type字段
    public static final byte OPC = 1; // 光电缆位置
    public static final byte OPC_MARK = 2; // 光电缆标识
    public static final byte CONSTRUCTION_CONTROL_PLAN_POINT = 3; // 方案施工点
    public static final byte RAILWAY_LINE = 4; // 铁路线
    public static final byte KILOMETER_MARKS = 5; // 公里标
    public static final byte EQUIPMENT = 6; // 设备

    private String id;
    private String dataId;
    private String name; // 名称
    private Double kilometerMark; // 公里标
    private Integer posType; // 解算状态值
    private byte type;
    private double longitude;
    private double latitude;
    private Double altitude;
    private Timestamp addTime;
    @SerializedName("serialNumber")
    private int seq;

    // page fields
    private String opcMarkName;


    private String opcMarkremark;
    private String opcMarkTypeName;
    private String opcMarkKilometerMark;
    private String opcColor;
    private String opcId;
    private String iconUrl;
    private String shadowUrl;

    public Location() {
    }

    public Location(String dataId, double longitude, double latitude, int seq, byte type) {
        this.id = UUID.randomUUID().toString();
        this.dataId = dataId;
        this.type = type;
        this.longitude = longitude;
        this.latitude = latitude;
        this.addTime = new Timestamp(System.currentTimeMillis());
        this.seq = seq;
    }

    public Location(String id, String dataId, String name, Double kilometerMark, byte type, double longitude,
                    double latitude, Double altitude, int seq) {
        this.id = id;
        this.dataId = dataId;
        this.name = name;
        this.kilometerMark = kilometerMark;
        this.type = RAILWAY_LINE;
        this.longitude = longitude;
        this.latitude = latitude;
        this.altitude = altitude;
        this.addTime = new Timestamp(System.currentTimeMillis());
        this.seq = seq;
    }

    @Id
    @Column(name = "id", nullable = false, length = 36)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "data_id", nullable = false, length = 36)
    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    @Basic
    @Column(name = "type", nullable = false)
    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    @Basic
    @Column(name = "longitude", nullable = false, precision = 0)
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Basic
    @Column(name = "latitude", nullable = false, precision = 0)
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Basic
    @Column(name = "altitude", nullable = true, precision = 0)
    public Double getAltitude() {
        return altitude;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    @Basic
    @Column(name = "add_time", nullable = true)
    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
        this.addTime = addTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return type == location.type &&
                Objects.equals(id, location.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Basic
    @Column(name = "seq", nullable = false)
    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    @Basic
    @Column(name = "name", nullable = true, length = 255)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "pos_type", nullable = true)
    public Integer getPosType() {
        return posType;
    }

    public void setPosType(Integer posType) {
        this.posType = posType;
    }

    @Basic
    @Column(name = "kilometer_mark", nullable = true, precision = 0)
    public Double getKilometerMark() {
        return kilometerMark;
    }

    public void setKilometerMark(Double kilometerMark) {
        this.kilometerMark = kilometerMark;
    }

    @Transient
    public String getOpcMarkName() {
        return opcMarkName;
    }

    public void setOpcMarkName(String opcMarkName) {
        this.opcMarkName = opcMarkName;
    }

    @Transient
    public String getOpcMarkremark() {
        return opcMarkremark;
    }

    public void setOpcMarkremark(String opcMarkremark) {
        this.opcMarkremark = opcMarkremark;
    }


    @Transient
    public String getOpcMarkTypeName() {
        return opcMarkTypeName;
    }

    public void setOpcMarkTypeName(String opcMarkTypeName) {
        this.opcMarkTypeName = opcMarkTypeName;
    }

    @Transient
    public String getOpcMarkKilometerMark() {
        return opcMarkKilometerMark;
    }

    public void setOpcMarkKilometerMark(String opcMarkKilometerMark) {
        this.opcMarkKilometerMark = opcMarkKilometerMark;
    }

    @Transient
    public String getOpcColor() {
        return opcColor;
    }

    public void setOpcColor(String opcColor) {
        this.opcColor = opcColor;
    }

    @Transient
    public String getOpcId() {
        return opcId;
    }

    public void setOpcId(String opcId) {
        this.opcId = opcId;
    }

    @Transient
    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    @Transient
    public String getShadowUrl() {
        return shadowUrl;
    }

    public void setShadowUrl(String shadowUrl) {
        this.shadowUrl = shadowUrl;
    }
}
