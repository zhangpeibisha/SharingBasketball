import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.junit.Test;

/**
 * Create by zhangpe0312@qq.com on 2018/2/26.
 */
public class SMS {
    public static void main(String[] args) throws Exception {
        HttpClient client = new HttpClient();
        PostMethod post = new PostMethod("http://sms.webchinese.cn/web_api/");
        post.addRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=gbk");// 在头文件中设置转码
        NameValuePair[] data = { new NameValuePair("Uid", "李尚84393031"), // 注册的用户名
                new NameValuePair("Key", "54523c8063c3a90b5a53"), // 注册成功后,登录网站使用的密钥
                new NameValuePair("smsMob", "18203085236"), // 手机号码
                new NameValuePair("smsText", "您好，感谢你注册成为Goll租房网的一员，我们因有你而精彩，此次的验证码为：123456,请您尽快到指定的网页进行激活，我们会在1个工作日内给你反馈，谢谢您的合作。") };//设置短信内容
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
    }

    @Test
    public void getReuslutInfo(){
        String str = "statusCode:200\n" +
                "Cache-Control: no-cache\n" +
                "\n" +
                "Content-Length: 1\n" +
                "\n" +
                "Content-Type: text/html\n" +
                "\n" +
                "Expires: Sun, 25 Feb 2018 08:41:18 GMT\n" +
                "\n" +
                "Server: Microsoft-IIS/7.5\n" +
                "\n" +
                "Set-Cookie: CHNET=Temp%5Fusername=201822616411950408; expires=Sun, 22-Nov-2020 08:41:18 GMT; path=/\n" +
                "\n" +
                "Set-Cookie: ASPSESSIONIDAQSSDACR=CDGGGNJBHLODKIEOEOMILFJD; path=/\n" +
                "\n" +
                "X-Powered-By: ASP.NET\n" +
                "\n" +
                "Date: Mon, 26 Feb 2018 08:41:18 GMT\n" +
                "\n" +
                "2";

        System.out.println(str.lastIndexOf("GMT"));
        System.out.println(str.substring(str.lastIndexOf("GMT")));
    }
}
