$(document).ready(function () {
    var orderListUrl = "http://localhost:8080/orderList.do";
    var pageLimit = 10;
    var currentPage = 1;

    function init() {
        $.ajax({
            type: 'POST',
            url: orderListUrl,
            data: {
                limit: pageLimit,
                currentPage: currentPage,
                all: 0
            },
            success: function (data) {
                console.log(data);
                if(data.data==0&&data.total!=0){
                    var listData = data.orderList;
                    var total = data.total;
                    showData(listData);
                    info(data.user,data.phone,data.money);
                    var num = (total+pageLimit -1)/pageLimit;//向上取整
                    $('#page').bootstrapPaginator({
                        bootstrapMajorVersion: 3,
                        currentPage: 1,//当前页码
                        totalPages: num,
                        numberOfPages: 5,
                        shouldShowPage:true,//是否显示该按钮
                        itemTexts: function (type, page, current) {
                            switch (type) {
                                case "first": return "首页";
                                case "prev": return "上一页";
                                case "next": return "下一页";
                                case "last": return "末页";
                                case "page": return page;
                            }
                        },
                        onPageClicked: function (event, originalEvent, type, page) {
                            $.ajax({
                                url: orderListUrl,
                                type: 'POST',
                                data: {
                                    limit: pageLimit,
                                    currentPage: page,
                                    all: 0
                                },
                                dataType: 'json',
                                success: function (data) {
                                    console.info(data);
                                    if(data.data==0){
                                        var listData = data.orderList;
                                        showData(listData);
                                        info(data.user,data.phone,data.money);
                                    }
                                    else if(data.data==0&&data.total==0){
                                        info(data.user,data.phone,data.money);
                                        noData();
                                    }
                                    else{
                                        alert(data.message);
                                    }
                                }
                            });
                        }
                    });
                }
                else if(data.data==0&&data.total==0){
                    info(data.user,data.phone,data.money);
                    noData();
                }
                else{
                    alert(data.message);
                }
            },
            dataType: "json"
        });
    }

    function MillisecondToDate(msd) {
        var time = parseFloat(msd) /1000;
        if (null!= time &&""!= time) {
            if (time >60&& time <60*60) {
                time = parseInt(time /60.0) +"分钟"+ parseInt((parseFloat(time /60.0) -
                    parseInt(time /60.0)) *60) +"秒";
            }else if (time >=60*60&& time <60*60*24) {
                time = parseInt(time /3600.0) +"小时"+ parseInt((parseFloat(time /3600.0) -
                    parseInt(time /3600.0)) *60) +"分钟"+
                    parseInt((parseFloat((parseFloat(time /3600.0) - parseInt(time /3600.0)) *60) -
                        parseInt((parseFloat(time /3600.0) - parseInt(time /3600.0)) *60)) *60) +"秒";
            }else {
                time = parseInt(time) +"秒";
            }
        }else{
            time = "0 时 0 分0 秒";
        }
        return time;
    }


    function showData(listData) {

        var temp = [], showNum = listData.length;

        temp.push('<table class="table table-hover">');
        temp.push('<thead><tr><th>订单编号</th><th>机柜编号</th><th>机柜状态</th>' +
            '<th>租借押金</th><th>小时租金</th><th>租借时间</th><th>归还时间</th><th>总计时</th>' +
            '<th>消费金额</th></tr><tbody>');

        for (var i = 0; i < showNum; i++) {
            var totalTime;

            if(listData[i].returnTime==null){
                listData[i].returnTime="暂无";
                totalTime="暂无";
            }
            else {
                totalTime = MillisecondToDate(listData[i].returnTime-listData[i].lendTime);
                listData[i].returnTime = new Date(listData[i].returnTime).toLocaleString();
            }
            listData[i].lendTime = new Date(listData[i].lendTime).toLocaleString();

            if(listData[i].basketball.cabinet=="0")
                listData[i].basketball.cabinet ="打开";
            else listData[i].basketball.cabinet ="关闭";

            temp.push("<tr><td>" + listData[i].orderID + "</td><td>" + listData[i].basketball.basketballID + "</td><td>"
                + listData[i].basketball.cabinet+ "</td><td>" + listData[i].basketball.rent.deposit + "</td><td>"
                + listData[i].basketball.rent.billing + "</td><td>" + listData[i].lendTime + "</td><td>"
                + listData[i].returnTime + "</td><td>"+ totalTime + "</td><td>"
                + listData[i].castMoney + "</td><td>");
        }
        temp.push('</tbody></table>');

        $('#list').html(temp.join(''));
    }

    function noData() {
        var temp = [];
        temp.push('<table class="table table-hover">');
        temp.push('<thead><tr><th>订单编号</th><th>机柜编号</th><th>机柜状态</th>' +
            '<th>租借押金</th><th>小时租金</th><th>租借时间</th>' +
            '<th>消费金额</th></tr><tbody>');
        temp.push("<tr><td colspan='9' style='text-align: center'>暂无数据</td></tr>");
        temp.push('</tbody></table>');

        $('#list').html(temp.join(''));
    }

    function info(user,phone,money) {
        var temp = [];
        temp.push('<div class="col-md-12"><p class="col-md-3">账号:</p><span>'+user +'</span></div>');
        temp.push('<div class="col-md-12"><p class="col-md-3">手机号:</p><span>'+ phone +'</span></div>');
        temp.push('<div class="col-md-12"><p class="col-md-3">存款:</p><span>'+ money +'元</span></div>');
        $('#pinfo').html(temp.join(''));
    }

    init();
});


