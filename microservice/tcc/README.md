![plantuml](http://www.plantuml.com/plantuml/png/fLH1JyCW6BtpAxwZ1hU6UbMprInUTbYmEtXaj1fc10EaneZ_NTOcwxf2H-AxKHxl-nxVemjZcRQLi8mX104uCeUTKk-b5fyG1PwT00U56aY16opGH9Mk593vBMos9MHSoLUXF-2AaXkQNpieDPE6SIkKF1ToK7XBGhx1HYjU6mDk5i63WqUbT2KaiqgFXNhmOhqzC_0_Qx6TLeW_qHNyg5-79YkvIFsqgQKAXPtXaPUWdU5-GCT7a00bzzck6rgsZ7NfcVc0MPpgDCdPQsBzu4jTzpenQglZei4OVOzHUvtdB-4Jx1TtuGZZuz6QcUzzRGTK7Eh7lof_vuTtHSbaZ0_4l2FcoSPYxw4BzijgtGRxyk4ZDNaqmTmZjAXbrVu3_m00)


![tcc plantuml](http://www.plantuml.com/plantuml/png/fPR1Ru8m5CRl_1KldZGTDh2hCuBnDDsCuimk9btjN6C40XqNw_--Z8qL2qstyNbfw9VVUxv-L2Uv89dOSq680qsrFGFf3U4zPo0eRT-7VmcvDQ5MSW7jwaUKpdbyz6w-5_x0s91q36Kr8V26jx1yN4U01QMOf1mNNhciawFfr4IMiCcPCgZdhbrZ2UlPwccsCf2Rek5yOvnyzsceBRDACbq-tCCvkKZ7N5hhGdR8HZUoVfBRDKmMBC_9WIr8clBuqEzXyPKfV4w9UBlq1awpAsIT8BALty7EgRrX7bIPliCdJhCaPrd1ASE1-bwLrw-ylkOD0UJdHu7BFDl3OVTguEBtgLDU2nZP9wZ0Thy2ooR13BRuDV2YDdwG8qiKR7wamC5LGN_emN8JWHODJ9FubMSda11gVh_57rP-yr3CilfLxzt0M2GffpzlOJXohy7qUJOlBnomIFStJmTPWcTGmxzbBtb2Zqo0EyOaJLNpsjMrWyuo_FQe5khI-md-34PkLnbkSUsOWv7hJ5YyB_-QV000)

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
