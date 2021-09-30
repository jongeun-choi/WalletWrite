package com.clonecoin.walletwrite.domain.event.dtofactor.LeadersDtoFactor;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class LeadersId {
    private Long leaderId;
    private List<LeadersCoins> coins = new ArrayList<>();

    public LeadersId(long leaderId, List<LeadersCoins> coins) {
        this.leaderId = leaderId;
        this.coins = coins;
    }
}
