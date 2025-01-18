package com.naipy.alpha.modules.external_api.maps.interfaces;

import com.naipy.alpha.modules.external_api.maps.models.GeocodeResponse;

public interface Maps {
    GeocodeResponse getAddressByZipCodeOrCompleteAddressFromMapsApi(String zipCodeOrCompleteAddress);
}
