package com.clonecoin.walletwrite.rest.feign;

import com.clonecoin.walletwrite.domain.event.LeadersDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "leaders-api", url = "http://15.165.49.213:8003/analysis/getall")
public interface LeadersApiClient {

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    LeadersDTO getLeaders();
}
