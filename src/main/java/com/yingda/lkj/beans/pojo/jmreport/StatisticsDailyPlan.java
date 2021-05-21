package com.yingda.lkj.beans.pojo.jmreport;

import java.math.BigInteger;

public class StatisticsDailyPlan {

    private String name;
    private BigInteger value;
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigInteger getValue() {
        return value;
    }

    public void setValue(BigInteger value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
