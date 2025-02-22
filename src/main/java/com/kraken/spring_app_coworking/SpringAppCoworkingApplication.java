package com.kraken.spring_app_coworking;


import com.kraken.spring_app_coworking.Services.impl.DataLoader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;

@SpringBootApplication(exclude = { RedisRepositoriesAutoConfiguration.class} )
public class  SpringAppCoworkingApplication  {

	public static void main(String[] args) {
		SpringApplication.run(SpringAppCoworkingApplication.class, args);
	}


}
