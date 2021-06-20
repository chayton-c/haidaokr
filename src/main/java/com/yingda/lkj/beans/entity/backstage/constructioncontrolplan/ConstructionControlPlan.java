package com.yingda.lkj.beans.entity.backstage.constructioncontrolplan;

import com.yingda.lkj.beans.entity.system.User;
import com.yingda.lkj.beans.enums.constructioncontrolplan.WarnLevel;

import javax.persistence.*;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "construction_control_plan")
public class ConstructionControlPlan {
    // processStatus 方案流程
    public static final byte PENDING_SUBMIT = 0; // 待提交
    public static final byte PENDING_COUNTERSIGN = 1; // 待会签
    public static final byte PENDING_RELEVANCE = 2; // 待关联
    public static final byte RELEVANCEED = 3; // 已关联
    public static final byte CLOSED = 4; // 已关闭

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

    // railwayLineStatus字段行别
    public static final byte UPRIVER = 0; // 上行
    public static final byte DOWNRIVER = 1; // 下行
    public static final byte SINGLE_LINE = 2; // 单线
    public static final byte UPRIVER_AND_DOWNRIVER = 3; // 上下行

    // warnStatus字段
    // approveStatus
    public static final byte HAS_NOT_APPROVED = 0;
    public static final byte APPROVED = 1;
    // signInStatus
    public static final byte HAS_NOT_SIGN_IN = 0;
    public static final byte SIGN_IN = 1;

    // constructionStatus 施工类型relevance-plan/relevance-list
    public static final byte BC_CONSTRUCTION = 0;
    public static final byte NORMAL_CONSTRUCTION = 1;

    // constructionLevel 施工等级或类别
    public static final byte LEVEL1 = 0;
    public static final byte LEVEL2 = 1;
    public static final byte LEVEL3 = 2;
    public static final byte TYPEB = 3;
    public static final byte TYPEC = 4;

    // investigationProgressStatus 调查进度
    public static final byte INVESTIGATION_NOT_OPENED = 0;
    public static final byte PENDING_INVESTIGATE = 1;
    public static final byte INVESTIGATING = 2;
    public static final byte INVESTIGATED = 3;

    // constructionPeriod 施工周期
    public static final byte EVERYDAY = 0;
    public static final byte EVERY_OTHER_DAY = 1;
    public static final byte DIEBUS_TERTIUS= 2;

    private String id;
    private byte investigationProgressStatus; // 调查进度
    private String constructionType; // 施工类别
    private String signInStationId; // 签入站
    private String code; // 方案编号
    private byte constructionStatus; // 施工类型
    private byte processStatus; // 方案流程状态
    private byte planStatus; // 方案状态
    private String constructionProjectInfo; // 施工项目
    private String constructionContentAndInfluenceArea; // 施工内容及影响范围
    private String constructDepartment; // 建设单位
    private String constructionDepartmentAndPrincipalName; // 施工单位及负责人
    private String executeOrganizationId; // 提交部门
    private String supervisionDepartmentAndPrincipalName; // 监理单位及负责人
    private String equipmentMonitoringDepartmentAndPrincipalName; // 设备监护单位及负责人
    private String remarks; // 备注
    private String starRating; // 星级
    private String auditDepartment;
    private int equipmentBindCount; // 绑定设备数量
    private int equipmentReleaseCount; // 解绑设备数量
    /**
     * warnStatus
     * @see WarnLevel
     */
    private byte warnStatus; // 报警状态
    private byte hasUploadedCooperativeScheme; // 已上传配合方案
    private byte hasUploadedSafetyProtocol; // 已上传安全协议
    private String workshopId; // 所属部门
    private Timestamp addTime;
    private Timestamp updateTime;

    // pageFields
    private String executorDisplayName;
    private String signInUserDisplayName;
    private String executeOrganizationName;
    private String railwayLineName; // 线路名
    private String startStationName; // 开始地点
    private String endStationName; // 结束地点
    private String constructionFormalPlanCodes;
    private WarnLevel warnLevel;
    private List<User> executors;
    private ConstructionFormalPlan constructionFormalPlan;
    private Timestamp startDate;
    private Timestamp endDate;
    private List<ConstructionControlPlanKilometerMark> constructionControlPlanKilometerMarks;
    private List<ConstructionControlPlanWorkTime> constructionControlPlanWorkTimes;


    // indexPageFileds
    private Timestamp startTime;
    private String workshopName; // 所属部门
    private BigInteger totals; // 方案数量（用于首页统计）
    private String constructionControlPlanKilometerMarkInfos;

    public ConstructionControlPlan() {
    }

