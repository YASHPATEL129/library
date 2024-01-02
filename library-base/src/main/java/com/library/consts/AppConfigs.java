package com.library.consts;

public interface AppConfigs {

//    Header

    String JWT_ID = "jwt-id";

    String ACCESS_TOKEN = "access-token";

    String DEVICE_TYPE_NAME = "device-type";
    String DEVICE_VERIFICATION_TOKEN = "device-token";



//    Redis TTLs

    Long USER_SESSION_TRACKER_TTL = 7L;
}