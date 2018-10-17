package com.datafbank.spider.imxam001.esfxx;

import com.datafbank.spider.imxam001.SpiderAddRequest;
import com.dataofbank.ryze.Ryze;
import com.dataofbank.ryze.Spider;

public class ESFXX implements Ryze {
    @Override
    public Spider spider() {
        return new SpiderAddRequest(new ESFXXProcessor());
    }

    public static void main(String[] args) {
        new ESFXX().spider().setThreadNum(3).run();
    }
}
