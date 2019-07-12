package com.spider.controller;

import com.spider.dao.PageDataRepository;
import com.spider.dao.PageDataTemRepository;
import com.spider.entity.PageData;
import com.spider.entity.PageDataTem;
import com.spider.github.SpiderGithub;
import com.spider.schedule.SpiderSchedule;
import com.spider.util.DateUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/githubSpider")
public class GithubSpiderController {

    private Logger log = LoggerFactory.getLogger(GithubSpiderController.class);

    @Autowired
    private PageDataRepository pageDateRepository;

    @Autowired
    private SpiderGithub spiderGithub;

    @Autowired
    private PageDataTemRepository pageDataTemRepository;

    @Autowired
    private SpiderSchedule spiderSchedule;




    @RequestMapping("/schedule")
    public void schedule() throws Exception {
        spiderSchedule.schedule();
    }


    @RequestMapping("/getData")
    public List<PageData> getData(int index, int rank, int language, String ip,HttpServletRequest request){

        log.info("url:{}", request.getRequestURI());
        log.info("pageIndex=[{}],type=[{}],language[{}],UserIp[{}]",index,rank,language,ip);
        System.out.println((index-1)*10);
        Pageable pageable=new PageRequest(index-1, 10);  //分页信息

        Page<PageData> pageList = pageDateRepository.getPageDataByLanguageAndTypeOrderById(String.valueOf(language), String.valueOf(rank), pageable);
        List<PageData> content = pageList.getContent();

        return content;

    }

}
