package com.clonecoin.walletwrite.domain;

import com.clonecoin.walletwrite.domain.event.LeadersDTO;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "walletWrite")
@Getter
@Setter
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "userId")
    private Long userId;

    @Column(name = "investment")
    private Double investment;


    // walletWrite(부모) Entity가 사라지면 profitWrite(자식) Entity도 사라진다.
    // profitWrite가 null이 되는 객체가 있다면 연관관계에서 delete한다.
    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Profit> profits=new ArrayList<>();


    public Wallet(){}

    @Override
    public String toString() {
        return "id= " + id + ", userId= " + userId + ", investment= " + investment;
    }


    public Wallet createWallet(Long userId) {
        this.userId = userId;
        return this;
    }

    public Wallet updateInvestment(Double investment) {
        this.investment = investment;
        return this;
    }


    public Wallet updateDayProfit(double totalProfitRatio){
        Profit profit = new Profit();
        profit.createProfit(totalProfitRatio);
        this.profits.add(profit);
        profit.setWallet(this);
        return this;
    }

}
