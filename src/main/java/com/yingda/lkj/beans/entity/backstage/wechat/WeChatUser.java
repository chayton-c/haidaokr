package com.yingda.lkj.beans.entity.backstage.wechat;

import com.yingda.lkj.beans.pojo.enterprisewechat.user.WeChatUserResponse;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "we_chat_user", schema = "okr_haida")
public class WeChatUser {
    private String id;
    private String name;
    private String position;
    private String avatar;
    private String mobile;
    private String departmentId;
    private Timestamp addTime;
    private Timestamp updateTime;

    public static WeChatUser createByResponse(WeChatUserResponse weChatUserResponse) {
        WeChatUser weChatUser = new WeChatUser();
        String userid = weChatUserResponse.getUserid();
        String mobile = weChatUserResponse.getMobile();
        String position = weChatUserResponse.getPosition();
        String avatar = weChatUserResponse.getAvatar();
        String name = weChatUserResponse.getName();

        weChatUser.setId(userid);
        weChatUser.setName(name);
        weChatUser.setPosition(position);
        weChatUser.setAvatar(avatar);
        weChatUser.setMobile(mobile);
        weChatUser.setAddTime(new Timestamp(System.currentTimeMillis()));
        weChatUser.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        weChatUser.setDepartmentId(weChatUserResponse.getMain_department() + "");

        return weChatUser;
    }

    @Id
    @Column(name = "id", nullable = false, length = 64)
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
    @Column(name = "position", nullable = true, length = 255)
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Basic
    @Column(name = "avatar", nullable = true, length = 255)
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Basic
    @Column(name = "mobile", nullable = true, length = 255)
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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
        WeChatUser that = (WeChatUser) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(position, that.position) && Objects.equals(avatar, that.avatar) && Objects.equals(mobile, that.mobile) && Objects.equals(addTime, that.addTime) && Objects.equals(updateTime, that.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, position, avatar, mobile, addTime, updateTime);
    }

    @Basic
    @Column(name = "department_id", nullable = true, length = 255)
    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }
}
