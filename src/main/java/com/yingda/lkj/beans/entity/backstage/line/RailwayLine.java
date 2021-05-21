package com.yingda.lkj.beans.entity.backstage.line;

import com.yingda.lkj.beans.Constant;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * 铁路线
 *
 * @author hood  2019/12/30
 */
@Entity
@Table(name = "railway_line")
public class RailwayLine {

    private String id;
    private String bureauId; // 局所在id
    private String name;
    private byte hide;
    private Timestamp addTime;
    private Timestamp updateTime;
    private String code;

    // field params
    private int lineNumber;

    //eggk page field
    private List<Station> stationList;
    private String bureauName;
    private String typeInfo;

    public RailwayLine() {
    }

    public RailwayLine(RailwayLine pageRailwayLine) {
        this.id = UUID.randomUUID().toString();
        this.bureauId = pageRailwayLine.getBureauId();
        this.name = pageRailwayLine.getName();
        this.hide = Constant.FALSE;
        this.code = pageRailwayLine.code;
        this.addTime = new Timestamp(System.currentTimeMillis());
    }

    @Basic
    @Column(name = "code", nullable = false, unique = true, length = 50)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
    @Column(name = "bureau_id", nullable = false, length = 36)
    public String getBureauId() {
        return bureauId;
    }

    public void setBureauId(String bureauId) {
        this.bureauId = bureauId;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 50)
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

    @Transient
    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    @Transient
    public List<Station> getStationList() {
        return stationList;
    }

    public void setStationList(List<Station> stationList) {
        this.stationList = stationList;
    }

    @Transient
    public String getBureauName() {
        return bureauName;
    }

    public void setBureauName(String bureauName) {
        this.bureauName = bureauName;
    }

    @Transient
    public String getTypeInfo() {
        return typeInfo;
    }

    public void setTypeInfo(String typeInfo) {
        this.typeInfo = typeInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RailwayLine that = (RailwayLine) o;
        return hide == that.hide &&
                Objects.equals(id, that.id) &&
                Objects.equals(bureauId, that.bureauId) &&
                Objects.equals(name, that.name) &&
                Objects.equals(addTime, that.addTime) &&
                Objects.equals(updateTime, that.updateTime) &&
                Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bureauId, name, code, hide, addTime, updateTime);
    }

    @Override
    public String toString() {
        return "RailwayLine{" +
                "id='" + id + '\'' +
                ", bureauId='" + bureauId + '\'' +
                ", name='" + name + '\'' +
                ", hide=" + hide +
                ", addTime=" + addTime +
                ", updateTime=" + updateTime +
                ", code='" + code + '\'' +
                ", lineNumber=" + lineNumber +
                ", stationList=" + stationList +
                ", bureauName='" + bureauName + '\'' +
                ", typeInfo='" + typeInfo + '\'' +
                '}';
    }
}
