package com.yingda.lkj.beans.entity.backstage.approve;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "approve_appeal", schema = "okr_haida", catalog = "")
public class ApproveAppeal {
    private String id;
    private String approveDetailId;
    private String reason;
    private String handlingOpinions;
    private Timestamp addTime;
    private Timestamp updateTime;

    // pageFields
    private ApproveDetail approveDetail;

    @Id
    @Column(name = "id", nullable = false, length = 36)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "approve_detail_id", nullable = false, length = 36)
    public String getApproveDetailId() {
        return approveDetailId;
    }

    public void setApproveDetailId(String approveDetailId) {
        this.approveDetailId = approveDetailId;
    }

    @Basic
    @Column(name = "reason", nullable = true, length = -1)
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Basic
    @Column(name = "handling_opinions", nullable = true, length = -1)
    public String getHandlingOpinions() {
        return handlingOpinions;
    }

    public void setHandlingOpinions(String handlingOpinions) {
        this.handlingOpinions = handlingOpinions;
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
        ApproveAppeal that = (ApproveAppeal) o;
        return Objects.equals(id, that.id) && Objects.equals(approveDetailId, that.approveDetailId) && Objects.equals(reason,
                that.reason) && Objects.equals(handlingOpinions, that.handlingOpinions) && Objects.equals(addTime, that.addTime) && Objects.equals(updateTime, that.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, approveDetailId, reason, handlingOpinions, addTime, updateTime);
    }

    @Transient
    public ApproveDetail getApproveDetail() {
        return approveDetail;
    }

    public void setApproveDetail(ApproveDetail approveDetail) {
        this.approveDetail = approveDetail;
    }
}
