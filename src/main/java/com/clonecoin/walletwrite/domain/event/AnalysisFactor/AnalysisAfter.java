package com.clonecoin.walletwrite.domain.event.AnalysisFactor;

import lombok.Getter;

import java.util.List;

@Getter
public class AnalysisAfter {
    private List<AnalysisCoins> coins;
    private Long totalKRW;
}
