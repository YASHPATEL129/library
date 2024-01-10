package com.library.service.hashRepository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.consts.AppConfigs;
import com.library.service.model.hashes.EmailCodeRequestTracker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.library.consts.RedisKeys.EMAIL_CODE_REQUESTS;

@Repository
public class EmailCodeRequestTrackerRepo {

    @Autowired
    RedisTemplate  template;

    @Autowired
    ObjectMapper objectMapper;

    public void save(EmailCodeRequestTracker codeRequestTracker){
        if (codeRequestTracker.getId() == null){
            codeRequestTracker.setId(prepareKey(codeRequestTracker.getIp(), codeRequestTracker.getEmail()));
        }
        template.opsForHash().put(codeRequestTracker.getIp(), codeRequestTracker.getId(), codeRequestTracker);
        template.expire(codeRequestTracker.getId(), AppConfigs.EMAIL_CODE_REQUEST_TRACKER_TTL, TimeUnit.HOURS);
    }

    public EmailCodeRequestTracker getTrack(String hostIp, String email){
        List<EmailCodeRequestTracker> resp = objectMapper.convertValue(template.opsForHash().values(prepareKey(hostIp, email)),
                new TypeReference<List<EmailCodeRequestTracker>>() {
                });
        return resp.isEmpty() ? null : resp.get(0);
    }

    public String prepareKey(String ip, String email){
        ip = ip.replaceAll(":",".");
        return EMAIL_CODE_REQUESTS + ip + "_" + email;
    }
}
