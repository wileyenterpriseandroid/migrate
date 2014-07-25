<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" session="false"%>
<html>
<head>
    <title>Login Page</title>
    <style>
        .errorblock {
            color: #ff0000;
            background-color: #ffEEEE;
            border: 3px solid #ff0000;
            padding: 8px;
            margin: 16px;
        }
    </style>
</head>
<body onload='document.f.j_username.focus();'>
<h3>Migrate Login:</h3>

<c:if test="${not empty param.auth_valid}">
    <div class="errorblock">
        Invalid credentials, try again.<br/>
    </div>
</c:if>

<form name='f' action='/j_spring_security_check' method='POST'>
    <table>
        <tr>
            <td>User:</td>
            <td><input type='text' name='j_username' value=''>
            </td>
        </tr>
        <tr>
            <td>Password:</td>
            <td><input type='password' name='j_password' />
            </td>
        </tr>
        <tr>
            <td colspan='2'><input name="submit" type="submit" value="Submit" />
            </td>
        </tr>
    </table>
</form>

<a href="/register.jsp">Register</a>

</body>
</html>
</body>
</html>
