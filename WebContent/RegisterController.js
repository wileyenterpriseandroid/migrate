
angular.module('MigrateUser.service', []).factory('MigrateUserService', function($http, $location, $window) {
    var migrateBase = "http://" + $location.host() + ":" + $location.port();

    return {
        register: function (username, password) {
            var postUserUri = migrateBase + "/migrate/users?username=" + username + "&password=" + password;

            // return the promise directly.
            return $http.post(postUserUri)
                .then(function (result) {
                    return result.data;
                });
        },
        setLocation : function (url) {
            var newUrl = migrateBase + url;
            $window.location.href = newUrl;
        }
    }
});

angular.module('MigrateUser', ['MigrateUser.service']);

function RegisterController($scope, MigrateUserService) {
    $scope.register = function () {
        var username = $scope.username;
        var password = $scope.password;

        if (empty(username)) {
            alert("Missing username.");
            return;
        }
        if (empty(password)) {
            alert("Missing password.");
            return;
        }

        return MigrateUserService.register(username, password).
            then(function (result) {
                // resolve the promise as the data
                if (result == "false") {
                    alert("Invalid credentials or duplicate user.");
                } else {
                    var newUrl = '/migrate/welcome.jsp';
                    MigrateUserService.setLocation(newUrl);
                }

                return result;
            });
    };
}
