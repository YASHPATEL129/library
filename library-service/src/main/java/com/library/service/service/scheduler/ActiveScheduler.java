package com.library.service.service.scheduler;

import com.library.repository.MyOrderRepository;
import com.library.repository.UserPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@EnableScheduling
public class ActiveScheduler {


    @Autowired
    private UserPlanRepository userPlanRepository;

    @Autowired
    private MyOrderRepository myOrderRepository;


//    @Scheduled(cron = "0 0/2 * * * ?") // Run every 2 minutes
    @Scheduled(cron = "0 0 0 * * ?")
    public void updatePlanStatus() {
        userPlanRepository.updateIsActiveBasedOnDate();
    }


//    @Scheduled(cron = "0 0/2 * * * ?") // Run every 2 minutes
    @Scheduled(cron = "0 0 * * * ?") // Run every hour
    public void deleteOldCreateOrders() {
        Instant oneHourAgo = Instant.now().minusSeconds(3600); // 1 hour ago
        myOrderRepository.deleteOldCreateOrders(oneHourAgo);
    }
}
