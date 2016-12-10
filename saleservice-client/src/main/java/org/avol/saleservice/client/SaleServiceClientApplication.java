package org.avol.saleservice.client;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@EnableZuulProxy
@EnableBinding(Source.class)
@EnableCircuitBreaker
@EnableDiscoveryClient
@SpringBootApplication
public class SaleServiceClientApplication {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(SaleServiceClientApplication.class, args);
    }
}

@RestController
@RequestMapping("/sales")
class SaleApiGatewayController {

    @Value("${saleServiceUrl}")
    private String saleServiceUrl;

    @Autowired
    public RestTemplate restTemplate;

    @Autowired
    private Source source;

    public Collection<String> getSalesFallback() {
        return Collections.emptyList();
    }

    ParameterizedTypeReference<Resources<Sale>> saleParameterizedTypeReference =
            new ParameterizedTypeReference<Resources<Sale>>() {
            };

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void save(@RequestBody Sale sale) {
        Message message = MessageBuilder.withPayload(sale).build();
        source.output().send(message);


    }

    @HystrixCommand(fallbackMethod = "getSalesFallback")
    @RequestMapping(method = RequestMethod.GET)
    public Collection<String> sales() {
        ResponseEntity<Resources<Sale>> resourceResponseEntity =
                restTemplate.exchange(saleServiceUrl, HttpMethod.GET, null, saleParameterizedTypeReference);
        return resourceResponseEntity
                .getBody()
                .getContent()
                .stream()
                .map(Sale::getProductName)
                .collect(Collectors.toList());
    }
}

class Sale {
    private int id;
    private String productName;
    private Double price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
