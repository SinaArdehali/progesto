package org.ocdm.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    public static final String CDP = "ROLE_CHEF_DE_PROJET";

    public static final String DP = "ROLE_DIRECTEUR_DE_PROJET";



    private AuthoritiesConstants() {
    }
}
