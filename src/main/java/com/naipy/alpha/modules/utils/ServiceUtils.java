package com.naipy.alpha.modules.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.f4b6a3.uuid.UuidCreator;
import com.naipy.alpha.modules.address.models.Address;
import com.naipy.alpha.modules.address.models.AddressEnriched;
import com.naipy.alpha.modules.user.models.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class ServiceUtils {

    protected static final ObjectMapper objectMapper = new ObjectMapper();

    protected static String generateUUID () {
        //UUID v7
        return UuidCreator.getTimeOrderedEpoch().toString();
    }

    protected static User getIdCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    protected static boolean isDifferent (Object o1, Object o2) {
        return !o1.equals(o2);
    }

    protected String removeNonNumeric (String dirtyString) {
        return dirtyString.replaceAll("[^a-zA-Z0-9]", "");
    }

    protected void equalizerObjectId(Address address, Address addressGoal) {
        addressGoal.setId(address.getId());
    }

    protected void equalizerObjectId(AddressEnriched addressEnriched, AddressEnriched addressEnrichedGoal) {
        addressEnrichedGoal.getAddress().setId(addressEnriched.getAddress().getId());
    }
}
