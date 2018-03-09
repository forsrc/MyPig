define(['app'], function (app) {
    'use strict';
    console.log("--> LoginService...")
    var jsName = "LoginService";
    return app.service("loginService", function () {
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
