package com.clonecoin.walletwrite.domain.event.dtofactor;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class LeadersId {
    private Long leaderid;
    private List<LeadersCoins> coins = new ArrayList<>();

    public LeadersId(long leaderid, List<LeadersCoins> coins) {
        this.leaderid = leaderid;
        this.coins = coins;
    }
}
