package com.yingda.lkj.service.impl.backstage.sms;

import com.yingda.lkj.beans.entity.backstage.sms.SmsLog;
import com.yingda.lkj.dao.BaseDao;
import com.yingda.lkj.service.backstage.sms.SmsService;
import com.yingda.lkj.utils.sms.SmsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("smsService")
public class SmsServiceImpl implements SmsService {

    @Autowired
    private BaseDao<SmsLog> smsLogBaseDao;

    @Override
    public void send(String phoneNumber, String... messageParams) throws Exception {
        SmsUtils.sendSms(phoneNumber, messageParams);
    }
}
