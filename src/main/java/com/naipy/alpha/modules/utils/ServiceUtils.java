package com.naipy.alpha.modules.utils;

import com.naipy.alpha.modules.user.models.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public class ServiceUtils {

    public static UUID generateUUID () {
        return UUID.randomUUID();
    }

    public static User getIdCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

}
