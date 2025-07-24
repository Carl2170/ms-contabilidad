-- =====================================================
-- SCRIPT DE DATOS SEMILLA PARA EL SISTEMA CONTABLE
-- =====================================================

-- 1. ACCOUNT NATURES (Naturalezas de Cuenta)
INSERT INTO account_natures (id, name, description, created_at, updated_at) VALUES
                                                                                (1, 'DEBIT', 'Cuentas de naturaleza débito (Activos, Gastos)', NOW(), NOW()),
                                                                                (2, 'CREDIT', 'Cuentas de naturaleza crédito (Pasivos, Patrimonio, Ingresos)', NOW(), NOW())
    ON CONFLICT (id) DO NOTHING;

-- 2. CHART OF ACCOUNTS (Plan de Cuentas)
INSERT INTO chart_of_accounts (id, name, description, is_active, created_at, updated_at) VALUES
    (1, 'Plan Contable Principal', 'Plan de cuentas principal del sistema', true, NOW(), NOW())
    ON CONFLICT (id) DO NOTHING;

-- 3. ACCOUNTING PERIODS (Períodos Contables)
INSERT INTO accounting_periods (id, name, start_date, end_date, is_closed, chart_of_account_id, created_at, updated_at) VALUES
                                                                                                                            (1, 'Enero 2024', '2024-01-01', '2024-01-31', false, 1, NOW(), NOW()),
                                                                                                                            (2, 'Febrero 2024', '2024-02-01', '2024-02-29', false, 1, NOW(), NOW()),
                                                                                                                            (3, 'Marzo 2024', '2024-03-01', '2024-03-31', false, 1, NOW(), NOW()),
                                                                                                                            (4, 'Abril 2024', '2024-04-01', '2024-04-30', false, 1, NOW(), NOW()),
                                                                                                                            (5, 'Mayo 2024', '2024-05-01', '2024-05-31', false, 1, NOW(), NOW()),
                                                                                                                            (6, 'Junio 2024', '2024-06-01', '2024-06-30', false, 1, NOW(), NOW()),
                                                                                                                            (7, 'Julio 2024', '2024-07-01', '2024-07-31', false, 1, NOW(), NOW()),
                                                                                                                            (8, 'Agosto 2024', '2024-08-01', '2024-08-31', false, 1, NOW(), NOW()),
                                                                                                                            (9, 'Septiembre 2024', '2024-09-01', '2024-09-30', false, 1, NOW(), NOW()),
                                                                                                                            (10, 'Octubre 2024', '2024-10-01', '2024-10-31', false, 1, NOW(), NOW()),
                                                                                                                            (11, 'Noviembre 2024', '2024-11-01', '2024-11-30', false, 1, NOW(), NOW()),
                                                                                                                            (12, 'Diciembre 2024', '2024-12-01', '2024-12-31', false, 1, NOW(), NOW())
    ON CONFLICT (id) DO NOTHING;

-- 4. ACCOUNTING ACCOUNTS (Cuentas Contables)
INSERT INTO accounting_accounts (id, code, name, description, account_nature_id, chart_of_account_id, parent_account_id, is_active, created_at, updated_at) VALUES
-- ACTIVOS (1000-1999)
(1, '1000', 'ACTIVOS', 'Activos totales', 1, 1, NULL, true, NOW(), NOW()),
(2, '1100', 'ACTIVO CORRIENTE', 'Activos corrientes', 1, 1, 1, true, NOW(), NOW()),
(3, '1010', 'CAJA', 'Caja general', 1, 1, 2, true, NOW(), NOW()),
(4, '1020', 'BANCOS', 'Cuentas bancarias', 1, 1, 2, true, NOW(), NOW()),
(5, '1030', 'TARJETAS', 'Pagos con tarjetas', 1, 1, 2, true, NOW(), NOW()),
(6, '1200', 'CUENTAS POR COBRAR', 'Cuentas por cobrar clientes', 1, 1, 2, true, NOW(), NOW()),
(7, '1210', 'CLIENTES NACIONALES', 'Clientes del mercado nacional', 1, 1, 6, true, NOW(), NOW()),
(8, '1220', 'CLIENTES EXTRANJEROS', 'Clientes del mercado internacional', 1, 1, 6, true, NOW(), NOW()),

