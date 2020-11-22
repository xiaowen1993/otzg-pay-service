package com.otzg.alipay.util;

import com.otzg.log.util.LogUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AlipayConfig {

    // 签名方式
    public final static String sign_type = "RSA2";
    //编码
    public final static String charset = "UTF-8";

    //正式环境
    public final static String alipay_gateway = "https://openapi.alipay.com/gateway.do";
    public final static String auth_url = "https://openauth.alipay.com/oauth2/appToAppAuth.htm?app_id=%s&redirect_uri=%s";


    //支付宝商户号，在此仅供支付宝向平台返利
    static String pid = "2088821411693203";
    //第三方应用
    static String app_id = "2019011162891191";
    //第三方应用
    static String app_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCL3NN95V5mzd1DkIYoEJyMVhNnM70wQk3F3bkzRshSyNjZYabSeuz9rXcgmO11CQ1IYkkjBv4yUvlftMEt/4KfI39Y4eG20Ky2GzdWvvWUNA/pf+EaAxc7VH+4PAmD9V5JsmkgzYyYvfgzSFOw+FybU2MmogeIh1yL0lCPwCar5C1OgWiGXx4goSBpMCxvsNv0M86le/eg3Q9rsIaJiHjloxBOYoBk80PFcKvQYrQ0B0r9aWwmg0emViMK97+p/mtkZ7S0OOyNQ9k2lAE+ZQSx47c2tND8pPnY8RmFf9vFesLWWD2vkfCZEAv782RufBtqQTiC85v9mmRNajYy65yPAgMBAAECggEAe9tZ+1KFL/s859lzxMU5LVoIossBzlvZLdF1ccBMJGuzBYbhyeGMP/Y+2cIz/wG+HozTgc5ui7hJGIUk0gxE65Lu8pJOZawUVsxxTEOdjNoyATD68iMsjqD83fVk49QF5LO3P5Jn4NfSle+GFeFLeU32Lz8r9q8QuM3OBA9A79S3lBv5VC2DiUyvIWHrf2BLtOUsBse+bib/pDOAVg1cYc7HM3Nlwfgah86sBcpxumyr0U57MCWjNMRd9gyqclxjRjL1T0F8jcoHOsJixkY1B1WOZ/lv0MoElXREGVzjYEj0pm8C6cc2HLluvs3O8d8ePtS06N+M0LCLa53YWj/kQQKBgQDRu6LNRRts2IsRdATK1Tw5vh0eUqxuMNRuB0YT7FtdHBBUvpE9AksWnSmDE5F2hPQTKM6HPk5CWra5WLW92Ex5H0hfveupI+XQ8VRr22dLylYHBhmGq8jeHDDIVxfwuV1j13QiyEi4GlfFazf/4/1vai39svrjrVCrVI+7wKpHYQKBgQCqt10SXAot049zeq0k28LUw8GvZJnnIJXuwoukcl+XHPvs99kFbnqeGvJ0sm6NRHE6bJsoGeSljITXo10Ve6+nfhSRHRso7CVXoj8qo1jtQGobI/qJmWPoU0SK560KEsl9/6xbjBsqh7Ye+u2kyvBeZw73KJzRj56F7FD2NYqZ7wKBgQDHTa2JrzA+oRCWh0++iB/xJ054cEvXcqOL43GeoS65Ll/+iBFwjmtYlATMwJ2sqO9f/Zk1P+oSeC3HuBsMyyzwtN+Ly+jUFH7hrVNyI07n4OEbT5qWNUxudQ+OceUYJq4uoKGGJBmmibH6ssbGbpt5csc9nQV5sktEZNkprA6kQQKBgGywU2xF9yEYCcPO/f9yfwexHlZJqYayg2LAr+FiBCQUivxjC+PeY+jXZTgRBjugsKouzVXprl4MKeOUmcX8umfb6MI/ErSqLFgv7yF5YDulACkJbhA+/ZHDuebp+4xnS6uRpS2f9QfN4ZC116lMn16rJKcNT1JIqve+7gjjK7w3AoGAS+chI5ypo2xGKir5+Pidazlvq1Y8WfXgZS2OzGHiLZ80euNyHGQvnfumDwRKifrBrngnalYlQYXra3OBPeHU/jHaG54otnj3oFxWvrer29kjRo73uuLlXgq92VauYoxmDcmDGcREXbT5THFj9r1gLGr6wUb+j6OMQKvSdkw4z3U=";

    //支付宝的公钥，查看地址：https://openhome.alipay.com/platform/keyManage.htm?keyType=partner
    static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA1EHvr+EMX5SM3NY9GtJrUNWGd6jeyeov6481Fi5ZFSxkuiY9MD8cYF1NZ/HrXZuXcrsFUTReZjm+Arl57tuU+It6dJga0R44yPirYJnJxxliAhjtY+pluQRUMaKwMMxap8i0aStdBj58frLWG3sr6QQD2V/DglQ6YaeFGMHLq9EMtQ87Hvms8P41t9x3PXBpsWQ44EYH6NI4eNgpgw47M6LQXNqG/5O0iE6E9iHvzMDE62M7Jm8fJULOMm9FEl8652jkquwB5W0291A84sXAhArkNCbckzbIRvMAw8Oohezkwis+l6uvQKqsUmagOD9flaM1IyK/oH6yhk2/2OprHwIDAQAB";


    //支付成功异步回调接口
    static String notifyUrl = "/pay/alipay/notify";
    static String authNotifyUrl = "/alipay/openAuthTokenApp/notify";

    @Value("${pay.alipay.notifyUrl}")
    public void setNotifyUrl(String value) {
        notifyUrl = value;
    }

    @Value("${pay.alipay.authNotifyUrl}")
    public void setAuthNotifyUrl(String value) {
        authNotifyUrl = value;
    }


    @Value("${pay.alipay.pid}")
    public void setPid(String value) {
        pid = value;
    }

    @Value("${pay.alipay.appId}")
    public void setAppId(String value) {
        app_id = value;
    }

    @Value("${pay.alipay.app-private-key}")
    public void setAppPrivateKey(String value) {
        app_private_key = value;
    }

    @Value("${pay.alipay.alipay-public-key}")
    public void setAlipayPublicKey(String value) {
        alipay_public_key = value;
    }


    public static String getNotifyUrl() {
        return LogUtil.getServUrl() + notifyUrl;
    }

    public static String getAuthNotifyUrl() {
        return LogUtil.getServUrl() + authNotifyUrl+"?uid=%s";
    }

}
