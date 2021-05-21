package com.yingda.lkj.beans.enums.constructioncontrolplan;

import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.WarningInfo;
import com.yingda.lkj.utils.StringUtils;
import com.yingda.lkj.utils.date.DateUtil;

import java.sql.Timestamp;
import java.util.Arrays;

public enum WarnLevel {
    LEVEL1(0, 0, 1.5, "红色预警", "1.5米线", "红"),
    LEVEL2(1, 1.5, 5, "橙色预警", "5米线", "橙"),
    LEVEL3(2, 5, 10, "黄色预警", "10米线", "黄"),
    LEVEL4(3, 10, 20, "蓝色预警", "20米线", "蓝"),
    LEVEL5(4, 20, Double.MAX_VALUE, "无预警", "-", "-"); // 绿色预警(不存在)
    public final int level;
    public final double minDistance;
    public final double maxDistance;
    public final String warnLevelName;
    public final String distanceStr;
    public final String color;

    WarnLevel(int level, double minDistance, double maxDistance, String warnLevelName, String distanceStr, String color) {
        this.level = level;
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.warnLevelName = warnLevelName;
        this.distanceStr = distanceStr;
        this.color = color;
    }

    public static WarnLevel getByLevel(byte level) {
        return Arrays.stream(values()).filter(x -> x.level == level).findFirst().orElse(null);
    }

    /**
     * 通过点到光缆标最近距离获取预警等级
     */
    public static WarnLevel getByDistance(double distance) {
       if (distance < LEVEL1.maxDistance && distance >= LEVEL1.minDistance)
           return LEVEL1;
        if (distance < LEVEL2.maxDistance && distance >= LEVEL2.minDistance)
            return LEVEL2;
        if (distance < LEVEL3.maxDistance && distance >= LEVEL3.minDistance)
            return LEVEL3;
        if (distance < LEVEL4.maxDistance && distance >= LEVEL4.minDistance)
            return LEVEL4;
        return LEVEL5;
    }

    public static void fillWarnInfoByDistance(WarningInfo warningInfo, double distance, String constructionProjectInfo) {
        Timestamp current = new Timestamp(System.currentTimeMillis());

        if (StringUtils.isEmpty(constructionProjectInfo)) constructionProjectInfo = "";

        WarnLevel warnLevel = getByDistance(distance);

        if (warningInfo.getWarnLevel() == WarnLevel.LEVEL5.level) {
            warningInfo.setWarnInfo("机具作业位置正常");
            warningInfo.setUpdateTime(current);
            return;
        };

        warningInfo.setWarnLevel((byte) warnLevel.level);
        String warnInfo = String.format("%s %s，%s 机具侵入，距离光缆%s%d米", DateUtil.format(current, "yy/MM/dd HH:mm:ss "), warnLevel.warnLevelName, constructionProjectInfo, warnLevel.distanceStr, distance);
        warningInfo.setWarnInfo(warnInfo);
        warningInfo.setUpdateTime(current);
    }
}
