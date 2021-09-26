package com.clonecoin.walletwrite;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/walletwrite")
public class Controller {
    @GetMapping("/test")
    public String test() {
        return "테스트";
    }
}
