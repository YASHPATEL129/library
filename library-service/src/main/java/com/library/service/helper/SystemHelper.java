package com.library.service.helper;

import java.security.SecureRandom;
import java.util.UUID;

public class SystemHelper {


    public static String generateUsername() {
        StringBuffer data =
                new StringBuffer().append(UUID.randomUUID().toString().replace("-", ""))
                        .append(UUID.randomUUID().toString().replace("-", ""))
                        .append(UUID.randomUUID().toString().replace("-", ""))
                        .append(UUID.randomUUID().toString().replace("-", ""));
        SecureRandom random = new SecureRandom();
        int index = random.nextInt(100);
        int len = index + 12;
        return data.substring(index, len).toLowerCase();
    }

}