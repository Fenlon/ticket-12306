package com.fenlonsky.ticket;

import com.fenlonsky.ticket.factory.*;
import com.fenlonsky.ticket.model.Email;
import com.fenlonsky.ticket.model.Task;
import com.fenlonsky.ticket.utils.TimeUtil;
import com.fenlonsky.ticket.utils.UrlUtil;
import com.google.common.base.Optional;
import com.google.common.primitives.Booleans;
import com.google.common.util.concurrent.Striped;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;

/**
 * Created by fenlon on 15-12-12.
 */
public class Main {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();

        final TicketConfig ticketConfig = TicketConfigFactory.getTicketConf();
        List<Task> tasks = TaskFactory.getInstance(ticketConfig).getTasks();
        final BlockingQueue emailQueue = new LinkedBlockingDeque<Email>();
        //eat
        executorService.execute(new Runnable() {
            PostFactory<Email> postFactory = new EmailPostFactory(ticketConfig, emailQueue);

            @Override
            public void run() {
                postFactory.start();
            }
        });

        //do task
        for (final Task task : tasks) {
            ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder()
                    .setNameFormat("scheduler thread %s").build());
            executor.scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
                    Long start = Timestamp.valueOf(task.getStartDate()).getTime();
                    Long end = Timestamp.valueOf(task.getEndDate()).getTime();
                    if (start > end) {
                        return;
                    }
                    doTask(task);
                }
            }, 10, 60, TimeUnit.SECONDS);
        }


        //post
        Sender sender = SendFactory.createMailSender(emailQueue);
        sender.send("wokao", new String[]{"935057327@qq.com", "853555703@qq.com"});


        try {
            Thread.currentThread().sleep(1000);
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void doTask(Task task) {
        for (String to : task.getStations()) {
            String url = UrlUtil.fillUrl(task.getUrlTemplate(), task.getStartDate(), to);
            Optional<String> result = HttpUtil.doGet(url);
            if (!result.isPresent()) {
                return;
            }

            //判断是否有车票
        }

    }
}
