package org.sistemaempresarial.mscontablidad.repository;


import org.sistemaempresarial.mscontablidad.entity.CustomerInvoiceDetail;
import org.sistemaempresarial.mscontablidad.entity.CustomerInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CustomerInvoiceDetailRepository extends JpaRepository<CustomerInvoiceDetail, Long> {

    List<CustomerInvoiceDetail> findByCustomerInvoice(CustomerInvoice customerInvoice);

    List<CustomerInvoiceDetail> findByCustomerInvoiceIdOrderById(Long customerInvoiceId);
}
