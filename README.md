# MyPig



![plantuml](http://www.plantuml.com/plantuml/png/rLVRQi9047tFLopapMsmsbeGH8nja8BGOkX3GOJj9jgWJKekbkBqtxkv6dCpiJ7IWSXiSC_Cd9azIKQMJKnRqMn2EA59upWE9GtS7rmBFCv11oH_cfg-GdJZA8YEfo_e18EBv8wR8ERDC6maAX-QZkXa9bLXXU_09JB7P-Jatj2NsWh9ghbLpUg4T678TnfF6v-B3SanrJKvYGt-ngNYYkyscxCxb6uVKCOYERoSdmLEGIo2Axquj0L3W9tn0xcruSZVUs70-ByA31SmW0MSIX_4vyTm2RByXD10006c1b6yczxc8JYQHqA_pzhKpTSzj2PBDW7qLPhkcUjTitFVOYa8GekX1_bui1DYgr_asm_kZmtpncH3TAgQ0L50Pz93dF3gbREjZiN03-TrluIK5pebQGSqKnNDIbRkALwR5JjFqXwT3eZyTF2ILbGv7A8LLKv5KQmU7AGDiMoDoYenwNj00D_hkfMta7zb16TpmCQCpK22TZm8xlsvycoDAkxvlzYMAKrr02tgm6bx3PYNXlBa9hePR-BGFxF7QE9x8HH_cvkLho6kU1rzmCH5E0_D3NPNxMq6PqFDFIbi7wKhHtLATl38unENVO6jQjiwS8edgffO-QfH2rJXS9wGLLrGE-kd5gXmCXl-xXkfkk9_R_u2)


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
