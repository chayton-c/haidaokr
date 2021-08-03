package com.yingda.lkj.controller.callback;

import com.yingda.lkj.beans.entity.backstage.approve.ApproveDetail;
import com.yingda.lkj.beans.exception.CustomException;
import com.yingda.lkj.beans.pojo.enterprisewechat.approve.ApproveDetailResponse;
import com.yingda.lkj.controller.BaseController;
import com.yingda.lkj.service.backstage.approve.ApproveDetailService;
import com.yingda.lkj.utils.JsonUtils;
import com.yingda.lkj.utils.StringUtils;
import com.yingda.lkj.utils.wechat.enterprise.EnterpriseWeChatApproveClient;
import com.yingda.lkj.utils.wechat.enterprise.EnterpriseWeChatClient;
import com.yingda.lkj.utils.wechat.enterprise.jsoncallback.AesException;
import com.yingda.lkj.utils.wechat.enterprise.jsoncallback.WXBizMsgCrypt;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.json.XML;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RequestMapping("/callback/enterpriseWeChat")
@RestController
public class EnterpriseWeCahtApproveCallbackController extends BaseController {

    @Autowired
    private ApproveDetailService approveDetailService;

    public static void main(String[] args) throws CustomException {
    }

    @RequestMapping("/approveCallback")
    public String approveCallback() throws AesException, IOException {
        String msg_signature = req.getParameter("msg_signature");
        String timestamp = req.getParameter("timestamp");
        String nonce = req.getParameter("nonce");

        WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(
                EnterpriseWeChatApproveClient.CALLBACK_TOKEN,
                EnterpriseWeChatApproveClient.ENCODING_AES_KEY,
                EnterpriseWeChatClient.CORP_ID
        );


        String echostr = req.getParameter("echostr");
        if (StringUtils.isNotEmpty(echostr))
            return wxcpt.VerifyURL(msg_signature, timestamp, nonce, echostr);

        String postData = new String(req.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

        try {
            String sMsg = wxcpt.DecryptMsg(msg_signature, timestamp, nonce, postData);

            JSONObject jsonObject = XML.toJSONObject(sMsg);
            CallbackResponse callbackResponse = JsonUtils.parse(jsonObject.toString(), CallbackResponse.class);

            Long spNo = callbackResponse.xml.ApprovalInfo.SpNo;
            Long spStatus = callbackResponse.xml.ApprovalInfo.SpStatus;

            if (spStatus == 2) {
                System.out.println("完成的：" + spNo);
                ApproveDetail approveDetail = approveDetailService.getByCode(spNo + "");
                if (approveDetail != null)
                    return postData;

                ApproveDetailResponse approveDetailResponse = EnterpriseWeChatApproveClient.getApproveDetail(spNo + "");
                List<ApproveDetail> approveDetails = ApproveDetail.fillByResponse(approveDetailResponse);
                approveDetailService.saveOrUpdate(approveDetails);
            }
        } catch (Exception e) {
            logger.error("", e);
        }

        return postData;
    }

    static class CallbackResponse {

        private CallbackXml xml;


        public CallbackXml getXml() {
            return xml;
        }

        public void setXml(CallbackXml xml) {
            this.xml = xml;
        }

        static class CallbackXml {

            private CallbackApprovalInfo ApprovalInfo;

            public CallbackApprovalInfo getApprovalInfo() {
                return ApprovalInfo;
            }

            public void setApprovalInfo(CallbackApprovalInfo approvalInfo) {
                this.ApprovalInfo = approvalInfo;
            }

            static class CallbackApprovalInfo {
                private Long SpStatus;
                private Long SpNo;

                public Long getSpStatus() {
                    return SpStatus;
                }

                public void setSpStatus(Long spStatus) {
                    SpStatus = spStatus;
                }

                public Long getSpNo() {
                    return SpNo;
                }

                public void setSpNo(Long spNo) {
                    SpNo = spNo;
                }
            }
        }
    }
}
