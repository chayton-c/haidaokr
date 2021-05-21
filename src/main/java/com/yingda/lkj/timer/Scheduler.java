package com.yingda.lkj.timer;

import com.sun.istack.Nullable;
import com.yingda.lkj.service.backstage.constructioncontrolplan.ConstructionControlPlanService;
import com.yingda.lkj.service.backstage.constructioncontrolplan.ConstructionDailyPlanService;
import com.yingda.lkj.socket.ConstructionDailyPlanWebSocket;
import com.yingda.lkj.utils.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

/**
 * @author hood  2020/4/7
 */
@Component
@EnableScheduling
public class Scheduler {

    @Autowired
    private ConstructionDailyPlanWebSocket constructionDailyPlanWebSocket;

    /**
     * Scheduling跟socket冲突了，这么写解决
     * https://blog.csdn.net/kzcming/article/details/102390593
     */
    @Bean
    @Nullable
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler threadPoolScheduler = new ThreadPoolTaskScheduler();
        threadPoolScheduler.setThreadNamePrefix("SockJS-");
        threadPoolScheduler.setPoolSize(Runtime.getRuntime().availableProcessors());
        threadPoolScheduler.setRemoveOnCancelPolicy(true);
        return threadPoolScheduler;
    }

    /**
     * <p>开始/结束日计划</p>
     * <p>每五分钟</p>
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void checkConstructionDailyPlan() {
        ConstructionDailyPlanService constructionDailyPlanService = (ConstructionDailyPlanService) SpringContextUtil.getBean("constructionDailyPlanService");
        constructionDailyPlanService.checkFinishedStatus();

        // 每五分钟通知前端刷新
        constructionDailyPlanWebSocket.sendMessage("refresh");
    }

    /**
     * <p>关闭方案</p>
     * <p>每小时执行</p>
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void closeConstructionControlPlan() {
        ConstructionControlPlanService constructionControlPlanService = (ConstructionControlPlanService) SpringContextUtil.getBean("constructionControlPlanService");
        constructionControlPlanService.checkClose();
    }

}
