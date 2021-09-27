package com.clonecoin.walletwrite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient

public class WalletwriteApplication {

	public static void main(String[] args) {
		SpringApplication.run(WalletwriteApplication.class, args);
		System.out.println("\n\n 작동 시작 \n\n");
	}

}
