package com.clonecoin.walletwrite.domain.event;

import com.clonecoin.walletwrite.domain.event.dtofactor.TickerDtoFactor.TickerCoinContent;
import lombok.Getter;

@Getter
public class TickerCoinDTO {
    private String status;
    TickerCoinContent data;
}
