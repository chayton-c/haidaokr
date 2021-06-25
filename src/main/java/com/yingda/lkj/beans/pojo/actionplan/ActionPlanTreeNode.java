package com.yingda.lkj.beans.pojo.actionplan;

import com.yingda.lkj.beans.Constant;
import com.yingda.lkj.beans.entity.backstage.actionplan.ActionPlanPrimaryNode;
import com.yingda.lkj.beans.entity.backstage.actionplan.ActionPlanSecondaryNode;
import com.yingda.lkj.utils.StreamUtil;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

public class ActionPlanTreeNode {
    private String key; // 前端树级组件要去传key而不是id
    private String id;
    private String name;

    private Byte milestoneNode; // 是里程碑节点
    private String planDescription; // 活动描述
    private String actionInput; // 输入
    private String actionOutput; // 输出（完成标志）

    private Timestamp planStartTime;
    private Timestamp planEndTime;
    private int seq;

    private String principalName; // 责任人
    private String checkerName; // 检查人


    private Timestamp actualStartTime;
    private Timestamp actualEndTime;
    private String completionDescription; // 完成情况描述
    private Byte finishedStatus;

    private List<ActionPlanTreeNode> children;

    public ActionPlanTreeNode() {

    }
    public ActionPlanTreeNode(ActionPlanPrimaryNode actionPlanPrimaryNode, List<ActionPlanSecondaryNode> actionPlanSecondaryNodes) {
        this.id = actionPlanPrimaryNode.getId();
        this.key = actionPlanPrimaryNode.getId();
        this.name = actionPlanPrimaryNode.getName();
        this.milestoneNode = Constant.FALSE;
        this.seq = actionPlanPrimaryNode.getSeq();
        this.children = StreamUtil.getList(actionPlanSecondaryNodes, ActionPlanTreeNode::new);
    }
    public ActionPlanTreeNode(ActionPlanSecondaryNode actionPlanSecondaryNode) {
        this.id = actionPlanSecondaryNode.getId();
        this.key = actionPlanSecondaryNode.getId();
        this.name = actionPlanSecondaryNode.getName();

        this.milestoneNode = actionPlanSecondaryNode.getMilestoneNode();
        this.planDescription = actionPlanSecondaryNode.getPlanDescription();
        this.actionInput = actionPlanSecondaryNode.getActionInput();
        this.actionOutput = actionPlanSecondaryNode.getActionOutput();

        this.planStartTime = actionPlanSecondaryNode.getPlanStartTime();
        this.planEndTime = actionPlanSecondaryNode.getPlanEndTime();
        this.seq = actionPlanSecondaryNode.getSeq();

        this.principalName = actionPlanSecondaryNode.getPrincipalName();
        this.checkerName = actionPlanSecondaryNode.getCheckerName();

        this.actualStartTime = actionPlanSecondaryNode.getActualStartTime();
        this.actualEndTime = actionPlanSecondaryNode.getActualEndTime();
        this.completionDescription = actionPlanSecondaryNode.getCompletionDescription();
        this.finishedStatus = actionPlanSecondaryNode.getFinishedStatus();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlanDescription() {
        return planDescription;
    }

    public void setPlanDescription(String planDescription) {
        this.planDescription = planDescription;
    }

    public String getActionInput() {
        return actionInput;
    }

    public void setActionInput(String actionInput) {
        this.actionInput = actionInput;
    }

    public String getActionOutput() {
        return actionOutput;
    }

    public void setActionOutput(String actionOutput) {
        this.actionOutput = actionOutput;
    }

    public Timestamp getPlanStartTime() {
        return planStartTime;
    }

    public void setPlanStartTime(Timestamp planStartTime) {
        this.planStartTime = planStartTime;
    }

    public Timestamp getPlanEndTime() {
        return planEndTime;
    }

    public void setPlanEndTime(Timestamp planEndTime) {
        this.planEndTime = planEndTime;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    public String getCheckerName() {
        return checkerName;
    }

    public void setCheckerName(String checkerName) {
        this.checkerName = checkerName;
    }

    public Timestamp getActualStartTime() {
        return actualStartTime;
    }

    public void setActualStartTime(Timestamp actualStartTime) {
        this.actualStartTime = actualStartTime;
    }

    public Timestamp getActualEndTime() {
        return actualEndTime;
    }

    public void setActualEndTime(Timestamp actualEndTime) {
        this.actualEndTime = actualEndTime;
    }

    public String getCompletionDescription() {
        return completionDescription;
    }

    public void setCompletionDescription(String completionDescription) {
        this.completionDescription = completionDescription;
    }

    public Byte getFinishedStatus() {
        return finishedStatus;
    }

    public void setFinishedStatus(Byte finishedStatus) {
        this.finishedStatus = finishedStatus;
    }

    public List<ActionPlanTreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<ActionPlanTreeNode> children) {
        this.children = children;
    }

    public Byte getMilestoneNode() {
        return milestoneNode;
    }

    public void setMilestoneNode(Byte milestoneNode) {
        this.milestoneNode = milestoneNode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActionPlanTreeNode that = (ActionPlanTreeNode) o;
        return seq == that.seq && Objects.equals(key, that.key) && Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(milestoneNode, that.milestoneNode) && Objects.equals(planDescription, that.planDescription) && Objects.equals(actionInput, that.actionInput) && Objects.equals(actionOutput, that.actionOutput) && Objects.equals(planStartTime, that.planStartTime) && Objects.equals(planEndTime, that.planEndTime) && Objects.equals(principalName, that.principalName) && Objects.equals(checkerName, that.checkerName) && Objects.equals(actualStartTime, that.actualStartTime) && Objects.equals(actualEndTime, that.actualEndTime) && Objects.equals(completionDescription, that.completionDescription) && Objects.equals(finishedStatus, that.finishedStatus) && Objects.equals(children, that.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, id, name, milestoneNode, planDescription, actionInput, actionOutput, planStartTime, planEndTime, seq,
                principalName, checkerName, actualStartTime, actualEndTime, completionDescription, finishedStatus, children);
    }
}
