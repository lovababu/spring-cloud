
package org.avol.sale;

import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.websocket.server.PathParam;
import java.util.Collection;
import java.util.Random;
import java.util.stream.Stream;

@EnableDiscoveryClient
//@EnableBinding(Sink.class)
//@IntegrationComponentScan
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

/*@MessageEndpoint
class SaleProcessor {
	@ServiceActivator(inputChannel = Sink.INPUT)
	public void processSale(Sale sale) {
      //persist in db
	}
}*/

@RefreshScope
@RestController
class SaleController {

	@Value("${message}")
	private String message;

	@RequestMapping(path = "/sayHello")
	public String sayHello() {
		return message;
	}
}

@RepositoryRestResource
interface SaleRepository extends JpaRepository<Sale, Integer> {

	@RestResource(path = "/by-productname")
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
