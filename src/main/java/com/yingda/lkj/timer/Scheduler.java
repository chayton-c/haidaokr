package com.yingda.lkj.timer;

import com.sun.istack.Nullable;
import com.yingda.lkj.beans.entity.backstage.wechat.EnterpriseWechatDepartment;
import com.yingda.lkj.beans.exception.CustomException;
import com.yingda.lkj.service.backstage.wechat.EnterpriseWechatDepartmentService;
import com.yingda.lkj.service.backstage.wechat.WeChatUserService;
import com.yingda.lkj.socket.ConstructionDailyPlanWebSocket;
import com.yingda.lkj.utils.SpringContextUtil;
import com.yingda.lkj.utils.wechat.enterprise.EnterpriseWeChatUserClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 定时器
 * 附：cron生成器
 * https://cron.qqe2.com/
 * @author hood  2020/4/7
 */
@Component
@EnableScheduling
@Slf4j
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
    @Scheduled(cron = "0 0 2 * * ? ")
    public void updateEnterpriseWechatDepartmentAndWechatUser() {
        try {
            EnterpriseWechatDepartmentService enterpriseWechatDepartmentService = (EnterpriseWechatDepartmentService) SpringContextUtil.getBean("enterpriseWechatDepartmentService");
            List<EnterpriseWechatDepartment> enterpriseWechatDepartments = enterpriseWechatDepartmentService.loadAndSaveFromWechat();
            WeChatUserService weChatUserService = (WeChatUserService) SpringContextUtil.getBean("weChatUserService");
            for (EnterpriseWechatDepartment enterpriseWechatDepartment : enterpriseWechatDepartments) {
                List<String> userIds = EnterpriseWeChatUserClient.getUserIdsByDepartmentId(enterpriseWechatDepartment.getId());
                weChatUserService.saveByUserIdsFromWechat(userIds);
            }
        } catch (CustomException customException) {
            log.error("updateEnterpriseWechatDepartmentAndWechatUser", customException);
        }
    }

}
