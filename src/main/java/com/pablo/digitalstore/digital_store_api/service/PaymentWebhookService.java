package com.pablo.digitalstore.digital_store_api.service;

import java.util.Map;

public interface PaymentWebhookService {
    void handleWebhookNotification(Map<String, Object> payload);
}
