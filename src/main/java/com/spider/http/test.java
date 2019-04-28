//package com.spider.http;
//
//import com.spider.github.SpiderGithub;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.config.RequestConfig;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.util.EntityUtils;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//public class test {
//
//    public static void main(String[] args) throws IOException, InterruptedException {
//
//
//        SpiderGithub spiderGithub = new SpiderGithub();
//
//
//        String url = "https://github.com/search?p=1&q=stars%3A%3E10000&type=Repositories";
////        String connectUrl = url.replace("p=x", "p=".concat(String.valueOf(i)));
//        System.out.println(url);
//        CloseableHttpClient httpCilent = HttpClients.createDefault();
//
//        HttpGet httpGet = new HttpGet(url);
//        httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
//        httpGet.setHeader("Accept-Encoding", "gzip, deflate, br");
//        httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
//        httpGet.setHeader("Cache-Control", "no-cache");
//        httpGet.setHeader("Connection", "keep-alive");
//        httpGet.setHeader("Host", "github.com");
//        httpGet.setHeader("Pragma", "no-cache");
//        httpGet.setHeader("Upgrade-Insecure-Requests", "1");
//        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.80 Safari/537.36");
//
//
//        RequestConfig requestConfig = RequestConfig.custom()
//                .setConnectTimeout(1000)   //设置连接超时时间
//                .setConnectionRequestTimeout(5000) // 设置请求超时时间
//                .setSocketTimeout(5000)
//                .setRedirectsEnabled(true)//默认允许自动重定向
//                .build();
//        httpGet.setConfig(requestConfig);
//        String resHtml = "";
//        try {
//            HttpResponse httpResponse = httpCilent.execute(httpGet);
//            if (httpResponse.getStatusLine().getStatusCode() == 200) {
//                resHtml = EntityUtils.toString(httpResponse.getEntity());//获得返回的结果
//                Document document = Jsoup.parse(resHtml);
//                spiderGithub.getPageData(document);
//            } else {
//                System.out.println("-----------------------------------------------------------------------------------------------------------" + httpResponse.getStatusLine().getStatusCode());
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                httpCilent.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//    }
//}
