package com.naipy.alpha.modules.utils.maps.services;

import com.naipy.alpha.modules.utils.maps.models.GeocodeResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
public class MapsService {

    private WebClient webClient;
    final String MAPS_KEY = "&key=AIzaSyDLr4j7hVxfeYDR1wEC1YnDSgw91UqOjsY";
    final String WHITESPACE_CODE = "%20";

    public MapsService (WebClient webClientBuilder) {
        webClient = WebClient.builder()
                .baseUrl("https://maps.googleapis.com/maps/api/geocode")
                .build();
    }

    public GeocodeResponse getAddressBySomethingFromMapsApi (String zipCodeOrAddressComplete) {
        zipCodeOrAddressComplete = zipCodeOrAddressComplete.replace(" ", WHITESPACE_CODE);

        return webClient
                .get()
                .uri("/json?address=" + zipCodeOrAddressComplete + MAPS_KEY)
                .accept(APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, error -> Mono.error(new RuntimeException("Parametro invalido")))
                .bodyToMono(GeocodeResponse.class)
                .block();
    }
}
