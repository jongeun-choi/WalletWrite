package com.clonecoin.walletwrite.domain.event;

import com.clonecoin.walletwrite.domain.event.AnalysisFactor.AnalysisAfter;
import com.clonecoin.walletwrite.domain.event.AnalysisFactor.AnalysisBefore;
import com.clonecoin.walletwrite.domain.event.AnalysisFactor.AnalysisType;
import lombok.Getter;

@Getter
public class AnalysisDTO {
    private AnalysisType type;
    private AnalysisBefore before;
    private AnalysisAfter after;

}
