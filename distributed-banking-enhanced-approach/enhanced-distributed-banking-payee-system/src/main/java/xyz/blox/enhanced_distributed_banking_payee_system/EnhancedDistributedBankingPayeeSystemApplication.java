package xyz.blox.enhanced_distributed_banking_payee_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class EnhancedDistributedBankingPayeeSystemApplication {

	@Bean
	public RestTemplate restTemplate() {
        return new RestTemplate();
    }
	
	public static void main(String[] args) {
		SpringApplication.run(EnhancedDistributedBankingPayeeSystemApplication.class, args);
	}

}
