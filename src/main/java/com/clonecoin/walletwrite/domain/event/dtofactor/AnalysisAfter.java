package com.clonecoin.walletwrite.domain.event.dtofactor;

import lombok.Getter;

import java.util.List;

@Getter
public class AnalysisAfter {
    private List<AnalysisCoins> coins;
    private Long totalKRW;
}
