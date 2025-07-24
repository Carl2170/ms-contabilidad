package org.sistemaempresarial.mscontablidad.service;


import org.sistemaempresarial.mscontablidad.entity.*;
import org.sistemaempresarial.mscontablidad.repository.CustomerInvoiceRepository;
import org.sistemaempresarial.mscontablidad.repository.CustomerInvoiceDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerInvoiceService {

    private final CustomerInvoiceRepository customerInvoiceRepository;
    private final CustomerInvoiceDetailRepository customerInvoiceDetailRepository;
    private final JournalEntryService journalEntryService;
    private final AccountingAccountService accountingAccountService;

    public List<CustomerInvoice> findAll() {
        return customerInvoiceRepository.findAll();
    }

    public Optional<CustomerInvoice> findById(Long id) {
        return customerInvoiceRepository.findById(id);
    }

    public Optional<CustomerInvoice> findByInvoiceNumber(String invoiceNumber) {
        return customerInvoiceRepository.findByInvoiceNumber(invoiceNumber);
    }

    public List<CustomerInvoice> findByCustomer(Customer customer) {
        return customerInvoiceRepository.findByCustomer(customer);
    }

    public List<CustomerInvoice> findByStatus(CustomerInvoice.InvoiceStatus status) {
        return customerInvoiceRepository.findByStatus(status);
    }

    public List<CustomerInvoice> findOverdueInvoices() {
        return customerInvoiceRepository.findOverdueInvoices(LocalDate.now());
    }

    public BigDecimal getTotalPendingAmountByCustomer(Long customerId) {
        return customerInvoiceRepository.getTotalPendingAmountByCustomer(customerId);
    }

    @Transactional
    public CustomerInvoice save(CustomerInvoice customerInvoice) {
        // Generar número de factura si no se proporciona
        if (customerInvoice.getInvoiceNumber() == null || customerInvoice.getInvoiceNumber().isEmpty()) {
            customerInvoice.setInvoiceNumber(generateInvoiceNumber());
        }

        // Calcular totales
        calculateTotals(customerInvoice);

        // Guardar factura
        CustomerInvoice savedInvoice = customerInvoiceRepository.save(customerInvoice);

        // Generar asiento contable automáticamente
        if (savedInvoice.getJournalEntry() == null) {
            generateJournalEntry(savedInvoice);
        }

        return savedInvoice;
    }

    @Transactional
    public void deleteById(Long id) {
        CustomerInvoice invoice = customerInvoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));

        if (invoice.getStatus() != CustomerInvoice.InvoiceStatus.PENDING) {
            throw new RuntimeException("Cannot delete non-pending invoices");
        }

        customerInvoiceRepository.deleteById(id);
    }

    private void calculateTotals(CustomerInvoice customerInvoice) {
        if (customerInvoice.getDetails() != null && !customerInvoice.getDetails().isEmpty()) {
            BigDecimal subtotal = customerInvoice.getDetails().stream()
                    .map(detail -> detail.getQuantity().multiply(detail.getUnitPrice()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            customerInvoice.setSubtotal(subtotal);
            customerInvoice.setTotalAmount(subtotal.add(customerInvoice.getTaxAmount()));

            // Actualizar totales de detalles
            customerInvoice.getDetails().forEach(detail ->
                    detail.setTotalAmount(detail.getQuantity().multiply(detail.getUnitPrice()))
            );
        }
    }

    private void generateJournalEntry(CustomerInvoice invoice) {
        try {
            // Crear asiento contable para la factura
            JournalEntry journalEntry = new JournalEntry();
            journalEntry.setAccountingPeriod(invoice.getAccountingPeriod());
            journalEntry.setEntryDate(invoice.getInvoiceDate());
            journalEntry.setDescription("Factura de venta #" + invoice.getInvoiceNumber());
            journalEntry.setSourceDocumentId(invoice.getInvoiceNumber());
            journalEntry.setSourceDocumentType("CUSTOMER_INVOICE");

            // Crear detalles del asiento
            List<JournalEntryDetail> details = List.of(
                    // Débito: Cuentas por Cobrar
                    createJournalEntryDetail(journalEntry, invoice.getCustomer().getAccountingAccount(),
                            "Factura #" + invoice.getInvoiceNumber(), invoice.getTotalAmount(), BigDecimal.ZERO),

                    // Crédito: Ingresos por Ventas (asumiendo cuenta 4000)
                    createJournalEntryDetail(journalEntry, getSalesAccount(),
                            "Venta #" + invoice.getInvoiceNumber(), BigDecimal.ZERO, invoice.getSubtotal()),

                    // Crédito: Impuestos por Pagar (si hay impuestos)
                    invoice.getTaxAmount().compareTo(BigDecimal.ZERO) > 0 ?
                            createJournalEntryDetail(journalEntry, getTaxAccount(),
                                    "Impuesto #" + invoice.getInvoiceNumber(), BigDecimal.ZERO, invoice.getTaxAmount()) : null
            ).stream().filter(detail -> detail != null).toList();

            journalEntry.setDetails(details);

            // Guardar asiento
            JournalEntry savedEntry = journalEntryService.save(journalEntry);

            // Actualizar factura con referencia al asiento
            invoice.setJournalEntry(savedEntry);
            customerInvoiceRepository.save(invoice);

        } catch (Exception e) {
            // Log error pero no fallar la creación de la factura
            System.err.println("Error generating journal entry for invoice: " + e.getMessage());
        }
    }

    private JournalEntryDetail createJournalEntryDetail(JournalEntry journalEntry, AccountingAccount account,
                                                        String description, BigDecimal debitAmount, BigDecimal creditAmount) {
        JournalEntryDetail detail = new JournalEntryDetail();
        detail.setJournalEntry(journalEntry);
        detail.setAccount(account);
        detail.setDescription(description);
        detail.setDebitAmount(debitAmount);
        detail.setCreditAmount(creditAmount);
        return detail;
    }

    private AccountingAccount getSalesAccount() {
        // Buscar cuenta de ingresos por ventas (código 4000 o similar)
        return accountingAccountService.findByCodeAndChartOfAccountId("4000", 1L)
                .orElseThrow(() -> new RuntimeException("Sales account not found"));
    }

    private AccountingAccount getTaxAccount() {
        // Buscar cuenta de impuestos por pagar
        return accountingAccountService.findByCodeAndChartOfAccountId("2100", 1L)
                .orElseThrow(() -> new RuntimeException("Tax account not found"));
    }

    private String generateInvoiceNumber() {
        Long count = customerInvoiceRepository.count();
        return String.format("FAC%08d", count + 1);
    }
}