package pl.joboffers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import pl.joboffers.infrastructure.offer.http.OfferFetcherRestTemplateConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(OfferFetcherRestTemplateConfigurationProperties.class)
@EnableScheduling
public class JobOffersApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobOffersApplication.class, args);
    }

}
