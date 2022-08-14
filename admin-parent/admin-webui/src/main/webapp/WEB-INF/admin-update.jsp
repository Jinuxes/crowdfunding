<%--
  Created by IntelliJ IDEA.
  User: CONAN
  Date: 2022/8/13
  Time: 1:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="zh-CN">
<%@include file="/WEB-INF/include-head.jsp" %>

<body>

<%@include file="/WEB-INF/include-nav.jsp" %>
<div class="container-fluid">
    <div class="row">
        <%@include file="/WEB-INF/include-sidebar.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <ol class="breadcrumb">
                <li><a href="admin/to/main/page.html">首页</a></li>
                <li><a href="admin/get/page.html">数据列表</a></li>
                <li class="active">修改</li>
            </ol>
            <div class="panel panel-default">
                <div class="panel-heading">表单数据<div style="float:right;cursor:pointer;" data-toggle="modal" data-target="#myModal"><i class="glyphicon glyphicon-question-sign"></i></div></div>
                <div class="panel-body">
                    <form action="admin/update.html" method="post" role="form">
                        <input type="hidden" name="id" value="${requestScope.updateAdmin.id}"/>
                        <input type="hidden" name="pageNum" value="${param.pageNum}"/>
                        <input type="hidden" name="keyword" value="${param.keyword}"/>
                        <p>${requestScope.exception.message}</p>
                        <div class="form-group">
                            <label for="update_acct">登陆账号</label>
                            <input type="text" class="form-control" name="loginAcct" id="update_acct" value="${requestScope.updateAdmin.loginAcct}">
                        </div>
                        <div class="form-group">
                            <label for="update_name">用户名称</label>
                            <input type="text" class="form-control" name="userName" id="update_name" value="${requestScope.updateAdmin.userName}">
                        </div>
                        <div class="form-group">
                            <label for="update_email">邮箱地址</label>
                            <input type="email" class="form-control" name="email" id="update_email" value="${requestScope.updateAdmin.email}">
                            <p class="help-block label label-warning">请输入合法的邮箱地址, 格式为： xxxx@xxxx.com</p>
                        </div>
                        <button type="submit" class="btn btn-success"><i class="glyphicon glyphicon-edit"></i> 修改</button>
                        <button type="reset" class="btn btn-danger"><i class="glyphicon glyphicon-refresh"></i> 重置</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>