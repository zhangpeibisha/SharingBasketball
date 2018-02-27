# 篮球借还系统



##Api接口

1. 用户登录
* 描述：控制用户登录的接口，使用md5加密方法
* URL：http://localhost:8080/login.do?user=用户名&password=用户密码
* HTTP请求方式： POST(改为GET方法)
* 请求参数：
    {
        user:xxx,
        password:xxx
    }
* 参数说明：
    user:用户名
    password：密码
* 返回参数：
{
    data:0
}
* 参数说明：
    data：0为登录成功，1为不存在当前用户，2为用户密码不正确,3为不是登陆页的请求


2. 用户注册
* 描述：用户注册接口，使用md5加密方法
* URL：http://localhost:8080/register.do?
* HTTP请求方式： POST
* 请求参数：
    {
        user:xxx,
        password:xxx,
        phone:用户手机号码,
        code:验证码
    }
* 参数说明：
    user:用户名
    password：密码
    phone:用户手机号码
    code:验证码
* 返回参数：
{
    data:0
}
* 参数说明：
    data：0为注册成功，1为注册失败


//这个不需要了，如果你那边要实现，就按照
（先申请短信验证，验证通过过后直接跳转到修改页码页面然后请求修改密码
密码不能找回，只能重新设置）这个方法实现
3. 找回密码
* 描述：用户找回密码接口
* URL：

* HTTP请求方式： POST
* 请求参数：
    {
        user:xxx
    }
* 参数说明：
    user:用户名
* 返回参数：
{
    data:0
}
* 参数说明：
    data：0为发送找回短信成功，1为发送找回短信失败 ， 2请求异常
    
    
    
4. 验证码接口（这个就是申请验证码的接口）

* 描述：用户找回密码验证接口
* 找回密码 URL：http://localhost:8080/sendSMSCode.do?user=用户名
* 注册 URL： http://localhost:8080/sendSMSCode.do?phone=手机号码
* HTTP请求方式： GET
* 请求参数：
    {
        phone：xxx(user:xxx)
    }
* 参数说明：
    user:用户id，
    phone：手机号
    
* 返回参数：
{
    data:0
}
* 参数说明：
    data：0为发送验证码失败，1为发送成功
    
5. 修改密码
* 描述：用户找回密码验证接口
* URL：http://localhost:8080/updatePassword.do?user="用户名"&password="新密码"
*HTTP请求方式： POST
* 请求参数：
    {
        user:xxx,
        password:xxx
    }
* 参数说明：
    user:用户id
    password:新密码
* 返回参数：
{
    data:0
}
* 参数说明：
    data：0为修改成功，1为修改失败 , 2没有这个用户
    
    
*权限存入到了session中 通过  httpSessionBindingEvent.getSession().setAttribute("permissions",permissions); 设置的
通过 httpSession.getAtribute("permissions");获取
    
6. 校园卡验证
* 用户注册时需要验证校园卡的账号和密码
*URL：http://localhost:8080/isSchoolCard.do?user=校园卡卡号&password=校园卡密码
*请求数据：
{
   user：校园卡号
   password:校园卡加密码
}

*返回参数
{
   data:返回代码
}

代码说明： 0验证成功 1验证失败 2验证异常

*测试账号
*校园卡号：201410610113
*密码：123456

7. 缴费模块
* 用户租借完成后进行订单付费
*URL：http://localhost:8080/payment.do?user=校园卡卡号&orderNumber=订单号
* HTTP请求方式： POST
*请求数据：
{
   user：校园卡号
   orderNumber:订单号
}
*返回参数
{
   money:付款金额,
   deposit:存款
}

8. 租借模块
* 显示所有可以租借的篮球
*URL：http://localhost:8080/rentList.do?
* HTTP请求方式： POST（GET）
*请求数据：
{
    无
}
*返回参数
{
   data:返回结果参数
   basketballs：当查询成功、并且数量不为0的时候有这个参数，这个参数代表可用篮球的一个数组
}

*返回结果参数说明
0 查询成功，并且有可用篮球  1查询成功，但是没有可用篮球 2查询异常

9. 请求租借模块
* 显示所有可以租借的篮球
*URL：http://localhost:8080/rent.do
* HTTP请求方式： POST
*请求数据：
{
    user：校园卡号
    basketballId:篮球id
}
*返回参数
{
   data：0;
}
* 参数说明：
    data：0为租借成功，1为租借失败