define(['./module'], function (services) {
    'use strict';
    console.log("--> login-service...")
    var jsName = "login-service";
    return services.service("loginService",
            function () {
            console.log("{0} --> function()".formatStr([jsName]));
    
            this.login = function (user) {
                console.info(user);
                if(user && user.username === "forsrc" && user.password === "forsrc"){
                    return true;
                }
                return false;
            }
        }
    );
});
