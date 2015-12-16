package com.fenlonsky.ticket.factory;

import com.fenlonsky.ticket.model.Email;
import com.fenlonsky.ticket.model.EmailProvider;

import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * Created by fenlon on 15-12-12.
 */
public class SendFactory {
    public static Sender createMailSender(BlockingQueue<Email> emailQueue) {
        return new MailSender(emailQueue);
    }

    public static Sender createSmsSender() {
        return new SmsSender();
    }
}
