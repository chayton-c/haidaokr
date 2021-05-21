package com.yingda.lkj.beans.entity.backstage.constructioncontrolplan;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * 施工位置表（施工计划ConstructionControlPlan的副表）
 */
@Entity
@Table(name = "construction_control_plan_kilometer_mark", schema = "opc_measurement", catalog = "")
public class ConstructionControlPlanKilometerMark {

    // downriver字段
    public static final byte UPRIVER = 0; // 上行
    public static final byte DOWNRIVER = 1; // 下行
    public static final byte SINGLE_LINE = 2; // 单线
    public static final byte UP_AND_DOWN = 3; // 上下行


    private String id;
    private String constructionControlPlanId; // 施工计划id
    private String railwayLineId; // 线路
    private byte downriver; // 行别
    private String startStationId; // 施工地点起始站
    private String endStationId; // 施工地点终点站
    private double startKilometer; // 起始公里标
    private double endKilometer; // 结束公里标
    private Timestamp addTime;
    private Timestamp updateTime;

    // pageField
    private String railwayLineName;
    private String startStationName;
    private String endStationName;
    private String detailInfo;

    @Id
    @Column(name = "id", nullable = false, length = 36)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
    @Column(name = "railway_line_id", nullable = false, length = 36)
    public String getRailwayLineId() {
        return railwayLineId;
    }

    public void setRailwayLineId(String railwayLineId) {
        this.railwayLineId = railwayLineId;
    }

    @Basic
    @Column(name = "start_station_id", nullable = false, length = 36)
    public String getStartStationId() {
        return startStationId;
    }

    public void setStartStationId(String startStationId) {
        this.startStationId = startStationId;
    }

    @Basic
    @Column(name = "end_station_id", nullable = false, length = 36)
    public String getEndStationId() {
        return endStationId;
    }

    public void setEndStationId(String endStationId) {
        this.endStationId = endStationId;
    }

    @Basic
    @Column(name = "start_kilometer", nullable = false, precision = 0)
    public double getStartKilometer() {
        return startKilometer;
    }

    public void setStartKilometer(double startKilometer) {
        this.startKilometer = startKilometer;
    }

    @Basic
    @Column(name = "end_kilometer", nullable = false, precision = 0)
    public double getEndKilometer() {
        return endKilometer;
    }

    public void setEndKilometer(double endKilometer) {
        this.endKilometer = endKilometer;
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
        ConstructionControlPlanKilometerMark that = (ConstructionControlPlanKilometerMark) o;
        return Double.compare(that.startKilometer, startKilometer) == 0 && Double.compare(that.endKilometer, endKilometer) == 0 && Objects.equals(id, that.id) && Objects.equals(constructionControlPlanId, that.constructionControlPlanId) && Objects.equals(railwayLineId, that.railwayLineId) && Objects.equals(startStationId, that.startStationId) && Objects.equals(endStationId, that.endStationId) && Objects.equals(addTime, that.addTime) && Objects.equals(updateTime, that.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, constructionControlPlanId, railwayLineId, startStationId, endStationId, startKilometer, endKilometer,
                addTime, updateTime);
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
    public String getStartStationName() {
        return startStationName;
    }

    public void setStartStationName(String startStationName) {
        this.startStationName = startStationName;
    }

    @Transient
    public String getEndStationName() {
        return endStationName;
    }

    public void setEndStationName(String endStationName) {
        this.endStationName = endStationName;
    }

    @Transient
    public String getDetailInfo() {
        return detailInfo;
    }

    public void setDetailInfo(String detailInfo) {
        this.detailInfo = detailInfo;
    }
}
