package com.yingda.lkj.utils.wechat.enterprise;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class EnterpriseWeChatGroupBotClient {

    public static void sendSalesTargerDetail() {
        String message = """
                {
                     "msgtype": "news",
                     "news": {
                        "articles" : [
                            {
                                "title" : "销售目标填写",
                                "description" : "销售目标填写",
                                "url" : "http://192.168.105.144:4200/#/sales-target/detail",
                                "picurl" : "https://img0.baidu.com/it/u=3331104553,1408483350&fm=11&fmt=auto&gp=0.jpg"
                            }
                         ]
                     }
                 }
                """;

        try {
            URL url = new URL("https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=2d7e1a72-a06a-426a-8229-33da78af4ba3");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.getOutputStream().write(message.getBytes());

            String input = new String(con.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            System.out.println(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送销售目标列表
     */
    public static void sendSalesTargetList() {
        String message = """
                {
                     "msgtype": "news",
                     "news": {
                        "articles" : [
                            {
                                "title" : "销售目标",
                                "description" : "销售目标列表",
                                "url" : "http://192.168.103.144:4200/#/sales-target/list",
                                "picurl" : "https://img0.baidu.com/it/u=3331104553,1408483350&fm=11&fmt=auto&gp=0.jpg"
                            }
                         ]
                     }
                 }
                """;

        try {
            URL url = new URL("https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=2d7e1a72-a06a-426a-8229-33da78af4ba3");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.getOutputStream().write(message.getBytes());

            String input = new String(con.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            System.out.println(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        sendSalesTargerDetail();
        sendSalesTargetList();
    }

}
