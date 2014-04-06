<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" session="false"%>
<html>
<head>
    <title>Register User</title>
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
<h3>Register new migrate user:</h3>

<c:if test="${not empty param.auth_valid}">
    <div class="errorblock">
        Invalid credentials, try again.<br/>
    </div>
</c:if>

<form name='f' action='/migrate/users' method='POST'>
    <table>
        <tr>
            <td>Username:</td>
            <td><input type='text' name='username'>
            </td>
        </tr>
        <tr>
            <td>Password:</td>
            <td><input type='text' name='password' />
            </td>
        </tr>
        <tr>
            <td>Roles:</td>
            <td>
                <select name="roles">
                    <option value="ROLE_USER">User</option>
                </select>
            </td>
        </tr>

        <tr>
            <td colspan='2'><input name="submit" type="submit" value="Register" />
            </td>
        </tr>

    </table>
</form>

</body>
</html>
</body>
</html>
