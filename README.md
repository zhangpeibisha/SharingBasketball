# 校园篮球自助租赁系统

##Api接口

1. 用户登录
* 描述：控制用户登录的接口，使用md5加密方法
* URL：http://localhost:8080/login.do?user=用户名&password=用户密码
* HTTP请求方式： GET
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


2. 校园卡验证
* 用户注册时需要验证校园卡的账号和密码
* URL：http://localhost:8080/isSchoolCard.do?user=校园卡卡号&password=校园卡密码
* 请求数据：
{
   user：校园卡号
   password:校园卡加密码
}
* 返回参数
{
   data:0验证成功 1验证失败 2验证异常
}

3. 注册请求验证码
* URL： http://localhost:8080/sendSMSCode.do
* HTTP请求方式： GET
* 请求参数：
    {
        phone：手机号,
        user:用户id
    }
* 返回参数：
{
    hasRegister:0为已经注册，1为未注册,
    result:0为发送验证码失败，1为发送成功
}

    
4. 用户注册
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
    data:0为注册成功，1为注册异常 2验证码错误
}
    
5. 忘记密码请求验证码
* 描述：忘记密码时请求验证码
* URL:http://localhost:8080/phoneVerification.do
*参数
{
   phone：用户手机号码
}
*返回参数
{
   data:-1 参数为空 -2输入不是手机号码 1手机号未注册 0发送成功 2服务器异常
   message:执行结果
   smsResult：{
      code：验证码信息
      result：发送信息返回结果（sms）
   }（发送成功后sms的返回结果）
}

6. 忘记密码验证
* 描述：验证手机号以及验证码
* URL：http://localhost:8080/submitVerification.do
* 参数
{
   phone:用户手机号码
   code:用户输入的验证码
}
*返回参数
{
   data: -1 参数为空 0 验证成功 1验证码不匹配  2服务器异常
   message:执行结果
} 

7. 修改密码
* 描述:修改密码
* URL:http://localhost:8080/updatePasswordRun.do
* 请求参数
{
   phone:手机号
   password:新密码
}
* 返回参数
{
  data:-1 参数为空 0更新成功 1用户的操作异常 2服务器异常
  message:执行结果
}
    
8. 缴费模块
* 用户租借完成后进行订单付费
* URL：http://localhost:8080/payment.do
* HTTP请求方式： POST
*请求数据：
{
   user：校园卡号
   orderNumber:订单号
   nowperssure：现在压力
}
*返回参数
{
   money:付款金额,
   deposit:存款
   order:订单信息
}

9. 租借列表
* 显示所有可以租借的篮球
* URL：http://localhost:8080/rentList.do
* HTTP请求方式： GET
* 请求数据：
{
   limit：每页最大显示行数,
   currentPage：当前页数
}
* 返回参数
{
   data:0 查询成功，并且有可用篮球  1查询成功，但是没有可用篮球 2查询异常
   basketballs：篮球列表
} 

10. 请求租借篮球
* 对于一个篮球进行请求租借
* URL：http://localhost:8080/rent.do
* HTTP请求方式： POST
*请求数据：
{
    basketballId:篮球id,
    pressure:当前篮球压力
}
* 返回参数
{
   data：0;
}
* 参数说明：
    data：0为租借成功，1为租借失败
    
11. 用户信息
* 显示用户个人信息已完成订单列表
* URL：http://localhost:8080/orderList.do
* HTTP请求方式： POST
* 请求数据：
{
    limit：每页最大显示行数,
    currentPage：当前页数
    all:指定需要什么样的列表
}
* 返回参数
{
    user:账号名,
    phone:手机号,
    deposit:用户存款,
    orderList:{
    orderId:订单编号,
    basketballID:机柜编号, 
    model:机柜状态,
    deposit:租借押金,
    billing:小时租金,
    lendTime:租借时间,
    returnTime:归还时间,
    totalTime:总计时,
    castMoney:消费金额
    }
}

12. 篮球列表模块
* 显示所有篮球的列表
* URL：http://localhost:8080/basketList.do?
    * // * HTTP请求方式： POST
* 请求参数：
{
    limit：每页最大显示行数,
    currentPage：当前页数
}
* 返回参数
{
    basketballID:机柜编号,
    model:机柜状态,
    pressure:压力标准值,
    nowPressure:当前压力值,
    isBad:是否损坏,
    isRent:是否可借
    deposit:租借押金,
    billing:小时租金
}

13. 篮球详细信息
* 点击租借后显示的篮球详细信息
* URL：http://localhost:8080/basketballDetail.do
* HTTP请求方式： POST
*请求数据：
{
    basketballID:机柜编号
}
*返回参数
{
    user:账号名,
    phone:手机号,
    deposit:用户存款,
    basketballID:机柜编号, 
    model:机柜状态,
    isRent: 是否可借，
    deposit:租借押金,
    billing:小时租金
}

13. 订单详细信息
* 点击租借后显示的篮球详细信息
* URL：http://localhost:8080/orderDetail.do
* HTTP请求方式： POST
*请求数据：
{
    orderId:订单编号
}
*返回参数
{
    user:账号名,
    phone:手机号,
    deposit:用户存款,
    orderDetail:{
    orderId:订单编号,
    basketballID:机柜编号, 
    model:机柜状态,
    deposit:租借押金,
    billing:小时租金,
    lendTime:租借时间,
    currentTime:当前时间(获取当前时间，不写入数据库),
    time:当前计时（获取当前时间计算，不写入数据库）,
    nowCastMoney:欲消费金额（实时计算，不写入数据库）
    }
}

13. 机柜操作
* 控制机柜的开关
* URL：http://localhost:8080/cabinet.do
* HTTP请求方式： POST
*请求数据：
{
    basketballID:机柜编号,
    status: 机柜状态（0为开，1为关）
}
*返回参数
{
    data:0操作成功
}
