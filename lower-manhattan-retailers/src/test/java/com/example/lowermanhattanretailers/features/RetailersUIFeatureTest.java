package com.example.lowermanhattanretailers.features;

import com.example.lowermanhattanretailers.models.Retailer;
import com.example.lowermanhattanretailers.repositories.RetailerRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RetailersUIFeatureTest {

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

        System.setProperty("selenide.browser", "Chrome");

        open("http://www.google.com");

        WebElement queryBox = $(By.name("q"));
        queryBox.sendKeys("Kent Beck");
        queryBox.submit();

        $("body").shouldHave(text("extreme programming"));

    }

}
