package com.zhongbang.sxb.event;

/**
 * Created by Administrator on 2017-03-29.
 */

public class MessageEvent {
    public String msg;
    public String from;

    public MessageEvent(String msg, String from) {
        this.msg = msg;
        this.from = from;
    }
}
