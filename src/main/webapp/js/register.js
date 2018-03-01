$(document).ready(function () {
    var userUrl = "http://localhost:8080/isSchoolCard.do";
    var registerUrl = "http://localhost:8080/register.do";
    var codeUrl = "http://localhost:8080/sendSMSCode.do";

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
        if(pass.length<6){
            alert("密码至少6位数");
            return;
        }
        if(pass.length>18){
            alert("密码至多18位数");
            return;
        }
        if(pass!=pass1){
            alert("两次输入的密码不一致！");
            $("#password1").val("");
            return;
        }
        else{
            pass= hex_md5(pass);
            $.ajax({
                type: 'GET',
                url: userUrl,
                data: {
                    user:name,
                    password:pass
                },
                success: function (data) {
                    if(data.data=="0"){
                        alert("验证成功！");
                        $("#getCode").removeAttr("disabled");
                        $("#user").attr("disabled","true");
                        $("#password").attr("disabled","true");
                        $("#password1").attr("disabled","true");
                        $("#valid").attr("disabled","true");
                    }
                    else if(data.data=="1")
                        alert("校园卡不存在或密码错误！");
                    else if(data.data=="2")
                        alert("error!");
                },
                dataType: "json"
            });
        }
    });

    $("#register").click(function () {
        var name = $("#user").val();
        var pass = $("#password").val();
        var phone = $("#telphone").val();
        var code = $("#code").val();
        pass= hex_md5(pass);
        code= hex_md5(code);
        $.ajax({
            type: 'POST',
            url: registerUrl,
            data: {
                card:name,
                password:pass,
                phone:phone,
                code:code
            },
            success: function (data) {
                console.info(data);
                if(data.data=="0"){
                    alert("注册成功");
                    $(location).attr('href', 'login.html');
                }
                else if(data.data=="1"){
                    alert("error！");
                }
                else if(data.data=="2"){
                    alert("验证码输入错误！");
                }
            },
            dataType: "json"
        });
    });

    $("#getCode").click(function () {

        var phone = $("#telphone").val();

        //验证码
        $.ajax({
            type: 'GET',
            url: codeUrl,
            data: {
                phone:phone
            },
            success: function (data) {
                console.info(data);
            },
            dataType: "json"
        });
        $("#register").removeAttr("disabled");
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