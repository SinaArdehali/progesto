package org.ocdm.config;

/**
 * Application constants.
 */
public final class Constants {

    //Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_'.@A-Za-z0-9-]*$";

    public static final String SYSTEM_ACCOUNT = "system";
    public static final String ANONYMOUS_USER = "anonymoususer";


    public static final int CONVERSIONJOURQUARTJOURNEEINT = 4;
    public static final float CONVERSIONJOURQUARTJOURNEEFLOAT = 4;

    private Constants() {
    }
}
