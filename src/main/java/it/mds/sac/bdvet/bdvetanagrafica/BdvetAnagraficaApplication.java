package it.mds.sac.bdvet.bdvetanagrafica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"it.mds.sac"})
@EnableAutoConfiguration
@EnableCaching
@EnableConfigurationProperties
public class BdvetAnagraficaApplication {

	public static void main(String[] args) {
		
		SpringApplication.run(BdvetAnagraficaApplication.class, args);
	}

}
