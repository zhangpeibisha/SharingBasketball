$(document).ready(function () {
    var basketListUrl = "http://localhost:8080/basketList.do";
    var pageLimit = 10;
    var currentPage = 1;

    function init() {
        $.ajax({
            type: 'GET',
            url: basketListUrl,
            data: {
                limit: pageLimit,
                currentPage: currentPage
            },
            success: function (data) {
                console.log(data);
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
                                url: basketListUrl,
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
                                    else if(data.data==1){
                                        noData();
                                    }
                                    else if(data.data==2)
                                        alert("error！");
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
        temp.push('<thead><tr><th>机柜编号</th><th>机柜状态</th><th>压力标准值</th>' +
            '<th>当前压力值</th><th>是否损坏</th><th>是否借出</th>'+
            '<th>租借押金</th><th>小时租金</th></tr><tbody>');
        for (var i = 0; i < showNum; i++) {
            if(listData[i].isRent=="0")
                listData[i].isRent ="否";
            else listData[i].isRent ="是";
            if(listData[i].isBad=="0")
                listData[i].isBad ="正常";
            else listData[i].isBad ="损坏";
            if(listData[i].cabinet=="0")
                listData[i].cabinet ="打开";
            else listData[i].cabinet ="关闭";
            temp.push("<tr><td>" + listData[i].basketballID + "</td><td>" + listData[i].cabinet + "</td><td>"
                + listData[i].pressure+ "磅</td><td>" + listData[i].nowPerssure + "磅</td><td>"
                + listData[i].isBad + "</td><td>" + listData[i].isRent + "</td><td>"
                + listData[i].rent.deposit + "</td><td>" + listData[i].rent.billing + "</td><td>");
        }
        temp.push('</tbody></table>');

        $('#list').html(temp.join(''));
    }

    function noData() {
        var temp = [];
        temp.push('<table class="table table-hover">');
        temp.push('<thead><tr><th>机柜编号</th><th>机柜状态</th><th>压力标准值</th>' +
        '<th>当前压力值</th><th>是否损坏</th><th>是否可借</th>'+
        '<th>租借押金</th><th>小时租金</th></tr><tbody>');
        temp.push("<tr><td colspan='8' style='text-align: center'>暂无数据</td></tr>");
        temp.push('</tbody></table>');

        $('#list').html(temp.join(''));
    }

    init();
});