package com.clonecoin.walletwrite.domain.event.AnalysisFactor;

import lombok.Getter;

import java.util.List;

@Getter
public class AnalysisBefore {
    private List<AnalysisCoins> coins;
    private Long totalKRW;
}
