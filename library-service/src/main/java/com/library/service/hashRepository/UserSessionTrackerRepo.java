package com.library.service.hashRepository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.consts.AppConfigs;
import com.library.service.model.hashes.UserSessionTracker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.library.consts.RedisKeys.USER_SESSION;

@Repository
public class UserSessionTrackerRepo {

    @Autowired
    RedisTemplate template;

    @Autowired
    ObjectMapper objectMapper;

    public void save(UserSessionTracker userSessionTracker) {
        if (null == userSessionTracker.getId()) {
            userSessionTracker.setId(prepareKey(userSessionTracker.getUserEmail(), userSessionTracker.getDeviceType().toString()));
        }
        template.opsForHash().put(userSessionTracker.getId(), userSessionTracker.getUserEmail(), userSessionTracker);
        template.expire(userSessionTracker.getId(), AppConfigs.USER_SESSION_TRACKER_TTL, TimeUnit.DAYS);
    }

    public UserSessionTracker getSession(String userEmail , String deviceType){
        List<UserSessionTracker> resp = objectMapper.convertValue(template.opsForHash().values(prepareKey(userEmail, deviceType)), new TypeReference<List<UserSessionTracker>>() {
        });
        return resp.isEmpty() ? null : resp.get(0);

    }

    public void delete(UserSessionTracker tracker){
        template.delete(prepareKey(tracker.getUserEmail(), tracker.getDeviceType().toString()));
    }

    private String prepareKey(String userName, String deviceType){
        return USER_SESSION + userName + "-" + deviceType ;
    }
}
