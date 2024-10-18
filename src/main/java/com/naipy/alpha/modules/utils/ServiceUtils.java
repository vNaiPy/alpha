package com.naipy.alpha.modules.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.f4b6a3.uuid.UuidCreator;
import com.naipy.alpha.modules.user.models.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class ServiceUtils {

    public static final String WHITESPACE = " ";
    public static final String EMPTY_STRING = "";
    protected static final ObjectMapper objectMapper = new ObjectMapper();

   /* public static UUID generateUUID () {
        //UUID v7
        return UuidCreator.getTimeOrderedEpoch();
    }*/

    public static String generateUUID () {
        //UUID v7
        return UuidCreator.getTimeOrderedEpoch().toString();
    }

    public static User getIdCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    public static boolean isDifferent (Object o1, Object o2) {
        return !o1.equals(o2);
    }

    public String removeNonNumeric (String dirtyString) {
        return dirtyString.replaceAll("[^a-zA-Z0-9]", "");
    }

}
