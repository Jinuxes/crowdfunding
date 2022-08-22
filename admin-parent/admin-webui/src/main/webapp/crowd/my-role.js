// 执行分页，生成页面效果
function generatePage(){
    // 1.获取分页数据
    var pageInfo = getPageInfoRemote();

    // 2.填充表格
    fillTableBody(pageInfo);

    // 3.生成导航条
    generateNavigator(pageInfo);
}

// 远程访问服务器端程序获取pageInfo数据
function getPageInfoRemote(){

    // 1.使用ajax发送同步请求获取pageInfo数据，这里可以使用异步的，只是练手以下ajax的同步请求
    var ajaxResult = $.ajax({
        "url":"role/get/page/info.json",
        "type":"post",
        "data":{
            "pageNum": window.pageNum,
            "pageSize": window.pageSize,
            "keyword": window.keyword
        },
        "dataType":"json",
        "async":false
    });

    // 判断当前响应状态码是否为200
    var statusCode = ajaxResult.status;

    // 如果当前响应状态码不为200，说明发生了错误或其它以外清空，显示提示消息，让当前函数停止执行
    if(statusCode != 200){
        layer.msg("失败！响应状态码open="+statusCode+" 说明信息="+ajaxResult.statusText);
        return null;
    }

    // 如果响应状态码是200，说明处理成功，获取pageInfo
    var resultEntity = ajaxResult.responseJSON;

    // 从resultEntity中获取result属性
    var result = resultEntity.result;

    // 判断result是否成功
    if(result == "FAILED"){
        layer.msg(resultEntity.message);
        return null;
    }

    //确认result为成功后获取pageInfo
    var pageInfo = resultEntity.data;

    //返回pageInfo
    return pageInfo;
}

// 填充表格
function fillTableBody(pageInfo){
    // 清除tbody中的旧的内容跟导航条中的内容
    $("#rolePageBody").empty();
    $("#Pagination").empty();

    // 判断pageInfo对象是否有效
    if(pageInfo == null || pageInfo == undefined || pageInfo.list == null || pageInfo.list.length == 0){
        $("#rolePageBody").append("<tr><td colspan='4' align='center'>抱歉！没有查询到您搜索的数据！</td></tr>");
        return;
    }

    // 使用pageInfo的list属性填充tbody
    for(var i =0; i<pageInfo.list.length; i++){
        var role = pageInfo.list[i];
        var roleId = role.id;
        var roleName = role.name;

        var numberTd = "<td>"+(i+1)+"</td>";
        var checkboxTd = "<td><input type='checkbox'/></td>";
        var roleNameTd = "<td>"+roleName+"</td>";

        var checkBtn = "<button id='"+roleId+"' type='button' class='btn btn-success btn-xs checkBtn'><i class=' glyphicon glyphicon-check'></i></button>";
        var pencilBtn= "<button type='button' id='"+roleId+"' class='btn btn-primary btn-xs'><i class=' glyphicon glyphicon-pencil'></i></button>";
        var removeBtn = "<button type='button' class='btn btn-danger btn-xs'><i class=' glyphicon glyphicon-remove'></i></button>";

        var buttonTd = "<td>"+checkBtn+" "+pencilBtn+" "+removeBtn+"</td>";
        var tr = "<tr>"+numberTd+checkboxTd+roleNameTd+buttonTd+"</tr>";
        $("#rolePageBody").append(tr);
    }
}

// 生成分页页码导航条
function generateNavigator(pageInfo){
    //获取总记录数
    var totalRecord = pageInfo.total;

    //声明一个JSON对象存储Pagination要设置的属性
    var properties = {
        num_edge_entries: 3,                               // 边缘页数
        num_display_entries: 5,                            // 主体页数
        callback: paginationCallBack,                      // 指定用户点击“翻页”的按钮时跳转页面的回调函数
        items_per_page: pageInfo.pageSize,                 // 每页要显示的数据的数量
        current_page: pageInfo.pageNum-1,                  // Pagination内部使用pageIndex来管理页码，pageIndex从0开始，pageNum从1开始，所以要减一
        prev_text: "上一页",                                // 上一页按钮上显示的文本
        next_text: "下一页"                                 // 下一页按钮上显示的文本
    }

    //生成页码导航条
    $("#Pagination").pagination(totalRecord, properties);
}

function paginationCallBack(pageIndex, jQuery){
    // 根据pageIndex计算得到PageNum
    window.pageNum = pageIndex + 1;

    // 调用分页函数
    generatePage();
    // 取消页码超链接的默认行为
    return false;
}

// 声明专门的函数用来在分配Auth的模态框中显示Auth的树形结构数据
function fillAuthTree(){
    // 1.发送ajax同步请求查询Auth数据
    var ajaxReturn = $.ajax({
        "url":"assign/get/all/auth.json",
        "type":"post",
        "dataType":"json",
        "async":false
    });

    if(ajaxReturn.status != 200){
        layer.msg("请求处理出错！响应状态码是："+ajaxReturn.status+"说明是："+ajaxReturn.statusText);
        return;
    }

    // 2.从响应结果中获取Auth的JSON数据
    // 从服务器端查询到的list不再组装成树形结构，这里我们交给前端的zTree去组装。之前的菜单维护那里时服务器查询数据后，直接在服务器端组装好一个树返回。
    var authList = ajaxReturn.responseJSON.data;

    // 3.指定使用简单的JSON数据，让zTree将数据组装成树
    var setting = {
        "data":{
            "simpleData":{
                "enable":true,  // 开启简单JSON功能
                "pIdKey":"categoryId"  // 使用categoryId属性关联父节点，不用默认的pid
            },
            "key":{
                // 使用title属性显示节点名称，不用默认的name作为属性名了
                "name":"title"
            }
        },
        "check":{
            "enable": true  // 显示树形结构前面的checkbox勾选框
        }
    };

    // 4.生成树形结构
    $.fn.zTree.init($("#authTreeDemo"),setting,authList);

    // 调用zTreeObj对象的方法，把节点全部展开
    var zTreeObj = $.fn.zTree.getZTreeObj("authTreeDemo");
    zTreeObj.expandAll(true);

    // 5.查询已分配的Auth的id组成的数组。同样的，为了后面的操作能按顺序进行，也是用同步的ajax请求
    ajaxReturn = $.ajax({
        "url":"assign/get/auth/id.json",
        "type":"post",
        "data":{
            "roleId":window.roleId
        },
        "dataType":"json",
        "async":false,
    });

    if(ajaxReturn.status != 200){
        layer.msg("请求处理出错！响应状态码是："+ajaxReturn.status+"说明是："+ajaxReturn.statusText);
        return;
    }

    // 从响应结果中获取authIdArray
    var authIdArray = ajaxReturn.responseJSON.data;

    // 6.根据authIdArray把树形结构中对应的节点勾选上
    // 遍历authIdArray
    for(var i=0;i<authIdArray.length;i++){
        var authId = authIdArray[i];

        // 根据id查询树形结构中对应的节点
        var treeNode = zTreeObj.getNodeByParam("id",authId);

        // checked设置为true表示节点勾选
        var checked = true;

        // checkTypeFlag设置为false，表示不”联动“，不联动是为了避免把不该勾选的勾选上
        var checkTypeFlag = false;
        // var checkTypeFlag = true;  //这是联动，这样会导致没有勾选的权限也会被勾选

        // 将treeNode设置为被勾选
        zTreeObj.checkNode(treeNode, checked, checkTypeFlag)
    }
}