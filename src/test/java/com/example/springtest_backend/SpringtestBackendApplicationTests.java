package com.example.springtest_backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringtestBackendApplicationTests {

    @Test
    void contextLoads() {

    }

    public static void main(String[] args) {
        String c = "dsass.fsaf.jpg";
        String a = c.substring(c.lastIndexOf(".") + 1);
        System.out.println("a = " + a);
    }

}
