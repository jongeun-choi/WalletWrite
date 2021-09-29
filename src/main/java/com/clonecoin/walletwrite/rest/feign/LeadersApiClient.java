package com.clonecoin.walletwrite.rest.feign;

import com.clonecoin.walletwrite.domain.event.LeadersDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "leaders-api", url = "http://localhost:8080/analysis/leaders")
public interface LeadersApiClient {

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    LeadersDTO getLeaders();
}
