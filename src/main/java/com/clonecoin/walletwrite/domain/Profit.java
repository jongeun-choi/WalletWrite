package com.clonecoin.walletwrite.domain;

import lombok.Getter;
import lombok.Setter;
import org.apache.tomcat.jni.Local;

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

    @Column(name = "registerDate")
    private LocalDate localDate;

    @ManyToOne
    @JoinColumn(name = "userId")
    private Wallet wallet;

    @Override
    public String toString(){
        return "id : " + id + " , profit : " + profit + " localDate : " + localDate;
    }

    public Profit createProfit(double totalProfitRatio) {
        this.profit = totalProfitRatio;
        LocalDate date = LocalDate.now();
        this.localDate=date;
        return this;
    }

    public Profit setWallet(Wallet wallet) {
        this.wallet = wallet;
        return this;
    }
}
