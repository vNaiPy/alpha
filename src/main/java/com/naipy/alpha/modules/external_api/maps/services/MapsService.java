package com.naipy.alpha.modules.external_api.maps.services;

import com.naipy.alpha.config.ConfigurationLoader;
import com.naipy.alpha.modules.external_api.maps.models.GeocodeResponse;
import com.naipy.alpha.modules.utils.ConstantVariables;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
public class MapsService {

    private final WebClient webClient;
    private static final String MAPS_KEY = ConfigurationLoader.getMapsKey();

    public MapsService (WebClient.Builder webClientBuilder) {
        final String endpointGeocodeAPI = "https://maps.googleapis.com/maps/api/geocode";
        this.webClient =
                webClientBuilder
                .baseUrl(endpointGeocodeAPI)
                .build();
    }

    public GeocodeResponse getAddressByZipCodeOrCompleteAddressFromMapsApi (String zipCodeOrCompleteAddress) {
        zipCodeOrCompleteAddress = zipCodeOrCompleteAddress.replace(" ", ConstantVariables.WHITESPACE_CODE);

        return this.webClient
                .get()
                .uri("/json?address=" + zipCodeOrCompleteAddress + MAPS_KEY)
                .accept(APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, error -> Mono.error(new RuntimeException("Parametro invalido")))
                .bodyToMono(GeocodeResponse.class)
                .block();
    }
}
