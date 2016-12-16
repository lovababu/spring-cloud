
package org.avol.sale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

@EnableDiscoveryClient
@EnableBinding(Sink.class)
@IntegrationComponentScan
@SpringBootApplication
public class SaleServiceApplication {

    @Autowired
    private SaleRepository saleRepository;

    @Bean
    CommandLineRunner commandLineRunner() {
        return strings -> Stream.of("LenovTab", "SamsungS7", "AppleIPod", "IPhone7", "LG TV").forEach(s -> {
            Sale sale = new Sale();
            sale.setProductName(s);
            sale.setPrice(new Random(35000).nextDouble());
            saleRepository.save(sale);
        });
    }

    public static void main(String[] args) {
        SpringApplication.run(SaleServiceApplication.class, args);
    }
}

@MessageEndpoint
class SaleProcessor {

    @Autowired
    private SaleRepository saleRepository;

    @StreamListener(Sink.INPUT)
    public void processSale(Sale sale) {
        System.out.println("SaleProcessor.processSale >> : " + sale.getProductName());
        saleRepository.save(sale);
    }
}

@RefreshScope
@RestController
class SaleController {

    @Value("${message}")
    private String message;

    @Autowired
    private SaleRepository saleRepository;

    @RequestMapping(path = "/sayHello")
    public String sayHello() {
        System.out.println("SaleController.sayHello: some one called me ?");
        return message;
    }

    @RequestMapping(path = "/sales", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Sale>> fetchAll() {
        List<Sale> sales = saleRepository.findAll();
        return ResponseEntity.ok(sales);
    }
}

//@RepositoryRestResource
interface SaleRepository extends JpaRepository<Sale, Integer> {

    //@RestResource(path = "/by-productname")
    Collection<Sale> findByProductName(@Param("productName") String productName);
}

@Entity
@Table(name = "SALE")
class Sale {

    @Id
    @GeneratedValue
    private int id;

    @Column(name = "PROD_NAE")
    private String productName;

    @Column(name = "PRICE")
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

    @Override
    public String toString() {
        return "Sale{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", price=" + price +
                '}';
    }
}
