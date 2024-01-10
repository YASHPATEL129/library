package com.library.service.helper;

import com.library.consts.AppConfigs;
import com.library.service.hashRepository.UserSessionTrackerRepo;
import com.library.service.model.hashes.UserSessionTracker;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.util.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.security.SecureRandom;
import java.util.UUID;

public class SystemHelper {

    public static String generateVerificationCode(){
        return RandomStringUtils.randomNumeric(6).toUpperCase();
    }

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

    public static Boolean validateDevice(HttpServletRequest request, String userEmail, UserSessionTrackerRepo userSessionTrackerRepo){
        String deviceType = request.getHeader(AppConfigs.DEVICE_TYPE_NAME);
        String deviceToken = request.getHeader(AppConfigs.DEVICE_VERIFICATION_TOKEN);
        if (!ObjectUtils.isEmpty(deviceType) && !StringUtils.isEmpty(deviceType)){
            UserSessionTracker session = userSessionTrackerRepo.getSession(userEmail , deviceType);
            return (session != null && StringUtils.equals(session.getVerificationId(), deviceToken));
        }
        return false;
    }
}