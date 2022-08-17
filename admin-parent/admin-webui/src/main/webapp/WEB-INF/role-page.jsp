<%--
  Created by IntelliJ IDEA.
  User: CONAN
  Date: 2022/8/13
  Time: 1:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<!DOCTYPE html>
<html lang="zh-CN">
<%@include file="/WEB-INF/include-head.jsp" %>
<link rel="stylesheet" href="css/pagination.css"/>
<script type="text/javascript" src="jquery/jquery.pagination.js"></script>
<script type="text/javascript" src="crowd/my-role.js" charset="UTF-8"></script>
<script type="text/javascript">
    $(function(){
        // 1.为分页操作准备初始化数据
        window.pageNum = 1;
        window.pageSize = 5;
        window.keyword = "";

        //调用执行分页的函数
        generatePage();

        //绑定查询按钮事件
        $("#searchBtn").click(function(){
            // 获取查询框的输入值，并设置window.keyword
            window.keyword=$("#keywordInput").val();
            // 调用generatePage()生成页面
            generatePage();
        });

        //点击新增按钮打开模态框
        $("#showAddModalBtn").click(function(){
            $("#addModal").modal("show");
        });

        // 给新增模态框中的保存按钮绑定单击响应函数
        $("#saveRoleBtn").click(function(){
            // 1.获取用户在文本框输入的角色名称
            var roleName = $.trim($("#addModal [name=roleName]").val());

            // 2.发送ajax请求
            $.ajax({
                "url": "role/save.json",
                "type":"post",
                "dataType":"json",
                "data":{"name":roleName},
                "success":function(response){
                    var result = response.result;

                    if(result == "SUCCESS"){
                        layer.msg("操作成功！");

                        //重新加载分页数据
                        window.pageNum = 999999;
                        generatePage();
                    }

                    if(result == "FAILED"){
                        layer.msg("操作失败！"+response.message);
                    }
                },
                "error":function(response){
                    layer.msg(response.status+" "+response.statusText);
                }
            });

            //关闭模态框
            $("#addModal").modal("hide");
            //清理模态框
            $("#addModal [name=roleName]").val("");
        });

        // 给每一行动态生成的数据的编辑按钮绑定点击事件
        $("#rolePageBody").on("click", ".btn-primary", function(){
            $("#editModal").modal("show");

            window.roleId = this.id;

            // 使用roleName的值设置模态框中的文本框
            var roleName = $(this).parent().prev().text();
            $("#editModal [name=roleName]").val(roleName);
        });

        // 给更新模态框中的更新按钮绑定单击响应函数
        $("#updateRoleBtn").click(function(){

            // 获取新的角色名称
            var role = $("#editModal [name=roleName]").val();
            $.ajax({
                "url":"role/update.json",
                "type":"post",
                "dataType":"json",
                "data":{
                    "id":window.roleId,
                    "name":role,
                },
                "success":function(response){
                    var result = response.result;
                    if(result == "SUCCESS"){
                        layer.msg("操作成功！");

                        //将页码定位到最后一页
                        window.pageNum = 999999;

                        //重新加载分页数据
                        generatePage();
                    }

                    if(result == "FAILED"){
                        layer.msg("操作失败！"+result.message);
                    }
                },
                "error":function(response){
                    layer.msg(response.status+" "+response.statusText);
                }
            });

            $("#editModal").modal("hide");
        });
    });
</script>

<body>
<%@include file="/WEB-INF/include-nav.jsp" %>
<div class="container-fluid">
    <div class="row">
        <%@include file="/WEB-INF/include-sidebar.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title"><i class="glyphicon glyphicon-th"></i> 数据列表</h3>
                </div>
                <div class="panel-body">
                    <form class="form-inline" role="form" style="float:left;">
                        <div class="form-group has-feedback">
                            <div class="input-group">
                                <div class="input-group-addon">查询条件</div>
                                <input id="keywordInput" class="form-control has-success" type="text" placeholder="请输入查询条件">
                            </div>
                        </div>
                        <button id="searchBtn" type="button" class="btn btn-warning"><i class="glyphicon glyphicon-search"></i> 查询</button>
                    </form>
                    <button type="button" class="btn btn-danger" style="float:right;margin-left:10px;"><i class=" glyphicon glyphicon-remove"></i> 删除</button>
                    <button type="button" id="showAddModalBtn" class="btn btn-primary" style="float:right;"><i class="glyphicon glyphicon-plus"></i> 新增</button>
                    <br>
                    <hr style="clear:both;">
                    <div class="table-responsive">
                        <table class="table  table-bordered">
                            <thead>
                            <tr>
                                <th width="30">#</th>
                                <th width="30"><input type="checkbox"></th>
                                <th>名称</th>
                                <th width="100">操作</th>
                            </tr>
                            </thead>
                            <tbody id="rolePageBody">
                            </tbody>
                            <tfoot>
                            <tr>
                                <td colspan="6" align="center">
                                    <div id="Pagination" class="pagination"><!-- 这里显示分页 --></div>
                                </td>
                            </tr>
                            </tfoot>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<%@include file="/WEB-INF/modal-role-add.jsp"%>
<%@include file="/WEB-INF/modal-role-edit.jsp"%>
</body>
</html>