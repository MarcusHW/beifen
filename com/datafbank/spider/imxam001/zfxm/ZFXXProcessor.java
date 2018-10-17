package com.datafbank.spider.imxam001.zfxm;

import com.datafbank.spider.imxam001.DateTool;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class ZFXXProcessor implements PageProcessor {
    @Override
    public Site getSite() {
        Site site = new Site("https://xa.lianjia.com");
        site.setAuthor("陈浮");
        site.setWebsiteName("链家-租房信息");
        site.setWebsiteCode("LJXA001");
        site.setTheme("租房信息");
        site.setThemeCode("ZFXX");
        Request seed = new Request("https://xa.lianjia.com/zufang/");
        site.addSeed(seed);
        site.setTimeout(6000);
        return site;
    }

    @Override
    public void process(Page page) {
        Request request = page.getRequest();
        int level = request.getLevel();
        switch (level) {
            case Request.Level.LEVEL1:
                process1(page);
                break;
            case Request.Level.LEVEL2:
                process2(page);
                break;
            case Request.Level.LEVEL3:
                process3(page);
                break;
        }
    }

    private void process1(Page page) {
        System.out.println("地区条件分类url:" + page.getRequest().getUrl());
        if (page.isSuccess()) {
            Document document = Jsoup.parse(page.getRawHtml());
            Elements elements = document.select("dl.dl-lst:nth-child(1) > dd:nth-child(2) > div:nth-child(1) > a");
            String baseUrl = "https://xa.lianjia.com/";
            for (int i = 1; i < elements.size(); i++) {
                String href = elements.get(i).attr("href");
                String url = baseUrl + href;
                Request request = new Request(url);
                request.setLevel(Request.Level.LEVEL2);
                page.addSubRequest(request);
            }
        }
    }

    private void process2(Page page) {
        System.out.println("地区分类下分页url:" + page.getRequest().getUrl());
        String pageUrl = page.getRequest().getUrl();
        Document document = Jsoup.parse(page.getRawHtml());
        String countStr = document.select(".list-head > h2:nth-child(1) > span:nth-child(1)").text();
        int count = Integer.parseInt(countStr);
        System.out.println(count);
        int pageCount = count % 30 > 0 ? (count / 30) + 1 : count / 30;
        if (pageCount > 1 && page.getRequest().getField("首页") == null) {
            String pgIndex;
            for (int i = 1; i <= pageCount; i++) {
                if (i == 1) {
                    pgIndex = "";
                } else {
                    pgIndex = "pg" + i;
                }
                String url =pageUrl + pgIndex ;
                Request request = new Request(url);
                request.putField("首页", 1);
                request.setLevel(Request.Level.LEVEL2);
                page.addSubRequest(request);
            }
        }


        Elements elements = document.select("#house-lst > li");
        for (Element element : elements) {
            String jumpUrl = element.select("div:nth-child(2) > h2:nth-child(1) > a:nth-child(1)").attr("href");
            String title = element.select("div:nth-child(2) > h2:nth-child(1) > a:nth-child(1)").attr("title");
            Request sub = new Request(jumpUrl);
            sub.setLevel(Request.Level.LEVEL3);
            sub.putField("标题", title);
            page.addSubRequest(sub);
        }
    }

    private void process3(Page page) {
        System.out.println("目标页面url:" + page.getRequest().getUrl());
        Document document = Jsoup.parse(page.getRawHtml());
        String sale = document.select(".total").text();
        sale = NumberFormatUtil.format(sale);
        String date = document.select("p.lf:nth-child(9)").text().replace("时间：", "");
        Date dateStr = DateTool.completionDate(date);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String realDate = dateStr != null ? format.format(dateStr.getTime()) : "";
        String homeScheme = document.select("p.lf:nth-child(2)").text().replace("房屋户型：", "");
        String area = document.select("p.lf:nth-child(1)").text().replace("面积：", "").replace("平米", "");
        area = NumberFormatUtil.format(area, 2);
        String floor = document.select("p.lf:nth-child(3)").text().replace("楼层：", "");
        String xiaoQu = document.select(".zf-room > p:nth-child(7) > a:nth-child(2)").text().replace("小区：", "");
        String quYu = document.select(".zf-room > p:nth-child(8) > a:nth-child(2)").text();
        String dizhi = document.select(".zf-room > p:nth-child(8) > a").text();
        dizhi = StringUtil.removeAllBlank(dizhi);
        HashMap<String, Object> map = new HashMap<>();
        map.put("标题", StringUtil.removeAllBlank(page.getRequest().getField("标题").toString()));
        map.put("房源信息", "经纪人");
        map.put("中介公司", "北京链家房地产经纪有限公司");
        map.put("发布时间", realDate);
        map.put("价格（元/月）", sale);
        map.put("户型", homeScheme);
        map.put("面积（平方米）", area);
        map.put("楼层", floor);
        map.put("小区名称", xiaoQu);
        map.put("区域", quYu);
        map.put("地址", dizhi);

        if (map.size() > 0) {
            System.out.println(map);
        } else {
            System.out.println("map为空");
        }
        page.addResultField(map);
    }
}
