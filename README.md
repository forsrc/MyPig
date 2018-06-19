# MyPig



![plantuml](http://www.plantuml.com/plantuml/png/rLTXQzim4FtkNt7WRz5CrdGR58gfkuMoaR4stZuCGa6LrLOqiuViP9HU__ka7AbN9IfHS5Bs4fCJij_TUpaVzadTSDqKgk4yIlQ9A8h01O81wuIUUTWHyjzQbJSWfhC2nEde1sm1V9NQsJwCkQgg1hBYbof1d8s7483u05-ZShP3JpvMvRMwWLpgkTJXXWmmZG_sNZRRbHl3cPPtV2ituaDhnPjuiC-QgIdDCLY69eeFmK6x4r4XR8NbaWuAwUkWtJ4a-50zebSX0-P4bDCzwmKmlmzcZwqemEpyytA9UVuDu3Z114SLBy004uiKPwgzo0-4Etvj-a3y78vQ2z-_nGXwYKcUz0mxWIreptWZ__1xIaxDOw9JFkMba1g90CLu-Cdm71tXbieDS5PBxNHjkyIL2338TeInB5JjAb_ClRuHEt5jVxf7a68tkGV1of7ODb4kiYmBJux3AQyR9NB9jRWb0upoWLNU0pfMazi-GFD9KiTsTfoadIXl4V2V_nUNhPbG7J3HYEg2g9zOJ3Qaee7JKYN_8AMxzcMZRBSjgU9ttgfeYc1gPxaB_nZsijba7yAUQjXwgibY6FRz6drOktqPL99_k7ca1ME-9Kz0HmM1hc2TK55pTbPLPrGFzMC7L6rdHfiDt_LhzdpDdf_B8f-AhqheoXWyLqBMPlrTyxBceb5LMTlzB-UPFUl-YZUgjj65VIg_6EVsXujR2RZJklVyKdmYosBnCluN)


* microservice

    microservice             | port  | start order |
    ------------------------ | ----- | ----------- |
    ui                       | 8888  | 99          |
    springboot-api-gateway   | 8088  | 5           |
    springboot-sso-server    | 10000 | 4           |
    springboot-eureka-server | 11100 | 3           |
    springboot-config-server | 12000 | 2           |
    springboot-admin         | 13000 | 1           |
    springboot-zipkin-server | 11110 | 1           |
    user                     | 10010 | 6           |
