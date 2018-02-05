

```shell
curl --request POST -u forsrc:forsrc "http://localhost:10000/sso/oauth/token?grant_type=password&username=forsrc&password=forsrc"

{"access_token":"1ea2b93d-1b06-4c40-9436-5323d0f11723","token_type":"bearer","refresh_token":"58416736-bac8-4f3c-a7b4-838e9bfcc165","expires_in":3571,"scope":"read write"}
```

```shell
curl --request GET "http://forsrc.local:10000/sso/me?access_token=1ea2b93d-1b06-4c40-9436-5323d0f11723"

{"authorities":[{"authority":"ROLE_ACTUATOR"},{"authority":"ROLE_ADMIN"},{"authority":"ROLE_USER"}],"details": ...}
```

