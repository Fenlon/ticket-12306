package com.fenlonsky.ticket;

import com.google.common.base.Optional;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by fenlon on 15-12-12.
 */
public class HttpUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);
    private static final HttpClient httpClient = HttpClients.createDefault();

    public static Optional<String> doGet(String url) {
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            response = (CloseableHttpResponse) httpClient.execute(httpGet);
            StatusLine result = response.getStatusLine();
            if (result.getStatusCode() != HttpStatus.SC_OK) {
                logger.error("访问远程服务器失败:url为：{}", url);
                return Optional.absent();
            }

            HttpEntity entity = response.getEntity();
            String res = EntityUtils.toString(entity);
            return Optional.of(res);
        } catch (IOException e) {
            logger.error("远程请求失败:", e.getMessage());
        } finally {
            close(response);
        }
        return Optional.absent();
    }

    private static void close(Closeable response) {
        try {
            if (response != null) {
                response.close();
            }
        } catch (IOException e) {
            logger.error("Httpresponse 关闭失败:", e.getMessage());
        }
    }

}
