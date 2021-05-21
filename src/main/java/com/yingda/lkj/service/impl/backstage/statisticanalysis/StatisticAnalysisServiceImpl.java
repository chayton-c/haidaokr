package com.yingda.lkj.service.impl.backstage.statisticanalysis;

import com.yingda.lkj.service.backstage.statisticanalysis.StatisticAnalysisService;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service("statisticAnalysisService")
public class StatisticAnalysisServiceImpl implements StatisticAnalysisService {

    @Override
    public List<String> getDaysByStartTimeAndEndTime(String startTime, String endTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null;
        Date endDate = null;

        try {
            startDate = sdf.parse(startTime);
            endDate = sdf.parse(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<String> daysList = new ArrayList<>();

        daysList.add(sdf.format(startDate));

        Calendar calBegin = Calendar.getInstance();
        Calendar calEnd = Calendar.getInstance();

        assert startDate != null;
        calBegin.setTime(startDate);
        assert endDate != null;
        calEnd.setTime(endDate);

        while (endDate.after(calBegin.getTime())) {
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            String dayStr = sdf.format(calBegin.getTime());
            daysList.add(dayStr);
        }
        return daysList;
    }
}
