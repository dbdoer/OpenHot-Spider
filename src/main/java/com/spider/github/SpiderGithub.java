package com.spider.github;

import com.spider.dao.LanguageRepository;
import com.spider.dao.PageDataRepository;
import com.spider.dao.PageDataTemRepository;
import com.spider.entity.Language;
import com.spider.entity.PageData;
import com.spider.entity.PageDataTem;
import com.spider.util.DateUtil;
import com.spider.util.HttpUtils;
import org.apache.http.HttpException;
import org.apache.http.conn.ConnectTimeoutException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
public class SpiderGithub {

    private Logger log = LoggerFactory.getLogger(SpiderGithub.class);

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private PageDataTemRepository pageDataTemRepository;

    @Autowired
    private HttpUtils httpUtils;

    public List<PageDataTem> getPageData(Document document) {

        Element repoList = document.getElementsByClass("repo-list").get(0);

        Elements lis = repoList.getElementsByClass("repo-list-item d-flex flex-column flex-md-row flex-justify-start py-4 public source");

        List<PageDataTem> list = new ArrayList<>();

        for (Element li : lis) {
            PageDataTem pageDateTem = new PageDataTem();

            Elements a = li.getElementsByClass("col-12 col-md-8 pr-md-3")
                    .get(0)
                    .getElementsByTag("h3")
                    .get(0)
                    .getElementsByTag("a");

            String uri = "https://github.com" + a.attr("href").intern();

            String projectName = a.text();

            Elements desc = li.getElementsByClass("flex-shrink-0 col-6 col-md-4 pt-2 pr-md-3 d-flex");
            String language = desc.get(0)
                    .getElementsByClass("text-gray flex-auto min-width-0")
                    .get(0)
                    .text();

            String star = desc.get(0)
                    .getElementsByClass("pl-2 pl-md-0 text-right flex-auto min-width-0")
                    .get(0)
                    .getElementsByClass("muted-link")
                    .get(0)
                    .text();


            String des = li.getElementsByClass("col-12 col-md-8 pr-md-3")
                    .get(0)
                    .getElementsByClass("col-12 col-md-9 d-inline-block text-gray mb-2 pr-4")
                    .text();

            pageDateTem.setProjectName(projectName);
            pageDateTem.setLanguage(language);
            pageDateTem.setStar(star);
            pageDateTem.setUrl(uri);
            pageDateTem.setDesc(des);

            list.add(pageDateTem);

            log.info("projectName[{}],language[{}],star[{}],uri[{}],des[{}]", projectName, language, star, uri, des);
        }

        return list;
    }


    public void scheduleSpider() throws Exception {
        String url = "https://github.com/search?type=Repositories&q=stars%3A%3E%3D10&p=x";

        List<Language> languages = languageRepository.findAll();

        // 0:全部 1:周 2:月 3:季，4：年
        for (int t = 0; t <= 4; t++) {

            String dateUrl = url;
            String date = "";

            if (t == 1) {
                date = DateUtil.getBeforeDate(7);
                log.info("当前爬取日期为一周");
            } else if (t == 2) {
                date = DateUtil.getBeforeDate(30);
                log.info("当前爬取日期为一月");
            } else if (t == 3) {
                date = DateUtil.getBeforeDate(90);
                log.info("当前爬取日期为一季度");
            } else if (t == 4) {
                date = DateUtil.getBeforeDate(365);
                log.info("当前爬取日期为一年");
            } else {
                log.info("当前爬取日期为全部");
            }

            if (!StringUtils.isEmpty(date)) {
                dateUrl = url.replace("q=stars%3A%3E%3D10", "q=stars%3A%3E%3D10+created%3A%3E" + date);
            }

            for (int l = 0; l < languages.size(); l++) {

                String lanUrl = dateUrl;
                Language la = languages.get(l);
                log.info("当前爬取语言为：{}", la.getLanguage());
                if (l != 0) {
                    lanUrl = dateUrl.concat("&l=" + URLEncoder.encode(la.getLanguage()));
                }

                int i = 1;
                for (; i <= 10; ) {
                    int i_t = i;
                    String url_index = lanUrl.replace("p=x", "p=".concat(String.valueOf(i)));
                    log.info("url:{}", url_index);

                    try {
                        String htmlStr = httpUtils.httpGetRequest(url_index);
                        Document document = Jsoup.parse(htmlStr);
                        List<PageDataTem> pageDataList = this.getPageData(document);
                        for (PageDataTem pageDataTem : pageDataList) {
                            pageDataTem.setLanguage(String.valueOf(l));
                            pageDataTem.setType(String.valueOf(t));

                            pageDataTemRepository.save(pageDataTem);
                        }

                        i++;
                        Thread.sleep(2000);
                    } catch (HttpException | ConnectTimeoutException e) {
                        log.info("爬取数据出现异常,120秒后重试");
                        i = i_t;
                        Thread.sleep(1200000);
                        continue;
                    } catch (Exception e) {
                        log.info("Exception:{}", e);
                        throw new Exception(e);
                    }
                }
            }
        }
    }

}

