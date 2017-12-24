angular.module('hello', ['ngRoute']).config(function ($routeProvider, $httpProvider) {

    $routeProvider.when('/home', {
        templateUrl: 'home.html',
        controller: 'home',
        controllerAs: 'controller'
    }).when('/', {
        templateUrl: 'home.html',
        controller: 'home',
        controllerAs: 'controller'
    }).otherwise('/');

    $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
    $httpProvider.defaults.headers.common['Accept'] = 'application/json';

}).controller('navigation',

    function ($rootScope, $http, $location, $route) {

        var self = this;

        self.tab = function (route) {
            return $route.current && route === $route.current.controller;
        };
        $rootScope.authenticated = false;

        // $http.get('user').then(function (response) {
        //     if (response.data.name) {
        //         $rootScope.authenticated = true;
        //     } else {
        //         $rootScope.authenticated = false;
        //     }
        // }, function () {
        //     $rootScope.authenticated = false;
        // });

        self.credentials = {};

        self.logout = function () {
            $http.post('logout', {}).finally(function () {
                $rootScope.authenticated = false;
                $location.path("/");
            });
        }

    }).controller('home', function ($rootScope, $http) {
    var self = this;
    // $http.get('resource/').then(function (response) {
    //     $rootScope.authenticated = true;
    //     self.greeting = response.data;
    // });

    $http.get('other-resource/').then(function (response) {
        $rootScope.authenticated = true;
        self.greeting2 = response.data;
    })
});
