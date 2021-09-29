package com.clonecoin.walletwrite.domain.event.dtofactor;

import lombok.Getter;

@Getter
public class TickerContent {
    private String opening_price;
    private String closing_price;
    private String min_price;
    private String max_price;
    private String units_traded;
    private String acc_trade_value;
    private String prev_closing_price;
    private String units_traded_24H;
    private String acc_trade_value_24H;
    private String fluctate_24H;
    private String fluctate_rate_24H;
}
