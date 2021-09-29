package com.clonecoin.walletwrite.service.Impl;

import com.clonecoin.walletwrite.domain.Profit;
import com.clonecoin.walletwrite.domain.Wallet;
import com.clonecoin.walletwrite.domain.event.LeadersDTO;
import com.clonecoin.walletwrite.domain.event.TestDTO;
import com.clonecoin.walletwrite.domain.event.TickerCoinDTO;
import com.clonecoin.walletwrite.domain.event.dtofactor.AnalysisCoins;
import com.clonecoin.walletwrite.domain.event.dtofactor.LeadersCoins;
import com.clonecoin.walletwrite.domain.event.dtofactor.LeadersId;
import com.clonecoin.walletwrite.repository.WalletRepository;
import com.clonecoin.walletwrite.rest.feign.LeadersApiClient;
import com.clonecoin.walletwrite.rest.feign.TickerOpenApiClient;
import com.clonecoin.walletwrite.service.WalletService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.netflix.discovery.converters.Auto;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@RequiredArgsConstructor
@ExtendWith(SpringExtension.class)
@DataJpaTest // JPA관련 Component만 로드 된다. 테스트 종료 후 rollback이 되기 때문에 실제 repository에는 저장되지 않는다.
class WalletServiceImplTest {

    private ModelMapper modelMapper = new ModelMapper();


    @Autowired
    private WalletRepository walletRepository;


    @Test
    void save() {
        TestDTO testDTO = new TestDTO(55L);
        Wallet wallet = new Wallet();

        modelMapper.map(testDTO, wallet);

        System.out.println(wallet.getUserId());

        assertThat(wallet.getUserId(), is(equalTo(55L)));
    }

    @Test
    void createWallet() {
        Wallet wallet = new Wallet();
        wallet.createWallet(1L);
        assertThat(wallet.getUserId(), is(1L));
    }

    @Test
    void findWallet(long l) {
        Wallet wallet = new Wallet();
        wallet.createWallet(1L);
        walletRepository.save(wallet);

        Optional<Wallet> res = walletRepository.findByUserId(1L);
        assertThat(res.get().getUserId(),is(1L));
    }

    @Test
    void updateInvestment() {
        Wallet wallet = new Wallet();
        wallet.createWallet(15L);
        walletRepository.save(wallet);

        AnalysisCoins a1 = new AnalysisCoins("btc", 500, 2);
        AnalysisCoins a2 = new AnalysisCoins("btc", 200, 2);
        AnalysisCoins a3 = new AnalysisCoins("btc", 150, 2);
        List<AnalysisCoins> coinsList = new ArrayList<>();
        coinsList.add(a1);
        coinsList.add(a2);
        coinsList.add(a3);

        Optional<Wallet> res = walletRepository.findByUserId(15L);
        double investment = coinsList.stream().mapToDouble(coin -> coin.getCoinQuantity() * coin.getAvgPrice()).sum();
        res.get().updateInvestment(investment);

        Optional<Wallet> result = walletRepository.findByUserId(15L);


        assertThat(result.get().getInvestment(),is(1700.0));
    }

    @Test
    void updateDayProfit() {
        LeadersCoins l1 = new LeadersCoins("BTC",5000,3.3);
        LeadersCoins l2 = new LeadersCoins("ETH",3000,2.3);

        LeadersCoins l3 = new LeadersCoins("BTC",4000,3.3);
        LeadersCoins l4 = new LeadersCoins("ETH",2000,2.3);

        List<LeadersCoins> list1 = new ArrayList<>();
        List<LeadersCoins> list2 = new ArrayList<>();

        list1.add(l1);
        list1.add(l2);
        list2.add(l3);
        list2.add(l4);

        LeadersId leadersId1 = new LeadersId(1L, list1);
        LeadersId leadersId2 = new LeadersId(2L, list2);

        Wallet wallet1 = new Wallet();
        Wallet wallet2 = new Wallet();
        wallet1.createWallet(1L);
        wallet2.createWallet(2L);

        List<LeadersId> leadersIdList = new ArrayList<>();
        leadersIdList.add(leadersId1);
        leadersIdList.add(leadersId2);

        LeadersDTO dto = new LeadersDTO(leadersIdList);

        List<Double[]> perCoinsRatio = new ArrayList<Double[]>(); // {수익률, 보유비중}
        perCoinsRatio.add(new Double[]{20.0, 0.8919});
        perCoinsRatio.add(new Double[]{2.0, 0.1080});

        double totalProfitRatio = perCoinsRatio.stream().mapToDouble(ratio -> ratio[0] * ratio[1]).sum();
        totalProfitRatio=Math.round(totalProfitRatio*1000)/1000.0;
        System.out.println("\ntotalProfitRatio : "+totalProfitRatio);

        wallet1.updateDayProfit(totalProfitRatio);
        wallet1.updateDayProfit(153215.123);

        wallet1.getProfits().stream().forEach(profit->System.out.println("profit : "+profit.toString()));

        System.out.println("\nwallet profit : "+wallet1.getProfits().get(0).toString());

        assertThat(totalProfitRatio,is(18.054));
    }



    @Test
    void walletAndprofit(){
        Wallet wallet = new Wallet();
        wallet.createWallet(1L);
        wallet.updateInvestment(23.23);

        wallet.updateDayProfit(-123);
        wallet.updateDayProfit(34.567);

        walletRepository.save(wallet);

        Wallet res = walletRepository.findByUserId(1L).get();
        System.out.println(wallet.toString());
        wallet.getProfits().stream().forEach(profit -> System.out.println(profit.getProfit()+" , "+profit.getLocalDate()));
    }
}