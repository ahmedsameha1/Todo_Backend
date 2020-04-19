package com.ahmedsameha1.todo;

public class Constants {
    public static final String AUTHORIZATION = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String SIGN_UP_URL = "/sign_up";
    public static final String EMAIL_VERIFICATION_URL = "/email_verification";
    public static final String SIGN_IN_URL = "/sign_in";
    public static final byte JWT_TOKEN_EXPIRATION_PERIOD_IN_DAYS = 10;
    public static final byte EMAIL_VERIFICATION_TOKEN_EXPIRATION_PERIOD_IN_DAYS = 1;

    public static class ErrorCode {
        public static final short BAD_EMAIL_VERIFICATION_TOKEN = 1;
        public static final short EXPIRED_EMAIL_VERIFICATION_TOKEN = 2;
        public static final short DISABLED_USER_ACCOUNT = 3;
    }
}
