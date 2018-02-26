# 篮球借还系统



##Api接口

1. 用户登录
* 描述：控制用户登录的接口，使用md5加密方法
* URL：http://localhost:8080/login.do?user=用户名&password=用户密码&login=用户名 + "login"
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

*请求路径：http://localhost:8080/login.do?user=用户名&password=用户密码&login=用户名 + "login"
*login=用户名 + "login" 为了保证用户是在登陆页面进入的  这个字符串请用MD5加密
请用md5加密后传输过来

2. 用户注册
* 描述：用户注册接口，使用md5加密方法
* URL：
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
    
*问题：需要先验证校园卡账号密码。这里验证的方式我的建议就是先验证校园卡
的卡号和密码是否正确，如果正确再设定这个系统的登陆密码，并用校园卡号作为
用户的唯一凭证

这个你看下要求文档，明天看到了给我信息，商量下怎么做，我把注册接口留给你的，但是功能为实现，等你商量好了再说
//验证校园卡 校园卡为12位数固定的
*请求路径为：http://localhost:8080/schoolCard.do?card="校园卡号"&password="校园卡密码"
//设置登陆密码
*请求路径为：http://localhost:8080/register.do?


3. 找回密码
* 描述：用户找回密码接口
* URL：http://localhost:8080/sendSMS.do?user="用户名"&sendSMS=用户名+“send”
*sendSMS=用户名+“send” 请使用md5加密
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
    
    
    
4. 找回密码验证

//验证信息用session来实现
验证码存入session中，并用md5码加密
你获取道用户输入的验证码，并加密然后和我存入session中的验证码比较
（验证码为6位随机数字可以不）



* 描述：用户找回密码验证接口
* URL：
* HTTP请求方式： GET
* 请求参数：
    {
        user：xxx,
        message:xxx
    }
* 参数说明：
    user:用户id
    message:短信验证码
    
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
* URL：http://localhost:8080/updatePasswordByOldPassword.do?user="用户名"&password="新密码"&updateOldPassword=用户名+”updateOldPassword“
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
    
