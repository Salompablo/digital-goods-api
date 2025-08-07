package com.pablo.digitalstore.digital_store_api.service;

import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;

public interface MercadoPagoService {
    Preference createPreferenceFromCurrentCart() throws MPException, MPApiException;
}
