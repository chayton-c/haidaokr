package com.yingda.lkj.beans.entity.backstage.approve;

import com.yingda.lkj.beans.pojo.enterprisewechat.approve.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "approve_detail", schema = "okr_haida", catalog = "")
public class ApproveDetail {
    private String id;
    private String approveId;
    private String code;
    private String name;
    private byte status;
    private String applyerId;
    private String applyerName;
    private double money;
    private Timestamp occurrenceTime;
    private String benifitierId;
    private String benifitierName;
    private Timestamp approveTime;
    private Timestamp addTime;
    private Timestamp updateTime;
    private String reason;
    private String remark;

    public static List<ApproveDetail> fillByResponse(ApproveDetailResponse approveDetailResponse) {
        List<ApproveDetail> approveDetails = new ArrayList<>();

        ApproveDetailInfo approveDetailResponseInfo = approveDetailResponse.getInfo();


        String code = approveDetailResponseInfo.getSp_no();
        String name = approveDetailResponseInfo.getSp_name();
        String applyer = approveDetailResponseInfo.getApplyer().getUserid();
        Timestamp current = new Timestamp(System.currentTimeMillis());
        Timestamp occurrenceTime = current;
        String reason = "";
        String remark = "";

        ApproveApplyData applyData = approveDetailResponseInfo.getApply_data();
        List<ApplyDataContent> contents = applyData.getContents();

        ApplyDataValue occurrenceTimeValue = new ApplyDataValue();
        ApplyDataValue reasonValue = new ApplyDataValue();
        ApplyDataValue remarkValue = new ApplyDataValue();
        ApplyDataValue tableValue = new ApplyDataValue();


        for (ApplyDataContent content : contents) {
            List<ApplyDataMap> title = content.getTitle();
            ApplyDataMap applyDataMap = title.stream().filter(x -> x.getLang().equals("zh_CN")).findFirst().orElse(null);
            assert applyDataMap != null;
            String titleStr = applyDataMap.getText();

            switch (titleStr) {
                case "发生日期" -> occurrenceTimeValue = content.getValue();
                case "申请事由", "奖罚所依据的制度规范" -> reasonValue = content.getValue();
                case "备注", "奖罚内容描述" -> remarkValue = content.getValue();
                case "奖罚明细" -> tableValue = content.getValue();
            }
        }

        occurrenceTime = new Timestamp(Long.parseLong(occurrenceTimeValue.getDate().getS_timestamp()) * 1000L);
        reason = reasonValue.getText();
        remark = remarkValue.getText();
        List<ApplyDataValueList> chilren = tableValue.getChildren();
        for (ApplyDataValueList child : chilren) {
            List<ApplyDataContent> subContents = child.getList();
            ApplyDataContent moneyContent =  // title为奖罚金额的content
                    subContents
                            .stream()
                            .filter(x -> "奖励金额|处罚金额".contains(Objects.requireNonNull(x
                                    .getTitle()
                                    .stream()
                                    .filter(y -> y.getLang().equals("zh_CN"))
                                    .findFirst().orElse(null)).getText())
                            ).findFirst().orElse(null);
            ApplyDataContent benifitierContent =  // title为被奖罚人的content
                    subContents
                            .stream()
                            .filter(x -> "被奖励人|被处罚人".contains(Objects.requireNonNull(x
                                    .getTitle()
                                    .stream()
                                    .filter(y -> y.getLang().equals("zh_CN"))
                                    .findFirst().orElse(null)).getText())
                            ).findFirst().orElse(null);

            assert moneyContent != null;
            double money = Double.parseDouble(moneyContent.getValue().getNew_money());
            if (name.equals("处罚审批"))
                money = 0 - money;

            assert benifitierContent != null;
            for (ApplayDataMember member : benifitierContent.getValue().getMembers()) {
                ApproveDetail approveDetail = new ApproveDetail();
                approveDetail.setId(UUID.randomUUID().toString());
                approveDetail.setCode(code);
                approveDetail.setApproveId(code);
                approveDetail.setName(name);
                // TODO
                approveDetail.setStatus((byte) 2);
                approveDetail.setApplyerId(applyer);
                approveDetail.setApproveTime(current);
                approveDetail.setAddTime(current);
                approveDetail.setUpdateTime(current);

                approveDetail.setOccurrenceTime(occurrenceTime);
                approveDetail.setReason(reason);
                approveDetail.setRemark(remark);
                approveDetail.setMoney(money);
                approveDetail.setBenifitierId(member.getUserid());
                approveDetail.setBenifitierName(member.getName());

                approveDetails.add(approveDetail);
            }
        }

        return approveDetails;
    }

    @Id
    @Column(name = "id", nullable = false, length = 36)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
    @Column(name = "code", nullable = false, length = 255)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
    @Column(name = "status", nullable = false)
    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    @Basic
    @Column(name = "applyer_id", nullable = false, length = 36)
    public String getApplyerId() {
        return applyerId;
    }

    public void setApplyerId(String applyerId) {
        this.applyerId = applyerId;
    }

    @Basic
    @Column(name = "money", nullable = false, precision = 0)
    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    @Basic
    @Column(name = "occurrence_time", nullable = true)
    public Timestamp getOccurrenceTime() {
        return occurrenceTime;
    }

    public void setOccurrenceTime(Timestamp occurrenceTime) {
        this.occurrenceTime = occurrenceTime;
    }

    @Basic
    @Column(name = "benifitier_id", nullable = false, length = 36)
    public String getBenifitierId() {
        return benifitierId;
    }

    public void setBenifitierId(String benifitierId) {
        this.benifitierId = benifitierId;
    }

    @Basic
    @Column(name = "benifitier_name", nullable = false, length = 36)
    public String getBenifitierName() {
        return benifitierName;
    }

    public void setBenifitierName(String benifitierName) {
        this.benifitierName = benifitierName;
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
    @Column(name = "approve_time", nullable = true)
    public Timestamp getApproveTime() {
        return approveTime;
    }

    public void setApproveTime(Timestamp approveTime) {
        this.approveTime = approveTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApproveDetail that = (ApproveDetail) o;
        return status == that.status && Double.compare(that.money, money) == 0 && Objects.equals(id, that.id) && Objects.equals(approveId
                , that.approveId) && Objects.equals(code, that.code) && Objects.equals(name, that.name) && Objects.equals(applyerId,
                that.applyerId) && Objects.equals(occurrenceTime, that.occurrenceTime) && Objects.equals(reason, that.reason) && Objects.equals(remark, that.remark) && Objects.equals(benifitierId, that.benifitierId) && Objects.equals(addTime, that.addTime) && Objects.equals(updateTime, that.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, approveId, code, name, status, applyerId, money, occurrenceTime, reason, remark, benifitierId, addTime, updateTime);
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
    @Column(name = "remark", nullable = true, length = -1)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Basic
    @Column(name = "applyer_name", nullable = true, length = 255)
    public String getApplyerName() {
        return applyerName;
    }

    public void setApplyerName(String applyerName) {
        this.applyerName = applyerName;
    }
}
