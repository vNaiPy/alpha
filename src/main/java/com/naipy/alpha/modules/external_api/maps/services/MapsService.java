package com.naipy.alpha.modules.external_api.maps.services;

import com.naipy.alpha.modules.external_api.maps.models.GeocodeResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
public class MapsService {

    private final WebClient webClient;
    private static final String MAPS_KEY = "&key=AIzaSyDLr4j7hVxfeYDR1wEC1YnDSgw91UqOjsY";
    private static final String WHITESPACE_CODE = "%20";

    public MapsService (WebClient.Builder webClientBuilder) {
        this.webClient =
                webClientBuilder
                .baseUrl("https://maps.googleapis.com/maps/api/geocode")
                .build();
    }

    public GeocodeResponse getAddressByZipCodeOrCompleteAddressFromMapsApi (String zipCodeOrCompleteAddress) {
        zipCodeOrCompleteAddress = zipCodeOrCompleteAddress.replace(" ", WHITESPACE_CODE);

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
