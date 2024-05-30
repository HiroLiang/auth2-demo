package com.tfs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class TfsAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(TfsAuthApplication.class, args);
    }

}
