package com.clonecoin.walletwrite.domain.event.dtofactor;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class AnalysisAfter {
    @JsonProperty("coins")
    private List<AnalysisCoins> coins = new ArrayList<>();

    @JsonProperty("totalKRW")
    private Long totalKRW;

    public AnalysisAfter() {

    }
}
