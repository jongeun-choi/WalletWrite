package com.clonecoin.walletwrite.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sun.istack.NotNull;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "walletWrite")
@Getter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "userId") // wallet 과 proit이 서로 역참조 되는 것을 막아줌
@JsonIdentityReference(alwaysAsId = true)
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "leaderId")
    private Long userId;

    @NotNull
    @Column(name = "leaderName")
    private String userName;


    // walletWrite(부모) Entity가 사라지면 profitWrite(자식) Entity도 사라진다.
    // profitWrite가 null이 되는 객체가 있다면 연관관계에서 delete한다.
    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Profit> profits = new ArrayList<>();


    public Wallet() {
    }

    @Override
    public String toString() {
        return "id= " + id + ", userId= " + userId+" , userName : "+userName;
    }


    public Wallet createWallet(Long userId,String userName) {
        this.userId = userId;
        this.userName = userName;
        return this;
    }

    public Profit updateDayProfit(double totalProfitRatio,double investment) {
        Profit profit = new Profit();
        profit.createProfit(totalProfitRatio,investment);
        this.profits.add(profit);
        profit.setWallet(this);
        return profit;
    }

}
