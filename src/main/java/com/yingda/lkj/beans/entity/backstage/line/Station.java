package com.yingda.lkj.beans.entity.backstage.line;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * 车站
 *
 * @author hood  2019/12/30
 */
@Entity
@Table(name = "station")
public class Station {
    private String id;
    private String code;
    private String name;
    private int seq; // 暂时用不上，因为线路车站多对多，如果对某条线路下的车站排序，使用中间表station_railway_line.seq排序
    private byte hide;
    private String workshopId;
    private Timestamp addTime;
    private Timestamp updateTime;
    private Double kilometerMark; // 公里标

    // page field
    private String bureauId;
    private String sectionId;
    private String sectionName;
    private String workshopName;
    private String workAreaName;
    private String lineName;
    private String railwayLineNames;
    private String railwayLineCodes;
    private String kilometerMarkText; // 公里标文本，例如“7K+509”

    // 虽然数据库中车站线路是多对多的，但是目前业务逻辑是多对一
    private String railwayLineId;

    public Station() {
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
    @Column(name = "name", nullable = false, length = 255)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "hide", nullable = false)
    public byte getHide() {
        return hide;
    }

    public void setHide(byte hide) {
        this.hide = hide;
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

    @Basic
    @Column(name = "code", nullable = false, length = 50)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Transient
    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    @Transient
    public String getWorkshopName() {
        return workshopName;
    }

    public void setWorkshopName(String workshopName) {
        this.workshopName = workshopName;
    }

    @Basic
    @Column(name = "seq", nullable = false)
    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    @Transient
    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    @Transient
    public String getWorkAreaName() {
        return workAreaName;
    }

    public void setWorkAreaName(String workAreaName) {
        this.workAreaName = workAreaName;
    }

    @Transient
    public String getBureauId() {
        return bureauId;
    }

    public void setBureauId(String bureauId) {
        this.bureauId = bureauId;
    }

    @Transient
    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    @Transient
    public String getRailwayLineId() {
        return railwayLineId;
    }

    public void setRailwayLineId(String railwayLineId) {
        this.railwayLineId = railwayLineId;
    }

    @Transient
    public String getRailwayLineNames() {
        return railwayLineNames;
    }

    public void setRailwayLineNames(String railwayLineNames) {
        this.railwayLineNames = railwayLineNames;
    }

    @Transient
    public String getRailwayLineCodes() {
        return railwayLineCodes;
    }

    public void setRailwayLineCodes(String railwayLineCodes) {
        this.railwayLineCodes = railwayLineCodes;
    }

    @Basic
    @Column(name = "workshop_id", nullable = false, length = 36)
    public String getWorkshopId() {
        return workshopId;
    }

    public void setWorkshopId(String workshopId) {
        this.workshopId = workshopId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return seq == station.seq &&
                hide == station.hide &&
                Objects.equals(id, station.id) &&
                Objects.equals(code, station.code) &&
                Objects.equals(name, station.name) &&
                Objects.equals(addTime, station.addTime) &&
                Objects.equals(updateTime, station.updateTime) &&
                Objects.equals(workshopId, station.workshopId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, workshopId, name, code, seq, hide, addTime, updateTime);
    }

    @Basic
    @Column(name = "kilometer_mark", nullable = true, precision = 4)
    public Double getKilometerMark() {
        return kilometerMark;
    }

    public void setKilometerMark(Double kilometerMark) {
        this.kilometerMark = kilometerMark;
    }

    @Transient
    public String getKilometerMarkText() {
        return kilometerMarkText;
    }

    public void setKilometerMarkText(String kilometerMarkText) {
        this.kilometerMarkText = kilometerMarkText;
    }
}
