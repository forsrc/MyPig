/**
 * loads sub modules and wraps them up into the main module
 * this should be used for top-level module definitions only
 */
define([
    'angular',
    './controllers/index',
    './directives/index',
    './filters/index',
    './services/index',
    'ionic',
    'ionic-angular',
    'ng-cordova',
    'console',
    'angular-route',
    'angular-resource',
    'angular-animate',
    'angular-aria',
    'angular-sanitize',
    'angular-material',
    'angular-material-icons',
    'angular-ui-router',
    'angular-material-icons',
    'angular-material',
    'angular-material-icons',
    'angular-messages',
    'angular-message-format',
    'angular-require',
    'angular-cookies',
    'ngStorage',
    'svg-assets-cache'
], function (angular) {
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

    return app;
});
