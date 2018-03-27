package com.example.retailersapi.controllers;

import com.example.retailersapi.models.Retailer;
import com.example.retailersapi.repositories.RetailerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import org.springframework.http.MediaType;

@RunWith(SpringRunner.class)
@WebMvcTest(RetailersController.class)
public class RetailersControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper jsonObjectMapper;

    private Retailer newRetailer;

    private Retailer updatedSecondRetailer;

    @MockBean
    private RetailerRepository mockRetailerRepository;


    @Test
    public void findAllRetailers_success_returnsStatusOK() throws Exception {

        this.mockMvc
                .perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    public void findAllRetailers_success_returnAllRetailersAsJSON() throws Exception {

        this.mockMvc
                .perform(get("/"))
                .andExpect(jsonPath("$", hasSize(2)));
    }

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

        newRetailer = new Retailer(
                "new_retailer_for_create",
                "New",
                "Retailer",
                "1",
                "2"
        );

        updatedSecondRetailer = new Retailer(
                "updated_orgname",
                "Updated",
                "Info",
                "1",
                "2"
        );


        given(mockRetailerRepository.save(updatedSecondRetailer)).willReturn(updatedSecondRetailer);
        given(mockRetailerRepository.save(newRetailer)).willReturn(newRetailer);

        Iterable<Retailer> mockRetailers =
                Stream.of(firstRetailer, secondRetailer).collect( Collectors.toList());

        given(mockRetailerRepository.findAll()).willReturn(mockRetailers);
        given(mockRetailerRepository.findOne(1L)).willReturn(firstRetailer);
        given(mockRetailerRepository.findOne(4L)).willReturn(null);

        doAnswer(invocation -> {
            throw new EmptyResultDataAccessException("ERROR MESSAGE FROM MOCK!!!", 1234);
        }).when(mockRetailerRepository).delete(4L);

    }

    @Test
    public void findAllRetailers_success_returnOrgNameForEachRetailer() throws Exception {

        this.mockMvc
                .perform(get("/"))
                .andExpect(jsonPath("$[0].orgName", is("org_name")));
    }

    @Test
    public void findAllRetailers_success_returnPrimaryNameForEachRetailer() throws Exception {

        this.mockMvc
                .perform(get("/"))
                .andExpect(jsonPath("$[0].primaryName", is("some primary")));
    }

    @Test
    public void findAllRetailers_success_returnSecondaryNameForEachRetailer() throws Exception {

        this.mockMvc
                .perform(get("/"))
                .andExpect(jsonPath("$[0].secondaryName", is("some secondary")));
    }

    @Test
    public void findAllRetailers_success_returnLatForEachRetailer() throws Exception {

        this.mockMvc
                .perform(get("/"))
                .andExpect(jsonPath("$[0].lat", is("1")));
    }

    @Test
    public void findAllRetailers_success_returnLonForEachRetailer() throws Exception {

        this.mockMvc
                .perform(get("/"))
                .andExpect(jsonPath("$[0].lon", is("2")));
    }

    @Test
    public void findRetailerById_success_returnsStatusOK() throws Exception {

        this.mockMvc
                .perform(get("/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void findRetailerById_success_returnOrgName() throws Exception {

        this.mockMvc
                .perform(get("/1"))
                .andExpect(jsonPath("$.orgName", is("org_name")));
    }

    @Test
    public void findRetailerById_success_returnPrimaryName() throws Exception {

        this.mockMvc
                .perform(get("/1"))
                .andExpect(jsonPath("$.primaryName", is("some primary")));
    }

    @Test
    public void findRetailerById_success_returnSecondaryName() throws Exception {

        this.mockMvc
                .perform(get("/1"))
                .andExpect(jsonPath("$.secondaryName", is("some secondary")));
    }

    @Test
    public void findRetailerById_success_returnLat() throws Exception {

        this.mockMvc
                .perform(get("/1"))
                .andExpect(jsonPath("$.lat", is("1")));
    }

    @Test
    public void findRetailerById_success_returnLon() throws Exception {

        this.mockMvc
                .perform(get("/1"))
                .andExpect(jsonPath("$.lon", is("2")));
    }

    @Test
    public void findRetailerById_failure_retailerNotFoundReturns404() throws Exception {

        this.mockMvc
                .perform(get("/4"))
                .andExpect(status().reason(containsString("Retailer with ID of 4 was not found!")));
    }

    @Test
    public void deleteRetailerById_success_returnsStatusOk() throws Exception {

        this.mockMvc
                .perform(delete("/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteRetailerById_success_deletesViaRepository() throws Exception {

        this.mockMvc.perform(delete("/1"));

        verify(mockRetailerRepository, times(1)).delete(1L);
    }

    @Test
    public void deleteRetailerById_failure_retailerNotFoundReturns404() throws Exception {

        this.mockMvc
                .perform(delete("/4"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createRetailer_success_returnsStatusOk() throws Exception {

        this.mockMvc
                .perform(
                        post("/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonObjectMapper.writeValueAsString(newRetailer))
                )
                .andExpect(status().isOk());
    }

    @Test
    public void createRetailer_success_returnsOrgName() throws Exception {

        this.mockMvc
                .perform(
                        post("/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonObjectMapper.writeValueAsString(newRetailer))
                )
                .andExpect(jsonPath("$.orgName", is("new_retailer_for_create")));
    }

    @Test
    public void createRetailer_success_returnsPrimaryName() throws Exception {

        this.mockMvc
                .perform(
                        post("/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonObjectMapper.writeValueAsString(newRetailer))
                )
                .andExpect(jsonPath("$.primaryName", is("New")));
    }

    @Test
    public void createRetailer_success_returnsSecondaryName() throws Exception {

        this.mockMvc
                .perform(
                        post("/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonObjectMapper.writeValueAsString(newRetailer))
                )
                .andExpect(jsonPath("$.secondaryName", is("Retailer")));
    }

    @Test
    public void createRetailer_success_returnsLat() throws Exception {

        this.mockMvc
                .perform(
                        post("/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonObjectMapper.writeValueAsString(newRetailer))
                )
                .andExpect(jsonPath("$.lat", is("1")));
    }

    @Test
    public void createRetailer_success_returnsLon() throws Exception {

        this.mockMvc
                .perform(
                        post("/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonObjectMapper.writeValueAsString(newRetailer))
                )
                .andExpect(jsonPath("$.lon", is("2")));
    }

    @Test
    public void updateRetailerById_success_returnsStatusOk() throws Exception {

        this.mockMvc
                .perform(
                        patch("/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonObjectMapper.writeValueAsString(updatedSecondRetailer))
                )
                .andExpect(status().isOk());
    }

    @Test
    public void updateRetailerById_success_returnsUpdatedOrgName() throws Exception {

        this.mockMvc
                .perform(
                        patch("/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonObjectMapper.writeValueAsString(updatedSecondRetailer))
                )
                .andExpect(jsonPath("$.orgName", is("updated_orgname")));
    }

    @Test
    public void updateRetailerById_success_returnsUpdatedPrimaryName() throws Exception {

        this.mockMvc
                .perform(
                        patch("/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonObjectMapper.writeValueAsString(updatedSecondRetailer))
                )
                .andExpect(jsonPath("$.primaryName", is("Updated")));
    }

    @Test
    public void updateRetailerById_success_returnsUpdatedSecondaryName() throws Exception {

        this.mockMvc
                .perform(
                        patch("/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonObjectMapper.writeValueAsString(updatedSecondRetailer))
                )
                .andExpect(jsonPath("$.secondaryName", is("Info")));
    }

    @Test
    public void updateRetailerById_success_returnsUpdatedLat() throws Exception {

        this.mockMvc
                .perform(
                        patch("/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonObjectMapper.writeValueAsString(updatedSecondRetailer))
                )
                .andExpect(jsonPath("$.lat", is("1")));
    }

    @Test
    public void updateRetailerById_success_returnsUpdatedLon() throws Exception {

        this.mockMvc
                .perform(
                        patch("/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonObjectMapper.writeValueAsString(updatedSecondRetailer))
                )
                .andExpect(jsonPath("$.lon", is("2")));
    }

    //  Unhappy tests
    @Test
    public void updateRetailerById_failure_retailerNotFoundReturns404() throws Exception {

        this.mockMvc
                .perform(
                        patch("/4")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonObjectMapper.writeValueAsString(updatedSecondRetailer))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateRetailerById_failure_retailerNotFoundReturnsNotFoundErrorMessage() throws Exception {

        this.mockMvc
                .perform(
                        patch("/4")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonObjectMapper.writeValueAsString(updatedSecondRetailer))
                )
                .andExpect(status().reason(containsString("Retailer with ID of 4 was not found!")));
    }



}

