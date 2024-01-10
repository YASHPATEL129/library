package com.library.service.hashRepository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.consts.AppConfigs;
import com.library.service.model.hashes.EmailRequestTracker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.library.consts.RedisKeys.EMAIL_CODE_REQUESTS;

@Repository
public class EmailRequestTrackerRepo {

    @Autowired
    RedisTemplate template;

    @Autowired
    ObjectMapper objectMapper;

    public void save(EmailRequestTracker emailRequestTracker) {
        if (emailRequestTracker.getId() == null) {
            emailRequestTracker.setId(prepareKey(emailRequestTracker.getIp()));
        }
        template.opsForHash().put(emailRequestTracker.getId(), emailRequestTracker.getId(), emailRequestTracker);
        template.expire(emailRequestTracker.getId(), AppConfigs.EMAIL_REQUEST_TRACKER_TTL , TimeUnit.HOURS);

    }

    public EmailRequestTracker getTrack(String hostIp){
        List<EmailRequestTracker> resp = objectMapper.convertValue(template.opsForHash().values(prepareKey(hostIp)), new TypeReference<List<EmailRequestTracker>>() {
        });
        return resp.isEmpty() ? null : resp.get(0);
    }

    private String prepareKey(String ip){
        ip = ip.replaceAll(":", ".");
        return EMAIL_CODE_REQUESTS + ip;
    }
}
