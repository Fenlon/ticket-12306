package com.fenlonsky.ticket;

import com.fenlonsky.ticket.factory.*;
import com.fenlonsky.ticket.model.Email;
import com.fenlonsky.ticket.model.Task;

import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by fenlon on 15-12-12.
 */
public class Main {
    public static void main(String[] args) {
        TicketConfig ticketConfig = TicketConfigFactory.getTicketConf();
        List<Task> tasks = TaskFactory.getInstance(ticketConfig).getTasks();

        Sender sender = SendFactory.createMailSender(ticketConfig, new LinkedBlockingDeque<Email>());
        sender.Send(null, null);
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
