package com.fenlonsky.ticket;

import com.fenlonsky.ticket.model.Task;
import com.fenlonsky.ticket.utils.TimeUtil;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by fenlon on 15-12-14.
 */
public class TaskExcutor {
    private List<Task> tasks;
    private final ExecutorService executorService = Executors
            .newCachedThreadPool(new ThreadFactoryBuilder().setNameFormat("job-thread-%s").build());

    public TaskExcutor(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void start() {
        for (Task task : tasks) {
            doTask(task);
        }
    }

    private void doTask(Task task) {
        String urlTemplate = task.getUrlTemplate();
        final Timestamp startDate = Timestamp.valueOf(TimeUtil.addZero(task.getStartDate()));
        final Timestamp endDate = Timestamp.valueOf(TimeUtil.addZero(task.getEndDate()));
        executorService.execute(new Runnable() {
            Timestamp now = new Timestamp(startDate.getTime());
            String url = null;

            public void run() {
                while (true && !Thread.currentThread().isInterrupted()) {
                    if (now.getTime() > endDate.getTime()) {
                        now = new Timestamp(startDate.getTime());
                    }
                    HttpUtil.doGet(url);
                    now = new Timestamp(TimeUtil.add1day(now.getTime()));
                }
            }
        });
    }
}
