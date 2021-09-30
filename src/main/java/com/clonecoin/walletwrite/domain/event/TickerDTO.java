package com.clonecoin.walletwrite.domain.event;

import com.clonecoin.walletwrite.domain.event.dtofactor.TickerDtoFactor.TickerData;
import lombok.Getter;

@Getter
public class TickerDTO {
    private String status;
    private TickerData data;
}
