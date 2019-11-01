/**
 * bootstraps angular onto the window.document node
 * NOTE: the ng-app attribute should not be on the index.html when using ng.bootstrap
 */
define([
    'require',
    'angular',
    'console',
    'app',
    'routes'
], function (require, angular, _console, app, routes) {
    'use strict';

    /*
     * place operations that need to initialize prior to app start here
     * using the `run` function on the top-level module
     */

    require(['domReady!'], function (document) {
        _console.group("bootstarap");
        _console.time("bootstarap");
        _console.debug("--> bootstarap...")

        angular.bootstrap(document, ['app']);

        _console.timeEnd("bootstarap");
        _console.groupEnd();
    });
});
