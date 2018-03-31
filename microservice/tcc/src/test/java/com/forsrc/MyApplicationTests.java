package com.forsrc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest
@ComponentScan(basePackages = "com.forsrc")
@WebAppConfiguration
public class MyApplicationTests {

    @Test
    public void contextLoads() {
    }

}
