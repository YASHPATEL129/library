package com.library.consts;

public interface AppConfigs {

//    Header

    String JWT_ID = "jwt-id";

    String ACCESS_TOKEN = "access-token";

    String DEVICE_TYPE_NAME = "device-type";

    String DEVICE_VERIFICATION_TOKEN = "device-token";



//    Redis TTLs

    Long USER_SESSION_TRACKER_TTL = 7L;

    Long EMAIL_CODE_TTL = 5L;

    Long EMAIL_CODE_REQUEST_TRACKER_TTL = 24L;

    Long EMAIL_REQUEST_TRACKER_TTL = 24L;


//    Get Ip
    String REAL_REMOTE_IP_HEADER = "X-Real-IP";


//    Email Code

    String WELCOME_TEMPLATE_HTML_KEY = "welcome_web";

    String EMAIL_CODE_TEMPLATE_HTML_KEY = "email_code";

    int EMAIL_CODE_INTERVAL_SECONDS = 60;

    int EMAIL_CODE_DAILY_NUMBER_ACCOUNT = 10;

    int EMAIL_CODE_DAILY_NUMBER_IP = 20;
}