package com.example.limitsservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SpringBootApplication
public class LimitsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LimitsServiceApplication.class, args);
	}

}

@Data
@NoArgsConstructor
@AllArgsConstructor
class LimitConfiguration{
	private int maximum;
	private int minimum;
}

@RestController
class LimitesContrller{
	
	@Autowired
	private ConfigurationValue configuration;
	
	@GetMapping("/limits")
	public LimitConfiguration retrieveLimitsFromConfigurations() {
		LimitConfiguration limitConfiguration = new LimitConfiguration(configuration.getMaximum(), 
				 configuration.getMinimum());
		return limitConfiguration;
	}
}

@Data
@ConfigurationProperties("limits-service")
 class ConfigurationValue {
	private int minimum;
	private int maximum;
}

