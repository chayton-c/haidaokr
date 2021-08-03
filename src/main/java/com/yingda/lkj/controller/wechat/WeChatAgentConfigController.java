package com.yingda.lkj.controller.wechat;

import com.yingda.lkj.beans.exception.CustomException;
import com.yingda.lkj.beans.system.Json;
import com.yingda.lkj.beans.system.JsonMessage;
import com.yingda.lkj.controller.BaseController;
import com.yingda.lkj.utils.wechat.enterprise.EnterpriseWeChatClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/wechat/weChatAgentConfig")
@RestController
public class WeChatAgentConfigController extends BaseController {

    @RequestMapping("/signature")
    public Json signature() throws CustomException {
        Map<String, Object> attributes = new HashMap<>();

        String noncestr = Math.random() + "";
        long timestamp = System.currentTimeMillis();
        String signature = EnterpriseWeChatClient.signature(noncestr, timestamp);

        attributes.put("corpid", EnterpriseWeChatClient.CORP_ID);
        attributes.put("angentid", EnterpriseWeChatClient.AGENT_ID);
        attributes.put("timestamp", timestamp);
        attributes.put("noncestr", noncestr);
        attributes.put("signature", signature);

        return new Json(JsonMessage.SUCCESS, attributes);
    }

}