-- PASIVOS (2000-2999)
(9, '2000', 'PASIVOS', 'Pasivos totales', 2, 1, NULL, true, NOW(), NOW()),
(10, '2100', 'PASIVO CORRIENTE', 'Pasivos corrientes', 2, 1, 9, true, NOW(), NOW()),
(11, '2200', 'CUENTAS POR PAGAR', 'Cuentas por pagar proveedores', 2, 1, 10, true, NOW(), NOW()),
(12, '2210', 'PROVEEDORES NACIONALES', 'Proveedores del mercado nacional', 2, 1, 11, true, NOW(), NOW()),
(13, '2220', 'PROVEEDORES EXTRANJEROS', 'Proveedores del mercado internacional', 2, 1, 11, true, NOW(), NOW()),

-- PATRIMONIO (3000-3999)
(14, '3000', 'PATRIMONIO', 'Patrimonio total', 2, 1, NULL, true, NOW(), NOW()),
(15, '3100', 'CAPITAL', 'Capital social', 2, 1, 14, true, NOW(), NOW()),
(16, '3200', 'UTILIDADES RETENIDAS', 'Utilidades acumuladas', 2, 1, 14, true, NOW(), NOW()),

-- INGRESOS (4000-4999)
(17, '4000', 'INGRESOS', 'Ingresos totales', 2, 1, NULL, true, NOW(), NOW()),
(18, '4100', 'INGRESOS OPERACIONALES', 'Ingresos por ventas', 2, 1, 17, true, NOW(), NOW()),
(19, '4110', 'VENTAS NACIONALES', 'Ventas en el mercado nacional', 2, 1, 18, true, NOW(), NOW()),
(20, '4120', 'VENTAS EXTRANJERAS', 'Ventas en el mercado internacional', 2, 1, 18, true, NOW(), NOW()),

-- GASTOS (5000-5999)
(21, '5000', 'GASTOS', 'Gastos totales', 1, 1, NULL, true, NOW(), NOW()),
(22, '5100', 'GASTOS OPERACIONALES', 'Gastos de operación', 1, 1, 21, true, NOW(), NOW()),
(23, '5110', 'GASTOS DE VENTAS', 'Gastos relacionados con ventas', 1, 1, 22, true, NOW(), NOW()),
(24, '5120', 'GASTOS ADMINISTRATIVOS', 'Gastos administrativos', 1, 1, 22, true, NOW(), NOW())
    ON CONFLICT (id) DO NOTHING;

