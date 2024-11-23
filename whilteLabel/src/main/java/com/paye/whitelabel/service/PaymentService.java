package com.paye.whitelabel.service;

import com.paye.whitelabel.Repository.TransactionRepository;
import com.paye.whitelabel.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class PaymentService {

    @Value("${currency.api.key}")
    private String apiKey;

    @Value("${currency.api.url}")
    private String apiUrl;

    private static final String DEFAULT_BASE_CURRENCY = "USD";

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private RestTemplate restTemplate;

    public BigDecimal convertCurrency(BigDecimal amount, String fromCurrency, String toCurrency) {
        String url = apiUrl + "?apikey=" + apiKey;

        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response == null || !response.containsKey("conversion_rates")) {
                throw new RuntimeException("Currency conversion failed. Response from API is invalid.");
            }

            Map<String, Object> rates = (Map<String, Object>) response.get("conversion_rates");
            Number fromRateNumber = (Number) rates.get(fromCurrency);
            Number toRateNumber = (Number) rates.get(toCurrency);

            if (fromRateNumber == null || toRateNumber == null) {
                throw new RuntimeException("Unable to find exchange rates for one or both currencies.");
            }

            Double fromRate = fromRateNumber.doubleValue();
            Double toRate = toRateNumber.doubleValue();
            BigDecimal exchangeRate = BigDecimal.valueOf(toRate / fromRate);

            return amount.multiply(exchangeRate);
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Currency conversion failed due to an API error.", e);
        }
    }

    public Transaction processTransaction(Transaction transaction) {
        try {
            BigDecimal convertedAmount = convertCurrency(
                    new BigDecimal(transaction.getAmount()),
                    transaction.getCurrency(),
                    DEFAULT_BASE_CURRENCY
            );
            transaction.setAmount(convertedAmount.toPlainString());
        } catch (RuntimeException e) {
            transaction.setMessage("Currency conversion failed: " + e.getMessage());
        }

        return transactionRepository.save(transaction);
    }
}
