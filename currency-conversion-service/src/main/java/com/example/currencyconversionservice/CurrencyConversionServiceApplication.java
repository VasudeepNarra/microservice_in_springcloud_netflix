package com.example.currencyconversionservice;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import brave.sampler.Sampler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@SpringBootApplication
@EnableFeignClients("com.example.currencyconversionservice")
@EnableDiscoveryClient
@EnableCircuitBreaker
public class CurrencyConversionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CurrencyConversionServiceApplication.class, args);
	}
	@Bean
	public Sampler defaSampler() {
		return Sampler.ALWAYS_SAMPLE;
	}

}
@Log4j2
@RestController
class CurrencyConversionController{
	@Autowired private HytrixCurrencyExchangeServce proxy;
	
	@GetMapping("/currency-converter/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversionBean covertCurrency(@PathVariable String from,
			@PathVariable String to,@PathVariable BigDecimal quantity) {
		Map<String,String> mp=new HashMap<>();
		mp.put("from", from);
		mp.put("to", to);
		ResponseEntity<CurrencyConversionBean> responceEntity = new RestTemplate().getForEntity("http://localhost:8000/currency-exchange/from/{from}/to/{to}", CurrencyConversionBean.class,mp);
		CurrencyConversionBean body = responceEntity.getBody();
		return new CurrencyConversionBean(body.getId(), from, to, body.getConversionMultiple(),
				quantity, quantity.multiply(body.getConversionMultiple()), body.getPort());
	}
	
	
	@GetMapping("/currency-converter-fiegn/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversionBean covertCurrencyFiegn(@PathVariable String from,
			@PathVariable String to,@PathVariable BigDecimal quantity) {
		CurrencyConversionBean body =  proxy.retriveExchangeValue(from, to);
		log.info("{}",body);
		return new CurrencyConversionBean(body.getId(), from, to, body.getConversionMultiple(),
				quantity, quantity.multiply(body.getConversionMultiple()), body.getPort());
	}
	
} 

@Component
class HytrixCurrencyExchangeServce{
	@Autowired private CurrencyExchangeServiceProxy proxy;
	
	public CurrencyConversionBean getBookmarksFallback(String from,String to) {
		System.out.println("getBookmarksFallback");
		return new CurrencyConversionBean(1L, from, to, BigDecimal.ONE,  BigDecimal.ONE,  BigDecimal.ONE, 0);
	}

	@HystrixCommand(fallbackMethod = "getBookmarksFallback")
	public CurrencyConversionBean retriveExchangeValue(String from,String to) {
		return proxy.retriveExchangeValue(from,to);
	}
	
}

//@FeignClient(name="currency-exchange-service")//,url="localhost:8000"
@FeignClient(name="netflix-zuul-api-gateway-server")
@RibbonClient(name="currency-exchange-service")
interface CurrencyExchangeServiceProxy{
	@GetMapping("/currency-exchange-service/currency-exchange/from/{from}/to/{to}")
	//@GetMapping("/currency-exchange/from/{from}/to/{to}")
	public CurrencyConversionBean retriveExchangeValue(@PathVariable String from ,@PathVariable String to);
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class CurrencyConversionBean{
	private Long id;
	private String from;
	private String to;
	private BigDecimal conversionMultiple;
	private BigDecimal quantity;
	private BigDecimal totalCalculateAmount;
	private int port;
}