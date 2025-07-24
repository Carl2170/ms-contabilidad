package org.sistemaempresarial.mscontablidad.repository;


import org.sistemaempresarial.mscontablidad.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByCode(String code);

    Optional<Customer> findByDocumentNumber(String documentNumber);

    List<Customer> findByIsActiveTrue();

    List<Customer> findByNameContainingIgnoreCase(String name);

    @Query("SELECT c FROM Customer c WHERE c.isActive = true ORDER BY c.name")
    List<Customer> findAllActiveOrderByName();

    @Query("SELECT c FROM Customer c WHERE c.code LIKE :code% OR c.name LIKE %:name%")
    List<Customer> searchByCodeOrName(@Param("code") String code, @Param("name") String name);
}