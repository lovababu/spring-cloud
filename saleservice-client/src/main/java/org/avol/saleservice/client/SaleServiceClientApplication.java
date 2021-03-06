package org.avol.saleservice.client;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

//@EnableZuulProxy
@EnableBinding(Source.class)
@EnableCircuitBreaker
//@EnableDiscoveryClient
@EnableEurekaClient
@SpringBootApplication
@EnableFeignClients
public class SaleServiceClientApplication {

    /*@Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    */
    public static void main(String[] args) {
        SpringApplication.run(SaleServiceClientApplication.class, args);
    }
}

@FeignClient("sale-service")
interface SaleServiceClient {

    @RequestMapping(value = "/sayHello", method = RequestMethod.GET)
    String sayHello();

    @RequestMapping(value = "/sales", method = RequestMethod.GET)
    ResponseEntity<List<Sale>> sales();
}


@RestController
@RequestMapping(value = "/sale", produces = MediaType.APPLICATION_JSON_VALUE)
class SaleClientController {

    @Autowired
    private SaleServiceClient saleServiceClient;

    @Autowired
    private Source source;

    public ResponseEntity<List<Sale>> getSalesFallback() {
        return ResponseEntity.ok(Collections.emptyList());
    }

    ParameterizedTypeReference<Resources<Sale>> saleParameterizedTypeReference =
            new ParameterizedTypeReference<Resources<Sale>>() {
            };

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void save(@RequestBody Sale sale) {
        System.out.println("SaleClientController.save : " + sale.getProductName());
        Message message = MessageBuilder.withPayload(sale).build();
        source.output().send(message);
    }

    @HystrixCommand(fallbackMethod = "getSalesFallback")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Sale>> fetchAll() {
        System.out.println("SaleClientController.fetchAll");
        return saleServiceClient.sales();
    }

    @RequestMapping("/sayHello")
    public String sayHello() {
        System.out.println("SaleClientController.sayHello");
        return saleServiceClient.sayHello();
    }

    /*{
        System.out.println("SaleApiGatewayController.sales");
        ResponseEntity<Resources<Sale>> resourceResponseEntity =
                restTemplate.exchange(saleServiceUrl, HttpMethod.GET, null, saleParameterizedTypeReference);
        return resourceResponseEntity
                .getBody()
                .getContent()
                .stream()
                .map(Sale::getProductName)
                .collect(Collectors.toList());
    }*/
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
