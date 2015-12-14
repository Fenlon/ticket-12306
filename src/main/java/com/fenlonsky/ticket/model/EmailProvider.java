package com.fenlonsky.ticket.model;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by fenlon on 15-12-12.
 */
public class EmailProvider {
    private String type;
    private String host;
    private Boolean auth;
    private String protocol;

    private List<Account> accounts = Lists.newArrayList();


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Boolean getAuth() {
        return auth;
    }

    public void setAuth(Boolean auth) {
        this.auth = auth;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public List<Account> getAccounts() {
        return accounts;
    }
}
