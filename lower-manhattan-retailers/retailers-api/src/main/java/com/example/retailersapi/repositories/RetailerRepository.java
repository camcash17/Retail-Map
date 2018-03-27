package com.example.retailersapi.repositories;

import com.example.retailersapi.models.Retailer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableEurekaClient
@RestController
public interface RetailerRepository extends CrudRepository<Retailer, Long> {

}
