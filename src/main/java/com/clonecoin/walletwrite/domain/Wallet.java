package com.clonecoin.walletwrite.domain;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "walletWrite")
@Getter
@Setter
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @Column(name = "userId")
    private Long userId;

    @Column(name = "investment")
    private Long investment;


    public Wallet(){}


    @Override
    public String toString() {
        return "id= " + id + ", userId= " + userId + ", investment= " + investment;
    }
}
