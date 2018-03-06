define(['./module'], function (directives) {
    'use strict';
    directives.directive('version', ['version', function (version) {
        return function (scope, elm) {
            elm.text(version);
        };
    }]);
});
