package com.yingda.lkj.service.backstage.statisticanalysis;

import java.util.List;

public interface StatisticAnalysisService {

    /**
     * 根据开始/结束时间范围获取每一天
     */
    List<String> getDaysByStartTimeAndEndTime(String startTime, String endTime);

}
