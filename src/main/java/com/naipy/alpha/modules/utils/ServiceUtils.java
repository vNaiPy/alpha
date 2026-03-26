package com.naipy.alpha.modules.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.f4b6a3.uuid.UuidCreator;
import com.naipy.alpha.modules.address.models.Address;
import com.naipy.alpha.modules.address.models.AddressEnriched;
import com.naipy.alpha.modules.user.models.User;
import com.naipy.alpha.modules.utils.models.PaginatorInput;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class ServiceUtils {

    public static final ObjectMapper objectMapper = new ObjectMapper();

    private ServiceUtils() {
        throw new UnsupportedOperationException("This class cannot be instanced");
    }

    public static String generateUUID () {
        //UUID v7
        return UuidCreator.getTimeOrderedEpoch().toString();
    }

    public static User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    public static boolean isDifferent (Object o1, Object o2) {
        return !o1.equals(o2);
    }

    public static String removeNonNumeric (String dirtyString) {
        return dirtyString.replaceAll("[^a-zA-Z0-9]", "");
    }

    public static void equalizerObjectId(Address address, Address addressGoal) {
        addressGoal.setId(address.getId());
    }

    public static void equalizerObjectId(AddressEnriched addressEnriched, AddressEnriched addressEnrichedGoal) {
        addressEnrichedGoal.getAddress().setId(addressEnriched.getAddress().getId());
    }

    public static Pageable createPageable (PaginatorInput paginatorInput) {
        Pageable pageableToReturn;
        if (notNull(paginatorInput.sortBy()) && !paginatorInput.sortBy().isBlank())
            pageableToReturn = PageRequest.of(paginatorInput.page(), validatePaginatorSize(paginatorInput.size()), Sort.by(paginatorInput.sortBy()).ascending());
        else
            pageableToReturn = PageRequest.of(paginatorInput.page(), validatePaginatorSize(paginatorInput.size()));
        return pageableToReturn;
    }

    public static boolean notNull (Object object) {
        return object != null;
    }

    private static int validatePaginatorSize (Integer size) {
        if (50 == size)
            return 50;
        else if (100 == size)
            return 100;
        else
            return 20;
    }
}
