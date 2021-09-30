package com.clonecoin.walletwrite.rest.feign;


import com.clonecoin.walletwrite.domain.event.TickerCoinDTO;
import com.clonecoin.walletwrite.domain.event.TickerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "ticker-open-api", url = "https://api.bithumb.com/public/ticker/")
public interface TickerOpenApiClient {

    @RequestMapping(method = RequestMethod.GET, produces = "application/json", value = "/ALL_KRW")
    TickerDTO getTicker();


    @GetMapping(value = "{coin}")
    TickerCoinDTO getTickerCoin(@PathVariable("coin") String coin);
}
