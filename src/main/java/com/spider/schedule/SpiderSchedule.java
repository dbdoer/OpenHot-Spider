package com.spider.schedule;

import com.spider.controller.GithubSpiderController;
import com.spider.dao.PageDataRepository;
import com.spider.dao.PageDataTemRepository;
import com.spider.github.SpiderGithub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SpiderSchedule {

    private Logger log = LoggerFactory.getLogger(GithubSpiderController.class);

    @Autowired
    private PageDataRepository pageDateRepository;

    @Autowired
    private SpiderGithub spiderGithub;

    @Autowired
    private PageDataTemRepository pageDataTemRepository;

    @Scheduled(cron = "${spider_cron}")
    public void schedule() throws Exception {
        log.info("爬虫任务开始");
        pageDataTemRepository.truncateTable();
        spiderGithub.scheduleSpider();
        pageDateRepository.truncateTable();
        pageDateRepository.copyTable();
        log.info("爬虫任务结束");
    }
}
