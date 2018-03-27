package com.example.lowermanhattanretailers.features;

import com.example.lowermanhattanretailers.models.Retailer;
import com.example.lowermanhattanretailers.repositories.RetailerRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.Is.is;
import static io.restassured.http.ContentType.JSON;

import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RetailersApiFeatureTest {

    @Autowired
    private RetailerRepository retailerRepository;

    @Before
    public void setUp() {
        retailerRepository.deleteAll();
    }

    @After
    public void tearDown() {
        retailerRepository.deleteAll();
    }

    @Test
    public void shouldAllowFullCrudForARetailer() throws Exception {

        Retailer firstRetailer = new Retailer(
                "retailer",
                "Fitness",
                "Gym",
                "1",
                "2"
        );

        Retailer secondRetailer = new Retailer(
                "other_retailer",
                "Dining",
                "Fastfood",
                "1",
                "2"
        );

        Stream.of(firstRetailer, secondRetailer)
                .forEach(retailer -> {
                    retailerRepository.save(retailer);
                });

        when()
                .get("http://localhost:8080/retailers/")
                .then()
                .statusCode(is(200))
                .and().body(containsString("retailer"))
                .and().body(containsString("other_retailer"));

        // Test creating a Retailer
        Retailer retailerNotYetInDb = new Retailer(
                "new_retailer",
                "Not",
                "Yet Created",
                "1",
                "2"
        );

        given()
                .contentType(JSON)
                .and().body(retailerNotYetInDb)
                .when()
                .post("http://localhost:8080/retailers")
                .then()
                .statusCode(is(200))
                .and().body(containsString("new_retailer"));

        // Test get all Retailers
        when()
                .get("http://localhost:8080/retailers/")
                .then()
                .statusCode(is(200))
                .and().body(containsString("retailer"))
                .and().body(containsString("other_retailer"))
                .and().body(containsString("Yet Created"));

        // Test finding one Retailer by ID
        when()
                .get("http://localhost:8080/retailers/" + secondRetailer.getId())
                .then()
                .statusCode(is(200))
                .and().body(containsString("retailer"))
                .and().body(containsString("other_retailer"));

        // Test updating a Retailer
        secondRetailer.setOrgName("changed_name");

        given()
                .contentType(JSON)
                .and().body(secondRetailer)
                .when()
                .patch("http://localhost:8080/retailers/" + secondRetailer.getId())
                .then()
                .statusCode(is(200))
                .and().body(containsString("changed_name"));

        // Test deleting a Retailer
        when()
                .delete("http://localhost:8080/retailers/" + secondRetailer.getId())
                .then()
                .statusCode(is(200));
    }
}
