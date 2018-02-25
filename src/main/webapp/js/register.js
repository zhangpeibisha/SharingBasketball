$(document).ready(function () {

    $("#login").click(function () {
        $(location).attr('href', 'login.html');
    });

    function regist() {
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
    }

    $("#register").click(function () {
        regist();
    })

});