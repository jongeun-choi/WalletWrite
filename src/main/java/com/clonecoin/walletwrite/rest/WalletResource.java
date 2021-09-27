package com.clonecoin.walletwrite.rest;

import com.clonecoin.walletwrite.domain.event.WalletDTO;
import com.clonecoin.walletwrite.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/walletwrite")
@RequiredArgsConstructor
public class WalletResource {

    private final WalletService walletService;

    @PostMapping("/wallet")
    public ResponseEntity<WalletDTO> walletcreate(
            @RequestBody WalletDTO walletDTO
    ) {
        System.out.println("\n서버 도착");
        walletService.createWallet(walletDTO);
        return ResponseEntity.ok().body(walletDTO); //
    }

}
