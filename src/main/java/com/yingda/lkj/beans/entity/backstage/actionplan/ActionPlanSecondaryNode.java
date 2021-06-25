package com.yingda.lkj.beans.entity.backstage.actionplan;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "action_plan_secondary_node", schema = "okr_haida", catalog = "")
public class ActionPlanSecondaryNode {
    private String id;
    private String actionPlanId;
    private String actionPlanPrimaryNodeId;
    private String name;
    private byte milestoneNode; // 是里程碑节点
    private String planDescription; // 活动描述
    private String actionInput; // 输入
    private String actionOutput; // 输出（完成标志）
    private Timestamp planStartTime;
    private Timestamp planEndTime;
    private int seq;
    private String principalId; // 责任人
    private String checkerId; // 检查人
    private Timestamp actualStartTime;
    private Timestamp actualEndTime;
    private String completionDescription; // 完成情况描述
    private byte finishedStatus;
    private Timestamp addTime;
    private Timestamp updateTime;

    // pageField
    private String principalName; // 责任人
    private String checkerName; // 检查人

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
    @Column(name = "plan_description", nullable = false, length = 255)
    public String getPlanDescription() {
        return planDescription;
    }

    public void setPlanDescription(String planDescription) {
        this.planDescription = planDescription;
    }

    @Basic
    @Column(name = "action_input", nullable = false, length = 255)
    public String getActionInput() {
        return actionInput;
    }

    public void setActionInput(String actionInput) {
        this.actionInput = actionInput;
    }

    @Basic
    @Column(name = "action_output", nullable = false, length = 255)
    public String getActionOutput() {
        return actionOutput;
    }

    public void setActionOutput(String actionOutput) {
        this.actionOutput = actionOutput;
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
    @Column(name = "seq", nullable = false)
    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    @Basic
    @Column(name = "principal_id", nullable = false, length = 36)
    public String getPrincipalId() {
        return principalId;
    }

    public void setPrincipalId(String principalId) {
        this.principalId = principalId;
    }

    @Basic
    @Column(name = "checker_id", nullable = false, length = 36)
    public String getCheckerId() {
        return checkerId;
    }

    public void setCheckerId(String checkerId) {
        this.checkerId = checkerId;
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
    @Column(name = "completion_description", nullable = true, length = 255)
    public String getCompletionDescription() {
        return completionDescription;
    }

    public void setCompletionDescription(String completionDescription) {
        this.completionDescription = completionDescription;
    }

    @Basic
    @Column(name = "finished_status", nullable = false)
    public byte getFinishedStatus() {
        return finishedStatus;
    }

    public void setFinishedStatus(byte finishedStatus) {
        this.finishedStatus = finishedStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActionPlanSecondaryNode that = (ActionPlanSecondaryNode) o;
        return seq == that.seq && finishedStatus == that.finishedStatus && Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(planDescription, that.planDescription) && Objects.equals(actionInput, that.actionInput) && Objects.equals(actionOutput, that.actionOutput) && Objects.equals(planStartTime, that.planStartTime) && Objects.equals(planEndTime, that.planEndTime) && Objects.equals(principalId, that.principalId) && Objects.equals(checkerId, that.checkerId) && Objects.equals(actualStartTime, that.actualStartTime) && Objects.equals(actualEndTime, that.actualEndTime) && Objects.equals(completionDescription, that.completionDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, actionInput, actionOutput, actualEndTime, actualStartTime, checkerId, completionDescription,
                finishedStatus, name, planDescription, planEndTime, planStartTime, principalId, seq);
    }

    @Basic
    @Column(name = "action_plan_id", nullable = false, length = 36)
    public String getActionPlanId() {
        return actionPlanId;
    }

    public void setActionPlanId(String actionPlanId) {
        this.actionPlanId = actionPlanId;
    }

    @Basic
    @Column(name = "action_plan_primary_node_id", nullable = false, length = 36)
    public String getActionPlanPrimaryNodeId() {
        return actionPlanPrimaryNodeId;
    }

    public void setActionPlanPrimaryNodeId(String actionPlanPrimaryNodeId) {
        this.actionPlanPrimaryNodeId = actionPlanPrimaryNodeId;
    }

    @Transient
    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    @Transient
    public String getCheckerName() {
        return checkerName;
    }

    public void setCheckerName(String checkerName) {
        this.checkerName = checkerName;
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
    @Column(name = "milestone_node", nullable = false)
    public byte getMilestoneNode() {
        return milestoneNode;
    }

    public void setMilestoneNode(byte milestoneNode) {
        this.milestoneNode = milestoneNode;
    }
}
