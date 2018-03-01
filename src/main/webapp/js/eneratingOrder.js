$(document).ready(function () {
    var detailUrl = "http://localhost:8080/rent.do";
    var creatOrderUrl = "";

    var loc = location.href;
    var n1 = loc.length;//地址的总长度
    var n2 = loc.indexOf("=");//取得=号的位置
    var basketID = decodeURI(loc.substr(n2+1, n1-n2));//从=号后面的内容

    function init() {
        $.ajax({
            type: 'POST',
            url: detailUrl,
            data: {
                user:"13752818831",
                basketballId:basketID
            },
            success: function (data) {
                console.info(data);
                if(data.data=="0"){
                    $("#sub").removeAttr("disabled");
                }
                else{
                    alert(data.message);
                }
            },
            dataType: "json"
        });
    }

    $("#sub").click(function () {
        $.ajax({
            type: 'GET',
            url: creatOrderUrl,
            data: {
                basketballId:basketID
            },
            success: function (data) {
                console.info(data);
                if(data.data=="0"){
                    alert("租借成功！");
                    $(location).attr('href', 'information.html');
                }
                else{
                    alert(data.message);
                }
            },
            dataType: "json"
        });
    });

    init();

});
