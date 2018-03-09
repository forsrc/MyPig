/**
 * Defines the main routes in the application.
 * The routes you see here will be anchors '#/' unless specifically configured otherwise.
 */

define(['angular', './app', 'services/DependencyResolverFor'], function (angular, app, dependencyResolverFor) {
    'use strict';
    return app.config([
        '$routeProvider',
        '$locationProvider',
        '$controllerProvider',
        '$compileProvider',
        '$filterProvider',
        '$provide',
        '$mdThemingProvider',
        '$stateProvider',
        '$requireProvider', function (
                $routeProvider,
                $locationProvider,
                $controllerProvider,
                $compileProvider,
                $filterProvider,
                $provide,
                $mdThemingProvider,
                $stateProvider,
                $requireProvider) {

            app.controller = $controllerProvider.register;
            app.directive  = $compileProvider.directive;
            app.filter     = $filterProvider.register;
            app.factory    = $provide.factory;
            app.service    = $provide.service;

            $mdThemingProvider
                .theme('default')
                .primaryPalette('red')
                .accentPalette('blue');

            $routeProvider.otherwise({
                redirectTo: '/login'
            });


           $routeProvider.when('/login', {
                templateUrl : 'app/views/login.html',
                resolve : dependencyResolverFor([
                    'controllers/LoginController'
                ])
            });

            // $routeProvider.when('/login', {
            //     templateUrl : 'app/views/login.html',
            //     controller : 'LoginCtrl'
            // });

//        $stateProvider.state("/login", {
//            url : "/login",
//            templateUrl : "app/views/login.html",
//            title : "Login",
//            controller : "loginController",
//            //controllerAs: "LoginCtrl",
//            resolve : {
////                 css: $requireProvider.requireCSS([
////                     'css!css/login.css'
////                 ]),
//                deps : $requireProvider.requireJS([
//                        'controllers/LoginController'
//                    ])
//            }
//        })
    }]);

});
