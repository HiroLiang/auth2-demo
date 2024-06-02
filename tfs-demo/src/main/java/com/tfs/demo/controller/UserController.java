package com.tfs.demo.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/isLogin")
    public ResponseEntity<String> checkLogin(HttpServletRequest request) {
        String result = "N";
        String headerToken = request.getHeader("JwtToken");
        if(headerToken != null)
            result = "Y";
        return ResponseEntity.ok(result);
    }

}
