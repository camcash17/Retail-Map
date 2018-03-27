package com.example.retailersapi.controllers;

import com.example.retailersapi.models.Retailer;
import com.example.retailersapi.repositories.RetailerRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class RetailersController {

    @Autowired
    private RetailerRepository retailerRepository;

    @GetMapping("/")
    public Iterable<Retailer> findAllRetailers() {
        return retailerRepository.findAll();
    }

    @GetMapping("/{retailerId}")
    public Retailer findRetailerById(@PathVariable Long retailerId) throws NotFoundException {

        Retailer foundRetailer = retailerRepository.findOne(retailerId);

        if (foundRetailer == null) {
            throw new NotFoundException("Retailer with ID of " + retailerId + " was not found!");
        }

        return foundRetailer;
    }

    @DeleteMapping("/{retailerId}")
    public HttpStatus deleteRetailerById(@PathVariable Long retailerId) throws EmptyResultDataAccessException {

        retailerRepository.delete(retailerId);
        return HttpStatus.OK;
    }

    @PostMapping("/")
    public Retailer createNewRetailer(@RequestBody Retailer newRetailer) {
        return retailerRepository.save(newRetailer);
    }

    @PatchMapping("/{retailerId}")
    public Retailer updateRetailerById(@PathVariable Long retailerId, @RequestBody Retailer retailerRequest) throws NotFoundException {
        Retailer retailerFromDb = retailerRepository.findOne(retailerId);

        if (retailerFromDb == null) {
            throw new NotFoundException("Retailer with ID of " + retailerId + " was not found!");
        }

        retailerFromDb.setOrgName(retailerRequest.getOrgName());
        retailerFromDb.setPrimaryName(retailerRequest.getPrimaryName());
        retailerFromDb.setSecondaryName(retailerRequest.getSecondaryName());
        retailerFromDb.setLat(retailerRequest.getLat());
        retailerFromDb.setLon(retailerRequest.getLon());

        return retailerRepository.save(retailerFromDb);
    }

    @ExceptionHandler
    void handleRetailerNotFound(
            NotFoundException exception,
            HttpServletResponse response) throws IOException {

        response.sendError(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }

    @ExceptionHandler
    void handleDeleteNotFoundException(
            EmptyResultDataAccessException exception,
            HttpServletResponse response) throws IOException {

        response.sendError(HttpStatus.NOT_FOUND.value());
    }
}
