package com.bcb.alipay.util;

public class AlipayConfig {

    //象过河的支付宝商户号，在此仅供支付宝向平台返利
    public final static String pid = "2088121753347357";

    // 签名方式
    public final static String sign_type = "RSA2";
    //编码
    public final static String charset = "UTF-8";


    //支付成功异步回调接口
    public static String notify_url = "/alipay/notify";
    public static String auth_notify_url = "/keyStore/openAuthTokenApp/save";


    //正式环境
    public final static String alipay_gateway = "https://openapi.alipay.com/gateway.do";
    public final static String auth_url = "https://openauth.alipay.com/oauth2/appToAppAuth.htm?app_id=%s&redirect_uri=%s";

    //正式环境(真货)
    public final static String app_id = "2016091201889599";

    //商户的私钥,需要PKCS8格式，RSA公私钥生成：https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.nBDxfy&treeId=58&articleId=103242&docType=1
    public final static String app_private_key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCRnO+aYHKF95qg" +
            "o0l+9zl11EnLs4CrcnHg66L8tdRkNJiagsJ1kr9I/nVYUhvBt2CgLJApABIWjWrp" +
            "FgV7dh5omX2d8PfkQnMdZpDT/XCZRSr8jbJtLmijB2A5R5T0oIw2wTm2JZeMCdDe" +
            "SARyNGtgLvYWP2uywE8zSdKlpgKmKbi5y7xku0Td2JaG/L4LYm3HMVuhojvKhSvh" +
            "5XUQyEoiHohTdiZnEYAPzKrDBBZhDZMINwWOAKy1c0uiOGupHMu7i4PNVcTlVR/E" +
            "/JDqbjMpJPytsvAEHq+636x/0s9AAbQbnQm36KsctJSDW5j41NNUD4aI/Nv0qDJq" +
            "Hx6as5gpAgMBAAECggEBAIl1d+7o+1OiQVNBsgDsi07DP4LPijjPCdfFOhCL6dtK" +
            "l4DMzZvGE1Np+waMRG2jmdC7IL/DtE+b4n/07On2wJAHzcDKmIjffeIUT8X/a0sV" +
            "M5ZEgVp2RAsazGhmPaSM5rBNyhg79osZPRaJL8FL4M1kp6Sq7BVLW7jUCMmdJMUM" +
            "EzJTOvfQBNKDiSwXvllIp8gHlcm/E7pHdiahOxNyPNJ4xz+5J2FV20c20yZv5rk/" +
            "TCP2dzfCflbErYDT/Wf+kQtBYlIgb9ozCdHgbTmtiJVWO9pTC1bnRdusD3zz6NBW" +
            "H/X5D2jKRqstiJsO5oOJtKFe04Sed7HXUZH6ItAU650CgYEAwVdm2gfFIQFAe/Qs" +
            "DVjBgMtgQDAQYFHqMqZ94vqlk7ZyQmpZOVgnbwD6DwkrpX804vm4/I1hOE9O56wD" +
            "iWpTA/QdqDU10GQls7HTsu1LKs2JdnIBwvadg9ea28+AMjpiPHURDuDjg5fCvaHx" +
            "UGaX1LypXDNup+3fqVmKRjYAAu8CgYEAwM29+OGm4DV1Wc5xW/hHIxAz6bZ+1mej" +
            "n8hRySjDXrK49Pmka+EGIJyshYAK9cMjkNKV1v2g0uyettZ7u2k7GMu+QlUPAI5D" +
            "//n5rZIaQUvG0YliyfEYZXzY/jfWa0zCCdNwWoRrpgJY6VQ6RemzSME8T5YA42Gr" +
            "Fp4FP5EsNmcCgYBorQNYZACMX/sjoQ0ApN8O9g1Ec0FKhM8BYTai3wUqNgsifiWU" +
            "cG/ZH3RDE7n3vilKAd3vjjPmormboHvBuDj92Pr9iOF709y9rzdoliSuJd2YrRzb" +
            "C144dVC4VV2Y1Bc/mDoGDiffpRigRr85wnHNkd83tfjhHl6Ld5jvbmkPLQKBgGJX" +
            "jxKnZoruOZE5L7ENjptf5FrNbxzFeDxD3ROR6zUeTCEIRMmR2aJAx+7ARNbeVgHE" +
            "qBElBScQ8lOoSyxonHAlXEJRSHmsFxs10hiqcSHlGOBAB1eh1iPN7pCcwo2wdTi1" +
            "1JUW/iGLCPbas259qajuh2jAxms0oiPDLkIiNj75AoGBALy8sei9ieTPaajj0g5e" +
            "43cPZck/FXwYP5HHEAUkiC5vKBV+mkjlPvUXUl251UID/9HGRQ0r59+Y20IjQr4v" +
            "/g/W+zIQZ4mNUDPiyqwUNVjotebRmKqHtKE2/oRY0l+0xA/erHZm7ZFxLzu03XgD" +
            "0h/5bsZkRXWEY/9ZgoUSM/VH";

