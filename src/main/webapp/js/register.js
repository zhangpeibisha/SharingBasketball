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
                    else {
                        alert(data.message);
                    }
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

        if(code.length==0)
        {
            alert('请输入验证码！');
            return;
        }
        if(code.length!=6)
        {
            alert('验证码为6位!');
            return;
        }

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
                else {
                    alert(data.message);
                }
            },
            dataType: "json"
        });
    });

    $("#getCode").click(function () {
        var phone = $("#telphone").val();

        if(phone.length==0)
        {
            alert('请输入手机号码！');
            return;
        }
        if(phone.length!=11)
        {
            alert('请输入有效的手机号码！');
            return;
        }

        var myreg = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
        if(!myreg.test(phone))
        {
            alert('请输入有效的手机号码！');
            return;
        }

        $.ajax({
            type: 'GET',
            url: codeUrl,
            data: {
                phone:phone
            },
            success: function (data) {
                if(data.data=="0"){
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
                    },1000);
                }
                else{
                    alert(data.message);
                }
            },
            dataType: "json"
        });

    });

});