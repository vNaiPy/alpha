package com.naipy.alpha.modules.utils.maps;

import com.naipy.alpha.modules.address.models.Address;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
public class MapsService {

    private WebClient webClient;
    final String MAPS_KEY = "&key=AIzaSyDLr4j7hVxfeYDR1wEC1YnDSgw91UqOjsY";
    final String WHITESPACE = "%20";

    public MapsService (WebClient webClientBuilder) {
        webClient = WebClient.builder()
                .baseUrl("https://maps.googleapis.com/maps/api/geocode")
                .build();
    }

    public GeocodeResponse getAddressFromMapsApi (String address) {

        String exampleAddressForRequest = "address=Rua%20Gasparini%20130%20Rudge";
        /*String addressForRequest = address.getStreet().replace(" ", WHITESPACE);
        this.webClient.get()
                .uri("/json?address=" + addressForRequest + MAPS_KEY);*/

        return webClient
                .get()
                .uri("/json?address=" + address + MAPS_KEY)
                .accept(APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, error -> Mono.error(new RuntimeException("Parametro invalido")))
                .bodyToMono(GeocodeResponse.class)
                .block();
    }
}
