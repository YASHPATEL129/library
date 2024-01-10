package com.library.service.model.hashes;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class EmailCode {

    @Id
    private String id;

    private String email;

    private String code;

}
