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
        .beta
        {
            color: #ff0000;
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
<h3>Welcome to Project-Migrate <span class="beta">Beta</span>.</h3>

<p>
    Welcome to the Beta version of Project-Migrate, an experimental open source project
    that provides the main example for the book,
    "<a href="http://www.amazon.com/Enterprise-Android-Programming-Database-Applications-ebook/dp/B00FX89KXM/ref=sr_1_1?ie=UTF8&qid=1398021351&sr=8-1&keywords=enterprise+android">
    Enterprise Android"</a>. Project-Migrate simplifies management of data for Android applications.
</p>

<p>
    Follow the steps below to create a Migrate application:
</p>

<p>
    Install the migrate-client.apk into an Android handset or Emulator.
    Log into or register a Migrate account using the Account settings area in
    Android settings app.<br>
</p>

<p>
    Create a schema to describe application data. You can create the schema in this backend migrate
    application instance, or you can create an application API class and use the "PostSchema"
    utility in migrate-sdk.
</p>

<p>
    Features in this version of Migrate:
</p>

<ul>
    <li>
        Rudimentary ability to create and add to schema
    </li>
    <li>
        Multi-tenant support
    </li>
    <li>
        Basic-auth to migrate-client content provider
    </li>
</ul>

<p>
    Get started working with Migrate!
</p>

<a href="dashboard.jsp">Dashboard</a>
<br>
<p>
    <a href="<c:url value="j_spring_security_logout" />" > Logout</a>
</p>
</body>
</html>
