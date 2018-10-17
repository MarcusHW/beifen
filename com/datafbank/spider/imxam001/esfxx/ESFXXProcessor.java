package com.datafbank.spider.imxam001.esfxx;

import com.dataofbank.ryze.Page;
import com.dataofbank.ryze.Request;
import com.dataofbank.ryze.Site;
import com.dataofbank.ryze.processor.PageProcessor;
import com.dataofbank.ryze.util.NumberFormatUtil;
import com.dataofbank.ryze.util.StringUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;

public class ESFXXProcessor implements PageProcessor {
    @Override
    public Site getSite() {
        Site site = new Site("https://xa.lianjia.com");
        site.setAuthor("陈浮");
        site.setWebsiteName("链家-二手房信息");
        site.setWebsiteCode("LJXA001");
        site.setTheme("二手房信息");
        site.setThemeCode("ESFXX");
        Request seed = new Request("https://xa.lianjia.com/ershoufang/");
        site.addSeed(seed);
        site.setTimeout(6000);
        return site;
    }

    @Override
    public void process(Page page) {
        Request request = page.getRequest();
        int level = request.getLevel();
        switch (level) {
            case Request.Level.LEVEL2:
                process2(page);
                break;
            case Request.Level.LEVEL3:
                process3(page);
                break;
            case Request.Level.LEVEL1:
                process1(page);
                break;
        }
    }

    private void process1(Page page) {
        System.out.println("1级" + page.getRequest().getUrl());
        Document document = Jsoup.parse(page.getRawHtml());
        Elements elements = document.select(".position > dl:nth-child(2) > dd:nth-child(2) > div:nth-child(1) > div:nth-child(1) > a");
        for (Element element : elements) {
            String href = element.attr("href");
            String url = "https://xa.lianjia.com" + href;
            Request sub = new Request(url);
            sub.setLevel(Request.Level.LEVEL2);
            page.addSubRequest(sub);
        }


    }

    private void process2(Page page) {
        System.out.println("2级" + page.getRequest().getUrl());
        Document document = Jsoup.parse(page.getRawHtml());
        String countStr = document.select(".total > span:nth-child(1)").text();
        int count = Integer.parseInt(countStr);

        if (count > 0 && page.getRequest().getField("首页") == null) {
            String pgIndex;
            int pgCount = count % 30 == 0 ? count / 30 : (count / 30) + 1;
            for (int i = 1; i <= pgCount; i++) {
                String pageUrl = page.getRequest().getUrl();
                if (i == 1) {
                    pgIndex = "";
                } else {
                    pgIndex = "pg" + i;
                }
                String url = pageUrl + pgIndex;
                Request sub = new Request(url);
                sub.putField("首页", 1);
                sub.setLevel(Request.Level.LEVEL2);
                page.addSubRequest(sub);
            }
        }
        Elements elements = document.select("li.clear");
        for (Element element : elements) {
            String jumpUrl = element.select("div:nth-child(2) > div:nth-child(1) > a:nth-child(1)").attr("href");
            String title = element.select("div:nth-child(2) > div:nth-child(1) > a:nth-child(1)").text();
            Request subRequest = new Request(jumpUrl);
            subRequest.putField("标题", title);
            subRequest.setLevel(Request.Level.LEVEL3);
            page.addSubRequest(subRequest);
        }
    }

    private void process3(Page page) {
        System.out.println("3级" + page.getRequest().getUrl());
        Document document = Jsoup.parse(page.getRawHtml());
        String date = document.select(".transaction > div:nth-child(2) > ul:nth-child(1) > li:nth-child(1) > span:nth-child(2)").text();
        String sale = document.select("span.total").text();
        sale = NumberFormatUtil.format(sale, 2);
        String saleScheme = document.select(".price > span:nth-child(2) > span:nth-child(1)").text();
        sale = saleScheme.matches("万") ? sale + "万" : "";
        sale = NumberFormatUtil.format(sale);
        String homeScheme = document.select(".room > div:nth-child(1)").text();
        String area = document.select(".area > div:nth-child(1)").text().replace("平米", "");
        area = NumberFormatUtil.format(area, 2);
        String floor = document.select(".room > div:nth-child(2)").text();
        String xiaoQu = document.select("a.info").text();
        String quYu = document.select(".areaName > span:nth-child(3) > a:nth-child(1)").text();
        String dizhi = document.select(".areaName > span:nth-child(3) > a").text();
        dizhi = StringUtil.removeAllBlank(dizhi);
        HashMap<String, Object> map = new HashMap<>();
        map.put("标题", StringUtil.removeAllBlank(page.getRequest().getField("标题").toString()));
        map.put("房源信息", "经纪人");
        map.put("中介公司", "北京链家房地产经纪有限公司");
        map.put("发布时间", date);
        map.put("价格（元）", sale);
        map.put("户型", homeScheme);
        map.put("建筑面积（平方米）", area);
        map.put("楼层", floor);
        map.put("小区名称", xiaoQu);
        map.put("区域", quYu);
        map.put("地址", dizhi);
        page.addResultField(map);
        System.out.println(map);
    }
}
