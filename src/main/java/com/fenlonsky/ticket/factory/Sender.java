package com.fenlonsky.ticket.factory;

/**
 * Created by fenlon on 15-12-12.
 */
public interface Sender {
    void send(String content, String[] tos);
}
