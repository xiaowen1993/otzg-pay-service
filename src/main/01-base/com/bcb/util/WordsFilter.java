package com.bcb.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 
 *
 *	敏感词过滤
 * @author ShengDecheng
 *
 */
public class WordsFilter {

	//正则表达式
	private static Pattern pattern = null;
	//敏感词命中数量
	public static int count = 0;

	//从words.properties初始化正则表达式字符串
	private static void initPattern() {
		StringBuffer patternBuffer = new StringBuffer();
		try {
			//加载词库
			InputStream in = WordsFilter.class.getClassLoader().getResourceAsStream("sensewords.properties");

			Properties property = new Properties();
			property.load(in);
			Enumeration<?> enu = property.propertyNames();

			//初始化敏感词数据结构
			patternBuffer.append("(");
			while (enu.hasMoreElements()) {
				patternBuffer.append(enu.nextElement().toString() + "|");
			}
			patternBuffer.deleteCharAt(patternBuffer.length() - 1);
			patternBuffer.append(")");

			// 装换编码
			pattern = Pattern.compile(patternBuffer.toString());
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		}
	}
	
	//过滤掉非法词汇并返回
	public static String doFilter(String str) {

		//如果没有加载词库
		if (pattern==null){
			initPattern();
		}

		//执行过滤匹配
		Matcher m = pattern.matcher(str);

		// 统计命中的数量
		while (m.find()) {
			count ++;
		}

		// 以**号代替敏感词返回
		return m.replaceAll("**");
	}

	public static void main(String[] args) {
//		long startNumer = System.currentTimeMillis();
		String str = "我日，操你妈，fuck，你妹的 干啥呢";

		str = WordsFilter.doFilter(str);
		System.out.println("命中敏感词的数量：" + WordsFilter.count);
//		long endNumber = System.currentTimeMillis();
//		System.out.println("总共耗时:"+(endNumber-startNumer)+"ms");
		System.out.println("替换后的字符串为:\n"+str);

		str = "太多的伤感情怀也许只局限于饲养基地 荧幕中的情节，主人公尝试着去用某种方式渐渐的很潇洒地释自杀指南怀那些自己经历的伤感。"
				+ "然后法车仑功 我们的扮演的角色就是跟随着主人yum公的喜红客联盟 怒哀20于饲养基地 荧幕中的情节，主人公尝试着去用某种方式渐渐的很潇洒地释自杀指南怀那些自己经历的伤感。"
				+ "关, 人, 流, 电, 发, 情, 太, 限,法 轮功, 个人, 经, 色, 许, 公, 动, 地, 方, 基, 在, 上, 红, 强, 自杀指南, 制, 卡, 三级片, 一, 夜, 多, 手机, 于, 自，"
				+ "难过就躺在某一个人的怀里尽情的阐述心扉或者手机卡复制器一个人一杯红酒一部电影在夜三级片 深人静的晚上，关上电话静静的发呆着。";
		str = WordsFilter.doFilter(str);
		System.out.println("命中敏感词的数量：" + WordsFilter.count);
		System.out.println("替换后的字符串为:\n"+str);
	}
}
