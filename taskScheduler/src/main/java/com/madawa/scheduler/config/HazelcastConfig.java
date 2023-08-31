package com.madawa.scheduler.config;

import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.scheduledexecutor.DuplicateTaskException;
import com.hazelcast.scheduledexecutor.IScheduledExecutorService;
import com.hazelcast.scheduledexecutor.IScheduledFuture;
import com.hazelcast.scheduledexecutor.TaskUtils;
import com.madawa.scheduler.task.MorningTask;
import com.madawa.scheduler.task.SendEmailTask;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class HazelcastConfig {
    public static String clusterName = "MyCluster";

    //No duplicates in the cluster by utilising a unique name per task
    public static String taskNameMorning = "MorningTask";
    public static String taskNameSendEmail = "SendEmailTask";

    @Bean
    public HazelcastInstance HazelCast() {

        Config config = new Config();

        config.setClusterName(clusterName);
        config.setInstanceName(clusterName);

        NetworkConfig network = config.getNetworkConfig();
        JoinConfig join = network.getJoin();
        join.getMulticastConfig().setEnabled(false); //Disabled bcs multicast communication might be restricted.
        join.getTcpIpConfig().setEnabled(true);
        //add the IP addresses of the machines you want to include in the cluster
        join.getTcpIpConfig().addMember("127.0.0.1"); //IP address of machine which is this code running

        HazelcastInstance hz = Hazelcast.newHazelcastInstance(config);
        if (null == hz) {
            return hz;
        }

        try {
            IScheduledExecutorService es = hz.getScheduledExecutorService ("default");

            IScheduledFuture <?> Future1 = es.scheduleAtFixedRate(
                    TaskUtils.named(taskNameMorning, new MorningTask()), 1, 10, TimeUnit.SECONDS);

            IScheduledFuture <?> Future2 = es.scheduleAtFixedRate(
                    TaskUtils.named(taskNameSendEmail, new SendEmailTask()), 2, 20, TimeUnit.SECONDS);

        } catch (DuplicateTaskException e) {
            System.out.println("Already scheduled task");
        }
        return hz;
    }
}
