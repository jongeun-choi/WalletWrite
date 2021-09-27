package com.clonecoin.walletwrite.rest;

import com.clonecoin.walletwrite.domain.Wallet;
import com.clonecoin.walletwrite.domain.event.WalletCreatedDTO;
import com.clonecoin.walletwrite.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/walletwrite")
@RequiredArgsConstructor
public class WalletResource {

    private final WalletService walletService;

    @PostMapping("/wallet")
    public ResponseEntity<WalletCreatedDTO> walletcreate(
            @RequestBody WalletCreatedDTO walletCreatedDTO
    ) {
        System.out.println("\n서버 도착");
        walletService.walletCreate(walletCreatedDTO);
        return ResponseEntity.ok().body(walletCreatedDTO); //
    }

}
