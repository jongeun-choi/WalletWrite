package com.clonecoin.walletwrite.service;

import com.clonecoin.walletwrite.domain.Wallet;
import com.clonecoin.walletwrite.domain.event.AnalysisDTO;
import com.clonecoin.walletwrite.domain.event.WalletDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface WalletService {

    //public Wallet save(WalletDTO walletDTO);

    public void createWallet(WalletDTO walletDTO);

    public Wallet findWallet(Long userId);

    //public Wallet updateInvestment(AnalysisDTO analysisDTO);

    public void updateDayProfit();

}
