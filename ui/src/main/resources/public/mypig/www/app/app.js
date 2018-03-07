/**
 * loads sub modules and wraps them up into the main module
 * this should be used for top-level module definitions only
 */
define([
    'angular',
    'console',
    './controllers/index',
    './directives/index',
    './filters/index',
    './services/index',
    './utils/index',
    'ionic',
    'ionic-angular',
    'ng-cordova',
    'angular-route',
    'angular-resource',
    'angular-animate',
    'angular-aria',
    'angular-sanitize',
    'angular-material',
    'angular-material-icons',
    'angular-messages',
    'angular-message-format',
    'angular-ui-router',
    'angular-require',
    'angular-cookies',
    'ngStorage',
    'svg-assets-cache'
], function (angular, _console) {
    'use strict';
    console.log('--> app...')

    var app =angular.module('app', [
        'ngResource',
        'app.controllers',
        'app.directives',
        'app.filters',
        'app.services',
        'ngRoute',
        'ionic',
        'ng',
        'ngRequire',
        'ui.router',
        'ngMaterial',
        'ngCordova',
        'ngStorage',
        'ngAnimate',
        'ngSanitize',
        'ngMdIcons',
        'ngAria',
        'ngMessages'
    ]);

    angular.element(window.document).ready(function() {
        console.log("app -> angular.element(document).ready()...");

        console.log("app -> app:", app);
        app.run(function($ionicPlatform) {
            console.log("app -> app.run():", $ionicPlatform);
            $ionicPlatform.ready(function() {
                console.log("aap -> $ionicPlatform.ready()...", $ionicPlatform);
                if (window.cordova && window.cordova.plugins && window.cordova.plugins.Keyboard) {
                    cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
                }
                if (window.StatusBar) {
                    // org.apache.cordova.statusbar required
                    StatusBar.styleDefault();
                }
            });
        });
        app.config(function($mdThemingProvider, $mdGestureProvider) {
            console.log("app -> app.config():", $mdThemingProvider, $mdGestureProvider);
            $mdGestureProvider.skipClickHijack();
            $mdThemingProvider.theme('default').primaryPalette('red').accentPalette('blue');
        });
    });

    return app;
});
