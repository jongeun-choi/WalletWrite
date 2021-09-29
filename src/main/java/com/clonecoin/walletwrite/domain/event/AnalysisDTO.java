package com.clonecoin.walletwrite.domain.event;

import com.clonecoin.walletwrite.domain.event.dtofactor.AnalysisAfter;
import com.clonecoin.walletwrite.domain.event.dtofactor.AnalysisBefore;
import lombok.Getter;

@Getter
public class AnalysisDTO {
    private Long leaderid;
    private AnalysisBefore before;
    private AnalysisAfter after;
}
