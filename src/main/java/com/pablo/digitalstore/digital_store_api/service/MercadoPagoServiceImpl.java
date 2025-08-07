package com.pablo.digitalstore.digital_store_api.service;


import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import com.pablo.digitalstore.digital_store_api.model.entity.CartEntity;
import com.pablo.digitalstore.digital_store_api.model.entity.UserEntity;
import com.pablo.digitalstore.digital_store_api.model.mapper.MercadoPagoMapper;
import com.pablo.digitalstore.digital_store_api.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MercadoPagoServiceImpl implements MercadoPagoService {

    private final AuthenticatedUserService authenticatedUserService;
    private final CartRepository cartRepository;
    private final MercadoPagoMapper mercadoPagoMapper;

    public MercadoPagoServiceImpl(AuthenticatedUserService authenticatedUserService,
                                  CartRepository cartRepository,
                                  MercadoPagoMapper mercadoPagoMapper) {
        this.authenticatedUserService = authenticatedUserService;
        this.cartRepository = cartRepository;
        this.mercadoPagoMapper = mercadoPagoMapper;
    }

    @Override
    public Preference createPreferenceFromCurrentCart() throws MPException, MPApiException {
        UserEntity user = authenticatedUserService.getCurrentUserEntity();
        CartEntity cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new IllegalStateException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cannot create preference with empty cart");
        }

        List<PreferenceItemRequest> items = cart.getItems().stream()
                .map(mercadoPagoMapper::toPreferenceItem)
                .toList();

        PreferenceBackUrlsRequest backUrlsRequest = PreferenceBackUrlsRequest.builder()
                .success("https://61e8d70ae705.ngrok-free.app/payment/success")
                .failure("https://61e8d70ae705.ngrok-free.app/payment/failure")
                .pending("https://61e8d70ae705.ngrok-free.app/payment/pending")
                .build();

        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(items)
                .backUrls(backUrlsRequest)
                .autoReturn("approved")
                .build();

        PreferenceClient client = new PreferenceClient();
        return client.create(preferenceRequest);
    }
}
