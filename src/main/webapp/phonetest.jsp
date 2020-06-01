<%--
  Created by IntelliJ IDEA.
  User: 王祖文
  Date: 2020/5/27
  Time: 20:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>手机验证码功能</title>
</head>
<body>
<form style="margin:0 auto;width: 450px" action="${pageContext.request.contextPath}/phoneTest" method="post">
    手机号：<input type="text" name="phone">
    <input id="sendcode" type="button" value="发送验证码"><br>
    <div id="div1"></div>
    验证码:<input type="text" name="code">
    <input id="sub" type="button" value="确定">
    <div id="div2"></div>
</form>
<script src="script/jquery-2.1.1.min.js"></script>
<script>

    $("#sendcode").click(function () {
        var phone=$("form input[name='phone']").val();
        $.ajax({
            type:"post",
            url:"${pageContext.request.contextPath}/sendphone",
            data:"phone="+phone,
            success:function (result) {
                $("#div1").empty();
                $("#div1").append("<span style='color: red'>"+result+"</span>")
            }
        })
    })

    $("#sub").click(function () {
        var code=$("form input[name='code']").val();
        $.ajax({
            type:"post",
            url:"${pageContext.request.contextPath}/docode",
            data:"code="+code,
            success:function (result) {
                $("#div2").empty();
                $("#div2").append("<span style='color: red'>"+result+"</span>")
            }
        })
    })


</script>

</body>
</html>