-- 5. CUSTOMERS (Clientes)
INSERT INTO customers (id, code, name, document_type, document_number, email, phone, address, accounting_account_id, is_active, created_at, updated_at) VALUES
                                                                                                                                                            (1, 'CLI001', 'ACME Corporation S.A.S.', 'NIT', '900123456-1', 'contacto@acme.com', '+57 1 234-5678', 'Calle 100 #15-20, Bogotá', 7, true, NOW(), NOW()),
                                                                                                                                                            (2, 'CLI002', 'Tech Solutions Ltda.', 'NIT', '800987654-2', 'ventas@techsolutions.com', '+57 1 987-6543', 'Carrera 7 #32-45, Bogotá', 7, true, NOW(), NOW()),
                                                                                                                                                            (3, 'CLI003', 'María González', 'CC', '12345678', 'maria.gonzalez@email.com', '+57 300 123-4567', 'Calle 45 #12-34, Medellín', 7, true, NOW(), NOW()),
                                                                                                                                                            (4, 'CLI004', 'Carlos Rodríguez', 'CC', '87654321', 'carlos.rodriguez@email.com', '+57 310 987-6543', 'Carrera 15 #67-89, Cali', 7, true, NOW(), NOW()),
                                                                                                                                                            (5, 'CLI005', 'Innovate S.A.', 'NIT', '700555444-3', 'info@innovate.co', '+57 1 555-4444', 'Avenida 68 #45-67, Bogotá', 7, true, NOW(), NOW()),
                                                                                                                                                            (6, 'CLI006', 'Global Trade Inc.', 'NIT', '600333222-4', 'orders@globaltrade.com', '+57 1 333-2222', 'Zona Franca, Barranquilla', 8, true, NOW(), NOW()),
                                                                                                                                                            (7, 'CLI007', 'Ana Martínez', 'CC', '11223344', 'ana.martinez@email.com', '+57 320 112-2334', 'Calle 80 #25-30, Bucaramanga', 7, true, NOW(), NOW()),
                                                                                                                                                            (8, 'CLI008', 'Digital Services Co.', 'NIT', '500111222-5', 'hello@digitalservices.co', '+57 1 111-2222', 'Carrera 11 #93-45, Bogotá', 7, true, NOW(), NOW()),
                                                                                                                                                            (9, 'CLI009', 'Luis Fernández', 'CC', '99887766', 'luis.fernandez@email.com', '+57 315 998-8776', 'Avenida 30 #50-25, Pereira', 7, false, NOW(), NOW()),
                                                                                                                                                            (10, 'CLI010', 'Export Solutions S.A.S.', 'NIT', '400999888-6', 'export@solutions.com', '+57 1 999-8888', 'Calle 26 #68-35, Bogotá', 8, true, NOW(), NOW())
    ON CONFLICT (id) DO NOTHING;

-- 6. CUSTOMER INVOICES (Facturas de Clientes)
INSERT INTO customer_invoices (id, invoice_number, customer_id, accounting_period_id, invoice_date, due_date, subtotal, tax_amount, total_amount, status, created_at, updated_at) VALUES
                                                                                                                                                                                      (1, 'FAC-2024-001', 1, 1, '2024-01-15', '2024-02-14', 1000000.00, 190000.00, 1190000.00, 'PENDING', NOW(), NOW()),
                                                                                                                                                                                      (2, 'FAC-2024-002', 2, 1, '2024-01-20', '2024-02-19', 2500000.00, 475000.00, 2975000.00, 'PAID', NOW(), NOW()),
                                                                                                                                                                                      (3, 'FAC-2024-003', 3, 2, '2024-02-05', '2024-03-06', 750000.00, 142500.00, 892500.00, 'PENDING', NOW(), NOW()),
                                                                                                                                                                                      (4, 'FAC-2024-004', 4, 2, '2024-02-10', '2024-03-11', 1800000.00, 342000.00, 2142000.00, 'OVERDUE', NOW(), NOW()),
                                                                                                                                                                                      (5, 'FAC-2024-005', 5, 2, '2024-02-25', '2024-03-26', 3200000.00, 608000.00, 3808000.00, 'PAID', NOW(), NOW()),
                                                                                                                                                                                      (6, 'FAC-2024-006', 6, 3, '2024-03-10', '2024-04-09', 5500000.00, 1045000.00, 6545000.00, 'PENDING', NOW(), NOW()),
                                                                                                                                                                                      (7, 'FAC-2024-007', 7, 3, '2024-03-15', '2024-04-14', 950000.00, 180500.00, 1130500.00, 'PAID', NOW(), NOW()),
                                                                                                                                                                                      (8, 'FAC-2024-008', 8, 3, '2024-03-20', '2024-04-19', 1200000.00, 228000.00, 1428000.00, 'PENDING', NOW(), NOW()),
                                                                                                                                                                                      (9, 'FAC-2024-009', 1, 4, '2024-04-05', '2024-05-04', 2200000.00, 418000.00, 2618000.00, 'PENDING', NOW(), NOW()),
                                                                                                                                                                                      (10, 'FAC-2024-010', 10, 4, '2024-04-12', '2024-05-11', 4800000.00, 912000.00, 5712000.00, 'PAID', NOW(), NOW())
    ON CONFLICT (id) DO NOTHING;

