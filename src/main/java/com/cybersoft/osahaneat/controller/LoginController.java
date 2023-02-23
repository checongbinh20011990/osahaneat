package com.cybersoft.osahaneat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {
    //permitAll()
    //authen()
    @PostMapping("/signin")
    public ResponseEntity<?> signin(){

        return new ResponseEntity<>("Hello login", HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(){

        return new ResponseEntity<>("Hello login signup", HttpStatus.OK);
    }

}
