$(document).ready(function () {
    var updateUrl = "http://localhost:8080/updatePasswordRun.do";

    var loc = location.href;
    var n1 = loc.length;//地址的总长度
    var n2 = loc.indexOf("=");//取得=号的位置
    var name = decodeURI(loc.substr(n2+1, n1-n2));//从=号后面的内容

    console.info(name);


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
                type: 'POST',
                url: updateUrl,
                data: {
                    phone:name,
                    password:pass
                },
                success: function (data) {
                    console.info(data);
                    if(data.data=="0"){
                        alert("密码修改成功！");
                        $(location).attr('href', 'welcome.html');
                    }
                    else{
                        alert(data.message);
                    }
                },
                dataType: "json"
            });
        }
    });

    $("#back").click(function () {
        $(location).attr('href','forgetPassword.html');
    });

});