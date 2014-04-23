<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html ng-app="MigrateSchema">
<head>
    <script src="/migrate/angular-1.2.7/angular.min.js"></script>
    <script src="/migrate/global.js"></script>
    <script src="/migrate/manage/schema/SchemaManagerController.js"></script>

    <link rel="stylesheet" href="/migrate/migrate.css">
</head>

<body>
<h3>Migrate Dashboard</h3><br>
<div ng-controller="SchemaManagerController" ng-init="updateSchemas()">
    <form ng-submit="updateSchemas()">

        <span ng-if="schemas.length > 0">
            <ul class="unstyled">
                <li ng-repeat="schema in schemas">
                    <span>{{schema.wd_id}}</span>
                </li>
            </ul>
            </span>
        <input class="btn-primary" type="submit" value="Update">
    </form>

    <br>
    <br>
    <a href="/migrate/manage/schema/editor.html">Add schema</a>
</div>
</body>
</html>


<a href="<c:url value="/migrate/j_spring_security_logout" />" > Logout</a>
</body>
</html>

