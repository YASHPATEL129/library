package com.library.service.model.hashes;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class EmailRequestTracker {

    @Id
    private String id;

    private String ip;

    private Integer count = 1;

}
