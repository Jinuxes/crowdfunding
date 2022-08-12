<%--
  Created by IntelliJ IDEA.
  User: CONAN
  Date: 2022/8/7
  Time: 21:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>$Title$</title>
    <base href="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/"/>
    <script type="text/javascript" src="jquery/jquery-3.6.0.min.js"></script>
    <script type="text/javascript" src="layer/layer.js"></script>
    <script type="text/javascript">
      $(function(){
        $("#btn1").click(function(){

          //准备要发送的数据
          var adminObject = {
            "id":100086,
            "userName":"Jim",
            "email":"Jim@10088.com"
          }

          //将JSON对象转换为JSON字符串
          var requestBody = JSON.stringify(adminObject);

          $.ajax({
            "url":"test/resultEntity.json",
            "type":"post",
            "data":requestBody,
            "contentType":"application/json;charset=UTF-8",
            "dataType":"json",
            "success":function(response){
              console.log(response);
            },
            "error":function(response){
              console.log(response);
            }
          });
        });

        $("#btn2").click(function(){
          alert("alert弹框！");
          layer.msg("layer弹框！");
        })
      });
    </script>
  </head>
  <body>
    <a href="test/ssm.html">测试SSM整合环境</a>

    <button id="btn1">测试ResultEntity</button>
    <br/>
    <button id="btn2">点我弹框</button>
  </body>
</html>
