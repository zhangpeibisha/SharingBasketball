/**
 * Create by zhangpe0312@qq.com on 2018/2/27.
 */
public class MD5 {
    public static void main(String[] args) {
        com.taobao.utils.sign.MD5 md5 = new com.taobao.utils.sign.MD5();
        System.out.println(md5.encryption("123456"));
    }
}
