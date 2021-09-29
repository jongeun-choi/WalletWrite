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
import java.util.ArrayList;
import java.util.List;
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


    public void createWallet(WalletDTO walletDTO){
        /*
        Optional<WalletDTO> result = Optional.ofNullable(walletDTO);
        if (result.isEmpty() || findWallet(walletDTO.getUserId())!=null ) {
            // walletDTO가 안넘와 왔거나
            // 이미 존재하는 Wallet일 경우
            // 예외처리
        }

         */

        Wallet wallet = new Wallet();
        wallet.createWallet(walletDTO.getUserId());

        walletRepository.save(wallet);
        System.out.println("\n"+wallet.toString());
    }

    public Wallet findWallet(Long userId) {
        Optional<Wallet> wallet=walletRepository.findByUserId(userId);
        if (wallet.isEmpty()) {
            // 비어 있는 경우 예외처리
        }

        // 존재할경우 해당 Wallet 리턴
        System.out.println("\n\n 존재하는 wallet 발견 : "+ wallet.get().toString());
        return wallet.get();
    }

    @Transactional
    public Wallet updateInvestment(AnalysisDTO analysisDTO) { // 총 투자금액 갱신

        Wallet wallet = findWallet(analysisDTO.getLeaderid());

        double investment = analysisDTO.getAfter().getCoins().stream().mapToDouble(coin -> coin.getCoinQuantity() * coin.getAvgPrice()).sum();

        wallet = wallet.updateInvestment(investment);
        System.out.println(wallet.getUserId()+" investment update : "+wallet.toString());
        return wallet;
    }

    //@Scheduled(cron = "0 0 0 ? * *") // 매일 00시 00분에 요청
    @Transactional
    public void updateDayProfit( ) {
        ModelMapper modelMapper = new ModelMapper();

        // analysis로 leaders 정보 받아오기
        LeadersDTO leadersDTO = modelMapper.map(leadersApiClient.getLeaders(), LeadersDTO.class);

        leadersDTO.getAll().stream()
                .forEach(leadersId -> {
                    Wallet wallet = findWallet(leadersId.getLeaderid());

                    List<Double[]> perCoinsRatio = new ArrayList<Double[]>(); // {수익률, 보유비중}

                    double unUpdatedInvestment = leadersId.getCoins().stream().mapToDouble(coins -> coins.getCoinQuantity() * coins.getAvgPrice()).sum(); // 로직 계산이 시작될 시점의 investment

                    leadersId.getCoins().stream() // 코인별 수익률 계산
                            .forEach(coins->{
                                String str="/"+coins.getCoinName()+"_KRW";
                                TickerCoinDTO tickerCoinDTO = modelMapper.map(tickerOpenApiClient.getTickerCoin(str), TickerCoinDTO.class);

                                // 1일 수익률 계산 로직
                                double opening_price = Double.parseDouble(tickerCoinDTO.getData().getOpening_price()); // 전날 종가
                                double profitRatio=(opening_price/ coins.getAvgPrice()-1)*100; // 해당 코인 수익률
                                double coinInvestment = coins.getAvgPrice() * coins.getCoinQuantity(); // 해당 코인 투자 금액
                                double coinHoldingRatio = coinInvestment / unUpdatedInvestment; // 해당 코인 보유비중
                                perCoinsRatio.add(new Double[]{profitRatio, coinHoldingRatio}); // 전체 수익률 계산을 위해, {수익률, 보유비중} List 에 저장
                            });

                    double totalProfitRatio = perCoinsRatio.stream().mapToDouble(ratio -> ratio[0] * ratio[1]).sum();
                    totalProfitRatio=Math.round(totalProfitRatio*1000)/1000.0; // 전체 수익률, 소수점 3자리까지

                    wallet.updateDayProfit(totalProfitRatio);

                });



        //TickerCoinDTO tickerCoinDTO = modelMapper.map(tickerOpenApiClient.getTickerCoin(str), TickerCoinDTO.class);
        //System.out.println(tickerCoinDTO.getData().getOpening_price());
        //TickerDTO tickerDTO = modelMapper.map(tickerOpenApiClient.getTicker(), TickerDTO.class);
    }
}
