package com.yingda.lkj.config.websocketPage;

import javax.websocket.Session;

public class BaseSession {
    private Session session;
    private Integer sendType;//0注册
    private Integer userSendType; //1表示用户在大屏页面
    private String dianwuduanId; //用户登陆的电务段id
    private String workshopId; //用户登陆的车间id
    private String userId;//用户id
    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Integer getSendType() {
        return sendType;
    }

    public void setSendType(Integer sendType) {
        this.sendType = sendType;
    }

    public Integer getUserSendType() {
        return userSendType;
    }

    public void setUserSendType(Integer userSendType) {
        this.userSendType = userSendType;
    }

    public String getDianwuduanId() {
        return dianwuduanId;
    }

    public void setDianwuduanId(String dianwuduanId) {
        this.dianwuduanId = dianwuduanId;
    }

    public String getWorkshopId() {
        return workshopId;
    }

    public void setWorkshopId(String workshopId) {
        this.workshopId = workshopId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
