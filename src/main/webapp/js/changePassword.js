$(document).ready(function () {

    $("#sub").click(function () {
        var pass = $("#password").val();
        var pass1 = $("#password1").val();
        if(pass!=pass1){
            alert("两次输入的密码不一致！");
            $("#password1").val("");
        }
        else{
            alert("密码修改成功！");
            $(location).attr('href', 'login.html');
        }
    });

    $("#back").click(function () {
        $(location).attr('href','forgetPassword.html');
    });

});