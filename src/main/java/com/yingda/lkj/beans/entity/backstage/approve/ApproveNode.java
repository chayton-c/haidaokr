package com.yingda.lkj.beans.entity.backstage.approve;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "approve_node", schema = "okr_haida", catalog = "")
public class ApproveNode {
    private String id;
    private byte status;
    private String approveId;
    private Timestamp addTime;
    private Timestamp updateTime;

    // pageFields
    private List<ApproveNodeDetail> approveNodeDetails;

    @Id
    @Column(name = "id", nullable = false, length = 36)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "status", nullable = false)
    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    @Basic
    @Column(name = "approve_id", nullable = false, length = 36)
    public String getApproveId() {
        return approveId;
    }

    public void setApproveId(String approveId) {
        this.approveId = approveId;
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
        ApproveNode that = (ApproveNode) o;
        return status == that.status && Objects.equals(id, that.id) && Objects.equals(approveId, that.approveId) && Objects.equals(addTime, that.addTime) && Objects.equals(updateTime, that.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, approveId, addTime, updateTime);
    }

    @Transient
    public List<ApproveNodeDetail> getApproveNodeDetails() {
        return approveNodeDetails;
    }

    public void setApproveNodeDetails(List<ApproveNodeDetail> approveNodeDetails) {
        this.approveNodeDetails = approveNodeDetails;
    }
}
