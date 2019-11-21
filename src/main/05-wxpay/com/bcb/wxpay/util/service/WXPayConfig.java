package com.bcb.wxpay.util.service;

import com.bcb.wxpay.util.company.WxpayConfig;
import com.bcb.wxpay.util.sdk.IWXPayDomain;

import java.io.InputStream;

public abstract class WXPayConfig {

    //菠菜包
    String mchId = "1513549201";
    String appId = "wxa574b9142c67f42e";
    String key = "Bcb19e0814494a248d28c591d4bc31e1";

    //沙箱key
    String sandBoxKey = "5ffeda033fb8fab92ae24d321257b01f";

    //提交地址
    String url;

    //自动回复
    boolean autoReport = true;

    String signType= SignType.MD5.name();
    byte[] certData;

    /**
     * 微信收款异步通知接口
     */
    //正式环境1111111111111
    public static String WXPAY_RECEIVE_NOTIFY = "http://c3t22e.natappfree.cc/jsPayNotify";

    //连接超时时间，默认10秒
    public final static int socketTimeout = 10000;

    //传输超时时间，默认30秒
    public final static int connectTimeout = 30000;
    //http读取时间超时
    public final static int httpReadTimeout = 20000;


    /**
     * 微信财富通三种支付场景
     * 	1=TRADETYPE_JSAPI 微信网页支付
     *  2=TRADETYPE_NATIVE 扫码支付
     *  3=TRADETYPE_APP app支付
     */
    public final static String TRADETYPE_JSAPI = "JSAPI";
    public final static String TRADETYPE_NATIVE = "NATIVE";
    public final static String TRADETYPE_APP = "APP";

    /**
     * 微信证书在服务器端存放位置
     */

    /**
     * 公众号商户证书
     */
    public final static String GZHCERTPATH = "/WEB-INF/weixin/gzh_cert/apiclient_cert.p12";
    /**
     * app移动应用商户证书
     */
    public final static String APPCERTPATH = "/WEB-INF/weixin/app_cert/apiclient_cert.p12";

    //服务商微信公钥
    public final static String APPPUBLICCERTPATH = "/WEB-INF/weixin/app_cert/public_cert.pem";

    /**
     * 公众号的全局唯一票据
     */
    public final static String WXPAY_ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
    /**
     * 获取jsapi_ticket
     */
    public final static String WXPAY_JSAPI_TICKET = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi";


    //获取证书路径
    public final String getCertPath(String syspath,String mchid){
        String path=syspath;
        if(mchid.equals(WxpayConfig.GZHMCHID)){
            path += WxpayConfig.GZHCERTPATH;
        }else if(mchid.equals(WxpayConfig.APPMCHID)){
            path += WxpayConfig.APPCERTPATH;
        }
        return path;
    }

    public final String getPublicCertPath(String syspath,String mchid){
        String path=syspath;
        if(mchid.equals(WxpayConfig.APPMCHID)){
            path += WxpayConfig.APPPUBLICCERTPATH;
        }
        return path;
    }




    /**
     * 获取 App ID
     *
     * @return App ID
     */
    public abstract String getAppId();

    /**
     *
     * @return
     */
    public abstract boolean getAutoReport();

    /**
     * 提交地址
     * @return
     */
    public abstract String getUrl();

    public abstract void setUrl(String url);
    /**
     * 获取 Mch ID
     *
     * @return Mch ID
     */
    public abstract String getMchId();


    /**
     * 获取 API 密钥
     *
     * @return API密钥
     */
    public abstract String getKey();

    /**
     * 获取签名方式
     * @return
     */
    public abstract String getSignType();

    //设置使用沙箱模式(测期间可用)
    public abstract void setUseSandBox(boolean useSandBox);

    /**
     * 获取商户证书内容
     *
     * @return 商户证书内容
     */
    public abstract InputStream getCertStream();

    /**
     * 获取WXPayDomain, 用于多域名容灾自动切换
     * @return
     */
    public abstract IWXPayDomain getWXPayDomain();

    /**
     * 是否自动上报。
     * 若要关闭自动上报，子类中实现该函数返回 false 即可。
     *
     * @return
     */
    public boolean shouldAutoReport() {
        return true;
    }

    /**
     * 进行健康上报的线程的数量
     *
     * @return
     */
    public int getReportWorkerNum() {
        return 6;
    }


    /**
     * 健康上报缓存消息的最大数量。会有线程去独立上报
     * 粗略计算：加入一条消息200B，10000消息占用空间 2000 KB，约为2MB，可以接受
     *
     * @return
     */
    public int getReportQueueMaxSize() {
        return 10000;
    }

    /**
     * 批量上报，一次最多上报多个数据
     *
     * @return
     */
    public int getReportBatchSize() {
        return 10;
    }

}
