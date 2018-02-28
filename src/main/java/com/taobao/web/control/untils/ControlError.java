package com.taobao.web.control.untils;

import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Create by zhangpe0312@qq.com on 2018/2/28.
 *
 * 用来包装Control可能遇到的错误代码返回值
 */
@Service
public class ControlError {

    public boolean isNull(Object... value){
        for (Object o : value) {
            if (o == null){
                return true;
            }
        }
        return false;
    }

    /**
     * 空参数异常 异常代码-1
     * @param map
     * @return
     */
    public Map<String,Object> nullParameter(Map<String,Object> map){
        map.put("data", "-1");
        map.put("message", "请求异常，参数为空");
        return map;
    }

    /**
     * 请求异常代码为 2
     * @param map
     * @return
     */
    public Map<String,Object> requestError(Map<String,Object> map){
        map.put("data", "2");
        map.put("message", "服务器发生异常");
        return map;
    }

}
