package org.sistemaempresarial.mscontablidad.service;


import org.sistemaempresarial.mscontablidad.entity.*;
import org.sistemaempresarial.mscontablidad.repository.CustomerPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerPaymentService {

    private final CustomerPaymentRepository customerPaymentRepository;
    private final CustomerInvoiceService customerInvoiceService;
    private final JournalEntryService journalEntryService;
    private final AccountingAccountService accountingAccountService;

    public List<CustomerPayment> findAll() {
        return customerPaymentRepository.findAll();
    }

    public Optional<CustomerPayment> findById(Long id) {
        return customerPaymentRepository.findById(id);
    }

    public Optional<CustomerPayment> findByPaymentNumber(String paymentNumber) {
        return customerPaymentRepository.findByPaymentNumber(paymentNumber);
    }

    public List<CustomerPayment> findByCustomer(Customer customer) {
        return customerPaymentRepository.findByCustomer(customer);
    }

    public List<CustomerPayment> findByCustomerInvoice(CustomerInvoice customerInvoice) {
        return customerPaymentRepository.findByCustomerInvoice(customerInvoice);
    }

    public BigDecimal getTotalPaymentsByInvoice(Long invoiceId) {
        return customerPaymentRepository.getTotalPaymentsByInvoice(invoiceId);
    }

    @Transactional
    public CustomerPayment save(CustomerPayment customerPayment) {
        // Generar número de pago si no se proporciona
        if (customerPayment.getPaymentNumber() == null || customerPayment.getPaymentNumber().isEmpty()) {
            customerPayment.setPaymentNumber(generatePaymentNumber());
        }

        // Validar que el monto no exceda el saldo pendiente
        if (customerPayment.getCustomerInvoice() != null) {
            validatePaymentAmount(customerPayment);
        }

        // Guardar pago
        CustomerPayment savedPayment = customerPaymentRepository.save(customerPayment);

        // Generar asiento contable automáticamente
        if (savedPayment.getJournalEntry() == null) {
            generateJournalEntry(savedPayment);
        }

        // Actualizar estado de la factura si está completamente pagada
        if (savedPayment.getCustomerInvoice() != null) {
            updateInvoiceStatus(savedPayment.getCustomerInvoice());
        }

        return savedPayment;
    }

    @Transactional
    public void deleteById(Long id) {
        CustomerPayment payment = customerPaymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));

        customerPaymentRepository.deleteById(id);

        // Actualizar estado de la factura
        if (payment.getCustomerInvoice() != null) {
            updateInvoiceStatus(payment.getCustomerInvoice());
        }
    }

    private void validatePaymentAmount(CustomerPayment payment) {
        BigDecimal totalPaid = getTotalPaymentsByInvoice(payment.getCustomerInvoice().getId());
        BigDecimal invoiceTotal = payment.getCustomerInvoice().getTotalAmount();
        BigDecimal availableAmount = invoiceTotal.subtract(totalPaid);

        if (payment.getAmount().compareTo(availableAmount) > 0) {
            throw new RuntimeException("Payment amount exceeds available balance. Available: " + availableAmount);
        }
    }

    private void updateInvoiceStatus(CustomerInvoice invoice) {
        BigDecimal totalPaid = getTotalPaymentsByInvoice(invoice.getId());
        BigDecimal invoiceTotal = invoice.getTotalAmount();

        if (totalPaid.compareTo(invoiceTotal) >= 0) {
            invoice.setStatus(CustomerInvoice.InvoiceStatus.PAID);
        } else {
            invoice.setStatus(CustomerInvoice.InvoiceStatus.PENDING);
        }

        customerInvoiceService.save(invoice);
    }

    private void generateJournalEntry(CustomerPayment payment) {
        try {
            // Crear asiento contable para el pago
            JournalEntry journalEntry = new JournalEntry();
            journalEntry.setAccountingPeriod(payment.getAccountingPeriod());
            journalEntry.setEntryDate(payment.getPaymentDate());
            journalEntry.setDescription("Pago de cliente #" + payment.getPaymentNumber());
            journalEntry.setSourceDocumentId(payment.getPaymentNumber());
            journalEntry.setSourceDocumentType("CUSTOMER_PAYMENT");

            // Crear detalles del asiento
            List<JournalEntryDetail> details = List.of(
                    // Débito: Bancos/Caja
                    createJournalEntryDetail(journalEntry, getCashAccount(payment.getPaymentMethod()),
                            "Pago #" + payment.getPaymentNumber(), payment.getAmount(), BigDecimal.ZERO),

                    // Crédito: Cuentas por Cobrar
                    createJournalEntryDetail(journalEntry, payment.getCustomer().getAccountingAccount(),
                            "Pago #" + payment.getPaymentNumber(), BigDecimal.ZERO, payment.getAmount())
            );

            journalEntry.setDetails(details);

            // Guardar asiento
            JournalEntry savedEntry = journalEntryService.save(journalEntry);

            // Actualizar pago con referencia al asiento
            payment.setJournalEntry(savedEntry);
            customerPaymentRepository.save(payment);

        } catch (Exception e) {
            System.err.println("Error generating journal entry for payment: " + e.getMessage());
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

    private AccountingAccount getCashAccount(CustomerPayment.PaymentMethod paymentMethod) {
        String accountCode = switch (paymentMethod) {
            case CASH -> "1010"; // Caja
            case BANK_TRANSFER, CHECK -> "1020"; // Bancos
            case CREDIT_CARD, DEBIT_CARD -> "1030"; // Tarjetas
            default -> "1010";
        };

        return accountingAccountService.findByCodeAndChartOfAccountId(accountCode, 1L)
                .orElseThrow(() -> new RuntimeException("Cash account not found: " + accountCode));
    }

    private String generatePaymentNumber() {
        Long count = customerPaymentRepository.count();
        return String.format("PAG%08d", count + 1);
    }
}