# MyPig



![plantuml](http://www.plantuml.com/plantuml/png/xPdnIzj05CT_zwzuaD_wTdHjvq0emUYAR7HCc-s7GH7Eowb7ksHSqWxn-R-lwL63PmE9QJmBVl7m7KdAX--tUOyqnsd6THQfZFC3vbnu0Cv5RvE1O2NSZSkD60Z2lrh5Do3cYmZ4oVWNl42qzpvW-mYLixXAaWo2w8-AGPnDHb13NezWxtt0zW4gP76QnDVg1aAfbrBN3w9FljVRVn-mVO0AMOmMMixuYxBm1daG7xp-lljWRk_Lwc_ALUKDm5RvaReCQC0jXakpJ6d-R1L76ZFGVV90fLaWe5KLo5ZDV2nFhF01d_b0mVdNpPR2y0V0a16ZSS8ZM8bOBfyMgig499Znwna7yNiqBc8y_4YF31t6aB4E33Tqwqy_uvdyn-yCWxat2EY4ptaifDvSBIQZBm3UqH4rz0605gdKrhrPREbP1LGh2uA9Z5Hg-z2ZWg7Ba4tjB1uR0fdQPh3Lq0S2iohu888WgCz0epbFCoL2oRMuDNQiVEXJmT01ixOm31aZHepvLXOuPFukVIXK-_lGNEyCksOmOyCjWvaPJXcwBDUqRGOpBme6nWhvfirIMmpUuOR1-rJgWnaRv7k3PWoLPdSPGuxucLxdkHktCqpNmxRRJXRL7wWlN__t1mHBr093NIsTCBJV7sh9N85k6KoFSk-3UuRxzC4jG_CUvLnb37QFshx7ftjyUYusP23uheHEKgcNIiWK27vg7gTSP2g9Km22R-T1VjRT5SzKcbSNG71wCGdpVrpSIg3zyi6vw4sylpYMSRH-c_8V)


* microservice

    microservice             | port  | start order |
    ------------------------ | ----- | ----------- |
    ui                       | 8888  | 99          |
    mypig-api-gateway        | 8088  | 5           |
    mypig-sso-server         | 10000 | 4           |
    mypig-eureka-server      | 11100 | 3           |
    mypig-config-server      | 12000 | 2           |
    mypig-admin-server       | 13000 | 1           |
    mypig-zipkin-server      | 11110 | 1           |
    mypig-user               | 10010 | 6           |
    mypig-tcc                | 10020 | 6           |
