$(document).ready(function () {

    $("#back").click(function () {
        $(location).attr('href','login.html');
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

    $("#sub").click(function () {
        alert("验证成功！");
        $(location).attr('href','changePassword.html');
    });

});