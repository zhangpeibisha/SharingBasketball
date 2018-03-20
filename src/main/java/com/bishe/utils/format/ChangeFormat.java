package com.bishe.utils.format;

import com.google.gson.Gson;
import net.sf.json.JSONObject;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Create by zhangpe0312@qq.com on 2018/1/31.
 */
public class ChangeFormat {

     private static Logger logger = Logger.getLogger(ChangeFormat.class+"");

    /**
     * 微信后台传过来的xml进行解析得到一个map
     *
     * @param rq 请求信息
     * @return 一个包含了xml所有节点的值的map
     * @throws IOException       获取二进制流失败
     * @throws DocumentException 读取二进制流失败
     */
    public static Map<String, String> messageToMap(HttpServletRequest rq) throws IOException, DocumentException {
        Map<String, String> map = new HashMap<>();

        InputStream in = rq.getInputStream();
        SAXReader reader = new SAXReader();
        Document document = reader.read(in);
        Element root = document.getRootElement();
        List<Element> list = root.elements();
        for (Element e : list) {
            map.put(e.getName(), e.getText());
        }

        return map;
    }

    public static String toJson(Object object){
        Gson gson = new Gson();
        String json = gson.toJson(object);
        return json;
    }

    public static JSONObject toJson(HttpServletRequest req) throws IOException {
        InputStream in = req.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String str = reader.readLine();
        return JSONObject.fromObject(str);
    }

}
