package com.yingda.lkj.beans.pojo.statisticanalysis;

public class StatisticsPlanOrDailyPlan {
    public static final byte CONSTRUCTION_CONTROL_TOTAL = 0;
    public static final byte CONSTRUCTION_CONTROL_FINISHED = 1;
    public static final byte BC_CONTROL_TOTAL = 2;
    public static final byte BC_CONTROL_FINISHED = 3;
    public static final byte TIAN_CHUANG_CONTROL_TOTAL = 4;
    public static final byte TIAN_CHUANG_CONTROL_FINISHED = 5;
    public static final byte DIAN_WAI_CONTROL_TOTAL = 6;
    public static final byte DIAN_WAI_CONTROL_FINISHED = 7;

    private String statisticsTime; // X轴统计时间
    private int constructionControlTotal; // 施工管控计划
    private int constructionControlFinished; // 施工管控完成
    private int bcControlTotal; // BC类管控计划
    private int bcControlFinished; // BC类管控完成
    private int tianChuangControlTotal; // 天窗管控计划
    private int tianChuangControlFinished; // 天窗管控完成
    private int dianWaiControlTotal; // 点外管控计划
    private int dianWaiControlFinished; // 点外管控完成

    private String name;
    private int value;
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getConstructionControlTotal() {
        return constructionControlTotal;
    }

    public void setConstructionControlTotal(int constructionControlTotal) {
        this.constructionControlTotal = constructionControlTotal;
    }

    public int getConstructionControlFinished() {
        return constructionControlFinished;
    }

    public void setConstructionControlFinished(int constructionControlFinished) {
        this.constructionControlFinished = constructionControlFinished;
    }

    public int getBcControlTotal() {
        return bcControlTotal;
    }

    public void setBcControlTotal(int bcControlTotal) {
        this.bcControlTotal = bcControlTotal;
    }

    public int getBcControlFinished() {
        return bcControlFinished;
    }

    public void setBcControlFinished(int bcControlFinished) {
        this.bcControlFinished = bcControlFinished;
    }

    public int getTianChuangControlTotal() {
        return tianChuangControlTotal;
    }

    public void setTianChuangControlTotal(int tianChuangControlTotal) {
        this.tianChuangControlTotal = tianChuangControlTotal;
    }

    public int getTianChuangControlFinished() {
        return tianChuangControlFinished;
    }

    public void setTianChuangControlFinished(int tianChuangControlFinished) {
        this.tianChuangControlFinished = tianChuangControlFinished;
    }

    public int getDianWaiControlTotal() {
        return dianWaiControlTotal;
    }

    public void setDianWaiControlTotal(int dianWaiControlTotal) {
        this.dianWaiControlTotal = dianWaiControlTotal;
    }

    public int getDianWaiControlFinished() {
        return dianWaiControlFinished;
    }

    public void setDianWaiControlFinished(int dianWaiControlFinished) {
        this.dianWaiControlFinished = dianWaiControlFinished;
    }

    public String getStatisticsTime() {
        return statisticsTime;
    }

    public void setStatisticsTime(String statisticsTime) {
        this.statisticsTime = statisticsTime;
    }
}
