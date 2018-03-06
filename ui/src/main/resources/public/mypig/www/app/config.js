"use strict";


require.config({
    paths: {
        "angular": "../lib/angular/angular",
        "angular-require": "../lib/require/angular-require",
        "angular-animate": "../lib/angular/angular-animate",
        "angular-aria": "../lib/angular/angular-aria",
        "angular-cookies": "../lib/angular/angular-cookies",
        "angular-loader": "../lib/angular/angular-loader",
        "angular-messages": "../lib/angular/angular-messages",
        "angular-message-format": "../lib/angular/angular-message-format",
        "angular-mocks": "../lib/angular/angular-mocks",
        "angular-parse-ext": "../lib/angular/angular-parse-ext",
        "angular-resource": "../lib/angular/angular-resource",
        "angular-route": "../lib/angular/angular-route",
        "angular-sanitize": "../lib/angular/angular-sanitize",
        //"angular-scenario": "../lib/angular/angular-scenario",
        "angular-touch": "../lib/angular/angular-touch",
        "angular-material": "../lib/angular-material/angular-material",
        "angular-material-icons": "../lib/angular-material/angular-material-icons",
        "ng-cordova": "../lib/ng/ng-cordova.min",
        "ngStorage": "../lib/ng/ngStorage.min",
        "angular-ui-router": "../lib/ng/angular-ui-router",
        "svg-assets-cache": "../lib/svg/svg-assets-cache",
        "ionic": "../lib/ionic/js/ionic",
        "ionic-angular": "../lib/ionic/js/ionic-angular",
        "console-min": "../lib/console/console-min",
        "console": "../lib/console/console",
        "order": "../lib/require/order",
        "css": "../lib/require/css",
        "text": "../lib/require/text",
        "md-data-table": "../lib/md/md-data-table",
        'domReady': '../lib/requirejs-domready/domReady'

    },
    shim: {
        angular: {
            exports: "angular"
        },
        "angular-require": {
            exports: "angular-require",
            deps: ["angular", "css"]
        },
        ionic: {
            exports: "ionic",
            deps: ["angular"]
        },
        "angular-animate": {
            deps: ["angular"]
        },
        "angular-sanitize": {
            deps: ["angular"]
        },
        "angular-aria": {
            deps: ["angular"]
        },
        "angular-messages": {
            deps: ["angular"]
        },
        "angular-message-format": {
            deps: ["angular"]
        },
        "angular-material": {
            deps: ["angular"]
        },
        "angular-material-icons": {
            deps: ["angular"]
        },
        "angular-route": {
            deps: ["angular"]
        },
        "ionic-angular": {
            deps: ["angular", "ionic"]
        },
        "ng-cordova": {
            deps: ["angular"]
        },
        "angular-ui-router": {
            deps: ["angular"]
        },
        "angular-cookies" : {
            deps: ["angular"]
        },
        "console": {
            deps: ["console-min", "order"]
        },
        "md-data-table": {
            deps: ["angular", "angular-material"]
        }
    },
    priority: [
        "angular"
    ],
    //baseUrl: '/',
    deps: [
        // kick start application... see bootstrap.js
        './bootstrap'
    ]
});