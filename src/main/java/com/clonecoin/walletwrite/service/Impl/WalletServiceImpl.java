package com.clonecoin.walletwrite.service.Impl;

import com.clonecoin.walletwrite.adaptor.WalletWriteProducerImpl;
import com.clonecoin.walletwrite.domain.Profit;
import com.clonecoin.walletwrite.domain.Wallet;
import com.clonecoin.walletwrite.domain.event.*;
import com.clonecoin.walletwrite.domain.event.dtofactor.LeadersDtoFactor.LeadersCoins;
import com.clonecoin.walletwrite.domain.event.dtofactor.LeadersDtoFactor.LeadersId;
import com.clonecoin.walletwrite.repository.WalletRepository;
import com.clonecoin.walletwrite.rest.feign.LeadersApiClient;
import com.clonecoin.walletwrite.rest.feign.TickerOpenApiClient;
import com.clonecoin.walletwrite.service.WalletService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final TickerOpenApiClient tickerOpenApiClient;
    private final LeadersApiClient leadersApiClient;
    private final WalletWriteProducerImpl walletWriteProducer;

    // Wallet 생성
    public void createWallet(WalletDTO walletDTO){
        Optional<Wallet> result = walletRepository.findByUserId(walletDTO.getUserId());
        if (result.isPresent()) {
            // 이미 존재하는 wallet
            System.out.println("\n\n 이미 존재하는 wallet 입니다. : "+walletDTO.getUserId());
            return;
        }

        Wallet wallet = new Wallet();
        wallet.createWallet(walletDTO.getUserId(), walletDTO.getUserName());

        walletRepository.save(wallet);
        System.out.println("\n"+wallet.toString());
    }

    // Wallet 불러오기
    public Wallet findWallet(Long userId) {
        Optional<Wallet> wallet=walletRepository.findByUserId(userId);
        if (wallet.isEmpty()) {
            // 비어 있는 경우 예외처리
            System.out.println("\n\n 존재하지 않는 wallet");
            throw new NoSuchElementException();
        }

        // 존재할경우 해당 Wallet 리턴
        System.out.println("\n\n 존재하는 wallet 발견 : "+ wallet.get().toString());
        return wallet.get();
    }


    // 00시에 리더들 정보 갱신
    @Scheduled(cron = "0 0 0 ? * *") // 매일 00시 00분에 요청
    @Transactional
    public void updateDayProfit(){

        ModelMapper modelMapper = new ModelMapper();


        //analysis로 leaders 정보 받아오기
        LeadersDTO leadersDTO = modelMapper.map(leadersApiClient.getLeaders(), LeadersDTO.class);


        System.out.println("\n\nupdateDayProfit 시작\n");

        leadersDTO.getAll().stream()
                .forEach(leadersId -> {
                    Wallet wallet = findWallet(leadersId.getLeaderId());
                    System.out.println("\n\nLeaderId : " + leadersId.getLeaderId());

                    List<Double[]> perCoinsRatio = new ArrayList<Double[]>(); // {수익률, 보유비중}

                    double UpdatedInvestment = leadersId.getCoins().stream().mapToDouble(coins -> coins.getCoinQuantity() * coins.getAvgPrice()).sum(); // 로직 계산이 시작될 시점의 investment
                    UpdatedInvestment = Math.round(UpdatedInvestment * 1000) / 1000.0;

                    double finalUpdatedInvestment = UpdatedInvestment;
                    leadersId.getCoins().stream() // 코인별 수익률 계산
                            .forEach(coins -> {
                                String str = coins.getCoinName() + "_KRW";
                                System.out.println("\n\nstr : " + str);
                                TickerCoinDTO tickerCoinDTO = modelMapper.map(tickerOpenApiClient.getTickerCoin(str), TickerCoinDTO.class);

                                // 1일 수익률 계산 로직
                                double opening_price = Double.parseDouble(tickerCoinDTO.getData().getOpening_price()); // 전날 종가 = 오늘의 시가
                                double profitRatio = (opening_price / coins.getAvgPrice() - 1) * 100; // 해당 코인 수익률
                                double coinInvestment = coins.getAvgPrice() * coins.getCoinQuantity(); // 해당 코인 투자 금액
                                double coinHoldingRatio = coinInvestment / finalUpdatedInvestment; // 해당 코인 보유비중
                                System.out.println("\n\nopening_price : "+opening_price);
                                System.out.println("\n\nprofitRatio : "+profitRatio);
                                System.out.println("\n\ncoinInvestment : "+coinInvestment);
                                System.out.println("\n\ncoinHoldingRatio : "+coinHoldingRatio+"\n\n");
                                perCoinsRatio.add(new Double[]{profitRatio, coinHoldingRatio}); // 전체 수익률 계산을 위해, {수익률, 보유비중} List 에 저장
                            });
                    perCoinsRatio.stream().forEach(ratio-> System.out.println("수익률 : "+ratio[0]+" , 보유비중 : "+ratio[1]));

                    double totalProfitRatio = perCoinsRatio.stream().mapToDouble(ratio -> ratio[0] * ratio[1]).sum();
                    totalProfitRatio = Math.round(totalProfitRatio * 1000) / 1000.0; // 전체 수익률, 소수점 3자리까지

                    Profit profit = wallet.updateDayProfit(totalProfitRatio,finalUpdatedInvestment); // wallet에 수익률 저장

                    ProfitDTO profitDTO = modelMapper.map(profit, ProfitDTO.class);

                    System.out.println(" profit 저장 완료");
                    WalletDTO walletDTO = new WalletDTO(wallet.getUserId(), wallet.getUserName(), profitDTO); // walletRead로 보내줄 walletDTO

                    try {
                        // 업데이트된 리더 1일 기준 수익률을 계산이 되면 바로바로 walletRead 로 전송
                        // Kafka 을 이용
                        walletWriteProducer.sendToWalletRead(walletDTO);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                });
    }
}
