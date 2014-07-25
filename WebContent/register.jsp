<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" session="false"%>
<html ng-app="MigrateUser">
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
<head>
    <script src="/angular-1.2.7/angular.min.js"></script>
    <script src="/global.js"></script>
    <script src="/RegisterController.js"></script>

    <link rel="stylesheet" href="migrate.css">
</head>

<body>

<h3>Register new migrate user:</h3>

<c:if test="${not empty param.auth_valid}">
    <div class="errorblock">
        Invalid credentials, try again.<br/>
    </div>
</c:if>

<div ng-controller="RegisterController">
    <form ng-submit="register()">
        <table>
            <tr>
                <td>Username:</td>
                <td><input type='text' ng-model='username'>
                </td>
            </tr>
            <tr>
                <td>Password:</td>
                <td><input type='text' ng-model='password' />
                </td>
            </tr>

            <tr>
                <td colspan='2'><input name="submit" type="submit" value="Register" />
                </td>
            </tr>

        </table>
    </form>
</div>

</body>
</html>
</body>
</html>
