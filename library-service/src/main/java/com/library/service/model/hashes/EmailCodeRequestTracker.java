package com.library.service.model.hashes;

import jdk.jfr.DataAmount;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class EmailCodeRequestTracker {

    @Id
    private String id;

    private String email;

    private String ip;

    private Integer attempts = 1;

    private Date timestamp;
}
