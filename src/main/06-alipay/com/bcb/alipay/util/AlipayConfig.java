package com.bcb.alipay.util;

import com.bcb.log.util.LogUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AlipayConfig {

    //支付宝商户号，在此仅供支付宝向平台返利
    public final static String pid = "2088821411693203";

    // 签名方式
    public final static String sign_type = "RSA2";
    //编码
    public final static String charset = "UTF-8";

    //正式环境
    public final static String alipay_gateway = "https://openapi.alipay.com/gateway.do";
    public final static String auth_url = "https://openauth.alipay.com/oauth2/appToAppAuth.htm?app_id=%s&redirect_uri=%s";

    //第三方应用
    static String app_id = "2019011162891191";
    //第三方应用
    static String app_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCL3NN95V5mzd1DkIYoEJyMVhNnM70wQk3F3bkzRshSyNjZYabSeuz9rXcgmO11CQ1IYkkjBv4yUvlftMEt/4KfI39Y4eG20Ky2GzdWvvWUNA/pf+EaAxc7VH+4PAmD9V5JsmkgzYyYvfgzSFOw+FybU2MmogeIh1yL0lCPwCar5C1OgWiGXx4goSBpMCxvsNv0M86le/eg3Q9rsIaJiHjloxBOYoBk80PFcKvQYrQ0B0r9aWwmg0emViMK97+p/mtkZ7S0OOyNQ9k2lAE+ZQSx47c2tND8pPnY8RmFf9vFesLWWD2vkfCZEAv782RufBtqQTiC85v9mmRNajYy65yPAgMBAAECggEAe9tZ+1KFL/s859lzxMU5LVoIossBzlvZLdF1ccBMJGuzBYbhyeGMP/Y+2cIz/wG+HozTgc5ui7hJGIUk0gxE65Lu8pJOZawUVsxxTEOdjNoyATD68iMsjqD83fVk49QF5LO3P5Jn4NfSle+GFeFLeU32Lz8r9q8QuM3OBA9A79S3lBv5VC2DiUyvIWHrf2BLtOUsBse+bib/pDOAVg1cYc7HM3Nlwfgah86sBcpxumyr0U57MCWjNMRd9gyqclxjRjL1T0F8jcoHOsJixkY1B1WOZ/lv0MoElXREGVzjYEj0pm8C6cc2HLluvs3O8d8ePtS06N+M0LCLa53YWj/kQQKBgQDRu6LNRRts2IsRdATK1Tw5vh0eUqxuMNRuB0YT7FtdHBBUvpE9AksWnSmDE5F2hPQTKM6HPk5CWra5WLW92Ex5H0hfveupI+XQ8VRr22dLylYHBhmGq8jeHDDIVxfwuV1j13QiyEi4GlfFazf/4/1vai39svrjrVCrVI+7wKpHYQKBgQCqt10SXAot049zeq0k28LUw8GvZJnnIJXuwoukcl+XHPvs99kFbnqeGvJ0sm6NRHE6bJsoGeSljITXo10Ve6+nfhSRHRso7CVXoj8qo1jtQGobI/qJmWPoU0SK560KEsl9/6xbjBsqh7Ye+u2kyvBeZw73KJzRj56F7FD2NYqZ7wKBgQDHTa2JrzA+oRCWh0++iB/xJ054cEvXcqOL43GeoS65Ll/+iBFwjmtYlATMwJ2sqO9f/Zk1P+oSeC3HuBsMyyzwtN+Ly+jUFH7hrVNyI07n4OEbT5qWNUxudQ+OceUYJq4uoKGGJBmmibH6ssbGbpt5csc9nQV5sktEZNkprA6kQQKBgGywU2xF9yEYCcPO/f9yfwexHlZJqYayg2LAr+FiBCQUivxjC+PeY+jXZTgRBjugsKouzVXprl4MKeOUmcX8umfb6MI/ErSqLFgv7yF5YDulACkJbhA+/ZHDuebp+4xnS6uRpS2f9QfN4ZC116lMn16rJKcNT1JIqve+7gjjK7w3AoGAS+chI5ypo2xGKir5+Pidazlvq1Y8WfXgZS2OzGHiLZ80euNyHGQvnfumDwRKifrBrngnalYlQYXra3OBPeHU/jHaG54otnj3oFxWvrer29kjRo73uuLlXgq92VauYoxmDcmDGcREXbT5THFj9r1gLGr6wUb+j6OMQKvSdkw4z3U=";
    //第三方应用(交给支付宝)
//    static String app_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAi9zTfeVeZs3dQ5CGKBCcjFYTZzO9MEJNxd25M0bIUsjY2WGm0nrs/a13IJjtdQkNSGJJIwb+MlL5X7TBLf+CnyN/WOHhttCsths3Vr71lDQP6X/hGgMXO1R/uDwJg/VeSbJpIM2MmL34M0hTsPhcm1NjJqIHiIdci9JQj8Amq+QtToFohl8eIKEgaTAsb7Db9DPOpXv3oN0Pa7CGiYh45aMQTmKAZPNDxXCr0GK0NAdK/WlsJoNHplYjCve/qf5rZGe0tDjsjUPZNpQBPmUEseO3NrTQ/KT52PEZhX/bxXrC1lg9r5HwmRAL+/NkbnwbakE4gvOb/ZpkTWo2MuucjwIDAQAB";

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


    //    李唐
//    public final static String pid = "2088431307185963";
    //正式环境(菠菜包)
//    public final static String app_id = "2017110709787421";
    //商户的私钥,需要PKCS8格式，RSA2公私钥生成：https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.nBDxfy&treeId=58&articleId=103242&docType=1
//    public final static String app_private_key = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCIF69L6pT91N5ll4avF44i+OsVN/LiIgi3mS9JCegQ0vBmrFZv5Gk8FknZN9CR9xGt7T96BL3uHXFSL5T0cEOeysz5zqztwDomxTXPFSXadOzXUekG4HnqTAbSUki4LELkooNqGHXeJ1089zMgQhJLrJhQdfFRVfybPALftQDxWXDjzuZQB1+kH9p5SpmdeiM5uZbvv1kxQzoOl73zV9yLfDWIKT4/KVS5L2cqAZECXUuVl5dWNAP74KySpEVxdbZPoswRQNfWezIJ2yjmQlcq0Ax/DuE51rAfD5f65+5sEW8RayMla7tgEeWlRiwIQb2+YoRr5AdG/x2WQbd9EFIBAgMBAAECggEAGBlBlErcyTnpi8nSMq0UIUM8tYwruTlXm9NHWUKk0l3X7gZ0Y+npbJdxykIk78P1YHwTcnLmgwS5rVj5onNCthqpQ08CtjME2Rqw1ZOkGVP9IH/DqNEVJZUC3Dlv4RUNX1kbtizQql8EFqibaAnrHXVZn13TNpjoW0C8LYrquNHeTn3p6iLw/NfGGcA9qPatdShPiLKa3pgxxNIdnU9oyYI/uzZK4GsSqBSG4DZkHRFfhpxxKfdAWS6+zPWaZPXMw9zKsrJCPs28S96kPoqlhSlYYHSMhHHEHpLmBXi6Wnulx73p0RdzAP5RoVg/Xr715tEpWop3yYUOg+Oqqrrs5QKBgQDy82cOr5Gaq3J/GfEtnxpS9EiPopIrmc5n5UqCjs0faZU0QyQcfCNWfruau86SPWmg8/gqhxrfrUmOWQ+ZNnC/ayacldDiku5ZggD+k09Dp/7ZEUs4FeXJXZZRdoMTyLu4NIXQQlI8C8lEFsOz1V96Ar8SLEZT9LGTGdeDK02pAwKBgQCPZvhpN9XQRUXa7kPWsNPs3SvsTO5GKVWIWZ1MWNmTwC2cqxawcuHsmXVX+/3TICgtT9lbro0lRJ4LjA1aLdLUT4ymyn/Xl690dKPJR8oy3Jomko+lJdT2MJvfpU/sac3BjZlLaKnrJWZ2uYGX65/floLRC2e8Xbuu3Py2BS3PqwKBgENRHBoenZqcrMH4/zGj5xhbJYvfAN9h46Y4Czg3tzBgAf6UJ/pYjzYVMYhDR46Pw6fcUcP+4XxeuIXfuYm7Yuw3FWDHxjQxgCd+9SWUzZ6yetPMjeoBb0UnUFJMIy+lLBZzPyygY8bNgTwDjsBSe/0Dq8uuRuJWZ6mZvHRU2FTfAoGAflvpgC1nBkJEL4nL1R2zX//zeCDBxKkfaSot5NxVvE+W4XoQYCKa2PTP5VtyadroDCVUDeldDf3Mlbgu+8ts/w+pjD7bL4nFXQR6Xh4YGYExg9OZJ5iScuyaLRNpvZPtbiPrc/sh3sXx56PGgatqEIZ7duBp5B1kB81KEMm3eEkCgYB65uuqmrs7BAuq0dwGgp+v2XAtLl6SHAy9lCR+sIRNtA5VqnFApQ5IgBmhzI5voBznJPM+3Kmpz7ocQkJ3UbPsjMV1alPf85AmDB75X1/jaZ8irZXFEDKujhknoMlf9Usdlq98+cN7u1a6dTN2tXjn1/D+yBq/f0n6qCllKZ/YaQ==";
    //app 2088821411693203 公钥
//    public final static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiBevS+qU/dTeZZeGrxeOIvjrFTfy4iIIt5kvSQnoENLwZqxWb+RpPBZJ2TfQkfcRre0/egS97h1xUi+U9HBDnsrM+c6s7cA6JsU1zxUl2nTs11HpBuB56kwG0lJIuCxC5KKDahh13iddPPczIEISS6yYUHXxUVX8mzwC37UA8Vlw487mUAdfpB/aeUqZnXojObmW779ZMUM6Dpe981fci3w1iCk+PylUuS9nKgGRAl1LlZeXVjQD++CskqRFcXW2T6LMEUDX1nsyCdso5kJXKtAMfw7hOdawHw+X+ufubBFvEWsjJWu7YBHlpUYsCEG9vmKEa+QHRv8dlkG3fRBSAQIDAQAB";


    //沙箱应环境
//    public final static String app_id = "2016082700320624";
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


}
