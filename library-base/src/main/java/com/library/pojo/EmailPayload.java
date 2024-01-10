package com.library.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class EmailPayload {
    String subject;
    String sendTo;
    String templateCode;
    Map<String, Object> properties;
}
