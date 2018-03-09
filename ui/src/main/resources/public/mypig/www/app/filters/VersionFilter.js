define(['./module'], function (filters) {
    'use strict';

    return filters.filter('versionFilter', ['version', function (version) {
        console.log("--> VersionFilter...", version)
        return function (text) {
            console.log("--> VersionFilter text", text)
            return String(text).replace(/\%VERSION\%/mg, version);
        }
    }]);
});
