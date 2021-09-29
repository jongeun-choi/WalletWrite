package com.clonecoin.walletwrite.domain.event.dtofactor;

import lombok.Getter;

@Getter
public class AnalysisCoins {
    private String name;
    private Double coinQuantity;
    private Double avgPrice;

    public AnalysisCoins(String name, double coinQuantity, double avgPrice) {
        this.name = name;
        this.coinQuantity = coinQuantity;
        this.avgPrice = avgPrice;
    }
}
