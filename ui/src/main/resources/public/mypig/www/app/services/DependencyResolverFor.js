define([], function() {
    console.log("--> DependencyResolverFor...")
    return function(dependencies) {

        var definition = {
            resolver : [ '$q', '$rootScope', function($q, $rootScope) {
                var deferred = $q.defer();
                console.log("--> DependencyResolverFor -> resolver:", dependencies)
                require(dependencies, function() {
                    $rootScope.$apply(function() {
                        deferred.resolve();
                    });
                });

                return deferred.promise;
            } ]
        }

        return definition;
    }
});
