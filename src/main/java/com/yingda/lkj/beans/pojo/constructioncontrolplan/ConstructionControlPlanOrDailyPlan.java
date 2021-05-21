package com.yingda.lkj.beans.pojo.constructioncontrolplan;

import com.yingda.lkj.beans.entity.backstage.location.Location;
import com.yingda.lkj.beans.enums.constructioncontrolplan.WarnLevel;
import com.yingda.lkj.beans.pojo.location.ContainsLocation;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

public class ConstructionControlPlanOrDailyPlan implements ContainsLocation {
    // planStatus 方案状态
    public static final byte FIRST_DRAFT = 0; // 初稿
    public static final byte TECH_COUNTERSIGN = 1; // 技术会签
    public static final byte SAFE_COUNTERSIGN = 2; // 安全会签
    public static final byte COUNTERSIGNED = 3; // 已会签
    public static final byte PENDING_START = 4; // 未开始
    public static final byte FORMAL_START = 5; // 方案开始(系统自动判断方案开始日期，状态变为已开始)
    public static final byte CONSTRUCTING = 6; // 施工中(系统自动判断日计划开始时间，状态变为施工中)
    public static final byte SYSTEM_CLOSED = 7; // 系统关闭
    public static final byte MANUALLY_CLOSED = 8; // 人工关闭

    // constructionStep字段
    public static final BigInteger STEP1 = BigInteger.valueOf(0);
    public static final BigInteger STEP2 = BigInteger.valueOf(1);
    public static final BigInteger STEP3 = BigInteger.valueOf(2);

    // planType字段
    public static final BigInteger DAILY = BigInteger.valueOf(0);
    public static final BigInteger CONTROL = BigInteger.valueOf(1);

    // warnStatus字段
    public static final byte LEVEL1 = (byte) WarnLevel.LEVEL1.level;
    public static final byte LEVEL2 = (byte) WarnLevel.LEVEL2.level;
    public static final byte LEVEL3 = (byte) WarnLevel.LEVEL3.level;
    public static final byte LEVEL4 = (byte) WarnLevel.LEVEL4.level;
    public static final byte LEVEL5 = (byte) WarnLevel.LEVEL5.level;

    // railwayLineStatus字段 行别
    public static final byte UPRIVER = 0;
    public static final byte DOWNRIVER = 1;

    private String id;
    private String startStationId;
    private String railwayLineId;
    private byte planStatus; // 方案状态
    private BigInteger planType; // 类型
    private BigInteger constructionStep; // 施工阶段
    private byte warnStatus = LEVEL1; // 风险等级
    private byte railwayLineStatus; // 行别
    private String code; // 方案编码
    private String constructionProjectInfo; // 施工内容
    private String railwayLineName; // 线路名
    private double startKilometer; // 起始公里标
    private double endKilometer; // 结束公里标
    private String startStationName; // 起始站
    private String endStationName; // 结束站
    private Timestamp startTime; // 开始时间
    private Timestamp endTime; // 结束时间
    private Timestamp addTime; // 录入时间
    private byte processStatus; // 方案流程

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public List<Location> getLocations() {
        return null;
    }

    @Override
    public void setLocations(List<Location> locations) {

    }

    public BigInteger getPlanType() {
        return planType;
    }

    public void setPlanType(BigInteger planType) {
        this.planType = planType;
    }

    public byte getWarnStatus() {
        return warnStatus;
    }

    public void setWarnStatus(byte warnStatus) {
        this.warnStatus = warnStatus;
    }

    public String getRailwayLineName() {
        return railwayLineName;
    }

    public void setRailwayLineName(String railwayLineName) {
        this.railwayLineName = railwayLineName;
    }

    public double getStartKilometer() {
        return startKilometer;
    }

    public void setStartKilometer(double startKilometer) {
        this.startKilometer = startKilometer;
    }

    public double getEndKilometer() {
        return endKilometer;
    }

    public void setEndKilometer(double endKilometer) {
        this.endKilometer = endKilometer;
    }

    public String getStartStationName() {
        return startStationName;
    }

    public void setStartStationName(String startStationName) {
        this.startStationName = startStationName;
    }

    public String getEndStationName() {
        return endStationName;
    }

    public void setEndStationName(String endStationName) {
        this.endStationName = endStationName;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public byte getRailwayLineStatus() {
        return railwayLineStatus;
    }

    public void setRailwayLineStatus(byte railwayLineStatus) {
        this.railwayLineStatus = railwayLineStatus;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getConstructionProjectInfo() {
        return constructionProjectInfo;
    }

    public void setConstructionProjectInfo(String constructionProjectInfo) {
        this.constructionProjectInfo = constructionProjectInfo;
    }

    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
        this.addTime = addTime;
    }

    public String getStartStationId() {
        return startStationId;
    }

    public void setStartStationId(String startStationId) {
        this.startStationId = startStationId;
    }

    public String getRailwayLineId() {
        return railwayLineId;
    }

    public void setRailwayLineId(String railwayLineId) {
        this.railwayLineId = railwayLineId;
    }

    public BigInteger getConstructionStep() {
        return constructionStep;
    }

    public void setConstructionStep(BigInteger constructionStep) {
        this.constructionStep = constructionStep;
    }

    public byte getPlanStatus() {
        return planStatus;
    }

    public void setPlanStatus(byte planStatus) {
        this.planStatus = planStatus;
    }

    public byte getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(byte processStatus) {
        this.processStatus = processStatus;
    }
}
