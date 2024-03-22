package com.example.customerservice.component;

import com.example.customerservice.cronjob.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.stereotype.Component;

@Component
public class TriggerCustom {
    public void saveTrigger(String time, Integer customerId) throws SchedulerException {
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger:" + customerId, "group").withSchedule(CronScheduleBuilder.cronSchedule(time)).build();
        JobDetail job = JobBuilder.newJob(Test.class).withIdentity("jobDetail:" + customerId, "group").build();
        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.deleteJob(job.getKey());
        scheduler.getContext().put("customerId", customerId);
        scheduler.start();
        scheduler.scheduleJob(job, trigger);
    }
}
