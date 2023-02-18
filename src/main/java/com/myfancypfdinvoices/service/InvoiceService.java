package com.myfancypfdinvoices.service;

import com.myfancypfdinvoices.model.Invoice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.UUID;

@Service
public class InvoiceService {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceService.class);

    private final String cdnUrl;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public InvoiceService(UserService userService,
                          @Value("${cdn.url}")
                                  String cdnUrl,
                          JdbcTemplate jdbcTemplate) {
        this.cdnUrl = cdnUrl;
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void init() {
        System.out.println("Fetching PDF Template from S3...");
    }

    @Transactional
    public List<Invoice> findAll() {
        logger.info("findAll method has been invoked");
        return jdbcTemplate.query("SELECT id, user_id, pdf_url, amount FROM invoices", (resultSet, rowNum) -> {
            Invoice invoice = new Invoice();
            invoice.setId(resultSet.getObject("id").toString());
            invoice.setPdfUrl(resultSet.getString("pdf_url"));
            invoice.setUserId(resultSet.getString("user_id"));
            invoice.setAmount(resultSet.getInt("amount"));
            return invoice;
        });
    }

    @Transactional
    public Invoice create(String userId, Integer amount) {
        logger.info("create method has been invoked");
        String generatedPfgUrl = cdnUrl + "/images/default/sample.pdf";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement("INSERT INTO invoices (user_id, pdf_url, amount) VALUES (?, ?, ?)",
                            Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, userId);
            ps.setString(2, generatedPfgUrl);
            ps.setInt(3, amount);
            return ps;
        }, keyHolder);

        String uuid = !keyHolder.getKeys().isEmpty() ? ((UUID) keyHolder.getKeys().values().iterator().next()).toString() : null;

        Invoice invoice = new Invoice();
        invoice.setId(uuid);
        invoice.setPdfUrl(generatedPfgUrl);
        invoice.setAmount(amount);
        invoice.setUserId(userId);

        return invoice;
    }

    @PreDestroy
    public void shutdown() {
        System.out.println("Deleting downloaded templates...");
    }
}
