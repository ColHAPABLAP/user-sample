'use strict';

angular
    .module('app', [
        'ngRoute',
        'ui.bootstrap',
        'app.userView'
    ])
    .config(['$locationProvider', '$routeProvider', function ($locationProvider, $routeProvider) {
        // Behaviour change for hashbang since 1.6: https://github.com/angular/angular.js/commit/aa077e81129c740041438688dff2e8d20c3d7b52
        // use for old style hashbang (just '#')
        $locationProvider.hashPrefix('');

        // default routing
        $routeProvider.otherwise({redirectTo: '/user'});
    }]);