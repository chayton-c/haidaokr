package com.yingda.lkj.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;

@Configuration
public class ShutDownConfig {

    @Bean
    public DestroyBean getTerminateBean() {
        return new DestroyBean();
    }

    static class DestroyBean {
        @PreDestroy
        public void preDestroy() {
        }
    }
}
