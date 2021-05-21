package com.yingda.lkj.beans.entity.backstage.constructioncontrolplan;

import com.yingda.lkj.beans.enums.constructioncontrolplan.WarnLevel;

import javax.persistence.*;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "warning_info", schema = "opc_measurement", catalog = "")
public class WarningInfo {

    // warnLevel字段
    public static final byte LEVEL1 = 0; // 红
    public static final byte LEVEL2 = 1; // 橙
    public static final byte LEVEL3 = 2; // 黄
    public static final byte LEVEL4 = 3; // 蓝
    public static final byte LEVEL5 = 4; // 绿

    // processStatus
    public static final byte NOT_PROCESSED = 0; // 未处理
    public static final byte PROCESSING = 1; // 处理中
    public static final byte PROCESSED = 2; // 已处理
    public static final byte CLOSED = 3; // 已关闭

    private String id;
    private String constructionControlPlanId; // 方案
    private String equipmentId; // 设备
    private byte warnLevel; // 预警等级
    private byte processStatus; // 处理状态
    private double distanceFromNearestOpc; // 施工距离光缆最近距离
    private String processResult; // 处理结果
    private String warnInfo; // 预警信息
    private Timestamp addTime;
    private Timestamp updateTime;

    // page fields
    private String constructionControlPlanCode; // 方案编号
    private String constructionControlPlanProjectInfo; // 施工项目名称
    private String equipmentImei; // 设备编号

    // index page fields
    private BigInteger totals;

    public WarningInfo() {
    }

    public WarningInfo(String constructionControlPlanId, String equipmentId, double distanceFromNearestOpc) {
        this.id = UUID.randomUUID().toString();
        this.constructionControlPlanId = constructionControlPlanId;
        this.equipmentId = equipmentId;
        this.distanceFromNearestOpc = distanceFromNearestOpc;
        this.warnLevel = (byte) WarnLevel.LEVEL5.level;
        this.processStatus = NOT_PROCESSED;
//        this.processResult = processResult;
//        this.warnInfo = warnInfo;
        this.addTime = new Timestamp(System.currentTimeMillis());
//        this.updateTime = updateTime;
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
    @Column(name = "warn_info", nullable = false, length = 255)
    public String getWarnInfo() {
        return warnInfo;
    }

    public void setWarnInfo(String warnInfo) {
        this.warnInfo = warnInfo;
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
    @Column(name = "equipment_id", nullable = false, length = 36)
    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    @Basic
    @Column(name = "process_status", nullable = false)
    public byte getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(byte processStatus) {
        this.processStatus = processStatus;
    }

    @Basic
    @Column(name = "process_result", nullable = true, length = 255)
    public String getProcessResult() {
        return processResult;
    }

    public void setProcessResult(String processResult) {
        this.processResult = processResult;
    }

    @Basic
    @Column(name = "warn_level", nullable = false)
    public byte getWarnLevel() {
        return warnLevel;
    }

    public void setWarnLevel(byte warnLevel) {
        this.warnLevel = warnLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WarningInfo that = (WarningInfo) o;
        return processStatus == that.processStatus &&
                warnLevel == that.warnLevel &&
                Objects.equals(id, that.id) &&
                Objects.equals(constructionControlPlanId, that.constructionControlPlanId) &&
                Objects.equals(equipmentId, that.equipmentId) &&
                Objects.equals(processResult, that.processResult) &&
                Objects.equals(warnInfo, that.warnInfo) &&
                Objects.equals(addTime, that.addTime) &&
                Objects.equals(updateTime, that.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, constructionControlPlanId, equipmentId, warnLevel, warnInfo, processStatus, processResult, addTime, updateTime);
    }

    @Transient
    public String getConstructionControlPlanCode() {
        return constructionControlPlanCode;
    }

    public void setConstructionControlPlanCode(String constructionControlPlanCode) {
        this.constructionControlPlanCode = constructionControlPlanCode;
    }

    @Transient
    public String getConstructionControlPlanProjectInfo() {
        return constructionControlPlanProjectInfo;
    }

    public void setConstructionControlPlanProjectInfo(String constructionControlPlanProjectInfo) {
        this.constructionControlPlanProjectInfo = constructionControlPlanProjectInfo;
    }

    @Transient
    public String getEquipmentImei() {
        return equipmentImei;
    }

    public void setEquipmentImei(String equipmentImei) {
        this.equipmentImei = equipmentImei;
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
    public BigInteger getTotals() {
        return totals;
    }

    public void setTotals(BigInteger totals) {
        this.totals = totals;
    }

    @Basic
    @Column(name = "distance_from_nearest_opc", nullable = false, precision = 0)
    public double getDistanceFromNearestOpc() {
        return distanceFromNearestOpc;
    }

    public void setDistanceFromNearestOpc(double distanceFromNearestOpc) {
        this.distanceFromNearestOpc = distanceFromNearestOpc;
    }
}
