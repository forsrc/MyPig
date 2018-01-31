# MyPig



![plantuml](http://www.plantuml.com/plantuml/png/SoWkIImgISaiIKpaqgK9uEBYKa07DI12ctIpj74OLQCGN52KcPUka9nJcg905AYXlQjG0HVKP6rSzcaZaN2LN3KAfSMfHKW6GZpi6E09vGO7-mD9d_p459Z7e6fhR91DjgjnJon9BK-iX3cknYan9p4l6MWsaXRqO4NrJmroy96X1Ni8uD42TZZEECTj4p3SoD6aG2-ho7CTZDQ716CC1QQ0qWCuNH6H3x2cIvYw6lm-8YOZ0d4Cn7nzyarHeDWTK516aGa4eeP8JsYvE6a1Ou0oGwWH8B32XsANL1VNKcO8F8LmqQIQud98pKi1ru80)









```uml
@startditaa
--------------------------------------------------------------------------------------


                 |    +--------------------------------------------------+
                 |    | Spring clud                                      |
                 |    +--------------------------------------------------+
                 |                  
+------+         |    +---------+-----+----------+
| user |         |    |API      |SSO  |Load      |
|      |<--------+--->|Gateway  |     |Balancer  |
|      |         |    |         |     |          |
|      |         |    |         |     |          |
+------+         |    |         |     |          +--->
                 |    |         |     |          |
                 |    |         |     |          |
                 |    |         |     |          |
                 |    |         |     |          |
                 |    |         |     |          |       |   /------\
                 |    |         |     |          |       |-->|user  |
                 |    +---------+-----+----------+       |   |      |
                 |                                       |   \--+---/
                 |                                              |
                 |                                              v
                 |                                          /------\
                 |                                          |*DB*  +
                 |                                          |      |
                 |                                          | user |
                 |                                          |   {s}|
                 |                                          \--+---/


------------------------------------------------------------------------------------
@endditaa
```
