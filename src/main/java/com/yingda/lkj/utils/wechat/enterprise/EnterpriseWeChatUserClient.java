package com.yingda.lkj.utils.wechat.enterprise;

import com.yingda.lkj.beans.exception.CustomException;
import com.yingda.lkj.beans.pojo.enterprisewechat.user.WeChatUserResponse;
import com.yingda.lkj.beans.system.JsonMessage;
import com.yingda.lkj.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Slf4j
public class EnterpriseWeChatUserClient {

    public static WeChatUserResponse getApproveDetail(String userId) throws CustomException {
        String raw = "";
        try {
            String accessToken = EnterpriseWeChatClient.getAccessToken();
            URL url = new URL(
                    String.format(
                            """
                                    https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token=%s&userid=%s
                                    """,
                            accessToken,
                            userId
                    )
            );

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("GET");

            String input = new String(con.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            raw = input;
            return JsonUtils.parse(input, WeChatUserResponse.class);
        } catch (Exception e) {
            System.out.println(userId);
            System.out.println(raw);
            log.error("EnterpriseWeChatClient.sendMessage()", e);
            throw new CustomException(JsonMessage.WX_WDNMD);
        }
    }

    public static void main(String[] args) throws CustomException {
        WeChatUserResponse approveDetail = getApproveDetail("0004");
        System.out.println(approveDetail);
    }
}