    public final static String app_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkZzvmmByhfeaoKNJfvc5ddRJy7OAq3Jx4Oui/LXUZDSYmoLCdZK/SP51WFIbwbdgoCyQKQASFo1q6RYFe3YeaJl9nfD35EJzHWaQ0/1wmUUq/I2ybS5oowdgOUeU9KCMNsE5tiWXjAnQ3kgEcjRrYC72Fj9rssBPM0nSpaYCpim4ucu8ZLtE3diWhvy+C2JtxzFboaI7yoUr4eV1EMhKIh6IU3YmZxGAD8yqwwQWYQ2TCDcFjgCstXNLojhrqRzLu4uDzVXE5VUfxPyQ6m4zKST8rbLwBB6vut+sf9LPQAG0G50Jt+irHLSUg1uY+NTTVA+GiPzb9Kgyah8emrOYKQIDAQAB";

    //支付宝的公钥，查看地址：https://openhome.alipay.com/platform/keyManage.htm?keyType=partner
    public final static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAncSHXy+sSstdffHeI5GtMSOsujlIalW+JFfmvPTpc0MS/RG43KBO+aVERdOIhILSTb1qcgnkvLkd9JOzLTAOtYLV/KqD/NNiRWNVInfi0KrZDwFXmLRQwBCmKMC3bR/Xasb9ZOmwmvKGPQ3gjRgOf7qQifdTRnSettey/Rvx3wZfa3YjoiQr6p2BNT54x6DkmaZv7ky5pCqLuIAsy37wN2exVLGIkY39uhdKUp77nzgmmptcr0WBHJC2pmpAnGImC6qVA9lI2YChpo7ciBcHeJwZxpyXXMIjQNGG6Qu+5FaAoFlulE8hcd18v+v55MJf0zKdyhyKnC1vl61aLJ9clwIDAQAB";








