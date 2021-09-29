package com.clonecoin.walletwrite.domain.event;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProfitDTO {
    private Double profit;
    private LocalDate localDate;

    public ProfitDTO() {

    }
}
