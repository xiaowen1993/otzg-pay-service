package com.bcb.wxpay.config;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class WxPayConfigImpl extends WXPayConfig{

    private String appId;
    private String mchId;
    private String key;
    private String signType;
    private byte[] certData;


    /**
     * 微信收款异步通知接口
     */
    public static String WXPAY_RECEIVE_NOTIFY = "http://c3t22e.natappfree.cc/jsPayNotify";

    //动态设置微信支付回调地址
    public static void setWxpayNotify(String path){
        WxPayConfigImpl.WXPAY_RECEIVE_NOTIFY = path;
    }

    public WxPayConfigImpl(String appId, String mchId,String key) {
        this.appId = appId;
        this.mchId = mchId;
        this.key = key;
    }

    public WxPayConfigImpl(String appId, String mchId, String key, String signType, String certRootPath) {
        try{
            this.appId = appId;
            this.mchId = mchId;
            this.key = key;
            this.signType = signType;
            String certPath = certRootPath+"/WEB-INF/weixin/app_cert/apiclient_cert.p12";
            System.out.println("文件路径="+certPath);

            File file = new File(certPath);
            if(file.exists() && file.isFile()){
                InputStream certStream = new FileInputStream(file);
                this.certData = new byte[(int) file.length()];
                certStream.read(this.certData);
                certStream.close();
            }else{
                System.out.println("文件路径错误="+certPath);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public String getAppID() {
        return this.appId;
    }
    @Override
    public String getMchID() {
        return this.mchId;
    }

    public String getKey() {
        return this.key;
    }

    @Override
    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public InputStream getCertStream() {
        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }

    @Override
    public int getHttpConnectTimeoutMs() {
        return super.getHttpConnectTimeoutMs();
    }

    @Override
    public int getHttpReadTimeoutMs() {
        return super.getHttpReadTimeoutMs();
    }

    // 这个方法需要这样实现, 否则无法正常初始化WXPay
    @Override
    public IWXPayDomain getWXPayDomain() {
        IWXPayDomain iwxPayDomain = new IWXPayDomain() {
            @Override
            public void report(String domain, long elapsedTimeMillis, Exception ex) {

            }
            @Override
            public DomainInfo getDomain(WXPayConfig config) {
                return new IWXPayDomain.DomainInfo(WXPayConstants.DOMAIN_API, true);
            }
        };
        return iwxPayDomain;
    }

    @Override
    public boolean shouldAutoReport() {
        return super.shouldAutoReport();
    }

    @Override
    public int getReportWorkerNum() {
        return super.getReportWorkerNum();
    }

    @Override
    public int getReportQueueMaxSize() {
        return super.getReportQueueMaxSize();
    }

    @Override
    public int getReportBatchSize() {
        return super.getReportBatchSize();
    }
}
