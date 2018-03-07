$(document).ready(function () {
    var detailUrl = "http://localhost:8080/basketballDetail.do";
    var creatOrderUrl = "http://localhost:8080/rent.do";

    var loc = location.href;
    var n1 = loc.length;//地址的总长度
    var n2 = loc.indexOf("=");//取得=号的位置
    var basketID = decodeURI(loc.substr(n2+1, n1-n2));//从=号后面的内容

    function init() {
        $.ajax({
            type: 'GET',
            url: detailUrl,
            data: {
                basketballID:basketID
            },
            success: function (data) {
                console.info(data);
                if(data.data=="0"){
                    info(data.basketball,data.user);
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
        var pressure = $("#pressure").val();
        if (pressure == null || pressure == "") {
                alert("请输入当前篮球压力！");
                return;
        }
        $.ajax({
            type: 'POST',
            url: creatOrderUrl,
            data: {
                basketballId:basketID,
                pressure:pressure
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

    function info(bas,user) {

        if(bas.isRent=="0")
            bas.isRent ="否";
        else bas.isRent ="是";
        if(bas.cabinet=="0")
            bas.cabinet ="打开";
        else {
            $("#sub").attr("disabled","true");
            bas.cabinet ="关闭";
        }
        var temp = [];
        temp.push('<div class="col-md-12"><p class="col-md-3">机柜编号:</p><span>'+ bas.basketballID +'</span></div>');
        temp.push('<div class="col-md-12"><p class="col-md-3">机柜状态:</p><span>'+ bas.cabinet +'</span></div>');
        temp.push('<div class="col-md-12"><p class="col-md-3">是否借出:</p><span>'+ bas.isRent +'</span></div>');
        temp.push('<div class="col-md-12"><p class="col-md-3">租借押金:</p><span>'+ bas.rent.deposit +'</span></div>');
        temp.push('<div class="col-md-12"><p class="col-md-3">小时租金:</p><span>'+ bas.rent.billing +'</span></div>');
        $('#binfo').html(temp.join(''));

        var tempp = [];
        tempp.push('<div class="col-md-12"><p class="col-md-3">当前用户:</p><span>'+user.schoolID +'</span></div>');
        tempp.push('<div class="col-md-12"><p class="col-md-3">存款:</p><span>'+ user.money +'元</span></div>');
        $('#pinfo').html(tempp.join(''));
    }

    init();

});
