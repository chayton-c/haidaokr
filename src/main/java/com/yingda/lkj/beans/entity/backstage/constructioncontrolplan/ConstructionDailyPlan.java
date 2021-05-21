package com.yingda.lkj.beans.entity.backstage.constructioncontrolplan;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * 方案日计划表
 */
@Entity
@Table(name = "construction_daily_plan", schema = "opc_measurement", catalog = "")
public class ConstructionDailyPlan {
    // downriver字段
    public static final byte UPRIVER = 0; // 上行
    public static final byte DOWNRIVER = 1; // 下行
    public static final byte SINGLE_LINE = 2; // 单线
    public static final byte UP_AND_DOWN = 3; // 上下行

    // 日计划状态-finishedStatus
    public static final byte PENDING_START = 0; // 未开始
    public static final byte PROCESSING = 1; // 进行中
    public static final byte CLOSED = 2; // 已结束

    private String id;
    private String constructionControlPlanId;
    private String railwayLineId; // 所在线路
    private String code;
    private String downriver;
    private Timestamp startTime;
    private Timestamp endTime;
    private double startKilometer;
    private double endKilometer;
    private Timestamp addTime;
    private Timestamp updateTime;
    private byte finishedStatus;
    private Timestamp actualStartTime; // 实际（手动）开始时间
    private Timestamp actualEndTime; // 实际（手动）结束时间

    // pageFields
    private String constructionControlPlanCode;
    private String railwayLineName;
    private String constructionProjectInfo;
    private String equipmentCodes;

    // indexPageFields
    private byte constructionLevel;
    private byte warnStatus;

    // 统计分析-SQL联查字段
    private byte constructionStatus; // 方案类型
    private String workshopId; // 车间ID-权限

    public ConstructionDailyPlan() {
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
    @Column(name = "construction_control_plan_id", nullable = false, length = 36)
    public String getConstructionControlPlanId() {
        return constructionControlPlanId;
    }

    public void setConstructionControlPlanId(String constructionControlPlanId) {
        this.constructionControlPlanId = constructionControlPlanId;
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
    @Column(name = "downriver", nullable = true, length = 255)
    public String getDownriver() {
        return downriver;
    }

    public void setDownriver(String downriver) {
        this.downriver = downriver;
    }

    @Basic
    @Column(name = "start_time", nullable = true)
    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    @Basic
    @Column(name = "end_time", nullable = true)
    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
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

    @Basic
    @Column(name = "finished_status", nullable = false)
    public byte getFinishedStatus() {
        return finishedStatus;
    }

    public void setFinishedStatus(byte finishedStatus) {
        this.finishedStatus = finishedStatus;
    }

    @Basic
    @Column(name = "actual_start_time", nullable = true)
    public Timestamp getActualStartTime() {
        return actualStartTime;
    }

    public void setActualStartTime(Timestamp actualStartTime) {
        this.actualStartTime = actualStartTime;
    }

    @Basic
    @Column(name = "actual_end_time", nullable = true)
    public Timestamp getActualEndTime() {
        return actualEndTime;
    }

    public void setActualEndTime(Timestamp actualEndTime) {
        this.actualEndTime = actualEndTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConstructionDailyPlan that = (ConstructionDailyPlan) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(constructionControlPlanId, that.constructionControlPlanId) &&
                Objects.equals(code, that.code) &&
                Objects.equals(downriver, that.downriver) &&
                Objects.equals(startTime, that.startTime) &&
                Objects.equals(endTime, that.endTime) &&
                Objects.equals(startKilometer, that.startKilometer) &&
                Objects.equals(endKilometer, that.endKilometer) &&
                Objects.equals(addTime, that.addTime) &&
                Objects.equals(updateTime, that.updateTime) &&
                Objects.equals(finishedStatus, that.finishedStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, constructionControlPlanId, code, downriver, startTime, endTime, startKilometer, endKilometer, addTime,
                updateTime, finishedStatus);
    }

    @Basic
    @Transient
    @Column(name = "constructionControlPlanCode", nullable = true, length = 255)
    public String getConstructionControlPlanCode() {
        return constructionControlPlanCode;
    }

    public void setConstructionControlPlanCode(String constructionControlPlanCode) {
        this.constructionControlPlanCode = constructionControlPlanCode;
    }

    @Basic
    @Transient
    @Column(name = "railwayLineName", nullable = true, length = 255)
    public String getRailwayLineName() {
        return railwayLineName;
    }

    public void setRailwayLineName(String railwayLineName) {
        this.railwayLineName = railwayLineName;
    }

    @Basic
    @Transient
    @Column(name = "constructionProjectInfo", nullable = true, length = 255)
    public String getConstructionProjectInfo() {
        return constructionProjectInfo;
    }

    public void setConstructionProjectInfo(String constructionProjectInfo) {
        this.constructionProjectInfo = constructionProjectInfo;
    }

    @Transient
    public String getEquipmentCodes() {
        return equipmentCodes;
    }

    public void setEquipmentCodes(String equipmentCodes) {
        this.equipmentCodes = equipmentCodes;
    }

    @Transient
    public byte getConstructionLevel() {
        return constructionLevel;
    }

    public void setConstructionLevel(byte constructionLevel) {
        this.constructionLevel = constructionLevel;
    }

    @Transient
    public byte getConstructionStatus() {
        return constructionStatus;
    }

    public void setConstructionStatus(Byte constructionStatus) {
        this.constructionStatus = constructionStatus;
    }

    @Transient
    public String getWorkshopId() {
        return workshopId;
    }

    public void setWorkshopId(String workshopId) {
        this.workshopId = workshopId;
    }

    @Transient
    public byte getWarnStatus() { return warnStatus;}

    public void setWarnStatus(byte warnStatus) {this.warnStatus = warnStatus; }

    @Basic
    @Column(name = "railway_line_id", nullable = false, length = 36)
    public String getRailwayLineId() {
        return railwayLineId;
    }

    public void setRailwayLineId(String railwayLineId) {
        this.railwayLineId = railwayLineId;
    }
}
