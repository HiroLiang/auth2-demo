package com.tfs.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {"com.tfs.demo", "com.tfs.auth"})
public class TfsDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TfsDemoApplication.class, args);
    }

}
