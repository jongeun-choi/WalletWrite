package com.clonecoin.walletwrite.domain.event;

import com.clonecoin.walletwrite.domain.event.dtofactor.AnalysisAfter;
import com.clonecoin.walletwrite.domain.event.dtofactor.AnalysisBefore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class AnalysisDTO {
    @JsonProperty("leaderId")
    private Long leaderId;

    @JsonProperty("before")
    private AnalysisBefore before;

    @JsonProperty("after")
    private AnalysisAfter after;

    public AnalysisDTO() {

    }

    public AnalysisDTO(long leaderId, AnalysisBefore before, AnalysisAfter after) {
        this.leaderId = leaderId;
        this.before = before;
        this.after = after;
    }
}
