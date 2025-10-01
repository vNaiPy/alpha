package com.naipy.alpha.modules.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.f4b6a3.uuid.UuidCreator;
import com.naipy.alpha.modules.address.models.Address;
import com.naipy.alpha.modules.address.models.AddressEnriched;
import com.naipy.alpha.modules.user.models.User;
import com.naipy.alpha.modules.utils.models.PaginatorInput;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

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

    protected Pageable createPageable (PaginatorInput paginatorInput) {
        Pageable pageableToReturn;
        if (paginatorInput.sortBy() != null && !paginatorInput.sortBy().isBlank())
            pageableToReturn = PageRequest.of(paginatorInput.page(), paginatorInput.size(), Sort.by(paginatorInput.sortBy()).ascending());
        else
            pageableToReturn = PageRequest.of(paginatorInput.page(), paginatorInput.size());
        return pageableToReturn;
    }
}
