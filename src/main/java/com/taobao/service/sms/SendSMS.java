package com.taobao.service.sms;


import com.taobao.exception.sms.SMSException;
import com.taobao.utils.properties.PropertiesUtil;
import com.taobao.utils.url.HttpReques;
import org.springframework.stereotype.Service;

/**
 * Create by zhangpe0312@qq.com on 2018/2/24.
 */
@Service
public class SendSMS {

    /**
     * 传入了一个被填充好了数据的SMS数据包
     *
     * @param url 请求路径
     * @param sms 请求数据
     * @return 返回发送结果
     * <p>
     * 返回参数意义
     * 短信发送后返回值	说　明
     * -1	没有该用户账户
     * -2	接口密钥不正确 [查看密钥]
     * 不是账户登陆密码
     * <p>
     * -21	MD5接口密钥加密不正确
     * -3	短信数量不足
     * -11	该用户被禁用
     * -14	短信内容出现非法字符
     * -4	手机号格式不正确
     * -41	手机号码为空
     * -42	短信内容为空
     * -51	短信签名格式不正确
     * 接口签名格式为：【签名内容】
     * -6	IP限制
     * 大于0	短信发送数量
     */
    private String send(String url, SMS sms) {

        String result = HttpReques.sendGet(url, sms.getUrl());

        switch (result) {
            case "-1":
                throw new SMSException("-1", "没有该用户账户");
            case "-2":
                throw new SMSException("-2", "接口密钥不正确 [查看密钥]");
            case "-21":
                throw new SMSException("-21", "MD5接口密钥加密不正确");
            case "-3":
                throw new SMSException("-3", "短信数量不足");
            case "-11":
                throw new SMSException("-11", "该用户被禁用");
            case "-14":
                throw new SMSException("-14", "短信内容出现非法字符");
            case "-4":
                throw new SMSException("-4", "手机号格式不正确");
            case "-41":
                throw new SMSException("-41", "手机号码为空");
            case "-42":
                throw new SMSException("-42", "短信内容为空");
            case "-51":
                throw new SMSException("-51", "短信签名格式不正确");
            case "-6":
                throw new SMSException("-6", "IP限制");
            default:
                return result;
        }
    }

    /**
     * 发送验证码信息
     *
     * @return
     */
    public String sendVerCode(SMS sms) {

        String sendUrl = PropertiesUtil.readValue("sms.properties", "发送验证码Url");

        return send(sendUrl, sms);
    }
}
