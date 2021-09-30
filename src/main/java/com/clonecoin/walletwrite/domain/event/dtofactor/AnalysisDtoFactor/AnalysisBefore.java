package com.clonecoin.walletwrite.domain.event.dtofactor.AnalysisDtoFactor;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class AnalysisBefore {
    @JsonProperty("coins")
    private List<AnalysisCoins> coins = new ArrayList<>();

    @JsonProperty("totalKRW")
    private Long totalKRW;

    public AnalysisBefore() {

    }

    public AnalysisBefore(List<AnalysisCoins> coins,long totalKRW) {
        this.coins = coins;
        this.totalKRW = totalKRW;
    }
}
