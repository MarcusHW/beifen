package com.datafbank.spider.imxam001;

import com.dataofbank.ryze.util.ReplicationDataUtil;

public class DataSave {
    public static void main(String[] args) {
        ReplicationDataUtil
                .replication("localhost:27017", "192.168.1.243:27017", "租房信息", "LJXA001");
    }
}
