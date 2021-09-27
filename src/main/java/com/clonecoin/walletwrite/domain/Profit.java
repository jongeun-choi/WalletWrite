package com.clonecoin.walletwrite.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "profitWrite")
@Getter
@Setter
public class Profit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "profit")
    private Long profit;

    @Column(name = "registerDate")
    private LocalDate localDate;

    @ManyToOne
    @JoinColumn(name = "userId")
    private Wallet wallet;
}
