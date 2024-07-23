package com.naipy.alpha.modules.utils;

import com.github.f4b6a3.uuid.UuidCreator;
import com.naipy.alpha.modules.user.models.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public class ServiceUtils {

    public final String WHITESPACE = " ";
    public final String EMPTY_STRING = "";

    public static UUID generateUUID () {
        //UUID v7
        return UuidCreator.getTimeOrderedEpoch();
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
