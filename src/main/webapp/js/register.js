$(document).ready(function () {

    $("#login").click(function () {
        $(location).attr('href', 'login.html');
    });

    $("#register").click(function () {
        var pass = $("#password").val();
        var pass1 = $("#password1").val();
        if(pass!=pass1){
            alert("两次输入的密码不一致！");
            $("#password1").val("");
        }
        else{
            alert("注册成功！");
            $(location).attr('href', 'login.html');
        }
    });

    $("#getCode").click(function () {
        alert("已发送验证码");
        $("#getCode").attr("disabled","true");
        var s=59;
        var start = setInterval(function(){
            $("#getCode").text(s+"s");
            s--;
            if(s<0){
                clearInterval(start);
                $("#getCode").text("获取验证码");
                $("#getCode").removeAttr("disabled");
            }
        },1000)
    });

});