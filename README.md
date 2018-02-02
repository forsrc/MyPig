# MyPig



![plantuml](http://www.plantuml.com/plantuml/png/nLTTQwj047tFhnZmpKNIZvj2fOIQLig5mBqat8SBGTYkjfMcMjIqb4x_UzTTDTQu6kcs7P8mw-hPCsTcP-Dvcj4ayyECqf7rdJOQZQ1fcFyGBV14WOwmV4p2w1ROUkC3iDdy7mmt_17kHBiArt6SWUC_X96SP5SBjorUun2_dDCGyy3x71-ItsihyuXjMhYR9BYdi0oIfo39HJd676gyfQ-14sVLeuzTEgGB1NjGLiNjKnHnNiJHJNXRncOSePmTjw9GKJ1CFY87AYvYLad54v_Ahuq-Pbi9ODVa48xDGi5gTxkIhJOr0Ap9WkfIp7E2h_4RAZDBAd_hbUkHPdBQZNHi66AH7VRe_FrT3d6v_0DmPg65yvZwucAHNu1Do8lrz1GjsveMMMiHz9xUyEfc_ztvTjjF3hXq9gTWMIP7B_W8s2kQ1S_qHIukouPxCxgc4GkIKbwsS2_xjpZfR1MuISL-QqiILZus4hctx0ZkcgPPoDA09knEAc1CHCSRys1NJTchne2LlxludjOLENv4wugSEOupV764HU27QJFdHtda9iXlo-Ay3dYPyHfeo4xEYckVZbpLaljplf_fMQAhjmrbwsd2iJNXe0wSiKZeQcyeqRwgY_MYcCeUqCVAc9HGnYybAzcJEBnTDCFUKxkhjzUUC4vGmr-R_6GcgfDtk7cQS50JpfCEdEx26XQNUJapWMY1qYSKGbxlcg3WDNtJMfdV_A9u7aI-U45z1m00)


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
