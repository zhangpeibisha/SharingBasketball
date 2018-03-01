$(document).ready(function () {
    var forgetCodeUrl = "http://localhost:8080/phoneVerification.do";

    $("#back").click(function () {
        $(location).attr('href','login.html');
    });

    $("#getCode").click(function () {

        var phone = $("#phone").val();
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
            type: 'POST',
            url: forgetCodeUrl,
            data: {
                phone:phone
            },
            success: function (data) {
                if(data.data=="0"){
                    $("#sub").removeAttr("disabled");
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
                }
                else{
                    alert(data.message);
                }
            },
            dataType: "json"
        });

    });

    $("#sub").click(function () {
        if(code.length==0)
        {
            alert('请输入验证码！');
            return;
        }
        if(phone.length!=6)
        {
            alert('验证码为6位!');
            return;
        }
        alert("验证成功！");
        var user = $("#user").val();
        $(location).attr('href','changePassword.html?user='+user);
    });

});