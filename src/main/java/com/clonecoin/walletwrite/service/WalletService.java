package com.clonecoin.walletwrite.service;

import com.clonecoin.walletwrite.domain.Wallet;
import com.clonecoin.walletwrite.domain.event.WalletCreatedDTO;
import com.clonecoin.walletwrite.repository.WalletRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    ModelMapper modelMapper = new ModelMapper();


    public void walletCreate(WalletCreatedDTO walletCreatedDTO){
        Optional<WalletCreatedDTO> result = Optional.ofNullable(walletCreatedDTO);

        if (result.isEmpty() || walletRepository.findById(walletCreatedDTO.getUserId()).isPresent() ) {
            //예외처리
        }

        Wallet wallet = new Wallet();
        modelMapper.map(walletCreatedDTO, wallet);
        walletRepository.save(wallet);
        System.out.println("\n"+wallet.toString());
    }

}
