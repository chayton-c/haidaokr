package com.yingda.lkj.beans.pojo.constructioncontrolplan;

public class AppConstructionControlPlanPoint {
    private String id;
    private String name;
    private double radius;
    private byte collectType;
    private String constructionControlPlanId;
    private String code;
    private Integer seq;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public byte getCollectType() {
        return collectType;
    }

    public void setCollectType(byte collectType) {
        this.collectType = collectType;
    }

    public String getConstructionControlPlanId() {
        return constructionControlPlanId;
    }

    public void setConstructionControlPlanId(String constructionControlPlanId) {
        this.constructionControlPlanId = constructionControlPlanId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }
}
