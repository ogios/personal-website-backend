package com.example.springtest_backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@SpringBootTest
class SpringtestBackendApplicationTests {

    @Test
    void contextLoads() {

    }

    public static void main(String[] args) {
        String a = new SimpleDateFormat("xyyyy-MM-dd HH:mm:ss").format(new Date());
        String b = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss").format(LocalDateTime.now());
        System.out.println("a = " + a);
        System.out.println("b = " + b);
    }
}
