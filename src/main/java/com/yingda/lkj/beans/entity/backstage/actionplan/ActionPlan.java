package com.yingda.lkj.beans.entity.backstage.actionplan;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * 行动计划主表
 */
@Entity
@Table(name = "action_plan", schema = "okr_haida", catalog = "")
public class ActionPlan {
    // submitStatus
    public static final byte PENDING_SUBMIT = 0;
    public static final byte SUBMITTED = 1;

    // finishedStatus
    public static final byte PENDING_START = 0;
    public static final byte PROCESSING = 1;
    public static final byte FINISHED = 2;

    // abolishedStatus
    public static final byte ACTING = 0;
    public static final byte ABOLISHED = 1;

    // historicalStatus
    public static final byte NOT_HISTORICAL = 0;
    public static final byte HISTORICAL = 1;

    private String id;
    private String actionPlanGroupId; // 行动计划分组id：计划变更时，会新建一个行动计划，这两个行动计划的actionPlanGroupId一致，用于查询历史记录
    private String organizationId; // 部门id
    private String executeUserId; // 提交人
    private String name; // 行动计划名称
    private String origin; // 行动计划来源
    private String client; // 行动计划客户
    private String purpose; // 行动计划目标
    private String target; // 行动计划目标
    private Timestamp planStartTime; // 计划开始时间
    private Timestamp planEndTime;
    private Timestamp actualStartTime; // 实际开始时间
    private Timestamp actualEndTime; // 实际结束时间
    private byte submitStatus; // 提交状态（未提交的不统计）
    private byte finishedStatus; // 完成状态
    private byte abolishedStatus; // 废止状态
    private byte historicalStatus; // 历史状态，计划变更时候，会新建一个状态为NOT_HISTORICAL的计划，并把被变更的计划状态置为HISTORICAL
    private Timestamp addTime;
    private Timestamp updateTime;

    // pageFields
    private String executorName;
    private String executorRoleName;
    private String executorOrganizationName;

    @Id
    @Column(name = "id", nullable = false, length = 36)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "action_plan_group_id", nullable = false, length = 36)
    public String getActionPlanGroupId() {
        return actionPlanGroupId;
    }

    public void setActionPlanGroupId(String actionPlanGroupId) {
        this.actionPlanGroupId = actionPlanGroupId;
    }

    @Basic
    @Column(name = "organization_id", nullable = false, length = 36)
    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    @Basic
    @Column(name = "execute_user_id", nullable = false, length = 36)
    public String getExecuteUserId() {
        return executeUserId;
    }

    public void setExecuteUserId(String executeUserId) {
        this.executeUserId = executeUserId;
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
    @Column(name = "origin", nullable = false, length = 255)
    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    @Basic
    @Column(name = "client", nullable = false, length = 255)
    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    @Basic
    @Column(name = "purpose", nullable = false, length = 255)
    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    @Basic
    @Column(name = "target", nullable = false, length = 255)
    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    @Basic
    @Column(name = "plan_start_time", nullable = true)
    public Timestamp getPlanStartTime() {
        return planStartTime;
    }

    public void setPlanStartTime(Timestamp planStartTime) {
        this.planStartTime = planStartTime;
    }

    @Basic
    @Column(name = "plan_end_time", nullable = true)
    public Timestamp getPlanEndTime() {
        return planEndTime;
    }

    public void setPlanEndTime(Timestamp planEndTime) {
        this.planEndTime = planEndTime;
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

    @Basic
    @Column(name = "finished_status", nullable = false)
    public byte getFinishedStatus() {
        return finishedStatus;
    }

    public void setFinishedStatus(byte finishedStatus) {
        this.finishedStatus = finishedStatus;
    }

    @Basic
    @Column(name = "abolished_status", nullable = false)
    public byte getAbolishedStatus() {
        return abolishedStatus;
    }

    public void setAbolishedStatus(byte abolishedStatus) {
        this.abolishedStatus = abolishedStatus;
    }

    @Basic
    @Column(name = "historical_status", nullable = false)
    public byte getHistoricalStatus() {
        return historicalStatus;
    }

    public void setHistoricalStatus(byte historicalStatus) {
        this.historicalStatus = historicalStatus;
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
    public String getExecutorName() {
        return executorName;
    }

    public void setExecutorName(String executorName) {
        this.executorName = executorName;
    }

    @Transient
    public String getExecutorRoleName() {
        return executorRoleName;
    }

    public void setExecutorRoleName(String executorRoleName) {
        this.executorRoleName = executorRoleName;
    }

    @Transient
    public String getExecutorOrganizationName() {
        return executorOrganizationName;
    }

    public void setExecutorOrganizationName(String executorOrganizationName) {
        this.executorOrganizationName = executorOrganizationName;
    }

    @Basic
    @Column(name = "submit_status", nullable = false)
    public byte getSubmitStatus() {
        return submitStatus;
    }

    public void setSubmitStatus(byte submitStatus) {
        this.submitStatus = submitStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActionPlan that = (ActionPlan) o;
        return submitStatus == that.submitStatus && finishedStatus == that.finishedStatus && abolishedStatus == that.abolishedStatus && historicalStatus == that.historicalStatus && Objects.equals(id, that.id) && Objects.equals(actionPlanGroupId, that.actionPlanGroupId) && Objects.equals(organizationId, that.organizationId) && Objects.equals(executeUserId, that.executeUserId) && Objects.equals(name, that.name) && Objects.equals(origin, that.origin) && Objects.equals(client, that.client) && Objects.equals(purpose, that.purpose) && Objects.equals(target, that.target) && Objects.equals(planStartTime, that.planStartTime) && Objects.equals(planEndTime, that.planEndTime) && Objects.equals(actualStartTime, that.actualStartTime) && Objects.equals(actualEndTime, that.actualEndTime) && Objects.equals(addTime, that.addTime) && Objects.equals(updateTime, that.updateTime) && Objects.equals(executorName, that.executorName) && Objects.equals(executorRoleName, that.executorRoleName) && Objects.equals(executorOrganizationName, that.executorOrganizationName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, actionPlanGroupId, organizationId, executeUserId, name, origin, client, purpose, target, planStartTime,
                planEndTime, actualStartTime, actualEndTime, submitStatus, finishedStatus, abolishedStatus, historicalStatus, addTime,
                updateTime, executorName, executorRoleName, executorOrganizationName);
    }
}
