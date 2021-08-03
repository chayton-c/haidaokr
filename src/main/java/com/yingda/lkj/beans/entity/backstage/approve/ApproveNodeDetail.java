package com.yingda.lkj.beans.entity.backstage.approve;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "approve_node_detail", schema = "okr_haida", catalog = "")
public class ApproveNodeDetail {
    private String id;
    private String approveNodeId;
    private byte status;
    private String approveUserId;
    private Timestamp addTime;
    private Timestamp updateTime;

    @Id
    @Column(name = "id", nullable = false, length = 36)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "approve_node_id", nullable = false, length = 36)
    public String getApproveNodeId() {
        return approveNodeId;
    }

    public void setApproveNodeId(String approveNodeId) {
        this.approveNodeId = approveNodeId;
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
    @Column(name = "approve_user_id", nullable = false, length = 36)
    public String getApproveUserId() {
        return approveUserId;
    }

    public void setApproveUserId(String approveUserId) {
        this.approveUserId = approveUserId;
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
        ApproveNodeDetail that = (ApproveNodeDetail) o;
        return status == that.status && Objects.equals(id, that.id) && Objects.equals(approveNodeId, that.approveNodeId) && Objects.equals(approveUserId, that.approveUserId) && Objects.equals(addTime, that.addTime) && Objects.equals(updateTime, that.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, approveNodeId, status, approveUserId, addTime, updateTime);
    }
}
