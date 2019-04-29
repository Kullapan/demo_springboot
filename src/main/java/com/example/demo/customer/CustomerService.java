package com.example.demo.customer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    private CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository repository) {
        this.customerRepository = repository;
    }

    public List<Customer> retrieveCustomer() {
        List<Customer> customers = (List<Customer>) customerRepository.findAll();
        return customers;
    }

    public Optional<Customer> retrieveCustomer(Long id) {
        return customerRepository.findById(id);
    }

    public List<Customer> retrieveCustomer(String name) {
        return customerRepository.findByFirstName(name);
    }

    public Customer createCustomer(Customer customer) {
        customer.setId(null);
        return customerRepository.save(customer);
    }

    public Optional<Customer> updateCustomer(Long id, Customer customer) {
        Optional<Customer> customerOptional = customerRepository.findById(id);
        if (!customerOptional.isPresent()) {
            return customerOptional;
        }
        customer.setId(id);
        return Optional.of(customerRepository.save(customer));
    }

    public boolean deleteCustomer(Long id) {
        try {
            customerRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    public String generateCustomerFile(List<Customer> customerList) throws IOException {

        String filename = "c:/Workspace/output.txt";
        File file = new File(filename);

        // Create the file
        if (file.createNewFile()) {
            System.out.println("File is created!");
        } else {
            System.out.println("File already exists.");
        }

        // Write Content
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
        for (Customer cust : customerList) {
            System.out.println(cust);
            System.out.println(cust.getId());
            StringBuilder fileContent = new StringBuilder();
            fileContent.append(cust.getId());
            fileContent.append("@#$");
            fileContent.append(cust.getFirstName());
            fileContent.append("@#$");
            fileContent.append(cust.getLastName());
            System.out.println(fileContent.toString());
            writer.write(fileContent.toString());
            writer.newLine();
        }

        writer.close();

        return filename;
    }

   

}