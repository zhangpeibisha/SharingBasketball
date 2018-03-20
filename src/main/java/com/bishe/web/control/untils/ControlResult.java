package com.bishe.web.control.untils;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Create by zhangpe0312@qq.com on 2018/2/28.
 * <p>
 * 用来包装Control可能遇到的错误代码返回值
 */
@Service
public class ControlResult {

    /**
     * 查询传入参数是否有空值
     *
     * @param value
     * @return 如果为空返回ture
     */
    public boolean isNull(Object... value) {
        for (Object o : value) {
            if (o == null) {
                return true;
            }
        }
        return false;
    }

    /**
     * 空参数异常 异常代码-1
     *
     * @param map
     * @return
     */
    public Map<String, Object> nullParameter(Map<String, Object> map, Logger logger) {
        map.put("data", "-1");
        map.put("message", "请求异常，参数为空");
        logger.error("请求参数为空，请求失败 ");
        return map;
    }

    /**
     * 请求异常代码为 2
     *
     * @param map
     * @return
     */
    public Map<String, Object> requestError(Map<String, Object> map, Logger logger, Exception e) {
        map.put("data", "2");
        map.put("message", "服务器发生异常");
        logger.error(" 请求异常 " + e);
        return map;
    }

    /**
     * 身份过期返回值 -11
     *
     * @param map
     * @return
     */
    public Map<String, Object> identityOutTime(Map<String, Object> map, Logger logger, String schoolCardId) {
        map.put("data", "-11");
        map.put("message", "用户身份过期，请重新登陆");
        logger.error(schoolCardId + "用户身份证码过期，需要重启登陆");
        return map;
    }

    /**
     * 用户违规操作 -12
     *
     * @param map
     * @param logger
     * @return
     */
    public Map<String, Object> violationControl(Map<String, Object> map, String description, Logger logger) {
        map.put("data", "-12");
        map.put("message", "用户操作违规，请求拒绝 " + description);
        logger.error("用户操作违规，请求拒绝 " + description);
        return map;
    }

    /**
     * 参数格式错误代码 -3
     *
     * @param map
     * @param logger
     * @return
     */
    public Map<String, Object> parameterFormatError(Map<String, Object> map, String description, Logger logger) {
        map.put("data", "3");
        map.put("message", description);
        logger.error("传入参数格式错误 " + description);
        return map;
    }

    /**
     * 操作成功返回值 0
     * 其他附加属性请在外部添加好
     *
     * @param map
     * @param description 对操作描述
     * @param logger
     * @return
     */
    public Map<String, Object> successfulContrl(Map<String, Object> map, String description, Logger logger) {
        map.put("data", "0");
        map.put("message", "请求操作成功 " + description);
        logger.info("请求操作成功 " + description);
        return map;
    }

    /**
     * 数据库查询失败 -5
     *
     * @param map
     * @param description
     * @param logger
     * @return
     */
    public Map<String, Object> inquireFail(Map<String, Object> map, String description, Logger logger) {
        map.put("data", "-5");
        map.put("message", description);
        logger.error("查询失败 " + description);
        return map;
    }

    /**
     * 用户输入信息和后台信息对比失败 -9
     *
     * @param map
     * @param description
     * @param logger
     * @return
     */
    public Map<String, Object> verificationFail(Map<String, Object> map, String description, Logger logger) {
        map.put("data", "-9");
        map.put("message", "验证失败 " + description);
        logger.error("验证失败 " + description);
        return map;
    }

    /**
     * 数据不可以使用 -21
     * @param map
     * @param description
     * @param logger
     * @return
     */
    public Map<String, Object> dataIsNotAvailable(Map<String, Object> map, String description, Logger logger) {
        map.put("data", "-21");
        map.put("message",  description);
        logger.error("数据不可用 " + description);
        return map;
    }


}
