'use strict';

angular
    .module('app.userView')
    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider
            .when('/user', {
                templateUrl: 'userView/userView.html',
                controller: 'UserViewController',
                controllerAs: 'userView'
            });
    }]);
