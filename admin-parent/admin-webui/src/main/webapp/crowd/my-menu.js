// 生成树形结构的函数
function generateTree(){
    // 1.创建JSON对象用于存储zTree所做的设置
    var setting = {
        "view":{
            "addDiyDom":myAddDiyDom,
            "addHoverDom":myAddHoverDom,
            "removeHoverDom":myRemoveHoverDom
        },
        "data":{
            "key":{
                "url":"notExists"
            }
        }
    };

    // 2.准备生成树形结构的JSON数据，数据的来源是发送Ajax请求向后端获取
    $.ajax({
        "url":"menu/get/tree.json",
        "type":"post",
        "dataType":"json",
        "success":function(response){
            var result = response.result;
            if(result == "SUCCESS"){
                var zNodes = response.data;

                // 3.初始化树形结构
                $.fn.zTree.init($("#treeDemo"), setting, zNodes);
            }

            if(result == "FAILED"){
                layer.msg(response.message);
            }
        },
    });
}

//修改默认的图标
function myAddDiyDom(treeId, treeNode){
    // 根据id的生成规则拼接出来span标签的id
    /*
     * zTree生成id的规则
     * <span id="treeDemo_7_ico" title="" treenode_ico="" class="button ico_open" style="background:url(glyphicon glyphicon-ok) 0 0 no-repeat;"></span>
     * 例子：treeDemo_7_ico
     * 解析：ul标签的id_当前节点的序号_功能
     * 提示：“ul标签的id_当前节点的序号”这部分可以通过访问参数treeNode的tId属性得到
     */
    var spanId = treeNode.tId +"_ico";

    // 根据控制图标的span标签的id找到这个span标签
    // 删除旧的class
    // 添加新的class
    $("#"+spanId).removeClass().addClass(treeNode.icon);
}

/*
 * 按钮组源码
 * <span id="btnGrouptreeDemo_5">
 *      <a class="btn btn-info dropdown-toggle btn-xs" style="margin-left:10px;padding-top:0px;" href="#" title="修改权限信息">
 *          &nbsp;&nbsp;<i class="fa fa-fw fa-edit rbg "></i>
 *      </a>
 *      <a class="btn btn-info dropdown-toggle btn-xs" style="margin-left:10px;padding-top:0px;" href="#">&nbsp;&nbsp;
 *           <i class="fa fa-fw fa-times rbg "></i>
 *      </a>
 *  </span>
 */

// 在鼠标移入节点范围时添加按钮组
function myAddHoverDom(treeId, treeNode){
    // 看上面源码可以看见按钮组的标签结构：<sapn><a><i></i></a><a><i></i></a></span>
    // 按钮组出现的位置：节点中treeDemo_n_a超链接的后面
    // 找到附着按钮组的超链接
    var anchorId = treeNode.tId+"_a";

    //为了在需要移除按钮的时候能够精确定位到按钮组所在的span，需要给span设置有规律的id
    var btnGroupId = treeNode.tId +"_btnGrp";

    // 判断以下是否已经添加了按钮组（因为Hover这个函数，鼠标移入过程中会多次触发该函数，所以按钮组会叠加出现，需要确保移入过程中只加一次，移出的时候删除即可，清除所有按钮组）
    if($("#"+btnGroupId).length > 0){
        return ;
    }

    // 准备各个按钮的HTML标签
    var addBtn = "<a id='"+treeNode.id+"' class='addBtn btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0px;' href='#' title='添加子节点'>&nbsp;&nbsp;<i class='fa fa-fw fa-plus rbg '></i></a>";
    var removeBtn = "<a id='"+treeNode.id+"' class='removeBtn btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0px;' href='#' title='删除节点'>&nbsp;&nbsp;<i class='fa fa-fw fa-times rbg '></i></a>";
    var editBtn = "<a id='"+treeNode.id+"' class='editBtn btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0px;' href='#' title='编辑节点'>&nbsp;&nbsp;<i class='fa fa-fw fa-edit rbg '></i></a>";

    // 获取当前节点的级别数据
    var level = treeNode.level;

    // 声明变量存储拼装好的按钮代码
    var btnHTML = "";

    // 判断当前节点的级别
    // 级别为0时时根节点，只能添加子节点
    if(level==0){
        btnHTML = btnHTML + addBtn;
    }

    // 级别为1时是分支节点，分支节点可以增加叶子节点或修改或删除，但是只有当叶子节点数量为0时才能删除
    if(level==1){
        btnHTML = addBtn + " " + editBtn;
        var length = treeNode.children.length; // 获取子节点的数量
        if(length == 0){
            btnHTML = btnHTML + " " + removeBtn;
        }
    }

    // 级别为2时是叶子节点，只能修改或删除
    if(level==2){
        btnHTML = editBtn + " " + removeBtn;
    }

    //执行在超链接后面附加span元素的操作
    $("#"+anchorId).after("<span id='"+btnGroupId+"'>"+btnHTML+"</span>");
}

// 在鼠标移入节点范围时删除按钮组
function myRemoveHoverDom(treeId, treeNode){
    // 拼接按钮组的id
    var btnGroupId = treeNode.tId +"_btnGrp";

    // 移除对应的元素
    $("#"+btnGroupId).remove();
}