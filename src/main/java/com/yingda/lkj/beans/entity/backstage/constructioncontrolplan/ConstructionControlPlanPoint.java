package com.yingda.lkj.beans.entity.backstage.constructioncontrolplan;

import com.yingda.lkj.beans.entity.backstage.location.Location;
import com.yingda.lkj.beans.pojo.constructioncontrolplan.AppConstructionControlPlanPoint;
import com.yingda.lkj.beans.pojo.location.ContainsLocation;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

/**
 * 施工测量点
 */
@Entity
@Table(name = "construction_control_plan_point", schema = "opc_measurement", catalog = "")
public class ConstructionControlPlanPoint implements ContainsLocation {
    // collectType
    public static byte POINT = 0;
    public static byte LINE = 1;
    public static byte AREA = 2;

    private String id;
    private String name;
    private double radius; // 影响范围
    private byte collectType; // 施工区域类型(点、线、区域)
    private String constructionControlPlanId; // 施工计划id
    private String code;
    private Timestamp addTime;
    private Timestamp updateTime;
    private Integer seq;

    // pagefield
    private List<Location> locations;
    private double shortestDistance;

    public ConstructionControlPlanPoint() {
    }

    public ConstructionControlPlanPoint(AppConstructionControlPlanPoint appConstructionControlPlanPoint) {
        this.id = appConstructionControlPlanPoint.getId();
        this.name = appConstructionControlPlanPoint.getName();
        this.radius = appConstructionControlPlanPoint.getRadius();
        this.collectType = appConstructionControlPlanPoint.getCollectType();
        this.constructionControlPlanId = appConstructionControlPlanPoint.getConstructionControlPlanId();
        this.code = appConstructionControlPlanPoint.getCode();
        this.addTime = new Timestamp(System.currentTimeMillis());
        this.updateTime = this.addTime;
        this.seq = appConstructionControlPlanPoint.getSeq();
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
    @Column(name = "name", nullable = true, length = 255)
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
        ConstructionControlPlanPoint that = (ConstructionControlPlanPoint) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(code, that.code) &&
                Objects.equals(addTime, that.addTime) &&
                Objects.equals(updateTime, that.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code, addTime, updateTime);
    }

    @Basic
    @Column(name = "seq", nullable = true)
    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    @Basic
    @Column(name = "construction_control_plan_id", nullable = false, length = 36)
    public String getConstructionControlPlanId() {
        return constructionControlPlanId;
    }

    public void setConstructionControlPlanId(String constructionControlPlanId) {
        this.constructionControlPlanId = constructionControlPlanId;
    }

    @Basic
    @Column(name = "radius", nullable = false, precision = 0)
    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    @Basic
    @Column(name = "collect_type", nullable = false)
    public byte getCollectType() {
        return collectType;
    }

    public void setCollectType(byte collectType) {
        this.collectType = collectType;
    }

    @Transient
    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    @Transient
    public double getShortestDistance() {
        return shortestDistance;
    }

    public void setShortestDistance(double shortestDistance) {
        this.shortestDistance = shortestDistance;
    }
}
