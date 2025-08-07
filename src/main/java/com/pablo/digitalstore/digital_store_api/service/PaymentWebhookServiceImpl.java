package com.pablo.digitalstore.digital_store_api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class PaymentWebhookServiceImpl implements PaymentWebhookService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentWebhookServiceImpl.class);

    @Override
    public void handleWebhookNotification(Map<String, Object> payload) {
        logger.info("📩 MercadoPago webhook received: {}", payload);

        if (!payload.containsKey("type") || !payload.containsKey("data")) {
            throw new IllegalArgumentException("Invalid MercadoPago webhook payload: missing 'type' or 'data'");
        }

        String type = (String) payload.get("type");
        Map<String, Object> data = (Map<String, Object>) payload.get("data");

        if (!"payment".equals(type)) {
            logger.warn("Ignoring unsupported webhook type: {}", type);
            return;
        }

        Long paymentId;
        try {
            paymentId = Long.valueOf(data.get("id").toString());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid payment ID format in webhook payload: " + data.get("id"), e);
        }

        logger.info("📦 Processing payment notification. Payment ID: {}", paymentId);
    }
}
