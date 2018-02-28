$(document).ready(function () {
    var listUrl = "http://localhost:8080/rentList.do";
    var pageLimit = 10;
    var currentPage = 1;
    var showCount1 = 11;


    function init() {
        $.ajax({
            type: 'GET',
            url: listUrl,
            data: {
                limit: pageLimit,
                currentPage: currentPage
            },
            success: function (data) {
                console.info(data);
                if(data.data==0){
                    var listData = data.basketballs;
                    console.info(listData);
                    data.total = 12;
                    callBackPagination(data.total,listData);
                }
                else if(data.data==1)
                    alert("查询成功但没有篮球！");
                else if(data.data==2)
                    alert("error！");
            },
            dataType: "json"
        });
    }


    function callBackPagination(total,listData) {
        var totalCount = total || 252, showCount = showCount1 ,
            limit = pageLimit || 10;
        createTable(1, limit, totalCount,listData);
        $('#page').extendPagination({
            totalCount: totalCount,
            showCount: showCount,
            limit: limit,
            callback: function (curr, limit, totalCount) {
                createTable(curr, limit, totalCount,listData);
            }
        });
    }

    function createTable(currPage, limit, total,listData) {
        var temp = [], showNum = limit;
        if (total - (currPage * limit) < 0) showNum = total - ((currPage - 1) * limit);

        temp.push('<table class="table table-hover">');
        temp.push('<thead><tr><th>篮球编号</th><th>篮球型号</th><th>是否可借</th>' +
            '<th>租借押金</th><th>小时租金</th><th>操作</th></tr><tbody>');
        for (var i = 0; i < showNum; i++) {
            if(listData[i].isRent=="0")
                listData[i].isRent ="可借";
            else listData[i].isRent ="不可借";
            temp.push("<tr><td>" + listData[i].basketballID + "</td><td>" + listData[i].model + "</td><td>"
                + listData[i].isRent + "</td><td>"+ listData[i].rent.deposit + "</td><td>"
                + listData[i].rent.billing + "</td><td>"
                + "<a class='rent-a'>租借</a>" + "</td></tr>");
        }
        temp.push('</tbody></table>');
        var mainObj = $('#list');
        mainObj.empty();
        mainObj.html(temp.join(''));
    }

    init();
});