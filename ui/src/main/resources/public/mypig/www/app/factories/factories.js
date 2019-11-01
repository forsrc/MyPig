define([ './module' ], function(factories) {
    'use strict';
    factories.factory('myFactory', function() {

        return {

            ping : function() {

                return "pong";
            }

        };

    });
});
