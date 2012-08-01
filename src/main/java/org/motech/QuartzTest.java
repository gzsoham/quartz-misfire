package org.motech;

import org.quartz.CalendarIntervalScheduleBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

public class QuartzTest {

    public static void main(String[] args) throws Exception {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.clear();
        scheduler.start();

        JobDetail job = newJob(HelloJob.class)
                .withIdentity("job1", "group1")
                .build();

        CalendarIntervalScheduleBuilder scheduleBuilder = CalendarIntervalScheduleBuilder.calendarIntervalSchedule()
                .withIntervalInMinutes(3)
                .withMisfireHandlingInstructionFireAndProceed();

        Trigger trigger = newTrigger()
                .withIdentity("trigger1", "group1")
                .startNow()
                .withSchedule(scheduleBuilder)
                .build();

        scheduler.scheduleJob(job, trigger);

        Thread.sleep(10 * 60 * 1000);
        System.out.println("Pausing trigger... Date: " + new Date());
        scheduler.pauseTrigger(trigger.getKey());
        Thread.sleep(10 * 60 * 1000);

        System.out.println("Resuming trigger... Date: " + new Date());
        scheduler.resumeAll();
    }
}
