package com.datafbank.spider.imxam001.zfxm;

import com.datafbank.spider.imxam001.SpiderAddRequest;
import com.dataofbank.ryze.Ryze;
import com.dataofbank.ryze.Spider;

public class ZFXM implements Ryze {

    @Override
    public Spider spider() {
        return new Spider(new ZFXXProcessor());//.changeDownloader(new ProxyHttpClientDownloader(new MayiProxy()));
    }


    public static void main(String[] args) {
        new ZFXM().spider().setThreadNum(3).run();
    }
}
