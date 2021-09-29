package com.clonecoin.walletwrite.rest;

import com.clonecoin.walletwrite.domain.Profit;
import com.clonecoin.walletwrite.domain.Wallet;
import com.clonecoin.walletwrite.domain.event.TickerCoinDTO;
import com.clonecoin.walletwrite.domain.event.TickerDTO;
import com.clonecoin.walletwrite.domain.event.WalletDTO;
import com.clonecoin.walletwrite.rest.feign.TickerOpenApiClient;
import com.clonecoin.walletwrite.service.WalletService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.List;

@RestController
@RequestMapping("/walletwrite")
@RequiredArgsConstructor
public class WalletResource {

    private final WalletService walletService;

    private final TickerOpenApiClient tickerOpenApiClient;


    @PostMapping("/wallet")
    public ResponseEntity<WalletDTO> createWallet(@RequestBody WalletDTO walletDTO) {
        walletService.createWallet(walletDTO);
        return ResponseEntity.ok().body(walletDTO);
    }


    /*
    @GetMapping("/feign")
    public TickerCoinDTO testFeign() {
        ModelMapper modelMapper = new ModelMapper();
        System.out.println(tickerOpenApiClient.getTickerCoin("BTC_KRW"));
        return modelMapper.map(tickerOpenApiClient.getTickerCoin("BTC_KRW"), TickerCoinDTO.class);
    }
    */


}
