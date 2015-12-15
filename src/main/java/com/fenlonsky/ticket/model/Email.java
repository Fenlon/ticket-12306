package com.fenlonsky.ticket.model;

/**
 * Created by fenlon on 15-12-14.
 */
public class Email {
    private String content;
    private String[] tos;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String[] getTos() {
        return tos;
    }

    public void setTos(String[] tos) {
        this.tos = tos;
    }
}
