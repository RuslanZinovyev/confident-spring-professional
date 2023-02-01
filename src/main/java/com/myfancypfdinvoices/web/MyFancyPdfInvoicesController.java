package com.myfancypfdinvoices.web;

import com.myfancypfdinvoices.dto.InvoiceDto;
import com.myfancypfdinvoices.model.Invoice;
import com.myfancypfdinvoices.service.InvoiceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MyFancyPdfInvoicesController {

    private final InvoiceService invoiceService;

    public MyFancyPdfInvoicesController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("/invoices")
    public List<Invoice> index() {
        return invoiceService.findAll();
    }

    @PostMapping("/invoices")
    public Invoice createInvoice(@RequestBody InvoiceDto invoiceDto) {
        return invoiceService.create(invoiceDto.getUserId(), invoiceDto.getAmount());
    }
}
