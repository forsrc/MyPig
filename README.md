# MyPig



![plantuml](http://www.plantuml.com/plantuml/png/nLTRRu8m57xtL_IPXf1T4uanmcQM9IPRPdmp9br1HtIm0Bei7l_xIhc8sg98vuveJalvpkthgVIYc8InuyM4jFHBIglLGli2oGzM0eybw826Nw7dpn1TB1s4g3qOeVe2_-8xLfw5zo28aULyUdvwazgsBGpu2lxSfzEGC5jfiaiQc43woz2T4pHqmvKRil4DT7JTA2rkx5459N8nSmKt01bdoxl9x4FWJxtPDgph92mcXX24y2hJz9bq8FE4tvLwCk5NePT6EpAH0fYbP735f1xtwd2FuMrgIW1GoegywfmZi0us82qhBlIjbjl3-xKH2nQm6-lum6kmNf_p8Go7Bvas28YJggnstMJfo6F3TXjqK-zcrH8aSawcZD6bL6dCjAvhg9FxmLdoH6Btc_oaHbEUCCqc2-9JDuJ6aJBWPSJ99oe653hi5UpqHelw2nB57erS4j8FJWg31JUMxdi5HaMHmYvAbL5gMTOvaRwvZXVjq5z3wLTWbDKPs8TnPZTItCdcpewqkgqsJwyQ4rKzggwt4qKufY8SK85Zy8AEJuRg97mg9ikawAPx_AXNTuKljrAl88Sti-PszEYjwj7b9-wFdPqdoPXtJaC5LEKPgERlPbrPgS2f9bQzkBH7MqDO2PIwH05A-Au82gsZZL9cNlWjhUVw3dyx_0K0)


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
