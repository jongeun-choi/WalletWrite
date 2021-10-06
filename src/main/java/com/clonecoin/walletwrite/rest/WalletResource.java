package com.clonecoin.walletwrite.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/walletwrite")
@RequiredArgsConstructor
public class WalletResource {

    //서버 확인
    @GetMapping("/welcome")
    public String Welome() {
        return "Welcome WalletWrite";
    }

}