-- 7. CUSTOMER INVOICE DETAILS (Detalles de Facturas)
INSERT INTO customer_invoice_details (id, customer_invoice_id, description, quantity, unit_price, total_amount) VALUES
-- Factura 1
(1, 1, 'Desarrollo de aplicación web', 1.00, 800000.00, 800000.00),
(2, 1, 'Hosting y dominio anual', 1.00, 200000.00, 200000.00),

-- Factura 2
(3, 2, 'Consultoría en sistemas', 20.00, 100000.00, 2000000.00),
(4, 2, 'Licencias de software', 1.00, 500000.00, 500000.00),

-- Factura 3
(5, 3, 'Diseño gráfico', 5.00, 150000.00, 750000.00),

-- Factura 4
(6, 4, 'Desarrollo móvil', 1.00, 1500000.00, 1500000.00),
(7, 4, 'Testing y QA', 10.00, 30000.00, 300000.00),

-- Factura 5
(8, 5, 'Infraestructura cloud', 12.00, 200000.00, 2400000.00),
(9, 5, 'Soporte técnico', 4.00, 200000.00, 800000.00),

-- Factura 6
(10, 6, 'Integración de sistemas', 1.00, 3000000.00, 3000000.00),
(11, 6, 'Capacitación de usuarios', 25.00, 100000.00, 2500000.00),

-- Factura 7
(12, 7, 'Mantenimiento de software', 1.00, 950000.00, 950000.00),

-- Factura 8
(13, 8, 'Desarrollo de API', 1.00, 1200000.00, 1200000.00),

-- Factura 9
(14, 9, 'Consultoría especializada', 22.00, 100000.00, 2200000.00),

-- Factura 10
(15, 10, 'Solución empresarial completa', 1.00, 4800000.00, 4800000.00)
    ON CONFLICT (id) DO NOTHING;

-- 8. CUSTOMER PAYMENTS (Pagos de Clientes)
INSERT INTO customer_payments (id, payment_number, customer_id, customer_invoice_id, accounting_period_id, payment_date, amount, payment_method, reference, created_at) VALUES
                                                                                                                                                                            (1, 'PAG-2024-001', 2, 2, 1, '2024-01-25', 2975000.00, 'BANK_TRANSFER', 'TRF-001-2024', NOW()),
                                                                                                                                                                            (2, 'PAG-2024-002', 5, 5, 2, '2024-02-28', 3808000.00, 'CHECK', 'CHK-12345', NOW()),
                                                                                                                                                                            (3, 'PAG-2024-003', 7, 7, 3, '2024-03-18', 1130500.00, 'CASH', NULL, NOW()),
                                                                                                                                                                            (4, 'PAG-2024-004', 10, 10, 4, '2024-04-15', 5712000.00, 'BANK_TRANSFER', 'TRF-002-2024', NOW()),
                                                                                                                                                                            (5, 'PAG-2024-005', 1, 1, 2, '2024-02-10', 500000.00, 'CREDIT_CARD', 'CC-VISA-1234', NOW()),
                                                                                                                                                                            (6, 'PAG-2024-006', 3, 3, 3, '2024-03-01', 400000.00, 'DEBIT_CARD', 'DC-MASTER-5678', NOW()),
                                                                                                                                                                            (7, 'PAG-2024-007', 8, 8, 4, '2024-04-01', 700000.00, 'BANK_TRANSFER', 'TRF-003-2024', NOW()),
                                                                                                                                                                            (8, 'PAG-2024-008', 1, 9, 4, '2024-04-20', 1000000.00, 'CHECK', 'CHK-67890', NOW()),
                                                                                                                                                                            (9, 'PAG-2024-009', 6, NULL, 3, '2024-03-25', 2000000.00, 'BANK_TRANSFER', 'TRF-004-2024', NOW()),
                                                                                                                                                                            (10, 'PAG-2024-010', 4, NULL, 3, '2024-03-30', 1000000.00, 'CASH', NULL, NOW())
    ON CONFLICT (id) DO NOTHING;

