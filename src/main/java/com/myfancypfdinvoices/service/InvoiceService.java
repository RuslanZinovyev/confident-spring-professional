package com.myfancypfdinvoices.service;

import com.myfancypfdinvoices.model.Invoice;
import com.myfancypfdinvoices.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class InvoiceService {

    private final UserService userService;

    private final String cdnUrl;
    private final JdbcTemplate jdbcTemplate;
    private final List<Invoice> invoices = new CopyOnWriteArrayList<>();

    @Autowired
    public InvoiceService(UserService userService,
                          @Value("${cdn.url}")
                          String cdnUrl,
                          JdbcTemplate jdbcTemplate) {
        this.userService = userService;
        this.cdnUrl = cdnUrl;
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void init() {
        System.out.println("Fetching PDF Template from S3...");
    }

    public List<Invoice> findAll() {
        return jdbcTemplate.query("SELECT id, user_id, pdf_url, amount FROM invoices", (resultSet, rowNum) -> {
            Invoice invoice = new Invoice();
            invoice.setId(resultSet.getObject("id").toString());
            invoice.setPdfUrl(resultSet.getString("pdf_url"));
            invoice.setUserId(resultSet.getString("user_id"));
            invoice.setAmount(resultSet.getInt("amount"));
            return invoice;
        });
    }

    public Invoice create(String userId, Integer amount) {
        User user = userService.findById(userId);
        if (user == null) {
            throw new IllegalStateException();
        }

        Invoice invoice = new Invoice(userId, amount, cdnUrl + "/images/default/sample.pdf");
        invoices.add(invoice);

        return invoice;
    }

    @PreDestroy
    public void shutdown() {
        System.out.println("Deleting downloaded templates...");
    }
}
