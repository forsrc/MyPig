define(['./module'], function (filters) {
    'use strict';

    return filters.filter('versionFilter', ['version', function (version) {
        return function (text) {
            return String(text).replace(/\%VERSION\%/mg, version);
        }
    }]);
});
