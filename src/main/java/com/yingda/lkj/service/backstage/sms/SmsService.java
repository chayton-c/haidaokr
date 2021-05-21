package com.yingda.lkj.service.backstage.sms;

public interface SmsService {
    void send(String phoneNumber, String... messageParams) throws Exception;
}
