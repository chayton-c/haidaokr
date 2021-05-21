package com.yingda.lkj.beans.entity.backstage.constructioncontrolplan;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * 施工时间
 */
@Entity
@Table(name = "construction_control_plan_work_time", schema = "opc_measurement", catalog = "")
public class ConstructionControlPlanWorkTime {
    private String id;
    private String constructionControlPlanId;
    private Timestamp startDate; // 开始日期（如2021年5月6日）
    private Timestamp startTime; // 开始时间（如9:20）
    private Timestamp endDate; // 结束日期（如2021年6月6日）
    private Timestamp endTime; // 结束时间（如19:20），这四个字段连起来就是 2021年5月6日到2021年6月6日，每天的9:20-19:20
    private Integer seq;
    private Timestamp addTime;
    private Timestamp updateTime;

    // pageFields
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
    @Column(name = "start_date", nullable = true)
    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
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
    @Column(name = "end_date", nullable = true)
    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
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
    @Column(name = "seq", nullable = true)
    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
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
        ConstructionControlPlanWorkTime that = (ConstructionControlPlanWorkTime) o;
        return Objects.equals(id, that.id) && Objects.equals(constructionControlPlanId, that.constructionControlPlanId) && Objects.equals(startDate, that.startDate) && Objects.equals(startTime, that.startTime) && Objects.equals(endDate, that.endDate) && Objects.equals(endTime, that.endTime) && Objects.equals(seq, that.seq) && Objects.equals(addTime, that.addTime) && Objects.equals(updateTime, that.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, constructionControlPlanId, startDate, startTime, endDate, endTime, seq, addTime, updateTime);
    }

    @Transient
    public String getDetailInfo() {
        return detailInfo;
    }

    public void setDetailInfo(String detailInfo) {
        this.detailInfo = detailInfo;
    }
}
