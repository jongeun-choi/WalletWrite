package com.clonecoin.walletwrite.domain.event.dtofactor;

import lombok.Getter;

@Getter
public class LeadersCoins {
    private String coinName;
    private Double avgPrice;
    private Double coinQuantity;

    public LeadersCoins(String coinName, double avgPrice, double coinQuantity) {
        this.coinName=coinName;
        this.avgPrice = avgPrice;
        this.coinQuantity = coinQuantity;
    }
}
