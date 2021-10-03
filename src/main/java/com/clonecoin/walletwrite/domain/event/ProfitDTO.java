package com.clonecoin.walletwrite.domain.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class ProfitDTO {
    @JsonProperty("profit")
    private Double profit;

    @JsonProperty("investment")
    private Double investment;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate localDate;

    public ProfitDTO() {}

    public ProfitDTO(double profit, double investment,LocalDate localDate) {
        this.profit = profit;
        this.investment = investment;
        this.localDate = localDate;
    }
}