    public ConstructionControlPlan(String id) {
        this.id = id;
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
    @Column(name = "code", nullable = false, length = 255)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Basic
    @Column(name = "construction_status", nullable = false)
    public Byte getConstructionStatus() {
        return constructionStatus;
    }

    public void setConstructionStatus(byte constructionStatus) {
        this.constructionStatus = constructionStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConstructionControlPlan that = (ConstructionControlPlan) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Basic
    @Column(name = "construct_department", nullable = true, length = 255)
    public String getConstructDepartment() {
        return constructDepartment;
    }

    public void setConstructDepartment(String constructDepartment) {
        this.constructDepartment = constructDepartment;
    }

    @Basic
    @Column(name = "construction_department_and_principal_name", nullable = true, length = 255)
    public String getConstructionDepartmentAndPrincipalName() {
        return constructionDepartmentAndPrincipalName;
    }

    public void setConstructionDepartmentAndPrincipalName(String constructionDepartmentAndPrincipalName) {
        this.constructionDepartmentAndPrincipalName = constructionDepartmentAndPrincipalName;
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
    @Column(name = "warn_status", nullable = false)
    public byte getWarnStatus() {
        return warnStatus;
    }

    public void setWarnStatus(byte warnStatus) {
        this.warnStatus = warnStatus;
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
    @Column(name = "construction_project_info", nullable = true, length = 255)
    public String getConstructionProjectInfo() {
        return constructionProjectInfo;
    }

    public void setConstructionProjectInfo(String constructionProjectInfo) {
        this.constructionProjectInfo = constructionProjectInfo;
    }

    @Basic
    @Column(name = "execute_organization_id", nullable = false, length = 36)
    public String getExecuteOrganizationId() {
        return executeOrganizationId;
    }

    public void setExecuteOrganizationId(String executeOrganizationId) {
        this.executeOrganizationId = executeOrganizationId;
    }

    @Transient
    public String getExecutorDisplayName() {
        return executorDisplayName;
    }

    public void setExecutorDisplayName(String executorDisplayName) {
        this.executorDisplayName = executorDisplayName;
    }

    @Transient
    public String getSignInUserDisplayName() {
        return signInUserDisplayName;
    }

    public void setSignInUserDisplayName(String signInUserDisplayName) {
        this.signInUserDisplayName = signInUserDisplayName;
    }

    @Transient
    public ConstructionFormalPlan getConstructionFormalPlan() {
        return constructionFormalPlan;
    }

    public void setConstructionFormalPlan(ConstructionFormalPlan constructionFormalPlan) {
        this.constructionFormalPlan = constructionFormalPlan;
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

    @Basic
    @Column(name = "construction_content_and_influence_area", nullable = true, length = 1000)
    public String getConstructionContentAndInfluenceArea() {
        return constructionContentAndInfluenceArea;
    }

    public void setConstructionContentAndInfluenceArea(String constructionContentAndInfluenceArea) {
        this.constructionContentAndInfluenceArea = constructionContentAndInfluenceArea;
    }

    @Basic
    @Column(name = "process_status", nullable = false)
    public byte getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(byte processStatus) {
        this.processStatus = processStatus;
    }

    @Transient
    public String getExecuteOrganizationName() {
        return executeOrganizationName;
    }

    public void setExecuteOrganizationName(String executeOrganizationName) {
        this.executeOrganizationName = executeOrganizationName;
    }

    @Basic
    @Column(name = "investigation_progress_status", nullable = false)
    public byte getInvestigationProgressStatus() {
        return investigationProgressStatus;
    }

    public void setInvestigationProgressStatus(byte investigationProgressStatus) {
        this.investigationProgressStatus = investigationProgressStatus;
    }

    @Transient
    public List<User> getExecutors() {
        return executors;
    }

    public void setExecutors(List<User> executors) {
        this.executors = executors;
    }

    @Basic
    @Column(name = "has_uploaded_cooperative_scheme", nullable = false)
    public byte getHasUploadedCooperativeScheme() {
        return hasUploadedCooperativeScheme;
    }

    public void setHasUploadedCooperativeScheme(byte hasUploadedCooperativeScheme) {
        this.hasUploadedCooperativeScheme = hasUploadedCooperativeScheme;
    }

    @Basic
    @Column(name = "has_uploaded_safety_protocol", nullable = false)
    public byte getHasUploadedSafetyProtocol() {
        return hasUploadedSafetyProtocol;
    }

    public void setHasUploadedSafetyProtocol(byte hasUploadedSafetyProtocol) {
        this.hasUploadedSafetyProtocol = hasUploadedSafetyProtocol;
    }

    @Transient
    public String getConstructionFormalPlanCodes() {
        return constructionFormalPlanCodes;
    }

    public void setConstructionFormalPlanCodes(String constructionFormalPlanCodes) {
        this.constructionFormalPlanCodes = constructionFormalPlanCodes;
    }

    @Basic
    @Column(name = "workshop_id", nullable = false, length = 36)
    public String getWorkshopId() {
        return workshopId;
    }

    public void setWorkshopId(String workshopId) {
        this.workshopId = workshopId;
    }

    @Transient
    public BigInteger getTotals() {
        return totals;
    }

    public void setTotals(BigInteger totals) {
        this.totals = totals;
    }

    @Transient
    public String getWorkshopName() {
        return workshopName;
    }

    public void setWorkshopName(String workshopName) {
        this.workshopName = workshopName;
    }

    @Basic
    @Column(name = "construction_type", nullable = false, length = 255)
    public String getConstructionType() {
        return constructionType;
    }

    public void setConstructionType(String constructionType) {
        this.constructionType = constructionType;
    }

    @Basic
    @Column(name = "plan_status", nullable = false)
    public byte getPlanStatus() {
        return planStatus;
    }

    public void setPlanStatus(byte planStatus) {
        this.planStatus = planStatus;
    }

    @Basic
    @Column(name = "equipment_bind_count", nullable = false)
    public int getEquipmentBindCount() {
        return equipmentBindCount;
    }

    public void setEquipmentBindCount(int equipmentBindCount) {
        this.equipmentBindCount = equipmentBindCount;
    }

    @Basic
    @Column(name = "equipment_release_count", nullable = false)
    public int getEquipmentReleaseCount() {
        return equipmentReleaseCount;
    }

    public void setEquipmentReleaseCount(int equipmentReleaseCount) {
        this.equipmentReleaseCount = equipmentReleaseCount;
    }

    @Basic
    @Column(name = "supervision_department_and_principal_name", nullable = true, length = 255)
    public String getSupervisionDepartmentAndPrincipalName() {
        return supervisionDepartmentAndPrincipalName;
    }

    public void setSupervisionDepartmentAndPrincipalName(String supervisionDepartmentAndPrincipalName) {
        this.supervisionDepartmentAndPrincipalName = supervisionDepartmentAndPrincipalName;
    }

    @Basic
    @Column(name = "equipment_monitoring_department_and_principal_name", nullable = true, length = 255)
    public String getEquipmentMonitoringDepartmentAndPrincipalName() {
        return equipmentMonitoringDepartmentAndPrincipalName;
    }

    public void setEquipmentMonitoringDepartmentAndPrincipalName(String equipmentMonitoringDepartmentAndPrincipalName) {
        this.equipmentMonitoringDepartmentAndPrincipalName = equipmentMonitoringDepartmentAndPrincipalName;
    }

    @Basic
    @Column(name = "remarks", nullable = true, length = 500)
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Basic
    @Column(name = "star_rating", nullable = true, length = 255)
    public String getStarRating() {
        return starRating;
    }

    public void setStarRating(String starRating) {
        this.starRating = starRating;
    }

    @Basic
    @Column(name = "sign_in_station_id", nullable = false, length = 36)
    public String getSignInStationId() {
        return signInStationId;
    }

    public void setSignInStationId(String signInStationId) {
        this.signInStationId = signInStationId;
    }

    @Transient
    public WarnLevel getWarnLevel() {
        return warnLevel;
    }

    public void setWarnLevel(WarnLevel warnLevel) {
        this.warnLevel = warnLevel;
    }

    @Transient
    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    @Basic
    @Column(name = "audit_department", nullable = true, length = 255)
    public String getAuditDepartment() {
        return auditDepartment;
    }

    public void setAuditDepartment(String auditDepartment) {
        this.auditDepartment = auditDepartment;
    }

    @Transient
    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    @Transient
    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    @Transient
    public List<ConstructionControlPlanKilometerMark> getConstructionControlPlanKilometerMarks() {
        return constructionControlPlanKilometerMarks;
    }

    public void setConstructionControlPlanKilometerMarks(List<ConstructionControlPlanKilometerMark> constructionControlPlanKilometerMarks) {
        this.constructionControlPlanKilometerMarks = constructionControlPlanKilometerMarks;
    }

    @Transient
    public List<ConstructionControlPlanWorkTime> getConstructionControlPlanWorkTimes() {
        return constructionControlPlanWorkTimes;
    }

    public void setConstructionControlPlanWorkTimes(List<ConstructionControlPlanWorkTime> constructionControlPlanWorkTimes) {
        this.constructionControlPlanWorkTimes = constructionControlPlanWorkTimes;
    }

    @Transient
    public String getConstructionControlPlanKilometerMarkInfos() {
        return constructionControlPlanKilometerMarkInfos;
    }

    public void setConstructionControlPlanKilometerMarkInfos(String constructionControlPlanKilometerMarkInfos) {
        this.constructionControlPlanKilometerMarkInfos = constructionControlPlanKilometerMarkInfos;
    }
}
