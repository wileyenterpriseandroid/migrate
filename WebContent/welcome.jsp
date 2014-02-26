<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" session="false"%>
<head>
    <style type="text/css">
        .divTable
        {
            display:  table;
            width:auto;
            background-color:#eee;
            border:1px solid  #666666;
            border-spacing:5px;/*cellspacing:poor IE support for  this*/
            /* border-collapse:separate;*/
        }

        .divRow
        {
            display:table-row;
            width:auto;

        }
        .divCell
        {
            float:left;/*fix for  buggy browsers*/
            display:table-column;
            width:200px;
            background-color:#ccc;

        }

    </style>
    <title>Login Page</title>
</head>
<body>
<%--<form action="./${contact.id}" method="post">--%>
<%--Name: <input type="text" name="name" value="${contact.name}"/><br />--%>
<%--Address:<br /> <textarea  name="address" cols="100" rows="4">${contact.address}</textarea><br /><br />--%>
<%--<input type="submit" name="update" value="update" />--%>
<%--<input type="submit" name="delete" value="delete" />--%>
<%--</form>--%>
<%--<a href="${pageContext.request.contextPath}/contacts">list contacts</a>--%>
<h3>Welcome!</h3>
Get started!
</form>

</body>
</html>
