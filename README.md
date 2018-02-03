# MyPig



![plantuml](http://www.plantuml.com/plantuml/png/nLVlQu906FxkNt7WjsxIQciGH6IRn23OIFPX4C7jj2Or7MgDqTl_llFKVlWhTMOl6Ez9zzpxFlVSemuSbzYkPhY425AT8GW2YWPuFxWIU9o23aZzjWrpWUXghI4w7B-X4W5NgHtNGSo7PRb8rhuC4z7HH2d2YjY2QrIEByZ9esNEZGLITNkZsyK9QKDVRFjvxSNZIt6Yh6rzIKfn8junGbetOhlUcZrfUhk14WPH2Gj-5JW6CGkk-E0qPWm1jjOEKdl3-_oGXGiU_oiZS0O3MCAnya5-VGw7eAel2FKaa61i4OtVv9Fxi3Ou2TtkGRT9xeUMPCuKGD-LyO6vpYqx_GrMWYGr67fG3uST4LV_8R_-uhviM3OaAs9ItGw80ZfHdeu9Bzuvcxfs6FYfNWz3Y6LXafDsG1DTCvnevzptcgpPQPJs_Uc0VHvVF6TaTGwdQ5cTKrcMYoyEoeeuha5LdTZqCo10xBIzpXl8ln8MPpfWCcCQYD2EUy6zVtUUx55vzZmZIkEMd5XH7zozgW2QLO7Jv7iqpGt57Mb_ytYV-_wnFaUJs0cXnFlKgdoRyVpjx3FesAVJqBp57egzzk1ag2bto-PPkbACAUSEddd2uhnlkaLZKmLEjgAA-PblB9L0POgpH5LLGMsTNILGeJ9h_a0SwAR6Fqp_0000)


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
