package com.example.springtest_backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class SpringtestBackendApplicationTests {

    @Test
    void contextLoads() {

    }

    public static void main(String[] args) {
        List<String> a = new ArrayList<>();
        for (int i=0; i < 10; i++) a.add("c" + i);
        System.out.println("a.contains(\"c2\") = " + a.contains("c2"));
    }
}
