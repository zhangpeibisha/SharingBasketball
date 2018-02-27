$(document).ready(function () {
    var updateUrl = "http://localhost:8080/updatePassword.do";
    alert(Request.QueryString("user"));

    $("#sub").click(function () {
        var pass = $("#password").val();
        var pass1 = $("#password1").val();
        if (pass == null || pass1 == null || pass == "" || pass1 == "" ) {
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
        }
        else{
            pass= hex_md5(pass);
            $.ajax({
                type: 'GET',
                url: updateUrl,
                data: {
                    user:name,
                    password:pass
                },
                success: function (data) {
                    console.info(data);
                    if(data.data=="0"){
                        alert("密码修改成功");
                        $(location).attr('href', 'welcome.html');
                    }
                    else if(data.data=="1"){
                        alert("当前用户不存在！");
                    }
                    else if(data.data=="2"){
                        alert("用户名或密码不正确！");
                    }
                },
                dataType: "json"
            });


            /*$(location).attr('href', 'login.html');*/
        }
    });

    $("#back").click(function () {
        $(location).attr('href','forgetPassword.html');
    });

});