![plantuml](http://www.plantuml.com/plantuml/png/fLH1JyCW6BtpAxwZ1hU6UbMprInUTbYmEtXaj1fc10EaneZ_NTOcwxf2H-AxKHxl-nxVemjZcRQLi8mX104uCeUTKk-b5fyG1PwT00U56aY16opGH9Mk593vBMos9MHSoLUXF-2AaXkQNpieDPE6SIkKF1ToK7XBGhx1HYjU6mDk5i63WqUbT2KaiqgFXNhmOhqzC_0_Qx6TLeW_qHNyg5-79YkvIFsqgQKAXPtXaPUWdU5-GCT7a00bzzck6rgsZ7NfcVc0MPpgDCdPQsBzu4jTzpenQglZei4OVOzHUvtdB-4Jx1TtuGZZuz6QcUzzRGTK7Eh7lof_vuTtHSbaZ0_4l2FcoSPYxw4BzijgtGRxyk4ZDNaqmTmZjAXbrVu3_m00)


![tcc plantuml](http://www.plantuml.com/plantuml/png/fPP1Zvim48Nl_8hHdeWeMARNT1M8SWffeWtfgfT8hkDchIY0mCrblV-zYhVKoBWTjNov8FpqF1u_comwnLfraegn0CO4u47J17vq0XJd_d7uBw9uGEHrle6ViEIyaDM5VlZraCx-M90Et6dzH42JZx3RxaiWYdF26abkr3mEdSlbqdMsWuTEM85z7-CyQjYldhwidlpEC8RdnG7h_8Hkc244uyntdzVmMhzPPulsLw4dP6EYixEJptjOR4JNiRFOiAQHrJby8EezK-IrOUhbxM4MZAkAq0v6sFH7E0Q34qQXp_GdSfMyhJlHtYGN97ENIvEqrwQEDWVGzr-Qf0crxYRKU49mxRitJTB0FQ6eDwurbGVZZaQ8CQQtnjfvZnXZ-g7nPh0DeET8931zP8qpi9Zcm-vGWXCHmklgksole25tzAb7d_Twl2bZKBsjVPenKNKZ-U_JcC_ZC2iAw9UwNSy6i_2lgyBSGe3WwiTRXQVGoqZ_BBvrDRy81M4-h3f-9AnfxBgSs-ruFs0K_WrpuC7Y_OZOWYnXzfVICh3Wtv3fk1Uzau3Cau25g4xctynF)

* tcc
    * TRY:     INSERT TCC_RESERVATION
    * CONFIRM: UPDATE TCC_RESERVATION SET STATUS=‘CONFIRMED’
    * CANCEL:  UPDATE TCC_RESERVATION SET STATUS=‘CANCELED’


| TCC Status             | code |
|------------------------|------|
| TRY(0)                 | 0    |
| CONFIRM(1)             | 1    |
| CANCEL(2)              | 2    |
| TRY_ERROR(3)           | 3    |
| ERROR(4)               | 4    |
| CONFIRM_ERROR(5)       | 5    |
| CANCEL_ERROR(6)        | 6    |
| ALREADY_CONFIRMED(7)   | 7    |
| ALREADY_CANCELED(8)    | 8    |
| TCC_NOT_FOUND(9)       | 9    |
| TCC_LINK_NOT_FOUND(10) | 10   |

```json
curl --request POST -u forsrc:forsrc "http://localhost:10000/sso/oauth/token?grant_type=password&username=forsrc@gmail.com&password=forsrc"
--> {"access_token":"651cafd3-8f22-4a16-9d28-8079354aeb84","token_type":"bearer","refresh_token":"c60f4ea3-0ec5-49cc-a322-6c5ac8c86c38","expires_in":2273,"scope":"read write"}

curl -H "Content-Type: application/json" --request POST "http://localhost:10000/sso/api/v1/tcc/user/?access_token=651cafd3-8f22-4a16-9d28-8079354aeb84"  -d '
{ "username": "A", "password": "a", "enabled": 1, "authorities": "ROLE_A, ROLE_TEST", "expire": "2018-02-14T12:00:00.000Z" }
'
--> {"id":"9f15305a-00b0-44a7-a9ac-861ebb19158d","username":"A","password":"a","enabled":1,"authorities":"ROLE_A, ROLE_TEST","status":0,"create":null,"update":null,"expire":"2018-02-14T12:00:00.000Z"}

curl -H "Content-Type: application/json" --request POST "http://localhost:10020/tcc/api/v1/?access_token=651cafd3-8f22-4a16-9d28-8079354aeb84"  -d '
{
  "expire": "2018-02-14T12:00:00.000Z",
  "links":[{
    "entityId": "9f15305a-00b0-44a7-a9ac-861ebb19158d",
    "uri": "http://SPRINGBOOT-SSO-SERVER/sso/api/v1/tcc/user/",
    "expire": "2018-02-14T12:00:00.000Z"
  }]
}
'
--> {"id":"e972b9bd-4f78-4dd6-82e5-0974ae77f34b","create":null,"update":null,"expire":"2018-02-14T12:00:00.000Z","status":0,"links":[{"id":"a81f331a-df4e-4107-a409-d911c41ed523","tccId":"e972b9bd-4f78-4dd6-82e5-0974ae77f34b","entityId":"9f15305a-00b0-44a7-a9ac-861ebb19158d","uri":"http://localhost:10000/sso/api/v1/tcc/user/","create":null,"update":null,"expire":null,"status":null}]}

curl -H "Content-Type: application/json" --request GET "http://localhost:10020/tcc/api/v1/e972b9bd-4f78-4dd6-82e5-0974ae77f34b/?access_token=651cafd3-8f22-4a16-9d28-8079354aeb84"
--> {"id":"e972b9bd-4f78-4dd6-82e5-0974ae77f34b","create":"2018-02-14T02:48:51.264Z","update":"2018-02-14T02:48:51.264Z","expire":"2018-02-14T12:00:00.000Z","status":null,"links":[{"id":"a81f331a-df4e-4107-a409-d911c41ed523","tccId":"e972b9bd-4f78-4dd6-82e5-0974ae77f34b","entityId":"9f15305a-00b0-44a7-a9ac-861ebb19158d","uri":"http://localhost:10000/sso/api/v1/tcc/user/","create":"2018-02-14T02:48:51.264Z","update":"2018-02-14T02:48:51.264Z","expire":null,"status":null}]}

curl -H "Content-Type: application/json" --request PUT "http://localhost:10020/tcc/api/v1/confirm/e972b9bd-4f78-4dd6-82e5-0974ae77f34b/?access_token=651cafd3-8f22-4a16-9d28-8079354aeb84"

curl -H "Content-Type: application/json" --request DELETE "http://localhost:10020/tcc/api/v1/cancel/e972b9bd-4f78-4dd6-82e5-0974ae77f34b/?access_token=651cafd3-8f22-4a16-9d28-8079354aeb84"

```