    //沙箱应环境
//    public final static String app_id = "2016091200492926";
//    public final static String alipay_gateway = "https://openapi.alipaydev.com/gateway.do";
//    public final static String auth_url = "https://openauth.alipaydev.com/oauth2/appToAppAuth.htm?app_id=%s&redirect_uri=%s";
//    public final static String app_private_key ="MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDJBJQqSXpqwKi2" +
//            "KYOjO5cOTJMS2f48JjcrGVsWCycJjcpferXRKZ4EtPZsQFIFCj0wjFkIWWyL70JM" +
//            "uZQucW+3oC929hAbClg3REd/tCb3h/sHmGiIgA67rWVXIAEV+WGVETL9baDt1b2c" +
//            "SzNitt2TZ+e8C6xAweGuY7lKx1BJMRd1Rys83yw0aLML9lgwdsqCf0+MZF2NgeCd" +
//            "/YF+6ZziA6Wxz1nAEEkA5OxgqsGwYb7pskrTthsRtpHCGh3Ylv/woimhVryMJff3" +
//            "bm3RIpOeI/mfz5QS8Exm5PUAsWNRPGQGR+/KBDMI42VAUwbD7DkifZpoBS3EZ31y" +
//            "rO5KPg33AgMBAAECggEBAMFFILvDp+qpz8xM/97atXKvFx0h8Pmd+J78owH/uQnL" +
//            "hQ7l6Zfv/pkh8GfGM9XasVhTKGsJp36njMQ+DYXV1QblAZ3MGVIWURjaEjBrYqfN" +
//            "2wLDe4X6MbAql+v7vm+FNVhgj/jwBbGGNublusMndMR8O2cmhfhBMR49jXKjTcL/" +
//            "tFT6LWM7R8uKDNBiu8kq5pPGG+0ojppAT498yGmkfnXG3jd72AJUkVOtctSuPUeV" +
//            "r09Gxyyfx3gkd64fqq3bclopD0SNO1+VIh97+pA/2MeFSaGf4D6FcMnzMMq49y2o" +
//            "dhn59iyruOoeuvtlAruS7FiTPPQ+QcPHvCIU/cEgQIkCgYEA/Y5Cmnp/6bM7BHIe" +
//            "365Y5DhoYSoGMiFwhhXxl0/SYDtZJIxefOM9wbjbITPdUnDQxXV3xRIef6mDVkig" +
//            "aW18FDIkHTSvLsT8gw5o8OFSxMfJ3hXTLV6rdQynXPDhiHi/SLzXTmftW13QG2/p" +
//            "olbMBbnUG+6mVO+WsWh4f6dDBpMCgYEAyvSpo49m22mHlPRzoqGwoMS1rnvU1yUB" +
//            "ibm8rsvpFly+C+aqpEJaYImnMiuK8GzZqHVi2hX0whbPhz41zu8c0ke6OYfxMz9s" +
//            "HKTOkTBVz4OpceoEEGrMgBYeuNIBfHkKBWgx6aEJ1L9tYVAPbwLyRG8WR/EEwkIi" +
//            "i20VlYmNNY0CgYBO6u5UAni5lm1yDssJN8y3C8+BHoFbLQlG0qGlRNTn2tD+DSqD" +
//            "mH7qQs0BYpEqTa7NJqimj+MqPOqB+ozjA46xAoI3DOTRm8I0UFHdV2RZlw1/sC9Q" +
//            "Wbzn645T4S1xOpPe4dfpsXMxEtvpDkYbnwRVa+dW4kqm+QNgKHII2ViFPQKBgQCO" +
//            "woitD0rnnZSMN2M7i4aln5i4kkxFxbcOPMM3JSHg4/2ee3OetTnDXDBqfuxtb/Ou" +
//            "pluRUg5nlAoIdL7+v31DgMbCfxgv1zXh9B+FSz2hCVjgUx7muWj91BFqKfZ3quBc" +
//            "q00orGkw6DI1WE5y5NyrPISsuCGDz2djMakUFbxpZQKBgQDVDzIaQyPGd9TPMbwP" +
//            "18ygHER1VQvbWBW1MpzUwTQRaAg5VJMuxx2biNfZD9Z3XT19G8TNNMJpNE9G4sfC" +
//            "DvfF1YmOzlGLdxmvyF9d72lkx16Z8G05JlSfPxzH5wBiw1eTLrflgFW7zZZciR8/" +
//            "Qaknt7rE8Y3iKpknxq18cnuplg==";
//
//    public final static String alipay_public_key  = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA1R5tfZqZMjmYZpno/zGCnMSH678S24zunycZ6Jr312t1fNQQHvRxJVAg0yciBJSY5KVN/IR58qpPhgXGSjsIXOYyuasv/FKcq8tLwb0rQgfTgRIh2CrFyPK4t66Q8JN53mdX/M1r376hpl8D74a9X15qVkf9qk/HJaYWz9JgV2HZoo1kVsMXLEYdMI+EYZSIEdZQwEoUdoDqRM+kb5ggg5sKN9mGFVWx1tTHWwuMhMETck9QcvaKKTPVlXyeuDE184fET1Rw3Scyrr3kiQQ+47CDnKzZvoDw3SN0dQCqE7WHpsTp9cn7W5JDmT2cXlb67o3QqdN3AeSE01XDC8ilBwIDAQAB";

    //动态设置支付宝回调地址
    public static void setAlipayNotify(String path){
        AlipayConfig.notify_url = path;
    }

}
