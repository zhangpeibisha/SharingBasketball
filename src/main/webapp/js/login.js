$(document).ready(function(){
    var loginUrl = "";

    //登录
    $("#login").click(function(){

        var user = $("#user").val();
        var password = $("#password").val();

        //md5加密


        $.post(loginUrl,data,success(data, textStatus, jqXHR),dataType);


    });

    //注册
    $("#register").click(function () {
        $(location).attr('href', 'register.html');
    })



});