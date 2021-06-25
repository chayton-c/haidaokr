package com.yingda.lkj.beans.entity.backstage.actionplan;

import com.yingda.lkj.beans.entity.backstage.organization.Organization;
import com.yingda.lkj.beans.entity.system.User;

import java.util.List;

public class ActionPlanUserStatistics {
    private String id;
    private String userDisplayName;
    private String userOrganizationName;
    private int planCount;
    private int finishedPlanCount;
    private float finishedRate;
    private int planChangeCount;
    private float planChangeRate;
    private int planDelayCount;
    private float planDelayRate;
    private int warnCount;

    public ActionPlanUserStatistics() {
    }

    public ActionPlanUserStatistics(User user, Organization organization, List<ActionPlan> actionPlans) {
        this.id = user.getId();
        this.userDisplayName = user.getDisplayName();
        this.userOrganizationName = organization.getName();
        this.planCount = 1;
        this.finishedPlanCount = 0;
        this.finishedRate = 1;
        this.planChangeCount = 0;
        this.planChangeRate = 1;
        this.planDelayCount = 0;
        this.planDelayRate = 1;
        this.warnCount = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }

    public String getUserOrganizationName() {
        return userOrganizationName;
    }

    public void setUserOrganizationName(String userOrganizationName) {
        this.userOrganizationName = userOrganizationName;
    }

    public int getPlanCount() {
        return planCount;
    }

    public void setPlanCount(int planCount) {
        this.planCount = planCount;
    }

    public int getFinishedPlanCount() {
        return finishedPlanCount;
    }

    public void setFinishedPlanCount(int finishedPlanCount) {
        this.finishedPlanCount = finishedPlanCount;
    }

    public float getFinishedRate() {
        return finishedRate;
    }

    public void setFinishedRate(float finishedRate) {
        this.finishedRate = finishedRate;
    }

    public int getPlanChangeCount() {
        return planChangeCount;
    }

    public void setPlanChangeCount(int planChangeCount) {
        this.planChangeCount = planChangeCount;
    }

    public float getPlanChangeRate() {
        return planChangeRate;
    }

    public void setPlanChangeRate(float planChangeRate) {
        this.planChangeRate = planChangeRate;
    }

    public int getPlanDelayCount() {
        return planDelayCount;
    }

    public void setPlanDelayCount(int planDelayCount) {
        this.planDelayCount = planDelayCount;
    }

    public float getPlanDelayRate() {
        return planDelayRate;
    }

    public void setPlanDelayRate(float planDelayRate) {
        this.planDelayRate = planDelayRate;
    }

    public int getWarnCount() {
        return warnCount;
    }

    public void setWarnCount(int warnCount) {
        this.warnCount = warnCount;
    }
}
