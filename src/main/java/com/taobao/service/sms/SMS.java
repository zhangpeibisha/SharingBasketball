package com.taobao.service.sms;



import com.taobao.utils.properties.PropertiesUtil;

import java.util.ArrayList;

/**
 * Create by zhangpe0312@qq.com on 2018/2/24.
 */
public class SMS {
    private String url = "http://utf8.api.smschinese.cn/?Uid=本站用户名&Key=接口安全秘钥&smsMob=手机号码&smsText=验证码:8888";
    private String uid;
    private String key;
    private ArrayList<String> toClien = new ArrayList<>();
    private String smsText;



    class SMSBulider {
        SMS sms = new SMS();


        public SMSBulider setUid(String uid) {
            sms.setUid(uid);
            return this;
        }

        public SMSBulider setKey(String key) {
            sms.setKey(key);
            return this;
        }

        public SMSBulider setToClien(String to) {
            sms.setToClien(to);
            return this;
        }

        public SMSBulider setSmsText(String smsText) {
            sms.setSmsText(smsText);
            return this;
        }

        public String send() {
            String url = sms.getUrl();

            String Uid = PropertiesUtil.readValue("sms.properties","本站用户名");
            String Key = PropertiesUtil.readValue("sms.properties","接口安全秘钥");
            String company = PropertiesUtil.readValue("sms.properties","公司名称");

            //本站用户名
            url.replaceAll("本站用户名", Uid);
            //注册时填写的接口秘钥
            url.replaceAll("接口安全秘钥", Key);
            //发送的信息
            url.replaceAll("验证码:8888", "" + sms.getSmsText());

            //设置要发送的用户
            StringBuffer toClien = new StringBuffer();
            for (int i = 0; i <sms.getToClien().size() ; i++) {
                toClien.append(sms.getToClien().get(i));
                if (i != sms.getToClien().size()-1){
                    toClien.append(",");
                }
            }

            url.replaceAll("手机号码", toClien.toString());

            return url;
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ArrayList<String> getToClien() {
        return toClien;
    }

    public void setToClien(String toClien) {
        this.toClien.add(toClien);
    }

    public String getSmsText() {
        return smsText;
    }

    public void setSmsText(String smsText) {
        this.smsText = smsText;
    }
}
