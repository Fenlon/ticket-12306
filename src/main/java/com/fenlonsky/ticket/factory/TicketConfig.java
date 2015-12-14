package com.fenlonsky.ticket.factory;

import com.fenlonsky.ticket.model.Account;
import com.fenlonsky.ticket.model.EmailProvider;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.*;

/**
 * Created by fenlon on 15-12-12.
 */
public class TicketConfig {
    public static final String CONF_DIR_KEY = "ticket.config";
    public static final String TICKET_HOME_KEY = "ticket.home";
    public static final String EMAIL_PROVIDERS = "ticket.mail.providers";
    public static final String EMAIL_MODE = "ticket.mail.mode";

    private final Map<String, Object> config;
    private List<String> modes;

    public TicketConfig(Map<String, Object> config) {
        super();
        this.config = config;
    }

    public String getConfDirKey() {
        return getString(CONF_DIR_KEY);
    }

    public String getTicketHomeKey() {
        return getString(TICKET_HOME_KEY);
    }

    public String getRandomMode() {
        if (modes == null || modes.size() == 0) {
            // default 163
            return "163";
        }
        Random random = new Random();
        return modes.get(random.nextInt(modes.size()));
    }


    public String getMailMode() {
        return getString(EMAIL_MODE);
    }


    public Map<String, EmailProvider> getEmailProviders() {
        List<LinkedHashMap> list = (List<LinkedHashMap>) config.get(EMAIL_PROVIDERS);
        if (list == null) {
            return null;
        }
        Map<String, EmailProvider> providers = Maps.newHashMap();
        modes = Lists.newArrayList();
        EmailProvider provider;
        for (LinkedHashMap map : list) {
            provider = new EmailProvider();
            provider.setType((String) map.get("type"));
            provider.setHost((String) map.get("host"));
            provider.setAuth((Boolean) map.get("auth"));
            provider.setProtocol((String) map.get("transport.protocol"));
            provider.setAccounts(parse((List<LinkedHashMap>) map.get("accounts")));
            providers.put(provider.getType(), provider);
            modes.add(provider.getType());
        }
        return providers;
    }

    private List<Account> parse(List<LinkedHashMap> list) {
        List<Account> accounts = Lists.newArrayList();
        Account account;
        for (LinkedHashMap map : list) {
            account = new Account();
            account.setUsername((String) map.get("username"));
            account.setPassword((String) map.get("password"));
            accounts.add(account);
        }
        return accounts;
    }


    public String getString(String key) {
        return getString(key, null);
    }


    public String getString(String key, String defaultValue) {
        Object value = config.get(key);
        if (value == null) {
            if (defaultValue == null) {
                throw new NullPointerException();
            }
            return defaultValue;
        }
        return (String) value;
    }

    public int getInt(String key) {
        return getInt(key, null);
    }

    public int getInt(String key, Integer defaultValue) {
        Object value = config.get(key);
        if (value == null) {
            if (defaultValue == null) {
                throw new NullPointerException();
            }
            return defaultValue;
        }
        return ((Number) value).intValue();
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, null);
    }

    public boolean getBoolean(String key, Boolean defaultValue) {
        Object value = config.get(key);
        if (value == null) {
            if (defaultValue == null) {
                throw new NullPointerException();
            }
            return defaultValue;
        }
        return ((Boolean) value).booleanValue();
    }

    public List<String> getList(String key) {
        return getList(key, null);
    }

    @SuppressWarnings("unchecked")
    public List<String> getList(String key, List<String> defaultValue) {
        Object value = config.get(key);
        if (value == null) {
            if (defaultValue == null) {
                throw new NullPointerException();
            }
            return defaultValue;
        }
        return Collections.unmodifiableList((List<String>) value);
    }


}
