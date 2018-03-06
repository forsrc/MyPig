define(['./module'], function (directives) {
    'use strict';
    directives.directive('version', ['version', function (version) {
        console.log("--> version-directive...")
        return function (scope, elm) {
            elm.text(version);
        };
    }]);
});
