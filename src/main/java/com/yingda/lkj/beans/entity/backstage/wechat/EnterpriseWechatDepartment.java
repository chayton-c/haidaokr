package com.yingda.lkj.beans.entity.backstage.wechat;

import com.yingda.lkj.beans.pojo.enterprisewechat.department.EnterpriseWechatDepartmentResponse;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "enterprise_wechat_department", schema = "okr_haida")
public class EnterpriseWechatDepartment {
    private String id;
    private String parentId;
    private String name;
    private Timestamp addTime;
    private Timestamp updateTime;
    private Integer seq;

    public static EnterpriseWechatDepartment createFromWechatResponse(EnterpriseWechatDepartmentResponse enterpriseWechatDepartmentResponse) {
        String id = enterpriseWechatDepartmentResponse.getId() + "";
        String parentid = enterpriseWechatDepartmentResponse.getParentid() + "";
        String name = enterpriseWechatDepartmentResponse.getName();
        Integer order = enterpriseWechatDepartmentResponse.getOrder();

        EnterpriseWechatDepartment enterpriseWechatDepartment = new EnterpriseWechatDepartment();
        enterpriseWechatDepartment.setId(id);
        enterpriseWechatDepartment.setName(name);
        enterpriseWechatDepartment.setParentId(parentid);
        enterpriseWechatDepartment.setSeq(order);

        enterpriseWechatDepartment.setAddTime(new Timestamp(System.currentTimeMillis()));
        enterpriseWechatDepartment.setUpdateTime(enterpriseWechatDepartment.getAddTime());

        return enterpriseWechatDepartment;
    }

    @Id
    @Column(name = "id", nullable = false, length = 255)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "parent_id", nullable = false, length = 255)
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Basic
    @Column(name = "name", nullable = true, length = 255)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
    @Column(name = "seq", nullable = true)
    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnterpriseWechatDepartment that = (EnterpriseWechatDepartment) o;
        return Objects.equals(id, that.id) && Objects.equals(parentId, that.parentId) && Objects.equals(name, that.name) && Objects.equals(addTime, that.addTime) && Objects.equals(updateTime, that.updateTime) && Objects.equals(seq, that.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, parentId, name, seq, addTime, updateTime);
    }
}
