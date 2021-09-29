package com.clonecoin.walletwrite.repository;

import com.clonecoin.walletwrite.domain.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {


    Optional<Wallet> findById(Long id); // 리더 지갑 객체 불러오기
    Optional<Wallet> findByUserId(Long id); // 리더 지갑 객체 불러오기

}
