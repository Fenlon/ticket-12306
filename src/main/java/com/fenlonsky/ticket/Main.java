package com.fenlonsky.ticket;

import com.fenlonsky.ticket.factory.*;
import com.fenlonsky.ticket.model.Email;
import com.fenlonsky.ticket.model.Task;

import java.util.List;
import java.util.concurrent.*;

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
}
