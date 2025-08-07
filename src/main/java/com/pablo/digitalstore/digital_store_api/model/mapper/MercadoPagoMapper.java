package com.pablo.digitalstore.digital_store_api.model.mapper;

import com.mercadopago.client.preference.PreferenceItemRequest;
import com.pablo.digitalstore.digital_store_api.model.entity.CartItemEntity;
import org.springframework.stereotype.Component;

@Component
public class MercadoPagoMapper {

    public PreferenceItemRequest toPreferenceItem(CartItemEntity item) {
        return PreferenceItemRequest.builder()
                .title(item.getProduct().getName())
                .quantity(item.getQuantity())
                .currencyId("ARS")
                .unitPrice(item.getUnitPrice())
                .build();
    }
}
