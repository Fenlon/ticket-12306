package com.fenlonsky.ticket.factory;

import com.fenlonsky.ticket.model.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;

/**
 * Created by fenlon on 15-12-12.
 */
public class MailSender implements Sender {
    private static final Logger logger = LoggerFactory.getLogger(MailSender.class);
    private BlockingQueue<Email> emailQueue;

    public MailSender(BlockingQueue<Email> emailQueue) {
        this.emailQueue = emailQueue;
    }

    public void send(String content, String[] tos) {
        Email mail = new Email();
        mail.setContent(content);
        mail.setTos(tos);
        this.emailQueue.offer(mail);
    }
}
