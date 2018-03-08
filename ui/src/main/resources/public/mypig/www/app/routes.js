/**
 * Defines the main routes in the application.
 * The routes you see here will be anchors '#/' unless specifically configured otherwise.
 */

define(['./app', 'services/DependencyResolverFor'], function (app, dependencyResolverFor) {
    'use strict';
    return app.config(['$routeProvider', '$mdThemingProvider', '$stateProvider', '$requireProvider', function ($routeProvider, $mdThemingProvider, $stateProvider, $requireProvider) {
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
                    'controllers/login-controller'
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
//            controller : "LoginCtrl",
//            //controllerAs: "LoginCtrl",
//            resolve : {
////                 css: $requireProvider.requireCSS([
////                     'css!css/login.css'
////                 ]),
//                deps : $requireProvider.requireJS([
//                        'controllers/login-controller'
//                    ])
//            }
//        })
    }]);

});
