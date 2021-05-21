package com.yingda.lkj.beans.entity.backstage.line;

import com.yingda.lkj.beans.entity.backstage.location.Location;
import com.yingda.lkj.beans.pojo.location.ContainsLocation;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * 铁路线段，用于表示铁路线经纬度
 */
@Entity
@Table(name = "railway_line_section", schema = "opc_measurement", catalog = "")
public class RailwayLineSection implements ContainsLocation {

    // downriver字段
    public static final byte UPRIVER = 0;
    public static final byte DOWNRIVER = 1;
    public static final byte SINGLE_LINE = 2;

    private String id;
    private String railwayLineId;
    private String leftStationId;
    private String rightStationId;
    private byte downriver; // 上行/下行/单线
    private Timestamp addTime;
    private Timestamp updateTime;
    private Double lineLength; // 长度

    // pageField
    private List<Location> locations;
    private String railwayLineName;
    private String railwayLineCode;
    private String leftStationName;
    private String leftStationCode;
    private String rightStationName;
    private String rightStationCode;

    // appInfo


    public RailwayLineSection() {
    }

    public RailwayLineSection(String railwayLineId, String leftStationId, String rightStationId, byte downriver) {
        this.id = UUID.randomUUID().toString();
        this.railwayLineId = railwayLineId;
        this.leftStationId = leftStationId;
        this.rightStationId = rightStationId;
        this.downriver = downriver;
        this.addTime = new Timestamp(System.currentTimeMillis());
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
    @Column(name = "railway_line_id", nullable = false, length = 36)
    public String getRailwayLineId() {
        return railwayLineId;
    }

    public void setRailwayLineId(String railwayLineId) {
        this.railwayLineId = railwayLineId;
    }

    @Basic
    @Column(name = "left_station_id", nullable = false, length = 36)
    public String getLeftStationId() {
        return leftStationId;
    }

    public void setLeftStationId(String leftStationId) {
        this.leftStationId = leftStationId;
    }

    @Basic
    @Column(name = "right_station_id", nullable = false, length = 36)
    public String getRightStationId() {
        return rightStationId;
    }

    public void setRightStationId(String rightStationId) {
        this.rightStationId = rightStationId;
    }

    @Basic
    @Column(name = "add_time", nullable = true)
    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
        this.addTime = addTime;
    }

    @Basic
    @Column(name = "update_time", nullable = true)
    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RailwayLineSection that = (RailwayLineSection) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(railwayLineId, that.railwayLineId) &&
                Objects.equals(leftStationId, that.leftStationId) &&
                Objects.equals(rightStationId, that.rightStationId) &&
                Objects.equals(addTime, that.addTime) &&
                Objects.equals(updateTime, that.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, railwayLineId, leftStationId, rightStationId, addTime, updateTime);
    }

    @Transient
    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    @Basic
    @Column(name = "downriver", nullable = false)
    public byte getDownriver() {
        return downriver;
    }

    public void setDownriver(byte downriver) {
        this.downriver = downriver;
    }

    @Transient
    public String getRailwayLineName() {
        return railwayLineName;
    }

    public void setRailwayLineName(String railwayLineName) {
        this.railwayLineName = railwayLineName;
    }

    @Transient
    public String getRailwayLineCode() {
        return railwayLineCode;
    }

    public void setRailwayLineCode(String railwayLineCode) {
        this.railwayLineCode = railwayLineCode;
    }

    @Transient
    public String getLeftStationName() {
        return leftStationName;
    }

    public void setLeftStationName(String leftStationName) {
        this.leftStationName = leftStationName;
    }

    @Transient
    public String getLeftStationCode() {
        return leftStationCode;
    }

    public void setLeftStationCode(String leftStationCode) {
        this.leftStationCode = leftStationCode;
    }

    @Transient
    public String getRightStationName() {
        return rightStationName;
    }

    public void setRightStationName(String rightStationName) {
        this.rightStationName = rightStationName;
    }

    @Transient
    public String getRightStationCode() {
        return rightStationCode;
    }

    public void setRightStationCode(String rightStationCode) {
        this.rightStationCode = rightStationCode;
    }

    @Basic
    @Column(name = "line_length")
    public Double getLineLength() {
        return lineLength;
    }

    public void setLineLength(Double lineLength) {
        this.lineLength = lineLength;
    }
}
