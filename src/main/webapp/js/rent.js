var cabinetUrl = "http://localhost:8080/cabinet.do";
function cabinetOpen(id) {
    console.info(id);
    $.ajax({
        type: 'POST',
        url: cabinetUrl,
        data: {
            basketballID:id,
            status:0
        },
        success: function (data) {
            if(data.data=="0"){
                window.location.reload();
            }
            else {
                alert(data.message);
            }
        },
        dataType: "json"
    });
}

function cabinetClose(id) {
    console.info(id);
    $.ajax({
        type: 'POST',
        url: cabinetUrl,
        data: {
            basketballID:id,
            status:1
        },
        success: function (data) {
            if(data.data=="0"){
                window.location.reload();
            }
            else {
                alert(data.message);
            }
        },
        dataType: "json"
    });

}

$(document).ready(function () {
    var listUrl = "http://localhost:8080/rentList.do";
    var pageLimit = 10;
    var currentPage = 1;


    function init() {
        $.ajax({
            type: 'GET',
            url: listUrl,
            data: {
                limit: pageLimit,
                currentPage: currentPage
            },
            success: function (data) {
                if(data.data==0&&data.total!=0){
                    var listData = data.basketballs;
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
                                url: listUrl,
                                type: 'GET',
                                data: {
                                    limit: pageLimit,
                                    currentPage: page
                                },
                                dataType: 'json',
                                success: function (data) {
                                    console.info(data);
                                    if(data.data==0){
                                        var listData = data.basketballs;
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
        temp.push('<thead><tr><th>机柜编号</th><th>机柜状态</th><th>是否借出</th>' +
            '<th>租借押金</th><th>小时租金</th><th>机柜操作</th><th>操作</th></tr><tbody>');
        for (var i = 0; i < showNum; i++) {

            if(listData[i].isRent=="0")
                listData[i].isRent ="否";
            else listData[i].isRent ="是";

            var cabi;
            if(listData[i].cabinet=="0"){
                listData[i].cabinet ="打开";
                cabi ="<a class='rent-a' onclick='cabinetClose("+listData[i].basketballID+")'>关闭</a></td>";
            }
            else {
                listData[i].cabinet ="关闭";
                cabi ="<a class='rent-a' onclick='cabinetOpen("+listData[i].basketballID+")'>打开</a></td>";
            }
            var tempHref = "../html/eneratingOrder.html?id=" +listData[i].basketballID;
            temp.push("<tr><td>" + listData[i].basketballID + "</td><td>" + listData[i].cabinet + "</td><td>"
                + listData[i].isRent + "</td><td>"+ listData[i].rent.deposit + "</td><td>"
                + listData[i].rent.billing + "</td><td>"
                + cabi
                + "<td><a class='rent-a' href="+ tempHref +">租借</a></td></tr>");

            $("#"+listData[i].basketballID).click(function () {
                alert('1');
            })
        }
        temp.push('</tbody></table>');


        $('#list').html(temp.join(''));
    }

    function noData() {
        var temp = [];
        temp.push('<table class="table table-hover">');
        temp.push('<thead><tr><th>机柜编号</th><th>机柜状态</th><th>是否可借</th>' +
            '<th>租借押金</th><th>小时租金</th><th>机柜操作</th></th><th>操作</th></tr><tbody>');
        temp.push("<tr><td colspan='7' style='text-align: center'>暂无数据</td></tr>");
        temp.push('</tbody></table>');

        $('#list').html(temp.join(''));
    }


    init();




});