package com.clonecoin.walletwrite.domain.event;

import com.clonecoin.walletwrite.domain.event.dtofactor.LeadersDtoFactor.LeadersId;
import lombok.Getter;

import java.util.List;

@Getter
public class LeadersDTO {
    private List<LeadersId> all;


    public LeadersDTO(List<LeadersId> all) {
        this.all = all;
    }
}
