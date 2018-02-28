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
            console.info(data);
            if(data.data==0){
                var listData = data.basketballs;
                showData(data.total,listData);
            }
            else if(data.data==1)
                alert("查询成功但没有篮球！");
            else if(data.data==2)
                alert("error！");
        },
        dataType: "json"
    });
}

function showData(total,listData) {
    var temp = [], showNum = listData.length;

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

    $('#list').html(temp.join(''));

    $('#page').bootstrapPaginator({
        bootstrapMajorVersion: 3,
        currentPage: 1,//当前页码
        totalPages: total,
        numberOfPages: pageLimit,//一页显示几个按钮
        itemTexts: function (type, page, current) {
            switch (type) {
                case "first": return "首页";
                case "prev": return "上一页";
                case "next": return "下一页";
                case "last": return "末页";
                case "page": return page;
            }
        },//改写分页按钮字样
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
                        showData(data.total,listData);
                    }
                    else if(data.data==1)
                        alert("查询成功但没有篮球！");
                    else if(data.data==2)
                        alert("error！");
                }
            });

        }
    });
}

init();