package com.library.service.hashRepository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.consts.AppConfigs;
import com.library.service.model.hashes.EmailCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.library.consts.RedisKeys.EMAIL_CODE_CODE;

@Repository
public class EmailCodeRepo {

    @Autowired
    RedisTemplate template;

    @Autowired
    ObjectMapper objectMapper;

    public void save(EmailCode emailCode){
        if (emailCode.getId() == null){
            emailCode.setId(prepareKey(emailCode.getEmail()));
        }
        template.opsForHash().put(emailCode.getId(), emailCode.getId(), emailCode);
        template.expire(emailCode.getId(), AppConfigs.EMAIL_CODE_TTL, TimeUnit.MINUTES);
    }

    public EmailCode getEmailCode(String email){
        List<EmailCode> resp = objectMapper.convertValue(template.opsForHash().values(
                        prepareKey(email)), new TypeReference<List<EmailCode>>() {
                });
        return resp.isEmpty() ? null : resp.get(0);
    }

    public void delete(EmailCode codeRes){
        template.delete(prepareKey(codeRes.getEmail()));
    }

    private String prepareKey(String email){
        return EMAIL_CODE_CODE + email;
    }
}
