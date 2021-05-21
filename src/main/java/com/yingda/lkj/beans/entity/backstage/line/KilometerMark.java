package com.yingda.lkj.beans.entity.backstage.line;

import com.yingda.lkj.beans.entity.backstage.location.Location;
import com.yingda.lkj.beans.pojo.location.ContainsLocation;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @see RailwayLineSection;
 */
@Entity
@Table(name = "kilometer_mark", schema = "opc_measurement", catalog = "")
public class KilometerMark implements ContainsLocation {
    private String id;
    private String railwayLineSectionId; // 线路经纬度数据
    private String stationId; // 车站id（可以为空，为空时表示车站公里标.现在的逻辑为一条线上只有一套公里标）
    private double kilometer; // 公里标数值
    private String name;
    private Timestamp addTime;
    private Timestamp updateTime;

    // pageField
    private List<Location> locations;


    public KilometerMark() {
    }

    public KilometerMark(String railwayLineSectionId, String stationId, double kilometer) {
        this.id = UUID.randomUUID().toString();
        this.railwayLineSectionId = railwayLineSectionId;
        this.stationId = stationId;
        this.kilometer = kilometer;
        this.addTime = new Timestamp(System.currentTimeMillis());
    }

    public KilometerMark(double kilometer, List<Location> locations) {
        this.kilometer = kilometer;
        this.locations = locations;
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
    @Column(name = "railway_line_section_id", nullable = false, length = 36)
    public String getRailwayLineSectionId() {
        return railwayLineSectionId;
    }

    public void setRailwayLineSectionId(String railwayLineSectionId) {
        this.railwayLineSectionId = railwayLineSectionId;
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
        KilometerMark that = (KilometerMark) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(railwayLineSectionId, that.railwayLineSectionId) &&
                Objects.equals(name, that.name) &&
                Objects.equals(addTime, that.addTime) &&
                Objects.equals(updateTime, that.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, railwayLineSectionId, name, addTime, updateTime);
    }

    @Basic
    @Column(name = "station_id", nullable = true, length = 36)
    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    @Basic
    @Column(name = "kilometer", nullable = false, precision = 0)
    public double getKilometer() {
        return kilometer;
    }

    public void setKilometer(double kilometer) {
        this.kilometer = kilometer;
    }

    @Transient
    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }
}
