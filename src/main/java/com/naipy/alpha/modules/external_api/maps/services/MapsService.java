package com.naipy.alpha.modules.external_api.maps.services;

import com.naipy.alpha.modules.exceptions.services.ExternalResponseNotReceivedException;
import com.naipy.alpha.modules.external_api.interfaces.Maps;
import com.naipy.alpha.modules.external_api.maps.models.GeocodeResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
public class MapsService implements Maps<GeocodeResponse> {

    private final WebClient webClient;
    //private static final String MAPS_KEY = ConfigurationLoader.getMapsKey();
    private static final String MAPS_KEY = "AIzaSyDLr4j7hVxfeYDR1wEC1YnDSgw91UqOjsY";

    public MapsService (WebClient.Builder webClientBuilder) {
        final String endpointGeocodeAPI = "https://maps.googleapis.com/maps/api/geocode";
        this.webClient =
                webClientBuilder
                .baseUrl(endpointGeocodeAPI)
                .build();
    }

    @Override
    public GeocodeResponse getAddressBy(String zipCodeOrCompleteAddress) {
        GeocodeResponse geocodeResponse = getAddressByZipCodeOrCompleteAddressFromMapsApi(zipCodeOrCompleteAddress);
        GeocodeResponse.isValidGeocodeResponse(geocodeResponse, zipCodeOrCompleteAddress);
        return geocodeResponse;
    }

    private GeocodeResponse getAddressByZipCodeOrCompleteAddressFromMapsApi (String zipCodeOrCompleteAddress) {
        return this.webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("/json")
                        .queryParam("address", zipCodeOrCompleteAddress)
                        .queryParam("key", MAPS_KEY)
                        .build())
                .accept(APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, resp -> Mono.error(new IllegalArgumentException("Invalid parameter sent to Maps API. Param: ".concat(zipCodeOrCompleteAddress))))
                .bodyToMono(GeocodeResponse.class)
                .switchIfEmpty(Mono.error(new ExternalResponseNotReceivedException("Geocode response not received!")))
                .block();
    }
}
