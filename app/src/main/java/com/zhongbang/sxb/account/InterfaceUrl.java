package com.zhongbang.sxb.account;

public class InterfaceUrl {
	public static String url ="http://chinazbjt.cn";
	public static String interface_url = "http://huanqiusc.com";
	private InterfaceUrl(){}
	private static InterfaceUrl interfaceUrl=new InterfaceUrl();
	private static InterfaceUrl getUrl(){
		return interfaceUrl;
	}
}
