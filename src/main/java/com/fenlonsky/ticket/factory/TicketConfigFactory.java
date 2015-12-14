package com.fenlonsky.ticket.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by fenlon on 15-12-12.
 */
public class TicketConfigFactory {
    private static final Logger logger = LoggerFactory.getLogger(TicketConfigFactory.class);
    private static final TicketConfig TICKET_CONF;

    static {
        String confDir = System.getProperty(TicketConfig.CONF_DIR_KEY);
        logger.info("custom conf path is " + confDir);
        TICKET_CONF = readConf(confDir);
    }

    private static TicketConfig readConf(String confDir) {
        InputStream isDefault;
        InputStream isCustom = null;
        Yaml yaml = new Yaml();
        try {
            isDefault = TicketConfigFactory.class
                    .getResourceAsStream("/ticket.default.yaml");
            Map<String, Object> defaultConf = (Map<String, Object>) yaml
                    .load(isDefault);
            Map<String, Object> customConf = null;

            if (confDir != null && !confDir.isEmpty()) {
                String path = confDir + File.separator + "ticket.yaml";
                logger.info("loading config from " + path + "");
                if (new File(path).exists()) {
                    isCustom = new FileInputStream(path);
                    customConf = (Map<String, Object>) yaml.load(isCustom);
                }
            }

            if (customConf != null) {
                defaultConf.putAll(customConf);
            }
            String home = System.getProperty(TicketConfig.TICKET_HOME_KEY, ".");
            defaultConf.put(TicketConfig.TICKET_HOME_KEY, home);

            defaultConf.put(TicketConfig.CONF_DIR_KEY, confDir);

            return new TicketConfig(defaultConf);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new Error(e);
        } finally {
            if (isCustom != null) {
                try {
                    isCustom.close();
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    public static TicketConfig getTicketConf() {
        return TICKET_CONF;
    }

}
