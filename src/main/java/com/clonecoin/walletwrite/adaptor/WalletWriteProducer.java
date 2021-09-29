package com.clonecoin.walletwrite.adaptor;

import com.clonecoin.walletwrite.domain.event.WalletDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.concurrent.ExecutionException;

public interface WalletWriteProducer {
    void sendToWalletRead(WalletDTO walletDTO) throws ExecutionException, InterruptedException, JsonProcessingException;
}
