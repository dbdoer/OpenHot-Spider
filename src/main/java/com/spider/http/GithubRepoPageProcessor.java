//package com.spider.http;
//
//
//import com.spider.github.SpiderGithub;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//
//import java.io.IOException;
//import java.util.Scanner;
//
//public class GithubRepoPageProcessor  {
//
//
//    public static void main(String[] args) throws IOException {
//
//        String uri = "https://github.com/search?type=Repositories&q=stars%3A%3E%3D10";
//        Scanner scanner =new Scanner(System.in);
//
//        System.out.println("GitHub热门榜单爬虫已开启.......");
//        System.out.print("请输入热门语言(all为全部):");
//        String la = scanner.next();
//        System.out.print("请输入创建日期(all为全部):");
//        String date = scanner.next();
//        System.out.print("请输入查询条数:");
//        int size = scanner.nextInt();
//
//        if(!"all".equals(la)){
//            uri= uri.concat("&l="+la);
//        }
//
//        if("".equals(size)){
//            uri= uri.concat("&p=x");
//        }
//
//        if(!"all".equals(date)){
//            uri = uri.replace("q=stars%3A%3E%3D10","q=stars%3A%3E%3D10+created%3A%3E"+date);
//        }
//
//
//        for (int i =1;i<=size/10;i++){
//            String connectUrl = uri.replace("p=x","p=".concat(String.valueOf(i)));
////            System.out.println(connectUrl);
//            Document document = Jsoup.connect(connectUrl)
//                    .userAgent("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)")
//                    .timeout(50000)
//                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
//                    .header("Accept-Encoding", "gzip, deflate, br")
//                    .header("Accept-Language", "zh-CN,zh;q=0.9")
//                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36")
//                    .get();
////            SpiderGithub.getPageDate(document);
//        }
//
//    }
//}
