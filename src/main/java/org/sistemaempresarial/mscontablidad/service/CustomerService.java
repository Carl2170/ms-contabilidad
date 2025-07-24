package org.sistemaempresarial.mscontablidad.service;

import org.sistemaempresarial.mscontablidad.entity.Customer;
import org.sistemaempresarial.mscontablidad.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public List<Customer> findAllActive() {
        return customerRepository.findAllActiveOrderByName();
    }

    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    public Optional<Customer> findByCode(String code) {
        return customerRepository.findByCode(code);
    }

    public Optional<Customer> findByDocumentNumber(String documentNumber) {
        return customerRepository.findByDocumentNumber(documentNumber);
    }

    public List<Customer> searchByCodeOrName(String searchTerm) {
        return customerRepository.searchByCodeOrName(searchTerm, searchTerm);
    }

    @Transactional
    public Customer save(Customer customer) {
        // Generar código automático si no se proporciona
        if (customer.getCode() == null || customer.getCode().isEmpty()) {
            customer.setCode(generateCustomerCode());
        }

        // Validar código único
        if (customer.getId() == null) {
            customerRepository.findByCode(customer.getCode())
                    .ifPresent(existing -> {
                        throw new RuntimeException("Customer code already exists: " + customer.getCode());
                    });
        }

        return customerRepository.save(customer);
    }

    @Transactional
    public void deleteById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));

        // Soft delete - marcar como inactivo en lugar de eliminar
        customer.setIsActive(false);
        customerRepository.save(customer);
    }

    private String generateCustomerCode() {
        Long count = customerRepository.count();
        return String.format("CLI%06d", count + 1);
    }
}