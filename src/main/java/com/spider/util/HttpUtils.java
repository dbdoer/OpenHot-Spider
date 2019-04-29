package com.spider.util;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class HttpUtils {

    private Logger log = LoggerFactory.getLogger(HttpUtils.class);

    public String httpGetRequest(String url) throws Exception {
        CloseableHttpClient httpCilent = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        httpGet.setHeader("Accept-Encoding", "gzip, deflate, br");
        httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
        httpGet.setHeader("Cache-Control", "no-cache");
        httpGet.setHeader("Connection", "keep-alive");
        httpGet.setHeader("Host", "github.com");
        httpGet.setHeader("Pragma", "no-cache");
        httpGet.setHeader("Upgrade-Insecure-Requests", "1");
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.80 Safari/537.36");

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(3000)   //设置连接超时时间
                .setConnectionRequestTimeout(3000) // 设置请求超时时间
                .setSocketTimeout(5000)
                .setRedirectsEnabled(false)//默认允许自动重定向
                .build();
        httpGet.setConfig(requestConfig);
        String resHtml = "";
        try {
            HttpResponse httpResponse = httpCilent.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                resHtml = EntityUtils.toString(httpResponse.getEntity());//获得返回的结果
            } else {
                log.info("http 状态异常,状态码[{}]", httpResponse.getStatusLine().getStatusCode());
                throw new HttpException("http 状态异常" + httpResponse.getStatusLine().getStatusCode());
            }
        }catch(HttpException e){
            log.info("HttpException:{}", e);
            throw new HttpException(e.getMessage());
        } catch (IOException e) {
            log.info("IOException:{}", e);
            throw new IOException(e);
        } catch (Exception e) {
            log.info("Exception:{}", e);
            throw new Exception(e);
        } finally {
            try {
                httpCilent.close();
            } catch (IOException e) {
                log.info("IOException:{}", e);
                throw new IOException(e);
            }
        }

        return resHtml;
    }
}
