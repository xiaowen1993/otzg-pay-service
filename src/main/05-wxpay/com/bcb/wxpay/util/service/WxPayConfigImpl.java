package com.bcb.wxpay.util.service;

import com.bcb.wxpay.util.sdk.IWXPayDomain;
import com.bcb.wxpay.util.sdk.WXPayConstants;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class WxPayConfigImpl extends WXPayConfig{

    public WxPayConfigImpl(){
    }
    public WxPayConfigImpl(String signType){
        this.signType=signType;
    }

    public WxPayConfigImpl(String signType,String certRootPath) {
        try{
            if(signType!=null){
                this.signType=signType;
            }
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
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getUrl() {
        return this.url;
    }

    @Override
    public String getAppId() {
        return this.appId;
    }
    @Override
    public String getMchId() {
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



    @Override
    public void setUseSandBox(boolean useSandBox) {
        this.key=this.sandBoxKey;
    }

    @Override
    public boolean getAutoReport() {
        return this.autoReport;
    }

}
