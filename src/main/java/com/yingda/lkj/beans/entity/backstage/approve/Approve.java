package com.yingda.lkj.beans.entity.backstage.approve;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Entity
public class Approve {
    private String id;
    private String executorId;
    private String weChatSpNo;
    private byte status;
    private Timestamp addTime;
    private Timestamp updateTime;

    // page fileds
    private List<ApproveDetail> approveDetails;
    private List<ApproveNode> approveNodes;

    @Id
    @Column(name = "id", nullable = false, length = 36)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "executor_id", nullable = false, length = 36)
    public String getExecutorId() {
        return executorId;
    }

    public void setExecutorId(String executorId) {
        this.executorId = executorId;
    }

    @Basic
    @Column(name = "we_chat_sp_no", nullable = true, length = 36)
    public String getWeChatSpNo() {
        return weChatSpNo;
    }

    public void setWeChatSpNo(String weChatSpNo) {
        this.weChatSpNo = weChatSpNo;
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
        Approve approve = (Approve) o;
        return status == approve.status && Objects.equals(id, approve.id) && Objects.equals(executorId, approve.executorId) && Objects.equals(weChatSpNo, approve.weChatSpNo) && Objects.equals(addTime, approve.addTime) && Objects.equals(updateTime, approve.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, executorId, weChatSpNo, status, addTime, updateTime);
    }

    @Transient
    public List<ApproveDetail> getApproveDetails() {
        return approveDetails;
    }

    public void setApproveDetails(List<ApproveDetail> approveDetails) {
        this.approveDetails = approveDetails;
    }

    @Transient
    public List<ApproveNode> getApproveNodes() {
        return approveNodes;
    }

    public void setApproveNodes(List<ApproveNode> approveNodes) {
        this.approveNodes = approveNodes;
    }
}
