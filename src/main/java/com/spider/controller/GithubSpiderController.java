package com.spider.controller;

import com.spider.dao.PageDataRepository;
import com.spider.dao.PageDataTemRepository;
import com.spider.entity.PageData;
import com.spider.entity.PageDataTem;
import com.spider.github.SpiderGithub;
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


    /**
     * @param index    页数
     * @param rank     0：总榜, 1：周榜, 2：月榜, 3：年榜
     * @param language '0 All', '1 Java', '2 Python', '3 JavaScript', '4 C', '5 C++', '6 C＃', '7 PHP','8 GO'
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    @RequestMapping("/getData1")
    public List<PageDataTem> getData1(int index, int rank, int language, HttpServletRequest request) throws IOException, InterruptedException {

        log.info("url:{}", request.getRequestURI());

        String url = "https://github.com/search?type=Repositories&q=stars%3A%3E%3D10&p=x";

        String date = "";
        if (rank != 0) {
            switch (rank) {
                case 1:
                    date = DateUtil.getBeforeDate(7);
                    break;
                case 2:
                    date = DateUtil.getBeforeDate(30);
                    break;
                case 3:
                    date = DateUtil.getBeforeDate(365);
                    break;
            }
            url = url.replace("q=stars%3A%3E%3D10", "q=stars%3A%3E%3D10+created%3A%3E" + date);
        }

        String la = "";
        if (language != 0) {
            switch (language) {
                case 1:
                    la = "Java";
                    break;
                case 2:
                    la = "Python";
                    break;
                case 3:
                    la = "JavaScript";
                    break;
                case 4:
                    la = "C";
                    break;
                case 5:
                    la = "C++";
                    break;
                case 6:
                    la = "C＃";
                    break;
                case 7:
                    la = "PHP";
                    break;
                case 8:
                    la = "GO";
                    break;
            }
            url = url.concat("&l=" + la);
        }


        List<PageDataTem> pageDateList = null;


        String connectUrl = url.replace("p=x", "p=".concat(String.valueOf(index)));
        System.out.println(connectUrl);

        CloseableHttpClient httpCilent = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet(connectUrl);
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
                Document document = Jsoup.parse(resHtml);
                pageDateList = spiderGithub.getPageData(document);
            } else {
                throw new Exception("http 状态异常" + httpResponse.getStatusLine().getStatusCode());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpCilent.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return pageDateList;

    }


    @RequestMapping("/schedule")
    public void schedule() throws Exception {

        log.info("爬虫任务开始");
        pageDataTemRepository.truncateTable();
        spiderGithub.scheduleSpider();
        pageDateRepository.truncateTable();
        pageDateRepository.copyTable();
        log.info("爬虫任务结束");
    }


    @RequestMapping("/getData")
    public List<PageData> getData(int index, int rank, int language, HttpServletRequest request){

        log.info("url:{}", request.getRequestURI());
        log.info("pageIndex=[{}],type=[{}],language[{}]",index,rank,language);
        System.out.println((index-1)*10);
        Pageable pageable=new PageRequest(index-1, 10);  //分页信息

        Page<PageData> pageList = pageDateRepository.getPageDataByLanguageAndTypeOrderById(String.valueOf(language), String.valueOf(rank), pageable);
        List<PageData> content = pageList.getContent();

        return content;

    }

}
