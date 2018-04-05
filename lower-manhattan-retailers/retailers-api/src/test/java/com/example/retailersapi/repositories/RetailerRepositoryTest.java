package com.example.retailersapi.repositories;

import com.example.retailersapi.models.Retailer;
import com.google.common.collect.Iterables;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RetailerRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RetailerRepository retailerRepository;

    @Before
    public void setUp() {
        Retailer firstRetailer = new Retailer(
                "org_name",
                "some primary",
                "some secondary",
                "1",
                "2"
        );

        Retailer secondRetailer = new Retailer(
                "second_org",
                "some other primary",
                "some other secondary",
                "1",
                "2"
        );

        entityManager.persist(firstRetailer);
        entityManager.persist(secondRetailer);
        entityManager.flush();
    }

    @Test
    public void findAll_returnsAllRetailers() {
        Iterable<Retailer> retailersFromDb = retailerRepository.findAll();

        assertThat(Iterables.size(retailersFromDb), is(2));
    }

    @Test
    public void findAll_returnsOrgName() {
        Iterable<Retailer> retailersFromDb = retailerRepository.findAll();

        String secondRetailersOrgName = Iterables.get(retailersFromDb, 1).getOrgName();

        assertThat(secondRetailersOrgName, is("second_org"));
    }

    @Test
    public void findAll_returnsPrimaryName() {
        Iterable<Retailer> retailersFromDb = retailerRepository.findAll();

        String secondRetailersPrimaryName = Iterables.get(retailersFromDb, 1).getPrimaryName();

        assertThat(secondRetailersPrimaryName, is("some other primary"));
    }

    @Test
    public void findAll_returnsSecondaryName() {
        Iterable<Retailer> retailersFromDb = retailerRepository.findAll();

        String secondRetailersSecondaryName = Iterables.get(retailersFromDb, 1).getSecondaryName();

        assertThat(secondRetailersSecondaryName, is("some other secondary"));
    }

}
