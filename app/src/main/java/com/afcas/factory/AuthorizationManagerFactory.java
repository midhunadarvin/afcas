package com.afcas.factory;

import com.afcas.impl.AuthorizationManager;
import com.afcas.objects.IAuthorizationManager;

public class AuthorizationManagerFactory {
    private static final IAuthorizationManager instance = new AuthorizationManager();

    private AuthorizationManagerFactory() {
        // Private constructor to prevent instantiation
    }

    public static IAuthorizationManager getInstance() {
        return instance;
    }
}
