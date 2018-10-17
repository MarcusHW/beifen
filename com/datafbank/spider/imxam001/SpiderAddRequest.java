package com.datafbank.spider.imxam001;

import com.dataofbank.ryze.Page;
import com.dataofbank.ryze.Request;
import com.dataofbank.ryze.Spider;
import com.dataofbank.ryze.processor.PageProcessor;
import com.dataofbank.ryze.util.excerecord.SeedDownloadException;

public class SpiderAddRequest extends Spider {

    public SpiderAddRequest(PageProcessor pageProcessor) {
        super(pageProcessor);
    }

    protected void processRequest(final Request request) {
        try {
            // long current = System.currentTimeMillis();
            Page page = download(request);// 页面下载
            if (page.isSuccess()) {
                pageProcess(page);// 页面处理
                pipelineProcess(page);// 数据处理
                addRequest(page);// 添加队列
                // minitor.successMinitor(current);// 监控爬虫
            } else {
                fail(page);
                addRequest(page);// 添加队列
                minitor.getLogger().error(request.getUrl() + "页面下载失败，不继续执行页面解析和数据保存功能！");
                // zj
                if (request.isSeed()) {
                    try {
                        throw new SeedDownloadException("种子页下载失败:" + request.getUrl());
                    } catch (Exception e) {
                        exceptionRecord.exceptionRecord(request, e);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            minitor.getLogger().warn(request.getUrl() + "的页面处理出现异常：" + e.getMessage());
            exceptionRecord.exceptionRecord(request, e);// zj
        }
        exceptionRecord.runTimeout(request, beginTime);// zj
    }

    public void fail(Page page) {
        Request request = page.getRequest();
        System.out.println(page.getRequest().getUrl());
        System.out.println("下载失败，重新加入下载");
        Request nxtReq = new Request(request.getUrl() + "?a=" + Math.random());
        nxtReq.addHeader("user-agent",
                "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
        nxtReq.setLevel(request.getLevel());
        page.addSubRequest(nxtReq);
    }

}
