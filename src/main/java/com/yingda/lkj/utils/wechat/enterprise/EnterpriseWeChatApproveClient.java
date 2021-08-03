package com.yingda.lkj.utils.wechat.enterprise;

import com.yingda.lkj.beans.exception.CustomException;
import com.yingda.lkj.beans.pojo.enterprisewechat.approve.ApproveDetailResponse;
import com.yingda.lkj.beans.system.JsonMessage;
import com.yingda.lkj.utils.JsonUtils;
import com.yingda.lkj.utils.date.CalendarUtil;
import com.yingda.lkj.utils.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
public class EnterpriseWeChatApproveClient {

    public static final String CALLBACK_TOKEN = "hood";
    public static final String ENCODING_AES_KEY = "WULICBtNw9WgfcWvh9S4kpchfoEb0Z7e6STfJl0gC8x";

    public static List<String> getApproveNumbers(String templateId) throws CustomException {
        try {
            long monthFirstDay = DateUtil.toTimestamp("2021-07-30 00:00:00").getTime();
            long monthLastDay = DateUtil.toTimestamp("2021-08-30 00:00:00").getTime();
            String accessToken = EnterpriseWeChatClient.getAccessToken();
            URL url = new URL("https://qyapi.weixin.qq.com/cgi-bin/oa/getapprovalinfo?access_token=" + accessToken);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            String pack = String.format(
                    """
                    {
                         "starttime" : %d,
                         "endtime" : %d,
                         "cursor" : 0 ,
                         "size" : 100 ,
                         "filters" : [
                             {
                                 "key": "template_id",
                                 "value": "%s"
                             }
                         ]
                     }
                    """, monthFirstDay/1000, monthLastDay/1000, templateId
            );
            con.getOutputStream().write(pack.getBytes());

            String input = new String(con.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            ApproveNoResponse parse = JsonUtils.parse(input, ApproveNoResponse.class);
            return parse.getSp_no_list();
        } catch (Exception e) {
            log.error("EnterpriseWeChatClient.sendMessage()", e);
            throw new CustomException(JsonMessage.WX_WDNMD);
        }
    }

    /**
     * 企业微信审批单号获取审批详情
     * @param approveNo
     * @return
     * @throws CustomException
     */
    public static ApproveDetailResponse getApproveDetail(String approveNo) throws CustomException {
        try {
            String accessToken = EnterpriseWeChatClient.getAccessToken();
            URL url = new URL("https://qyapi.weixin.qq.com/cgi-bin/oa/getapprovaldetail?access_token=" + accessToken);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            String pack = String.format(
                    """
                    {
                       "sp_no" : "%s"
                    }
                    """, approveNo
            );
            con.getOutputStream().write(pack.getBytes());

            String input = new String(con.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            return JsonUtils.parse(input, ApproveDetailResponse.class);
        } catch (Exception e) {
            log.error("EnterpriseWeChatClient.sendMessage()", e);
            throw new CustomException(JsonMessage.WX_WDNMD);
        }
    }

    public static void getAccessTemplateDetail(String templateId) {
        try {
            String accessToken = EnterpriseWeChatClient.getAccessToken();
            URL url = new URL("https://qyapi.weixin.qq.com/cgi-bin/oa/gettemplatedetail?access_token=" + accessToken);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            String pack = String.format(
                    """
                     {
                         "template_id" : "%s"
                     }
                    """, templateId
            );
            con.getOutputStream().write(pack.getBytes());

            String input = new String(con.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            ApproveDetailResponse parse = JsonUtils.parse(input, ApproveDetailResponse.class);
        } catch (Exception e) {
            log.error("EnterpriseWeChatClient.sendMessage()", e);
        }

    }

    public static void main(String[] args) throws CustomException {
    }

    private static class ApproveNoResponse {
        private String errcode;
        private String errmsg;
        private List<String> sp_no_list;
        private String next_cursor;

        public String getErrcode() {
            return errcode;
        }

        public void setErrcode(String errcode) {
            this.errcode = errcode;
        }

        public String getErrmsg() {
            return errmsg;
        }

        public void setErrmsg(String errmsg) {
            this.errmsg = errmsg;
        }

        public List<String> getSp_no_list() {
            return sp_no_list;
        }

        public void setSp_no_list(List<String> sp_no_list) {
            this.sp_no_list = sp_no_list;
        }

        public String getNext_cursor() {
            return next_cursor;
        }

        public void setNext_cursor(String next_cursor) {
            this.next_cursor = next_cursor;
        }
    }

}
