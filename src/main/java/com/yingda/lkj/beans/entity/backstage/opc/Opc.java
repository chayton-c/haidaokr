package com.yingda.lkj.beans.entity.backstage.opc;

import com.yingda.lkj.beans.entity.backstage.location.Location;
import com.yingda.lkj.beans.pojo.location.ContainsLocation;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

/**
 * @author hood  2020/12/14
 */
@Entity
@Table(name = "opc", schema = "opc_measurement")
public class Opc implements ContainsLocation {
    private String id;
    private String name;
    private String code;
    private String leftStationId;
    private String rightStationId;
    private String opcTypeId;
    private String measureMode; // app用的
    private Double measureDistance; // 光电缆距离
    private Timestamp addTime;
    private Timestamp updateTime;

    // page fields
    private String opcTypeName;
    private List<OpcMark> opcMarks;
    private List<Location> locations;
    private String opcMarkName;
    private String opcMarkId;

    @Id
    @Column(name = "id", nullable = false, length = 36)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 255)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "code", nullable = true, length = 255)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
        Opc opc = (Opc) o;
        return Objects.equals(id, opc.id) &&
                Objects.equals(name, opc.name) &&
                Objects.equals(code, opc.code) &&
                Objects.equals(leftStationId, opc.leftStationId) &&
                Objects.equals(rightStationId, opc.rightStationId) &&
                Objects.equals(addTime, opc.addTime) &&
                Objects.equals(updateTime, opc.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code, leftStationId, rightStationId, addTime, updateTime);
    }

    @Transient
    public List<OpcMark> getOpcMarks() {
        return opcMarks;
    }

    public void setOpcMarks(List<OpcMark> opcMarks) {
        this.opcMarks = opcMarks;
    }

    @Transient
    @Override
    public List<Location> getLocations() {
        return locations;
    }

    @Override
    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    @Basic
    @Column(name = "opc_type_id", nullable = false, length = 36)
    public String getOpcTypeId() {
        return opcTypeId;
    }

    public void setOpcTypeId(String opcTypeId) {
        this.opcTypeId = opcTypeId;
    }

    @Transient
    public String getOpcTypeName() {
        return opcTypeName;
    }

    public void setOpcTypeName(String opcTypeName) {
        this.opcTypeName = opcTypeName;
    }

    @Basic
    @Column(name = "measure_mode", nullable = true, length = 255)
    public String getMeasureMode() {
        return measureMode;
    }

    public void setMeasureMode(String measureMode) {
        this.measureMode = measureMode;
    }

    @Transient
    public String getOpcMarkName() {
        return opcMarkName;
    }

    public void setOpcMarkName(String opcMarkName) {
        this.opcMarkName = opcMarkName;
    }

    @Transient
    public String getOpcMarkId() {
        return opcMarkId;
    }

    public void setOpcMarkId(String opcMarkId) {
        this.opcMarkId = opcMarkId;
    }

    @Basic
    @Column(name = "measure_distance", nullable = true, precision = 0)
    public Double getMeasureDistance() {
        return measureDistance;
    }

    public void setMeasureDistance(Double measureDistance) {
        this.measureDistance = measureDistance;
    }
}
