/**
 * bootstraps angular onto the window.document node
 * NOTE: the ng-app attribute should not be on the index.html when using ng.bootstrap
 */
define([
    'require',
    'angular',
    'ionic',
    'ionic-angular',
    'ng-cordova',
    'console',
    'app',
    'routes'
], function (require, angular, ionic, ionicAngular, ngCordova, console, app, routes) {
    'use strict';

    /*
     * place operations that need to initialize prior to app start here
     * using the `run` function on the top-level module
     */

    require(['domReady!'], function (document) {
        console.group("bootstarap");
        console.time("bootstarap");
        console.debug("--> bootstarap...")
        var _console = console;
        angular.element(window.document).ready(function() {
            _console.debug("angular.element(document).ready()");
            var angularModule = app;
            _console.debug("angularModule:", angularModule);
            angularModule.run(function($ionicPlatform) {
                console.log("angularModule.run():", $ionicPlatform);
                $ionicPlatform.ready(function() {
                    // Hide the accessory bar by default (remove this to show the accessory bar above the keyboard
                    // for form inputs)
                    if (window.cordova && window.cordova.plugins && window.cordova.plugins.Keyboard) {
                        cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
                    }
                    if (window.StatusBar) {
                        // org.apache.cordova.statusbar required
                        StatusBar.styleDefault();
                    }
                });
            });
            angularModule.config(function($mdThemingProvider, $mdGestureProvider) {
                console.log("app.config()", $mdThemingProvider, $mdGestureProvider);
                $mdGestureProvider.skipClickHijack();
                $mdThemingProvider.theme('default').primaryPalette('red').accentPalette('blue');
            });
        });
        angular.bootstrap(document, ['app']);

 
        console.timeEnd("bootstarap");
        console.groupEnd();
    });
});
