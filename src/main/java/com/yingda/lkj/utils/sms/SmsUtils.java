package com.yingda.lkj.utils.sms;

import com.yingda.lkj.beans.exception.CustomException;
import com.yingda.lkj.beans.system.JsonMessage;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class SmsUtils {

    private static final ResourceBundle bundle = ResourceBundle.getBundle("config");

    private final static String huaweiSmsUrl = bundle.getString("huaweiSmsUrl");
    private final static String huaweiSmsAppKey = bundle.getString("huaweiSmsAppKey");
    private final static String huaweiSmsAppSecret = bundle.getString("huaweiSmsAppSecret");
    private final static String huaweiSmsSender = bundle.getString("huaweiSmsSender");
    private final static String huaweiSmsTemplateId = bundle.getString("huaweiSmsTemplateId");

    //无需修改,用于格式化鉴权头域,给"X-WSSE"参数赋值
    private static final String WSSE_HEADER_FORMAT = "UsernameToken Username=\"%s\",PasswordDigest=\"%s\",Nonce=\"%s\",Created=\"%s\"";
    //无需修改,用于格式化鉴权头域,给"Authorization"参数赋值
    private static final String AUTH_HEADER_VALUE = "WSSE realm=\"SDP\",profile=\"UsernameToken\",type=\"Appkey\"";

    public static void main(String[] args) throws Exception {
        String s = sendSms("18246135672", "20");
        System.out.println(s);
    }

    public static String sendSms(String phoneNumber, String... messageParams) throws Exception {
        //条件必填,国内短信关注,当templateId指定的模板类型为通用模板时生效且必填,必须是已审核通过的,与模板类型一致的签名名称
        //国际/港澳台短信不用关注该参数
        String signature = "华为云短信测试"; //签名名称

        //必填,全局号码格式(包含国家码),示例:+8615123456789,多个号码之间用英文逗号分隔
        String receiver = "+86" + phoneNumber; //短信接收人号码

        //选填,短信状态报告接收地址,推荐使用域名,为空或者不填表示不接收状态报告
//        String statusCallBack = "http://akagi.natapp4.cc/test/tttt";
        String statusCallBack = "";

        /**
         * 选填,使用无变量模板时请赋空值 String templateParas = "";
         * 单变量模板示例:模板内容为"您的验证码是${1}"时,templateParas可填写为"[\"369751\"]"
         * 双变量模板示例:模板内容为"您有${1}件快递请到${2}领取"时,templateParas可填写为"[\"3\",\"人民公园正门\"]"
         * 模板中的每个变量都必须赋值，且取值不能为空
         * 查看更多模板和变量规范:产品介绍>模板和变量规范
         */
        // 结果："[\"红\", \"200\", \"2.3\"]"
        String templateParas = "[" + Arrays.stream(messageParams).map(x -> "\"" + x + "\"").collect(Collectors.joining(",")) + "]";
        //模板变量，此处以单变量验证码短信为例，请客户自行生成6位验证码，并定义为字符串类型，以杜绝首位0丢失的问题（例如：002569变成了2569）。

        //请求Body,不携带签名名称时,signature请填null
        String body = buildRequestBody(huaweiSmsSender, receiver, huaweiSmsTemplateId, templateParas, statusCallBack, signature);
        if (null == body || body.isEmpty())
            throw new CustomException(JsonMessage.SYS_ERROR, "body is null");

        //请求Headers中的X-WSSE参数值
        String wsseHeader = buildWsseHeader(huaweiSmsAppKey, huaweiSmsAppSecret);
        if (null == wsseHeader || wsseHeader.isEmpty())
            throw new CustomException(JsonMessage.SYS_ERROR, "wsse header is null.");

        //如果JDK版本低于1.8,可使用如下代码
        //CloseableHttpClient client = HttpClients.custom()
        //        .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
        //            @Override
        //            public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        //                return true;
        //            }
        //        }).build()).setSSLHostnameVerifier(new DefaultHostnameVerifier()).build();

        //如果JDK版本是1.8,可使用如下代码
        CloseableHttpClient client = HttpClients.custom()
                .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null,
                        (x509CertChain, authType) -> true).build())
                .setSSLHostnameVerifier(new DefaultHostnameVerifier())
                .build();

        HttpResponse response = client.execute(RequestBuilder.create("POST") // 请求方法POST
                .setUri(huaweiSmsUrl)
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .addHeader(HttpHeaders.AUTHORIZATION, AUTH_HEADER_VALUE)
                .addHeader("X-WSSE", wsseHeader)
                .setEntity(new StringEntity(body)).build());

//        System.out.println(response.toString()); //打印响应头域信息
        return EntityUtils.toString(response.getEntity());
    }

    /**
     * 构造请求Body体
     * @param sender
     * @param receiver
     * @param templateId
     * @param templateParas
     * @param statusCallbackUrl
     * @param signature | 签名名称,使用国内短信通用模板时填写
     * @return
     */
    static String buildRequestBody(String sender, String receiver, String templateId, String templateParas,
                                   String statusCallbackUrl, String signature) {
        if (null == sender || null == receiver || null == templateId || sender.isEmpty() || receiver.isEmpty()
                || templateId.isEmpty()) {
            System.out.println("buildRequestBody(): sender, receiver or templateId is null.");
            return null;
        }
        List<NameValuePair> keyValues = new ArrayList<NameValuePair>();

        keyValues.add(new BasicNameValuePair("from", sender));
        keyValues.add(new BasicNameValuePair("to", receiver));
        keyValues.add(new BasicNameValuePair("templateId", templateId));
        if (null != templateParas && !templateParas.isEmpty()) {
            keyValues.add(new BasicNameValuePair("templateParas", templateParas));
        }
        if (null != statusCallbackUrl && !statusCallbackUrl.isEmpty()) {
            keyValues.add(new BasicNameValuePair("statusCallback", statusCallbackUrl));
        }
        if (null != signature && !signature.isEmpty()) {
            keyValues.add(new BasicNameValuePair("signature", signature));
        }

        return URLEncodedUtils.format(keyValues, StandardCharsets.UTF_8);
    }

    /**
     * 构造X-WSSE参数值
     * @param appKey
     * @param appSecret
     * @return
     */
    static String buildWsseHeader(String appKey, String appSecret) {
        if (null == appKey || null == appSecret || appKey.isEmpty() || appSecret.isEmpty()) {
            System.out.println("buildWsseHeader(): appKey or appSecret is null.");
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String time = sdf.format(new Date()); //Created
        String nonce = UUID.randomUUID().toString().replace("-", ""); //Nonce

        byte[] passwordDigest = DigestUtils.sha256(nonce + time + appSecret);
        String hexDigest = Hex.encodeHexString(passwordDigest);

        //如果JDK版本是1.8,请加载原生Base64类,并使用如下代码
        String passwordDigestBase64Str = Base64.getEncoder().encodeToString(hexDigest.getBytes()); //PasswordDigest
        //如果JDK版本低于1.8,请加载三方库提供Base64类,并使用如下代码
        //String passwordDigestBase64Str = Base64.encodeBase64String(hexDigest.getBytes(Charset.forName("utf-8"))); //PasswordDigest
        //若passwordDigestBase64Str中包含换行符,请执行如下代码进行修正
        //passwordDigestBase64Str = passwordDigestBase64Str.replaceAll("[\\s*\t\n\r]", "");

        return String.format(WSSE_HEADER_FORMAT, appKey, passwordDigestBase64Str, nonce, time);
    }
}
