package com.fenlonsky.ticket.factory;

import com.fenlonsky.ticket.model.Account;
import com.fenlonsky.ticket.model.Email;
import com.fenlonsky.ticket.model.EmailProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by fenlon on 15-12-12.
 */
public class MailSender implements Sender {
    private static final Logger logger = LoggerFactory.getLogger(MailSender.class);

    ExecutorService executorService = Executors.newSingleThreadExecutor();
    private EmailProvider provider;

    private BlockingQueue<Email> emailQueue;

    public MailSender(EmailProvider provider, BlockingQueue<Email> emailQueue) {
        this.provider = provider;
        this.emailQueue = emailQueue;
    }

    public void start() {
        EmailThread thread0 = new EmailThread();
        EmailThread thread2 = new EmailThread();

        //取出提交到池子中
        while (true && !Thread.currentThread().isInterrupted()) {
            try {
                Email email = emailQueue.take();
                if (email == null) {
                    return;
                }
                if (thread0.getEmail() != null) {
                    thread0.setEmail(email);
                    new Thread(thread0).start();
                }
                if (thread2.getEmail() != null) {
                    thread2.setEmail(email);
                    new Thread(thread2).start();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void Send(String content, String[] tos) {
        Email mail = new Email();
        mail.setContent(content);
        mail.setTos(tos);
        this.emailQueue.offer(mail);
    }

    private class EmailThread implements Runnable {
        private Email email;

        public EmailThread() {
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Email getEmail() {
            return email;
        }

        public void run() {
            send(email, provider.getAccounts());
            this.email = null;
        }
    }

    public void send(Email email, List<Account> accounts) {
        Properties props = new Properties();
        props.setProperty("mail.smtp.host", provider.getHost());
        props.setProperty("mail.smtp.auth", String.valueOf(provider.getAuth()));
        props.setProperty("mail.transport.protocol", "smtp");
        Session session = Session.getInstance(props);
        try {
            MimeMultipart mm = buildTemplate(email.getContent());
            for (Account account : accounts) {
                if (!account.getActive()) {
                    continue;
                }
                Message message = createMessage(session, account.getUsername(), email.getTos());
                message.setContent(mm);
                message.saveChanges();
                try {
                    Transport ts = session.getTransport();
                    if (send(message, ts, account)) {
                        return;
                    }
                } catch (NoSuchProviderException e) {
                    logger.error(e.getMessage(), e);
                    return;
                }
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


    private Boolean send(Message message, Transport ts, Account account) {
        try {
            ts.connect(account.getUsername(), account.getPassword());
            ts.sendMessage(message, message.getAllRecipients());
            ts.close();
            return true;
        } catch (MessagingException e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    private MimeMultipart buildTemplate(String content) throws MessagingException {
        // 创建MimeBodyPart封装正文
        MimeBodyPart text = new MimeBodyPart();
        Date time = new Date();
        text.setContent(time.toString() + "\n" + content, "text/html;charset=UTF-8");
        // 创建
        MimeMultipart mm = new MimeMultipart();
        mm.addBodyPart(text);
        mm.setSubType("related");
        return mm;
    }


    private Message createMessage(Session session, String from, String[] to) {
        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(from));
            InternetAddress toAddress = null;
            for (int i = 0; i < to.length; i++) {
                toAddress = new InternetAddress(to[i]);
                message.addRecipient(Message.RecipientType.TO, toAddress);
            }
//            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("车票通知邮件");
            return message;
        } catch (MessagingException e) {
            e.printStackTrace();
            logger.error("构建邮件信息失败,{}", e.getMessage());
        }
        return null;
    }
}
