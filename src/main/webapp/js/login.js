$(document).ready(function(){
    var loginUrl = "http://localhost:8080/login.do";

    //登录
    $("#login").click(function(){
        var name = $("#user").val();
        var password = $("#password").val();

        if (name == null || password == null || name == "" || password == "") {
            alert("输入不能为空");
            return;
        }
        password= hex_md5(password);
        $.ajax({
            type: 'GET',
            url: loginUrl,
            data: {
                user:name,
                password:password
            },
            success: function (data) {
                console.info(data);
                if(data.data=="0"){
                     $(location).attr('href', 'welcome.html');
                }
                else{
                    alert(data.message);
                }
            },
            dataType: "json"
        });
    });

    //注册
    $("#register").click(function () {
        $(location).attr('href', 'register.html');
    });

    //忘记密码
    $("#forget").click(function () {
        $(location).attr('href', 'forgetPassword.html');
    });

});