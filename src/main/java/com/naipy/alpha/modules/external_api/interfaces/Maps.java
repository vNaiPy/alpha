package com.naipy.alpha.modules.external_api.interfaces;

public interface Maps<T> {
    T getAddressBy(String zipCodeOrCompleteAddress);
}
