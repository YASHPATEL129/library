package com.library.service.helper;

import com.library.pojo.EmailPayload;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class EmailHelper {

    @Autowired
    private JavaMailSender sender;

    @Autowired
    SpringTemplateEngine templateEngine;


    @Value("${spring.mail.username}")
    private String senderMail;

    @Value("${spring.mail.sender.name}")
    private String senderName;

    public Boolean send(EmailPayload payload){
        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED, StandardCharsets.UTF_8.name());
            helper.setSubject(payload.getSubject());
            helper.setFrom(senderMail, senderName);
            helper.setTo(payload.getSendTo());
            Context context = new Context();
            context.setVariables(payload.getProperties());
            String html = templateEngine.process(payload.getTemplateCode(), context);
            helper.setText(html, true);
            sender.send(message);
        } catch (Exception e){
            log.error(e.getMessage());
            return false;
        }
        return true;
    }
}
