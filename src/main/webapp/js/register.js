$(document).ready(function () {
    var userUrl = "http://localhost:8080/schoolCard.do";
    var registerUrl = "http://localhost:8080/register.do";

    $("#login").click(function () {
        $(location).attr('href', 'login.html');
    });

    $("#valid").click(function () {
        var name = $("#user").val();
        var pass = $("#password").val();
        var pass1 = $("#password1").val();
        if (name == null || pass == null || pass1 == null || name == "" || pass == "" || pass1 == "" ) {
            alert("输入不能为空");
            return;
        }
        if(pass!=pass1){
            alert("两次输入的密码不一致！");
            $("#password1").val("");
        }
        else{
            pass= hex_md5(pass);
            $.ajax({
                type: 'POST',
                url: userUrl,
                data: {
                    card:name,
                    password:pass
                },
                success: function (data) {
                    console.info(data);
                    $("#getCode").removeAttr("disabled");
                    $("#register").removeAttr("disabled");
                },
                dataType: "json"
            });
        }
    });


    $("#register").click(function () {

        var name = $("#user").val();
        var pass = $("#password").val();
        var pass1 = $("#password1").val();
        if (name == null || pass == null || pass1 == null || name == "" || pass == "" || pass1 == "" ) {
            alert("输入不能为空");
            return;
        }
        if(pass!=pass1){
            alert("两次输入的密码不一致！");
            $("#password1").val("");
        }
        else{
            pass= hex_md5(pass);
            $.ajax({
                type: 'POST',
                url: registerUrl,
                data: {
                    card:name,
                    password:pass
                },
                success: function (data) {
                    console.info(data);
                    alert("登陆成功！");
                },
                dataType: "json"
            });
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