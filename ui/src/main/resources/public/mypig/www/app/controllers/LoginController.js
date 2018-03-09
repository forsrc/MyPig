define(['app', '../services/LoginService'], function (app, loginService) {
    'use strict';
    console.log("--> LoginController...", app, loginService)
    var jsName = "LoginController";
    app.controller('loginController', function ($scope, $http, $location, $httpParamSerializer, $mdToast) {
        console.log("--> LoginController...")

        console.log("{0} --> function()".formatStr([jsName]), $scope);
        $scope.user = {
            username: "forsrc",
            password: "forsrc"
        };
        $scope.message = 'hello world!';
        // $scope.login = function () {
        //     $scope.dataLoading = true;
        //     console.info($scope.user);
        // };
        var toast = $mdToast.simple()
          .textContent('Welcome {0}'.formatStr([$scope.user.username]))
          .action('OK')
          .highlightAction(true)
          .highlightClass('md-accent')// Accent is used by default, this just demonstrates the usage.
          .position("right");
        $scope.toLogin = function () {
            $mdToast.show(toast).then(function(response) {
                if (response === 'ok') {
                     console.debug("{0} --> toLogin() toast:".formatStr([jsName]), toast, response);
                }
            });
            var success = loginService.login($scope.user);
            if (success) {
                //$location.path("/menu");
            }
            $scope.dataLoading = true;
            $scope.encoded = "en_US";
            var req = {
                method: 'POST',
                //url: "app/data/login.jsonp",
                url: "https://forsrc.local:10000/sso/oauth/token",
                headers: {
                    "Authorization": "Basic " + btoa("forsrc:forsrc"),
                    "Content-type": "application/x-www-form-urlencoded; charset=utf-8"
                },
                //data: $httpParamSerializer($scope.user)
                data: $httpParamSerializer({
                    grant_type: "password",
                    client_id: "forsrc",
                    //client_secret: "forsrc",
                    username: $scope.user.username,
                    password: $scope.user.password
                })
            };
            $http(req).then(function(response) {
                console.log("{0} --> toLogin() response:".formatStr([jsName]), response);
                console.log("{0} --> toLogin() response:".formatStr([jsName]), response.status, response.data);
                if(response.status === 200){
                    //access_token:"eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE0OTAxNzAxNTksInVzZXJfbmFtZSI6ImZvcnNyYyIsImF1dGhvcml0aWVzIjpbIlJPTEVfQURNSU4iLCJST0xFX0FDVFVBVE9SIl0sImp0aSI6IjBlYzZjZGU0LTM3ZmUtNDBmZC05N2M0LTRhYTkzM2JlNGNlMiIsImNsaWVudF9pZCI6ImZvcnNyYyIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdfQ.OniyjMgAvqwQDqoRSnjim_vPss_7Nl2Ur3qswb-f9rScFrVtpfKfW2hv8lxU5mPhV8QIJD_MkuzgIl79U34pr9vUd9v7Q6mJ8v0KMoUu-Aq_yJTDPKRvt6zvRJw5xR9u5-P5xkEmuSNry1pxkWaXcJtQ9yV6a_uDjdA6KG8mlNAGnkJ7SFL6Z2VIYJq8JXAEMo5zISiE_EjpMi2M3bX3E6103h8GF6wrxeKtNdfLaWRYE7-L0_pV2yAp189zgiXYriolPuDxtifzSu6bc22IuH5U79qESy_c2VYq_DiTzvSa2oXL32Iut3X6tuOSOyp4GlWwgDBaLkA_lUtIbMoiGA"
                    //token_type:"bearer"
                    //refresh_token:"eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJmb3JzcmMiLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXSwiYXRpIjoiMGVjNmNkZTQtMzdmZS00MGZkLTk3YzQtNGFhOTMzYmU0Y2UyIiwiZXhwIjoxNDkyNzYwMzU5LCJhdXRob3JpdGllcyI6WyJST0xFX0FETUlOIiwiUk9MRV9BQ1RVQVRPUiJdLCJqdGkiOiJlZTYxYmUxNi05ZTUyLTQwMTgtYjBjNS0wMGYyNWUxOTFiNjUiLCJjbGllbnRfaWQiOiJmb3JzcmMifQ.MoLK_1IeXC2OFEhN-zQiEfRwkLUV3RYRlR1QQEjpcIrvawJqL0-0Gx6Hf5-az-OBDmmPjja1u6NQeVbNF3IsUZEYrMWNwLsRk30QyQImFRnZtFPxrMgZjzp3mFwcFVt2YxYP-ryTBDGupBr1sFYonOxxnVoMgBx2vJN4h9sT0xze1PY8kJikRcuJEUivS9c4KEy6-8U_4cbr0Vc-QMMEsa0MNDYCbDnOHyR-dCBvAEarG5KTXkwzJdexsynIymTHnoVcuapz6uURNHgyB-yRirWr4bsy4_ARu8eDXJix6i1BAHx4xvnrraRYFFmmzVU__YAVtB7DWB6bKi1P9emPyw"
                    //expires_in:1799
                    //scope:"read write"
                    //jti:"0ec6cde4-37fe-40fd-97c4-4aa933be4ce2"
                    $_shared.auth.access_token = response.data.access_token;
                    $_shared.auth.token_type = response.data.token_type;
                    $_shared.auth.refresh_token = response.data.refresh_token;
                    $_shared.auth.expires_in = response.data.expires_in;
                    $_shared.auth.scope = response.data.scope;
                    $_shared.auth.jti = response.data.jti;
                    console.debug("{0} --> toLogin() response:".formatStr([jsName]), $_shared.auth);
                    //$location.path("/menu/home1");
                }
                $location.path("/menu/home1");
            }, function(response) {
                console.debug("{0} --> toLogin() error".formatStr([jsName]), response);
            });
            //$scope.$apply();
        };
        
    });
});
