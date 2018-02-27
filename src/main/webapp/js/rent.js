$(document).ready(function () {
    var listUrl = "http://localhost:8080/rentList.do";

    function init() {
        $.ajax({
            type: 'GET',
            url: listUrl,
            data: {
            },
            success: function (data) {
                console.info(data);
                if(data.data==0){
                    var listData = data.basketballs;
                    var temp = "";
                    for(var i = 0; i < listData.length; i++) {
                        if(listData[i].isRent=="0")
                            listData[i].isRent ="可借";
                        else listData[i].isRent ="不可借";
                        temp = "<tr><td>" + listData[i].basketballID + "</td><td>" + listData[i].model + "</td><td>"
                            + listData[i].isRent + "</td><td>"+ listData[i].pressure + "</td><td>"
                            + "<a>租借</a>" + "</td></tr>";
                        $("#list").append(temp);
                    }

                }
                else if(data.data==1)
                    alert("加载失败！");
                else if(data.data==2)
                    alert("没有数据");
            },
            dataType: "json"
        });


    }





    init();

});