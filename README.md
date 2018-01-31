# MyPig



![plantuml](http://www.plantuml.com/plantuml/png/SoWkIImgISaiIKpaqgK9uEBYKa07DI12ctIpj74OLQCGN52KcPUka9nJcg905AYXlQjG0HVKP6rSzcaZaL1pQnHAYrEBa4o3-DOnm1FA3Gxs1v8--OafC5z1rTRO81jhL-EUM99QdbW9SrcDKs9EObuoq6maBUXXYkgP6kIm8qCBTk_ZqG9sECu8nsFJCDA2HEj3mY-60bD02G6Se8X8_PYf4KbT3NuV4PFzW3W6Ohu--QOeKEmEA2YZp0G2SKEiDxHSdBI0SK2XALG94rZ9GxbRgejhAJG5dawuQ595SJcavgM0Qq00)









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
                 |                                           /------\
                 |                                           |*DB*  +
                 |                                           |      |
                 |                                           | user |
                 |                                           |   {s}|
                 |                                           \--+---/


------------------------------------------------------------------------------------
@endditaa
```
