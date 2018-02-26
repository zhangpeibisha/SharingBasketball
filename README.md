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
* URL：http://localhost:8080/register.do?user=用户名字&password=用户加密后的密码&phone=手机号码&role=角色名字
* HTTP请求方式： POST
* 请求参数：
    {
        user:xxx,
        password:xxx,
        phone:用户手机号码，
        role:用户注册的角色名字
    }
* 参数说明：
    user:用户名
    password：密码
    phone:用户手机号码
    role:用户注册的角色名字
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
* 已经注册了的用户的 URL：http://localhost:8080/sendSMSCode.do?user=用户名
* 注册的时候通过手机号码申请的 URL： http://localhost:8080/sendSMSCode.do?phone=手机号码
* HTTP请求方式： GET
* 请求参数：
    {
        user：xxx,
    }
* 参数说明：
    user:用户id
    
* 返回参数：发送成功有的参数
{
    data:0
    code：加密后的验证码
    result：发送成功后的返回结果
}
*发送失败的返回参数：
{
   data:1
}
* 参数说明：
    data：0为发送找回成功，1为发送找回失败 , 2未找到这个用户
    
    
    
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
    
6.校园卡验证
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