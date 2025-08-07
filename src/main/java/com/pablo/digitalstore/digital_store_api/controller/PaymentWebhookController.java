package com.pablo.digitalstore.digital_store_api.controller;

import com.pablo.digitalstore.digital_store_api.service.PaymentWebhookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/webhooks/mercadopago")
public class PaymentWebhookController {

    private final PaymentWebhookService paymentWebhookService;

    @Autowired
    public PaymentWebhookController(PaymentWebhookService paymentWebhookService) {
        this.paymentWebhookService = paymentWebhookService;
    }

    @Operation(
            summary = "Receive MercadoPago webhook notifications",
            description = "Endpoint to receive and process MercadoPago webhook events such as payment updates."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Notification received and processed successfully",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid payload",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PostMapping
    public ResponseEntity<Void> receiveNotification(
            @Parameter(description = "Payload sent by MercadoPago webhook", required = true)
            @RequestBody Map<String, Object> payload) {
        paymentWebhookService.handleWebhookNotification(payload);
        return ResponseEntity.ok().build();
    }
}
