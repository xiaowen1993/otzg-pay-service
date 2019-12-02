///**
// *
// */
//package com.bcb.wxpay.util.company;
//
///**
// *  @author G/2016年10月14日
// *
// */
//public class WxpayConfig {
//
//	//菠菜包
//	public final static String APPMCHID = "1513549201";		//服务商
//	public final static String APPAPPID = "wxa574b9142c67f42e";   //公众号appid
//	public final static String APPKEY = "Bcb19e0814494a248d28c591d4bc31e1";
//	public final static String APPSECRET = "b649bd1448add18b23ac1ee1fc960f8f";
//	//平台证书序列号
//	public final static String CERTSN = "749535475D89DF9ECF413A6CA05F74FF4CEDDCB8";
//
//
////	public final static String APPMCHID = "1405082702";
////	public final static String APPAPPID = "wxd8de5d37b6976b55";
//
//
//	//是否使用沙箱环境
//	public static boolean USESANDBOX = false;
//	//使用备用域名
//	public static boolean USEDOMAIN2 = false;
//
//	//公众号appid=wxa574b9142c67f42e mchid=1513549201 对应的沙箱key
//	public final static String SANDBOXKEY = "ccc6dbfaabfe4d13c01e8666b38c3f3e";
//
//
//	//子商户
////103725374	郑州李唐电子科技有限公司	1525006091	入驻成功	2019-01-25 17:36	查看申请
////103523970	河南乐之途电子科技有限公司	1523387771	入驻成功	2019-01-12 16:56	查看申请
////103127995	河南乐之生商贸有限公司	1521225291	入驻成功	2018-12-19 15:44	查看申请
////2019-1-14号测试媒体上传返回的 media_id W56fsKod1rgFm3W7Xin0HIWYTvWcHWNN2l62P8myGhDeN7dwQ3Xzen1K3EdCEuzRIOHUpEky61GQ8lZQ3JjxDrZ_k23PUCJc9pQathUIR8I
//
//
//
//
//
//	//公众号绑定的商户号
//	public final static String GZHMCHID = "1414532102";
//	//公众号appid
//	public final static String GZHAPPID = "wx768c398f4aacabe3";
//	public final static String GZHAPPSECRET = "673de4d8efe4ed53087d5cba28f151dc";
//
//	//32位随机数字，公众号及app公用
//	public final static String GZHKEY = "BYbjmxM85NwGvQY2wTibAbGXnvOOFU23";
//	//求职商户号
//	public final static String qz_mchId = "1490602002";
//
//
//
//
//
//	/**
//	 * 微信财富通三种支付场景
//	 * 	1=TRADETYPE_JSAPI 微信网页支付
//	 *  2=TRADETYPE_NATIVE 扫码支付
//	 *  3=TRADETYPE_APP app支付
//	 */
//	public final static String TRADETYPE_JSAPI = "JSAPI";
//	public final static String TRADETYPE_NATIVE = "NATIVE";
//	public final static String TRADETYPE_APP = "APP";
//
//	/**
//	 * 微信证书在服务器端存放位置
//	 */
//
//	/**
//	 * 公众号商户证书
//	 */
//	public final static String GZHCERTPATH = "/WEB-INF/weixin/gzh_cert/apiclient_cert.p12";
//	/**
//	 * app移动应用商户证书
//	 */
//	public final static String APPCERTPATH = "/WEB-INF/weixin/app_cert/apiclient_cert.p12";
//
//	//服务商微信公钥
//	public final static String APPPUBLICCERTPATH = "/WEB-INF/weixin/app_cert/public_cert.pem";
//
//
//	/**
//	 * 微信收款异步通知接口
//	 */
//	//正式环境1111111111111
//	public static String WXPAY_RECEIVE_NOTIFY = "http://c3t22e.natappfree.cc/jsPayNotify";
//	//动态设置微信支付回调地址
//	public static void setWxpayNotify(String path){
//		WXPAY_RECEIVE_NOTIFY = path;
//	}
//
//
//	/**
//	 * 公众号的全局唯一票据
//	 */
//	public final static String WXPAY_ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
//	/**
//	 * 获取jsapi_ticket
//	 */
//	public final static String WXPAY_JSAPI_TICKET = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi";
//
//
//
//
//
//	//微信支付渠道域名
//	private static final String DOMAIN_API = "https://api.mch.weixin.qq.com";
//	private static final String DOMAIN_API2 = "https://api2.mch.weixin.qq.com";
//
//	//微信财付通预下单接口
//	private static final String UNIFIEDORDER_URL_SUFFIX = "/pay/unifiedorder";
//	//提交付款码支付
//	private static final String MICROPAY_URL_SUFFIX     = "/pay/micropay";
//	//查询订单
//	private static final String ORDERQUERY_URL_SUFFIX   = "/pay/orderquery";
//	private static final String REVERSE_URL_SUFFIX      = "/secapi/pay/reverse";
//	//微信财付通取消订单接口URL
//	private static final String CLOSEORDER_URL_SUFFIX   = "/pay/closeorder";
//	//微信财付通退款接口
//	private static final String REFUND_URL_SUFFIX       = "/secapi/pay/refund";
//	//微信财付通退款查询接口
//	private static final String REFUNDQUERY_URL_SUFFIX  = "/pay/refundquery";
//	private static final String DOWNLOADBILL_URL_SUFFIX = "/pay/downloadbill";
//	private static final String REPORT_URL_SUFFIX       = "/payitil/report";
//	private static final String SHORTURL_URL_SUFFIX     = "/tools/shorturl";
//	private static final String AUTHCODETOOPENID_URL_SUFFIX = "/tools/authcodetoopenid";
//	private static final String PROFITSHARING_URL_SUFFIX = "/secapi/pay/profitsharing";
//	//企业付款到领取(提现)
//	private static final String TRANSFER_URL_SUFFIX = "/mmpaymkttransfers/promotion/transfers";
//    //申请入驻
//	private static final String MICRO_URL_SUFFIX = "/applyment/micro/submit";
//	//查询申请入驻
//	private static final String MICRO_QUERY_URL_SUFFIX = "/applyment/micro/getstate";
//	//图片上传API
//	private static final String MICRO_UPLOAD_URL_SUFFIX = "/secapi/mch/uploadmedia";
//
//	//支付押金（人脸支付）
//	public static final String DEPOSIT_FACEPAY_URL_SUFFIX = "/deposit/facepay";
//	//支付押金（付款码支付）
//	public static final String DEPOSIT_MICROPAY_URL_SUFFIX = "/deposit/micropay";
//
//	//发普通红包接口
//	public static final String TRANSFERS_SENDREDPACK_URL_SUFFIX = "/mmpaymkttransfers/sendredpack";
//	//发裂变(群红包)红包接口
//	public static final String TRANSFERS_SENDGROUPREDPACK_URL_SUFFIX = "/mmpaymkttransfers/sendgroupredpack";
//
//	// sandbox
//	private static final String SANDBOX_SUFFIX = "/sandboxnew";
//
//	//返回请求连接
//	private static String getDomain(){
//		String currentDomain=DOMAIN_API;
//		if(USEDOMAIN2){
//			currentDomain=DOMAIN_API2;
//		}
//		if(USESANDBOX){
//			return currentDomain+SANDBOX_SUFFIX;
//		}
//		return currentDomain;
//	}
//
//	public static final String getUnifiedOrderUrl(){
//		return getDomain()+UNIFIEDORDER_URL_SUFFIX;
//	}
//
//	//订单查询链接
//	public static final String getOrderQueryUrl(){
//		return getDomain() + ORDERQUERY_URL_SUFFIX;
//	}
//
//	//微信财付通退款查询接口
//	public static final String getPayRefundQueryUrl(){
//		return getDomain()+REFUNDQUERY_URL_SUFFIX;
//	}
//	//微信财付通退款接口
//	public static final String getPayRefundUrl(){
//		return getDomain()+REFUND_URL_SUFFIX;
//	}
//	//微信财付通退款接口
//	public static final String getPayCloseOrderUrl(){
//		return getDomain()+CLOSEORDER_URL_SUFFIX;
//	}
//
//	public static final String getMicroSubmitUrl(){
//		return getDomain()+MICRO_URL_SUFFIX;
//	}
//	public static final String getMicroSubmitQueryUrl(){
//		return getDomain()+MICRO_QUERY_URL_SUFFIX;
//	}
//	public static final String getMicroUploadUrl(){
//		return getDomain()+MICRO_UPLOAD_URL_SUFFIX;
//	}
//
//	public static final String getRePackPayUrl(){
//		return getDomain()+TRANSFERS_SENDREDPACK_URL_SUFFIX;
//	}
//
//	//企业付款接口
//	public static String getTransPayUrl() {
//		return getDomain()+TRANSFER_URL_SUFFIX;
//	}
//
//	//获取证书路径
//	public static final String getCertPath(String syspath,String mchid){
//		String path=syspath;
//		if(mchid.equals(WxpayConfig.GZHMCHID)){
//			path += WxpayConfig.GZHCERTPATH;
//		}else if(mchid.equals(WxpayConfig.APPMCHID)){
//			path += WxpayConfig.APPCERTPATH;
//		}
//		return path;
//	}
//
//	public static final String getPublicCertPath(String syspath,String mchid){
//		String path=syspath;
//		if(mchid.equals(WxpayConfig.APPMCHID)){
//			path += WxpayConfig.APPPUBLICCERTPATH;
//		}
//		return path;
//	}
//
//
//
//}
