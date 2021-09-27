package com.clonecoin.walletwrite.domain.event.AnalysisFactor;

import lombok.Getter;

@Getter
public class AnalysisCoins {
    private String name;
    private Long amount;
    private Long avgPrice;
}
