<%--
  Created by IntelliJ IDEA.
  User: 王祖文
  Date: 2020/5/28
  Time: 10:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>秒杀案例</title>
</head>
<body>
<form method="post">
    <input type="hidden" name="pid" value="1001">
    <a href="javascrip:;">点击参与一元秒杀iphone11promax</a>
    <div>

    </div>
</form>

<script src="script/jquery-2.1.1.min.js"></script>
<script>
    $("form a").click(function () {
        $.ajax({
            type:"post",
            url:"${pageContext.request.contextPath}/seckill",
            data:$("form").serialize(),
            success:function (result) {
                $("div").empty();
                $("div").append("<span style='color: red'>"+result+"</span>")
            }
        })
    })


</script>
</body>
</html>
