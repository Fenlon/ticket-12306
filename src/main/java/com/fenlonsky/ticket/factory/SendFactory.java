package com.fenlonsky.ticket.factory;

import com.fenlonsky.ticket.model.Email;
import com.fenlonsky.ticket.model.EmailProvider;

import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * Created by fenlon on 15-12-12.
 */
public class SendFactory {
    public static Sender createMailSender(TicketConfig config, BlockingQueue<Email> emailQueue) {
        Map<String, EmailProvider> providers = config.getEmailProviders();
        EmailProvider provider = null;
        if ("all".equals(config.getMailMode())) {
            provider = providers.get(config.getRandomMode());
        } else {
            provider = providers.get(config.getMailMode());
        }
        return new MailSender(provider, emailQueue);
    }

    public static Sender createSmsSender() {
        return new SmsSender();
    }
}
