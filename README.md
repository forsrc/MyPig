# MyPig



![plantuml](http://www.plantuml.com/plantuml/png/nLTRRu9G37xdL_IP4yAxCKRObcM9oPOPtyoICq17TB00iYpM_xvpKGG5nD2vYjgIaw_jrmjXd6OyoVmmuzomRYc6OS2feFnX9F2i0HrX-fM4qHAyzSO7yDp93AuN_9VO6HqBceJtECx0yJ_3I3BXkd-QCLDNt-oOE3aGLsDvw-LXamGh3jCWoOC4S6XPTkbmxuXQ23eJHW6oltiNHujmMKvBY7qHXEkIisjQHlrX1x9hSr0UPTqy7R6ieRH2RLLae0uREZvs5aQkha8lMHN91G1kunqs5fKL-b4x-6CLgr5O3NbCjFbfrEYyF1rCd4wVzzeavhvMHUL6ykWi15d6WNNWgRRsAxuGpLmYt5IpPSBmE6lM21zv5dpp7-rqtoA0Bb_po0iIx9sf0Dwa0hI-9x3GfUOvZjCodmrB1nxMFCr2Bmruudserh95SlF6gQywRHsXQpB5AaeNfqoLj7kchu4VffKXCa5_QvmAR1GVxO0qq_Qbsf_UFYw8maCYd69FKO3b53YsAkEyCzHGTb5nizmA8ptP95HfgxJLBZvkspPp5RJhyx9DvcfVsXHGGjw8S9089wV0QM-iw_8oxrqJ60aK7L48ij-9e62RxaWxyyPlPkCWyjKRuIy0)


* microservice

    microservice             | port  | start order |
    ------------------------ | ----- | ----------- |
    ui                       | 8888  | 99          |
    springboot-api-gateway   | 8088  | 5           |
    springboot-sso-server    | 10000 | 4           |
    springboot-eureka-server | 11100 | 3           |
    springboot-config-server | 12000 | 2           |
    springboot-admin         | 13000 | 1           |
    user                     | 10010 | 6           |
