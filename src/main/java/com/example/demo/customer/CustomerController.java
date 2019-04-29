package com.example.demo.customer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @GetMapping()
    public List<Customer> getCustomers() {
        return customerService.retrieveCustomer();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomer(@PathVariable Long id) {
        Optional<Customer> customer = customerService.retrieveCustomer(id);
        if (!customer.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(customer);
    }

    @GetMapping(params = "name")
    public List<Customer> getCustomer(@RequestParam(value = "name") String name) {
        return customerService.retrieveCustomer(name);
    }

    @PostMapping()
    public ResponseEntity<?> postCustomer(@Valid @RequestBody Customer body) {
        System.out.println("AAAA Post");
        Customer customer = customerService.createCustomer(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(customer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> putCustomer(@PathVariable Long id, @Valid @RequestBody Customer body) {
        Optional<Customer> customer = customerService.updateCustomer(id, body);
        if (!customer.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
        if (!customerService.deleteCustomer(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/generate")
    public void generateCustomerFile(HttpServletResponse response) {
        List<Customer> customerList = customerService.retrieveCustomer();
        try {
            String filename = customerService.generateCustomerFile(customerList);
            // get your file as InputStream
            InputStream is = new FileInputStream(filename);
            // copy it to response's OutputStream
            IOUtils.copy(is, response.getOutputStream());

            response.setContentType("application/x-msdownload");
            response.setHeader("Content-disposition", "attachment; filename=" + filename);
            response.flushBuffer();

            is.close();
            File file = new File(filename);
            System.out.println(file);
            if (file.delete()) {
                System.out.println("Delete");
            } else {
                System.out.println("Not Delete");
            }
        } catch (IOException ex) {
            // Log.info("Error writing file to output stream. Filename was '{}'", filename,
            // ex);
            throw new RuntimeException("IOError writing file to output stream");
        }
    }
}