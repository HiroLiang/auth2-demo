package com.tfs.demo.controller;

import com.tfs.util.JwtValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @JwtValidation
    @GetMapping("/JwtAop")
    public ResponseEntity<String> testJwtAop() {
        log.info("testJwtAop");
        return ResponseEntity.ok("Test JwtAop.");
    }
}
