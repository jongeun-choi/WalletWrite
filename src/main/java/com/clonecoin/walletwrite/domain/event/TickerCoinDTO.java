package com.clonecoin.walletwrite.domain.event;

import com.clonecoin.walletwrite.domain.event.dtofactor.TickerCoinContent;
import lombok.Getter;

@Getter
public class TickerCoinDTO {
    private String status;
    TickerCoinContent data;
}
