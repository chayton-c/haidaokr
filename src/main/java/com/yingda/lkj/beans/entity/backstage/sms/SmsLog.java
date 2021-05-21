package com.yingda.lkj.beans.entity.backstage.sms;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "sms_log", schema = "opc_measurement", catalog = "")
public class SmsLog {
    private String id;
    private String originTo; // 华为响应字段（手机号）
    private String huaweiFrom; // 华为响应字段（使用的通道）
    private String message; // 完整消息
    private String smsMsgId; // 华为响应字段
    private String status; // 华为响应字段
    private String createTime; // 华为响应字段
    private Timestamp sendTime; // 发送时间

    @Id
    @Column(name = "id", nullable = false, length = 36)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "origin_to", nullable = false, length = 255)
    public String getOriginTo() {
        return originTo;
    }

    public void setOriginTo(String originTo) {
        this.originTo = originTo;
    }


    @Basic
    @Column(name = "huawei_from", nullable = false, length = 255)
    public String getHuaweiFrom() {
        return huaweiFrom;
    }

    public void setHuaweiFrom(String huaweiFrom) {
        this.huaweiFrom = huaweiFrom;
    }

    @Basic
    @Column(name = "message", nullable = false, length = 255)
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Basic
    @Column(name = "sms_msg_id", nullable = false, length = 255)
    public String getSmsMsgId() {
        return smsMsgId;
    }

    public void setSmsMsgId(String smsMsgId) {
        this.smsMsgId = smsMsgId;
    }

    @Basic
    @Column(name = "status", nullable = false, length = 255)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "create_time", nullable = false, length = 255)
    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "send_time", nullable = true)
    public Timestamp getSendTime() {
        return sendTime;
    }

    public void setSendTime(Timestamp sendTime) {
        this.sendTime = sendTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SmsLog smsLog = (SmsLog) o;
        return Objects.equals(id, smsLog.id) &&
                Objects.equals(originTo, smsLog.originTo) &&
                Objects.equals(huaweiFrom, smsLog.huaweiFrom) &&
                Objects.equals(message, smsLog.message) &&
                Objects.equals(smsMsgId, smsLog.smsMsgId) &&
                Objects.equals(status, smsLog.status) &&
                Objects.equals(createTime, smsLog.createTime) &&
                Objects.equals(sendTime, smsLog.sendTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, originTo, huaweiFrom, message, smsMsgId, status, createTime, sendTime);
    }
}
