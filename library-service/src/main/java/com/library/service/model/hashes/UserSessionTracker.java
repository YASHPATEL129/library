package com.library.service.model.hashes;

import com.library.enums.DeviceTypes;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
public class UserSessionTracker {

    @Id
    private String id;

    private String userEmail;

    private String verificationId;

    private DeviceTypes deviceType;
}