-- 9. JOURNAL ENTRIES (Asientos Contables) - Ejemplos básicos
INSERT INTO journal_entries (id, accounting_period_id, entry_date, description, source_document_id, source_document_type, created_at, updated_at) VALUES
                                                                                                                                                      (1, 1, '2024-01-25', 'Pago de cliente ACME Corporation', 'PAG-2024-001', 'CUSTOMER_PAYMENT', NOW(), NOW()),
                                                                                                                                                      (2, 2, '2024-02-28', 'Pago de cliente Innovate S.A.', 'PAG-2024-002', 'CUSTOMER_PAYMENT', NOW(), NOW()),
                                                                                                                                                      (3, 3, '2024-03-18', 'Pago de cliente Ana Martínez', 'PAG-2024-003', 'CUSTOMER_PAYMENT', NOW(), NOW()),
                                                                                                                                                      (4, 4, '2024-04-15', 'Pago de cliente Export Solutions', 'PAG-2024-004', 'CUSTOMER_PAYMENT', NOW(), NOW()),
                                                                                                                                                      (5, 1, '2024-01-15', 'Factura a ACME Corporation', 'FAC-2024-001', 'CUSTOMER_INVOICE', NOW(), NOW())
    ON CONFLICT (id) DO NOTHING;

-- 10. JOURNAL ENTRY DETAILS (Detalles de Asientos Contables)
INSERT INTO journal_entry_details (id, journal_entry_id, account_id, description, debit_amount, credit_amount) VALUES
-- Asiento 1: Pago de cliente (Débito: Banco, Crédito: Cliente)
(1, 1, 4, 'Pago recibido por transferencia', 2975000.00, 0.00),
(2, 1, 7, 'Abono a cuenta cliente Tech Solutions', 0.00, 2975000.00),

-- Asiento 2: Pago de cliente (Débito: Banco, Crédito: Cliente)
(3, 2, 4, 'Pago recibido por cheque', 3808000.00, 0.00),
(4, 2, 7, 'Abono a cuenta cliente Innovate S.A.', 0.00, 3808000.00),

-- Asiento 3: Pago en efectivo (Débito: Caja, Crédito: Cliente)
(5, 3, 3, 'Pago recibido en efectivo', 1130500.00, 0.00),
(6, 3, 7, 'Abono a cuenta cliente Ana Martínez', 0.00, 1130500.00),

-- Asiento 4: Pago por transferencia (Débito: Banco, Crédito: Cliente)
(7, 4, 4, 'Pago recibido por transferencia', 5712000.00, 0.00),
(8, 4, 8, 'Abono a cuenta cliente Export Solutions', 0.00, 5712000.00),

-- Asiento 5: Facturación (Débito: Cliente, Crédito: Ventas)
(9, 5, 7, 'Factura FAC-2024-001 a ACME Corporation', 1190000.00, 0.00),
(10, 5, 19, 'Venta de servicios', 0.00, 1000000.00),
(11, 5, 19, 'IVA por pagar', 0.00, 190000.00)
    ON CONFLICT (id) DO NOTHING;

-- Actualizar secuencias para PostgreSQL
SELECT setval('account_natures_id_seq', (SELECT MAX(id) FROM account_natures));
SELECT setval('chart_of_accounts_id_seq', (SELECT MAX(id) FROM chart_of_accounts));
SELECT setval('accounting_periods_id_seq', (SELECT MAX(id) FROM accounting_periods));
SELECT setval('accounting_accounts_id_seq', (SELECT MAX(id) FROM accounting_accounts));
SELECT setval('customers_id_seq', (SELECT MAX(id) FROM customers));
SELECT setval('customer_invoices_id_seq', (SELECT MAX(id) FROM customer_invoices));
SELECT setval('customer_invoice_details_id_seq', (SELECT MAX(id) FROM customer_invoice_details));
SELECT setval('customer_payments_id_seq', (SELECT MAX(id) FROM customer_payments));
SELECT setval('journal_entries_id_seq', (SELECT MAX(id) FROM journal_entries));
SELECT setval('journal_entry_details_id_seq', (SELECT MAX(id) FROM journal_entry_details));

-- Mensaje de confirmación
SELECT 'Datos semilla insertados correctamente' as resultado;
