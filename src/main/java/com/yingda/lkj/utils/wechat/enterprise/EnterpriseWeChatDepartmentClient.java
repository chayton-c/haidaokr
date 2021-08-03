package com.yingda.lkj.utils.wechat.enterprise;

import com.yingda.lkj.beans.exception.CustomException;
import com.yingda.lkj.beans.pojo.enterprisewechat.EnterpriseWechatResponse;
import com.yingda.lkj.beans.pojo.enterprisewechat.department.EnterpriseWechatDepartmentResponse;
import com.yingda.lkj.beans.system.JsonMessage;
import com.yingda.lkj.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
public class EnterpriseWeChatDepartmentClient {

    public static List<EnterpriseWechatDepartmentResponse> loadDepartments() throws CustomException {
        try {
            URL url = new URL("https://qyapi.weixin.qq.com/cgi-bin/department/list?access_token=" + EnterpriseWeChatClient.getAccessToken());

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("GET");

            String input = new String(con.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            EnterpriseWechatResponse response = JsonUtils.parse(input, EnterpriseWechatResponse.class);
            return response.getDepartment();
        } catch (Exception e) {
            log.error("EnterpriseWeChatClient.sendMessage()", e);
            throw new CustomException(JsonMessage.WX_WDNMD);
        }
    }

    public static void main(String[] args) throws CustomException {
        List<EnterpriseWechatDepartmentResponse> enterpriseWechatDepartmentResponses = loadDepartments();
        System.out.println(enterpriseWechatDepartmentResponses);
    }
}
