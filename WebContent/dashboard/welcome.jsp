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
<ul>
    <li>
        Install the migrate-client.apk into an Android handset or Emulator.
    </li>
    <li>
        Register a Migrate account using the web interface or with the Android settings app:<br>
        Settings -> Acounts -> Add Account -> webdata SyncAdapter -> Sign in or Register
    </li>
    <li>
        Turn on Sync:<br>
        Settings -> Accounts -> webata SyncAdapter -> Check the Sync Button
    </li>
</ul>

</p>

<p>
    Create schemas to describe application data. You can create the schema in this backend migrate
    application instance, or you can create an application API class and use the "PostSchema"
    utility in the migrate-sdk.
</p>

<p>
    Features in this version of Migrate:
</p>

<ul>
    <li>
        Web interface for managing schema:
        <ul>
            <li>
                Register users with login support
            </li>
            <li>
                Schema viewing dashboard
            </li>
            <li>
                Schema editing: Create and add fields to schema
            </li>
        </ul>
    </li>
    <li>
        Migrate service:
        <ul>
            <li>
                Multi-tenant support: Schema and data partitioned by user for sharing the same migrate service
            </li>
            <li>
                Scalable synchronization support with client side content providers: CRUD operations on single level
                data fields. Object reference not yet supported.
            </li>
        </ul>
        migrate-client.apk:
        <ul>
            <li>
                Multi-tenant support: Schema and data partitioned by user for sharing the same migrate service
            </li>
            <li>
                Conflict resolution
            </li>
            <li>
                Schema sync, Data sync, CRUD editing through content provider. Joins not currently supported.
            </li>
            <li>
                Log in and register
            </li>
        </ul>
        Migrate SDK:
        <ul>
            <li>
                Binary versions of migrate service: migrate.war, migrate-client.apk, migrate-browser.apk
            </li>
            <li>
                Ability to postSchema, see usage MigrateContacts example in github.com/wileyenterpriseandroid/Examples
            </li>

        </ul>

    </li>
    <li>
        Security:
        <ul>
            <li>
                Basic-auth to migrate-client content provider
            </li>
            <li>
                NO HTTPS! Yikes! I'll fix this asap.
            </li>

        </ul>
    </li>
</ul>

<a href="dashboard.jsp">Dashboard</a>
<br>
<p>
    <a href="<c:url value="/migrate/j_spring_security_logout" />" > Logout</a>
</p>
</body>
</html>
