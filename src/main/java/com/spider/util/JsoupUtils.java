package com.spider.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

@Component
public class JsoupUtils {

    public Document htmlToDoc(String htmlStr){
        return Jsoup.parse(htmlStr);
    }

}
