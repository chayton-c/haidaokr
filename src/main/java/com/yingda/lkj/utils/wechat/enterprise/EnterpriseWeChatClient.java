package com.yingda.lkj.utils.wechat.enterprise;

import com.yingda.lkj.beans.exception.CustomException;
import com.yingda.lkj.beans.system.JsonMessage;
import com.yingda.lkj.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class EnterpriseWeChatClient {

    // 海达
    public static final String CORP_ID = "wwe394a1df0ae73e8f";
    public static final String CORP_SECRET = "pqhLAAphlbms0odc7JYJ3lKIYAexa6DMgEfK7h_lOtM";


    public static final String AGENT_ID = "1000056";
    public static final String URL = "http://hood.natapp1.cc/";

    public static String accessToken = "";
    public static long accessTokenUpdateTimestamp = 0L;
    public static String jsapiTicket = "";
    public static long jsapiTicketUpdateTimestamp = 0L;

    public static String getAccessToken() throws CustomException {
        if (System.currentTimeMillis() - accessTokenUpdateTimestamp < 3600000)
            return accessToken;

        try {
            String accessTokenUrl = String.format(
                    "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=%s&corpsecret=%s",
                    CORP_ID, CORP_SECRET
            );
            URL url = new URL(accessTokenUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            String input = new String(con.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            WeChatMessage parse = JsonUtils.parse(input, WeChatMessage.class);
            String access_token = parse.getAccess_token();
            accessToken = access_token;
            accessTokenUpdateTimestamp = System.currentTimeMillis();
            return access_token;
        } catch (Exception e) {
            log.error("EnterpriseWeChatClient.getAccessToken()", e);
            throw new CustomException(JsonMessage.WX_WDNMD);
        }
    }

    public static String getJsApiTicket() throws CustomException {
        if (System.currentTimeMillis() - jsapiTicketUpdateTimestamp < 3600000)
            return jsapiTicket;

        try {
            URL url = new URL("https://qyapi.weixin.qq.com/cgi-bin/ticket/get?type=agent_config&access_token=" + getAccessToken());

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("GET");

            String input = new String(con.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            WeChatMessage parse = JsonUtils.parse(input, WeChatMessage.class);
            String ticket = parse.getTicket();
            jsapiTicket = ticket;
            jsapiTicketUpdateTimestamp = System.currentTimeMillis();
            return ticket;
        } catch (Exception e) {
            log.error("EnterpriseWeChatClient.sendMessage()", e);
            throw new CustomException(JsonMessage.WX_WDNMD);
        }
    }

    public static String signature(String noncestr, long timestamp) throws CustomException {
        String pendingSign = String.format(
                "jsapi_ticket=%s&noncestr=%s&timestamp=%s&url=%s",
                getJsApiTicket(),
                noncestr,
                timestamp,
                URL
        );

        return DigestUtils.sha1Hex(pendingSign);
    }

    public static void main(String[] args) throws CustomException {
        getJsApiTicket();
    }


    static class WeChatMessage {
        private String access_token;
        private String media_id;
        private String ticket;

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public String getMedia_id() {
            return media_id;
        }

        public void setMedia_id(String media_id) {
            this.media_id = media_id;
        }

        public String getTicket() {
            return ticket;
        }

        public void setTicket(String ticket) {
            this.ticket = ticket;
        }
    }
}
