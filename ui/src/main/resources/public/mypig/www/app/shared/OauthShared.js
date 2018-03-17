define(['./module'], function (services) {
    'use strict';
    console.log("--> version-service...")
    return services.service("$_shared", function() {
            this.info = {
                title: "MyPig",
                auth: "forsrc"
            };
            this.auth = {
                access_token: null,
                token_type: "bearer",
                refresh_token: null,
                expires_in: 0,
                scope:"read write",
                jti: null
            };
        });
});
