package com.example.customerservice.cronjob;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

@Component
public class Test implements Job {
    @Override
    public void execute(JobExecutionContext context) {
        System.out.println("->>>>>>>>>>>>>>>>>>>>");
    }
}
