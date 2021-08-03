package com.yingda.lkj.utils.wechat.enterprise;

import com.yingda.lkj.beans.exception.CustomException;
import com.yingda.lkj.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class EnterpriseWeChatMessageClient {

    /**
     * 文本消息
     * @param message
     */
    public static void sendMessage(List<String> touser, String message) {
        try {
            URL url = new URL("https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + EnterpriseWeChatClient.getAccessToken());

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.getOutputStream().write(String.format(
                    """
                    {
                       "touser" : "%s",
                       "msgtype" : "text",
                       "agentid" : 1000056,
                       "text" : {
                           "content" : "%s"
                       },
                       "safe":0,
                    }
                    """, String.join("|", touser), message).getBytes());

            String input = new String(con.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            System.out.println(input);
        } catch (Exception e) {
            log.error("EnterpriseWeChatClient.sendMessage()", e);
        }
    }

    public static void main(String[] args) {




        String description = """
                编号：202107270160
                奖罚金额：-30.000000
                提交人：孙赞 (工程师)
                """;
        sendLink(List.of("00003922"), "奖罚明细确认", description, "http://123.56.165.86:4200/#/passport/login?mobileRedirectUrl=/approve-mobile/user-detail&id=fc5918c4-88cf-4c46-a7b7-5773a02f3351");
    }

    /**
     * 图文消息
     */
    public static void sendLink(List<String> touser, String title, String descrption, String redirectUrl) {
        try {
            URL url = new URL("https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + EnterpriseWeChatClient.getAccessToken());

            String message = String.format("""
                {
                   "touser" : "%s",
                   "msgtype" : "news",
                   "agentid" : 1000056,
                   "news" : {
                       "articles" : [
                           {
                               "title" : "%s",
                               "description" : "%s",
                               "url" : "%s",
                               "picurl" : "https://img1.baidu.com/it/u=1423580379,3195300716&fm=26&fmt=auto&gp=0.jpg"
                           }
                       ]
                   },
                   "enable_id_trans": 0,
                   "enable_duplicate_check": 0,
                   "duplicate_check_interval": 1800
                }
                """, String.join("|", touser), title, descrption, redirectUrl);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.getOutputStream().write(message.getBytes());

            String input = new String(con.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            System.out.println(input);
        } catch (Exception e) {
            log.error("EnterpriseWeChatClient.sendLink()", e);
        }
    }


    public static String uploadFile() throws CustomException {
        String filePath = "C://Users/goubi/Desktop/bd61fb2c0f03973c046a109db103877.png";
        String fileName = Arrays.stream(filePath.split("/")).reduce("", (x, y) -> y);
        System.out.println(fileName);
        File file = new File(filePath);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setContentLength(file.length());
        headers.setContentDispositionFormData("media", file.getName());

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("content-type", "application/octet-stream");
        body.add("filename", fileName);
        body.add("name", "media");
        body.add("filelength", file.length());
        body.add("file", new FileSystemResource(file.getPath()));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        String serverUrl = "https://qyapi.weixin.qq.com/cgi-bin/media/upload?access_token=" + EnterpriseWeChatClient.getAccessToken() + "&type=file";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(serverUrl, requestEntity, String.class);
        System.out.println(response);
        EnterpriseWeChatClient.WeChatMessage weChatMessage = JsonUtils.parse(response.getBody(), EnterpriseWeChatClient.WeChatMessage.class);
        return weChatMessage.getMedia_id();
    }

    public static void sendFile(String mediaId) {
        try {
            URL url = new URL("https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + EnterpriseWeChatClient.getAccessToken());

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.getOutputStream().write(String.format("""
                    {
                       "touser" : "@all",
                       "msgtype" : "file",
                       "agentid" : 1000056,
                       "file" : {
                        "media_id" : "%s"
                       },
                       "safe":0,
                    }
                    """, mediaId).getBytes());

            String input = new String(con.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("EnterpriseWeChatClient.sendMessage()", e);
        }
    }
}
