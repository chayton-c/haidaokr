package com.yingda.lkj.beans.entity.backstage.actionplan;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "action_plan_primary_node", schema = "okr_haida", catalog = "")
public class ActionPlanPrimaryNode {
    private String id;
    private String name;
    private int seq;
    private Timestamp addTime;
    private Timestamp updateTime;
    private String actionPlanId;

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
    @Column(name = "seq", nullable = false)
    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
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
        ActionPlanPrimaryNode that = (ActionPlanPrimaryNode) o;
        return seq == that.seq && Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(addTime, that.addTime) && Objects.equals(updateTime, that.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, seq, addTime, updateTime);
    }

    @Basic
    @Column(name = "action_plan_id", nullable = false, length = 36)
    public String getActionPlanId() {
        return actionPlanId;
    }

    public void setActionPlanId(String actionPlanId) {
        this.actionPlanId = actionPlanId;
    }
}
