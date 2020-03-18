# MyPig



![plantuml](http://www.plantuml.com/plantuml/png/xPdnQzf05CT_zwzuaD_wTZXr7HGaDEsaR3ZMcks7WHIkbsiRTCcuH4Vfw_y-us6pNWqQB_Ccx8l1ToI63zzlxZtKqopdAe_YdFCMSoviWNFHlyb0i10sux8L0q7uKyN97OZfB09nDlm6EuWEtWTi6i4YYviqpI68ViG9Y8lH06h8wn0Sl0_O281j5kTfSXlVGIZLNAhwGNJ9zpg7xmFQ0pIGnM2cv8Jlb8NNAu9uutKFtGUzlOidQrKkAgy7fghFr6L03MumNEj3b_fbgZnZpK2FwHDLPe60HbKYutRcOtLYfG_uoWSABZ-kbXI6Nm3wZ1WDKnx1GiGA-JIBjvcG11D-E-4WlW-6POp7R-cPuOWnPEm8-YkwvTqlU2v_yGVDeFS60PtnAK-4LAjFYz7W0u1tSaAMFW3GB9FAs9lbabvLGBMo81Z9ACvC7pfKChGPih6PnND385sR3AOiVI1zqsry444Gr6UWmPHdUIn2oPMurtOiVEXIoN04-bZ3q6UC637c6rbWd_cD-b2gtlDWhyOP_ZFeiU6MGSyCjmnjLcZyjndql2WPs1_oTPkbdHbW9sqWzuvNvDwxIlVqi27VwzcwjtdYLp8KYEzfFz_MSJt3U3cYsqqpKCtWh_V_gmLkXB9cgA-v0uOzT9LTsWAwPT2TpRqFxXaUioUt3FOzoh6g6SmUjNwDBzVuybgqP23u70kLPbBDOo4p8FYgU99naSTfaW4GVBeCYgiURdWUPqLr1GJdLwEmUEFYNW8TbW_EXItcNgrJcKJB_s1-0m00)


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
