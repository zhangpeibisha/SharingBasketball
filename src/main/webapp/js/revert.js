$(document).ready(function () {

    var revertListUrl = "http://localhost:8080/orderList.do";
    var pageLimit = 10;
    var currentPage = 1;

    function init() {
        $.ajax({
            type: 'POST',
            url: revertListUrl,
            data: {
                limit: pageLimit,
                currentPage: currentPage
            },
            success: function (data) {
                console.log(data);
                if(data.data==0&&data.total!=0){
                    var listData = data.orderList;
                    var total = data.total;
                    showData(listData);
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
                                url: revertListUrl,
                                type: 'POST',
                                data: {
                                    limit: pageLimit,
                                    currentPage: page
                                },
                                dataType: 'json',
                                success: function (data) {
                                    console.info(data);
                                    if(data.data==0){
                                        var listData = data.orderList;
                                        showData(listData);
                                    }
                                    else if(data.data==0&&data.total==0){
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
                    info(data.user,data.phone,data.deposit);
                    noData();
                }
                else{
                    alert(data.message);
                }
            },
            dataType: "json"
        });
    }


    function showData(listData) {
        var temp = [], showNum = listData.length;

        temp.push('<table class="table table-hover">');
        temp.push('<thead><tr><th>订单编号</th><th>篮球编号</th><th>篮球型号</th>' +
            '<th>租借押金</th><th>小时租金</th><th>租借时间</th><th>操作</th></tr><tbody>');
        for (var i = 0; i < showNum; i++) {
            listData[i].lendTime = new Date(listData[i].lendTime).toLocaleString();

            var tempHref = "../html/completeOrder.html?id=" +listData[i].orderID;

            temp.push("<tr><td>" + listData[i].orderID + "</td><td>" + listData[i].basketball.basketballID + "</td><td>"
                + listData[i].basketball.model+ "</td><td>" + listData[i].basketball.rent.deposit + "</td><td>"
                + listData[i].basketball.rent.billing + "</td><td>" + listData[i].lendTime + "</td><td>"
                + "<a class='revert-a' href="+ tempHref +">归还</a></td></tr>");
        }
        temp.push('</tbody></table>');

        $('#list').html(temp.join(''));
    }

    function noData() {

        var temp = [];
        temp.push('<table class="table table-hover">');
        temp.push('<thead><tr><th>订单编号</th><th>篮球编号</th><th>篮球型号</th>' +
            '<th>租借押金</th><th>小时租金</th><th>租借时间</th><th>归还时间</th>' +
            '<th>消费金额</th></tr><tbody>');
        temp.push("<tr><td colspan='9' style='text-align: center'>暂无数据</td></tr>");
        temp.push('</tbody></table>');

        $('#list').html(temp.join(''));
    }

    init();



});