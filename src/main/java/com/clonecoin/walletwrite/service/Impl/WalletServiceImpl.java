package com.clonecoin.walletwrite.service.Impl;

import com.clonecoin.walletwrite.domain.Wallet;
import com.clonecoin.walletwrite.domain.event.AnalysisDTO;
import com.clonecoin.walletwrite.domain.event.LeadersDTO;
import com.clonecoin.walletwrite.domain.event.dtofactor.AnalysisCoins;
import com.clonecoin.walletwrite.domain.event.TickerCoinDTO;
import com.clonecoin.walletwrite.domain.event.WalletDTO;
import com.clonecoin.walletwrite.repository.WalletRepository;
import com.clonecoin.walletwrite.rest.feign.LeadersApiClient;
import com.clonecoin.walletwrite.rest.feign.TickerOpenApiClient;
import com.clonecoin.walletwrite.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.rmi.NoSuchObjectException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final TickerOpenApiClient tickerOpenApiClient;
    private final LeadersApiClient leadersApiClient;

    public Wallet save(WalletDTO walletDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Wallet wallet = new Wallet();
        modelMapper.map(walletDTO,wallet);
        return walletRepository.save(wallet);
    }

    // Wallet 생성
    public void createWallet(WalletDTO walletDTO){

        Optional<Wallet> result = walletRepository.findByUserId(walletDTO.getUserId());
        if (result.isPresent()) {
            // 이미 존재하는 wallet
            System.out.println("\n\n 이미 존재하는 wallet 입니다. : "+walletDTO.getUserId());
            return;
        }

        Wallet wallet = new Wallet();
        wallet.createWallet(walletDTO.getUserId());

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

    // 총 투자금액 갱신
    @Transactional
    public Wallet updateInvestment(AnalysisDTO analysisDTO) { 

        Wallet wallet = findWallet(analysisDTO.getLeaderId());

        double investment = analysisDTO.getAfter().getCoins().stream().mapToDouble(coin -> coin.getCoinQuantity() * coin.getAvgPrice()).sum();

        wallet = wallet.updateInvestment(investment);
        System.out.println(wallet.getUserId()+" investment update : "+wallet.toString());
        return wallet;
    }


    // 통합할때 시헌이 형이랑 이부분 맞춰보기
    // 00시에 리더들 정보 갱신
    @Scheduled(cron = "0 0 0 ? * *") // 매일 00시 00분에 요청
    @Transactional
    public void updateDayProfit() {
        
        ModelMapper modelMapper = new ModelMapper();

        // analysis로 leaders 정보 받아오기
        LeadersDTO leadersDTO = modelMapper.map(leadersApiClient.getLeaders(), LeadersDTO.class);

        leadersDTO.getAll().stream()
                .forEach(leadersId -> {
                    Wallet wallet = findWallet(leadersId.getLeaderId());

                    List<Double[]> perCoinsRatio = new ArrayList<Double[]>(); // {수익률, 보유비중}

                    double unUpdatedInvestment = leadersId.getCoins().stream().mapToDouble(coins -> coins.getCoinQuantity() * coins.getAvgPrice()).sum(); // 로직 계산이 시작될 시점의 investment

                    leadersId.getCoins().stream() // 코인별 수익률 계산
                            .forEach(coins -> {
                                String str = "/" + coins.getCoinName() + "_KRW";
                                TickerCoinDTO tickerCoinDTO = modelMapper.map(tickerOpenApiClient.getTickerCoin(str), TickerCoinDTO.class);

                                // 1일 수익률 계산 로직
                                double opening_price = Double.parseDouble(tickerCoinDTO.getData().getOpening_price()); // 전날 종가
                                double profitRatio = (opening_price / coins.getAvgPrice() - 1) * 100; // 해당 코인 수익률
                                double coinInvestment = coins.getAvgPrice() * coins.getCoinQuantity(); // 해당 코인 투자 금액
                                double coinHoldingRatio = coinInvestment / unUpdatedInvestment; // 해당 코인 보유비중
                                perCoinsRatio.add(new Double[]{profitRatio, coinHoldingRatio}); // 전체 수익률 계산을 위해, {수익률, 보유비중} List 에 저장
                            });

                    double totalProfitRatio = perCoinsRatio.stream().mapToDouble(ratio -> ratio[0] * ratio[1]).sum();
                    totalProfitRatio = Math.round(totalProfitRatio * 1000) / 1000.0; // 전체 수익률, 소수점 3자리까지

                    wallet.updateDayProfit(totalProfitRatio);


                    // 업데이트된 리더 1일 기준 수익률을 계산이 되면 바로바로 walletRead 로 전송
                    // Kafka 을 이용
                });


        //TickerCoinDTO tickerCoinDTO = modelMapper.map(tickerOpenApiClient.getTickerCoin(str), TickerCoinDTO.class);
        //System.out.println(tickerCoinDTO.getData().getOpening_price());
        //TickerDTO tickerDTO = modelMapper.map(tickerOpenApiClient.getTicker(), TickerDTO.class);
    }
}
