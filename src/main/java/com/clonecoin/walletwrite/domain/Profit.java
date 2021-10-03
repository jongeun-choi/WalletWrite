package com.clonecoin.walletwrite.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "profitWrite")
@Getter
public class Profit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "profit")
    private Double profit;

    @Column(name = "investment")
    private Double investment;

    @Column(name = "registerDate")
    private LocalDate localDate;

    @ManyToOne
    @JoinColumn(name = "walletId")
    private Wallet wallet;

    @Override
    public String toString(){
        return "id : " + id + " , profit : " + +profit + " , investment : "+investment+" localDate : " + localDate;
    }

    public Profit createProfit(double totalProfitRatio, double investment) {
        this.profit = totalProfitRatio;
        this.investment = investment;
        LocalDate date = LocalDate.now();
        this.localDate=date;
        return this;
    }

    public Profit setWallet(Wallet wallet) {
        this.wallet = wallet;
        return this;
    }
}
