package com.yingda.lkj.beans.pojo.enterprisewechat.user;

import java.util.List;

public class WeChatUserResponse {
    private String userid;
    private String name;
    private String position;
    private String mobile;
    private String avatar;
    private Integer main_department;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getMain_department() {
        return main_department;
    }

    public void setMain_department(Integer main_department) {
        this.main_department = main_department;
    }
}
