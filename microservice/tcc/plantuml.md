```plantuml

@startditaa

                    |1. bookTrip                          +------------+
                    v                                     |            |
              +-----------------+ 1.3 PUT /confirm (R1,R2)| Transaction|
              | Booking Process |------------------------>| Coordinator|
              |           cBLU  |                         |            |
              +-----------------+       +-----------------|    cPNK {o}|
                  |          |          |                 +------------+
                  |          |          |                             |
        +---------+          +--------------------+                   |
        |1.1 R1=/booking/A              |         |1.2 R1=/booking/B  |
        v                               |         v                   |
   +------------+                       |     +------------+          |
   |            |                       |     |            |          |
   |    Swiss   |<----------------------+     |   easyjet  |<---------+
   |            |  1.3.1 PUT R1               |            |1.3.2 PUT R2
   |            |                             |            |
   |    cYEL {o}|                             |    cYEL {o}|
   +------------+                             +------------+


@endditaa


@startditaa
                   
                    |1. use tcc
                    |                                                      +------------+
                    v                       (TccLink1,TccLink2)            |            |
              +-----------------------+ 1.3 POST /tcc/api/v1/tcc/          | TCC        |
              | Use TCC               |----------------------------------->| SERVER     |
              |                   cBLU|                                    |            |
              +-----------------------+  +---------------------------------|    cPNK {o}|
                  |          |           |                                 +------------+
                  |          |           |                                         | TCC WS @MessageMapping("/tccLink/{path}")
        +---------+          +----------------------+                              | ^ ^
        |1.1 TccLink1=/microservice/A    |          |1.2 TccLink2=/microservice/B  | | |2.2.1
        |POST                            |          |POST                          | |2.1.2
        v                                |          v                              | | |
   +--------------+                      |         +--------------+                | | |
   |              |                      |         |              |                | | |
   | Microservice |<---------------------+         | Microservice |<---------------+ | |
   | A            |2.1 PUT /microservice/A/confirm | B            |2.2 PUT /microservice/B/confirm
   |              |                                |              |                  | |
   |              |                                |              |/topic/tccLink/B<-(ALL confirm OK)
   |      cYEL {o}|                                |      cYEL {o}|<-----------------| |
   +--------------+                                +--------------+ Websocket (B confirm OK)->/app/tccLink/B
              ^2.1.1                                               2.2.1               |
              |Websocket           (A confirm OK)->/app/tccLink/A                      |
              +------------------------------------------------------------------------+
              /topic/tccLink/A<-(ALL confirm OK)
@endditaa




```
