define(['./module'], function (directives) {
    'use strict';
    directives.directive('version', ['version', function (version) {
        console.log("--> VersionDirective...")
        return function (scope, elm) {
            elm.text(version);
        };
    }]);
});
