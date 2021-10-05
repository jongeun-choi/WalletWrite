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

    @JsonProperty("leaderId")
    private Long userId;

    @JsonProperty("leaderName")
    private String userName;

    @JsonProperty("profitDto")
    private ProfitDTO profitDto;

    public WalletDTO() {

    }

    public WalletDTO(Long userId, String userName, ProfitDTO profitDto) {
        this.userId = userId;
        this.profitDto = profitDto;
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
