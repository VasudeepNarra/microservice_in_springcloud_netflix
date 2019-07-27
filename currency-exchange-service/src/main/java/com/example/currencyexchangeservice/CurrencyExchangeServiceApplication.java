package com.example.currencyexchangeservice;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import brave.sampler.Sampler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@SpringBootApplication
@EnableDiscoveryClient
public class CurrencyExchangeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CurrencyExchangeServiceApplication.class, args);
	}
	@Bean
	public Sampler defaSampler() {
		return Sampler.ALWAYS_SAMPLE;
	}

}

@Log4j2
@RestController
class CurrencyController{
	@Autowired private Environment env;
	@Autowired private ExchangeValueRepository repo;
	
	@GetMapping("/currency-exchange/from/{from}/to/{to}")
	public ExchangeValue retriveExchangeValue(@PathVariable String from ,@PathVariable String to) {
		ExchangeValue ex=repo.findByFromAndTo(from,to);
		ex.setPort(Integer.parseInt(env.getProperty("local.server.port")));
		log.info("{}",ex);
		//return new ExchangeValue(110L, from, to, BigDecimal.valueOf(65),Integer.parseInt(env.getProperty("local.server.port")));
	  return ex;
	}
}


interface ExchangeValueRepository extends JpaRepository<ExchangeValue, Long>{
	ExchangeValue findByFromAndTo(String from,String to);
}

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
class ExchangeValue{
	@Id
	private Long id;
	@Column(name="currency_from")
	private String from;
	@Column(name="currency_to")
	private String to;
	private BigDecimal conversionMultiple;
	private int port;
}