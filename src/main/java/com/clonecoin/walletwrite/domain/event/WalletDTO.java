package com.clonecoin.walletwrite.domain.event;

import com.clonecoin.walletwrite.domain.Profit;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class WalletDTO {

    @JsonProperty("userId")
    private Long userId;

    @JsonProperty("investment")
    private Double investment;

    private List<Profit> profitsDto = new ArrayList<>();

    public WalletDTO() {

    }

    /*
    public WalletDTO setProfits(Profit profit){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        this.profitsDto.add(modelMapper.map(profit, ProfitDTO.class));
        return this;
    }

     */

}
