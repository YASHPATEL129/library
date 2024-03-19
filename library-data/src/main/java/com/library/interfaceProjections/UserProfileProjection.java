package com.library.interfaceProjections;

import com.library.entity.PlanDetails;
import org.springframework.data.rest.core.config.Projection;

import java.time.Instant;

@Projection(name = "transactionInfo", types = { PlanDetails.class })
public interface UserProfileProjection {


    String getOrderId();
    Instant getStartingDate();
    Instant getEndingDate();
    String getPlanName();
}
