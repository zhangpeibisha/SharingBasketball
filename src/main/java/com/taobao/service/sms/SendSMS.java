package com.taobao.service.sms;

import com.taobao.utils.data.ManagementData;
import com.taobao.utils.properties.PropertiesUtil;
import com.taobao.utils.sign.MD5;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Create by zhangpe0312@qq.com on 2018/2/26.
 */
@Service
public class SendSMS {

    private static Logger logger = Logger.getLogger(SendSMS.class);

    @Autowired
    MD5 md5;

    /**
     * 给指定人员发送信息
     *
     * @param smsMod  准备发送的人
     * @param smsText 准备发送的信息
     * @return 发送结果
     * @throws IOException IO错误
     */
    public String send(String smsMod, String smsText) throws IOException {

        String Uid = ManagementData.SMS_ACCOUNT_NUMBER;
        String Key = ManagementData.SMS_PASSWORD;

        HttpClient client = new HttpClient();
        PostMethod post = new PostMethod("http://sms.webchinese.cn/web_api/");
        post.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=gbk");// 在头文件中设置转码
        NameValuePair[] data = {new NameValuePair("Uid", Uid), // 注册的用户名
                new NameValuePair("Key", Key), // 注册成功后,登录网站使用的密钥
                new NameValuePair("smsMob", smsMod), // 手机号码
                new NameValuePair("smsText", smsText)};//设置短信内容
        post.setRequestBody(data);

        client.executeMethod(post);
        Header[] headers = post.getResponseHeaders();
        int statusCode = post.getStatusCode();
        System.out.println("statusCode:" + statusCode);
        for (Header h : headers) {
            System.out.println(h.toString());
        }
        String result = new String(post.getResponseBodyAsString().getBytes("gbk"));
        System.out.println(result);
        post.releaseConnection();
        return result;
    }

    /**
     * 给单个用户发送验证信息
     *
     * @param smsMod           准备发送的用户
     * @param VerificationCode 验证码
     * @return 返回发送结果
     * @throws IOException IO异常
     */
    private String sendVerificationCode(String smsMod, String VerificationCode) throws IOException {
        String Code = ManagementData.VerificationCodeText;
        Code = Code.replace("Code", VerificationCode);
        return send(smsMod, Code);
    }

    /**
     * 给指定用户发送短信
     *
     * @param smsMod 指定发送人的信息
     * @return 包含了 加密后的验证码 发送结果
     * @throws IOException 发生IO错误
     */
    public Map<String, String> sendVerificationCode(String smsMod) throws IOException {
        Map<String, String> map = new HashMap<>();
        String code = getSix();
        logger.info("测试使用，查看验证码 " + code);
        try {
            String result = sendVerificationCode(smsMod, code);
            map.put("code", md5.encryption(code));
            map.put("result", result);
        }catch (Exception e){
            logger.error("发送发生错误 " + e);
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 产生随机的六位数
     *
     * @return 返回一个随机刘伟数
     */
    private String getSix() {
        Random rad = new Random();

        String result = rad.nextInt(1000000) + "";

        if (result.length() != 6) {
            return getSix();
        }
        return result;
    }


}
