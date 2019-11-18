package com.bcb.util;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MobileUtil {

	// \b 是单词边界(连着的两个(字母字符 与 非字母字符) 之间的逻辑上的间隔), 
	// 字符串在编译时会被转码一次,所以是 "\\b" 
	// \B 是单词内部逻辑间隔(连着的两个字母字符之间的逻辑上的间隔) 
	private static String phoneReg = "\\b(ip(hone|od)[\\d]|android|opera m(ob|in)i" 
	+"|windows (phone|ce)|blackberry" 
	+"|s(ymbian|eries60|amsung)|p(laybook|alm|rofile/midp" 
	+"|laystation portable)|nokia|fennec|htc[-_]" 
	+"|mobile|up.browser|[1-4][0-9]{2}x[1-4][0-9]{2})\\b"; 
	private static String tableReg = "\\b(ipad|tablet|(Nexus 7)|up.browser" 
	+"|[1-4][0-9]{2}x[1-4][0-9]{2})\\b"; 
	private static Pattern phonePat = Pattern.compile(phoneReg, Pattern.CASE_INSENSITIVE); 
	private static Pattern tablePat = Pattern.compile(tableReg, Pattern.CASE_INSENSITIVE); 

	/**
	 * 终端的类型是电脑还是移动端
	 * @author G/2016-3-19 上午10:59:08
	 * @param ua
	 * @return
	 */
	public final static String TerminalType(String ua){
		StringBuffer type=new StringBuffer();
		//判断终端类型
		if(ua.toLowerCase().indexOf("windows")>-1){
			type.append("Windows");
		}else if(ua.toLowerCase().indexOf("linux")>-1){
			type.append("Linux");
		}else if(ua.toLowerCase().indexOf("mac")>-1){
			type.append("Mac os");
		}else{
			type.append("Other os");
		}
			
		//设备类型
		if(ua.toLowerCase().indexOf("android")>-1){
			type.append(">Android");
		}else if(ua.toLowerCase().indexOf("iphone")>-1){
			type.append(">Iphone");
		}else if(ua.toLowerCase().indexOf("ipad")>-1){
			type.append(">Ipad");
		}
		
		//判断浏览器类型
		if(ua.toLowerCase().indexOf("chrome")>-1){
			type.append(">Chrome");
		}else if(ua.toLowerCase().indexOf("msie")>-1){
			type.append(">IE");
		}else if(ua.toLowerCase().indexOf("firefox")>-1){
			type.append(">FireFox");	   
		}else if(ua.toLowerCase().indexOf("qq")>-1 || ua.toLowerCase().indexOf("micromessenger")>-1){
			type.append(">Tencent browser");
		}else if(ua.toLowerCase().indexOf("ucbrowser")>-1){
			type.append(">UC browser");
		}else if(ua.toLowerCase().indexOf("safari")>-1){
			type.append(">Safari");
		}else if(ua.toLowerCase().indexOf("baiduspider")>-1){
			type.append(">Baidu Spider");
		}else if(ua.toLowerCase().indexOf("googlebot")>-1){
			type.append(">Google bot");
		}else if(ua.toLowerCase().indexOf("bingbot")>-1){
			type.append(">Bing bot");
		}else if(ua.toLowerCase().indexOf("sogou web spider")>-1){
			type.append(">Sogou web spider");
		}
		return type.toString();
	}

	/** 
	 * 判断是否是异步请求
	 * @author G/2018/4/23 16:48
	 * @param request       
	 */
	public final static boolean isAjax(HttpServletRequest request){
		String xReq = request.getHeader("x-requested-with");
		if (!CheckUtil.isEmpty(xReq) && "XMLHttpRequest".equalsIgnoreCase(xReq)) {
			// 是ajax异步请求
			return true;
		}else{
			return false;
		}
	}

	//判断是否是移动设备
	public final static boolean isMobile(String userAgent){ 
		if(userAgent == null)userAgent = ""; 
		// 匹配 
		Matcher matcherPhone = phonePat.matcher(userAgent); 
		Matcher matcherTable = tablePat.matcher(userAgent); 
		if(matcherPhone.find() || matcherTable.find()){ 
			return true; 
		}
		return false;
	} 

	/**
	 * 判断是否是手机端
	 * @author G/2017年6月13日 下午4:39:23
	 * @param request
	 * @return
	 */
	public final static boolean isMobile(HttpServletRequest request){ 
		return isMobile(request.getHeader("USER-AGENT").toLowerCase());
	}

	/**
	 * 判断是否是手机app
	 * @author G/2017年6月13日 下午4:40:11
	 * @param ua
	 * @return
	 */
	public final static boolean isApp(String ua){
		if(ua.toLowerCase().indexOf("aba9f441f809ad35c031033224e2d7ac/fanshangqu_app")>-1)
			return true;
		return false;
	}

	/**
	 * 判断是否是手机app
	 * @author G/2017年1月9日 上午9:56:52
	 * @param request
	 * @return
	 */
	public final static boolean isApp(HttpServletRequest request){
		return isApp(request.getHeader("USER-AGENT").toLowerCase());
	}
	
	public final static boolean isQQBrowser(String ua){
		if(ua.toLowerCase().indexOf("mqqbrowser")>0 || ua.toLowerCase().indexOf("micromessenger")>0)
			return true;
		return false;
	}
	
	public final static boolean isUCBrowser(String ua){
		if(ua.toLowerCase().indexOf("ucbrowser")>0)
			return true;
		return false;
	}
	
	public final static boolean isIphoneBrowser(String ua){
		if(ua.toLowerCase().indexOf("iphone")>0)
			return true;
		return false;
	}
	
	public final static boolean isAndroidBrowser(String ua){
		if(ua.toLowerCase().indexOf("android")>0)
			return true;
		return false;
	}
}
