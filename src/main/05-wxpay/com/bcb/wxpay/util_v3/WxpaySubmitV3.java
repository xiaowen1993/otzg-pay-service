package com.bcb.wxpay.util_v3;

import com.bcb.util.CheckUtil;
import com.bcb.wxpay.config.WXPayUtil;
import com.bcb.wxpay.entity.MicroAccount;
import com.bcb.wxpay.util.*;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WxpaySubmitV3 {


    /**
     * 请求退款的数据
     * <p>
     * <xml>
     * <appid>wx2421b1c4370ec43b</appid>
     * <mch_id>10000100</mch_id>
     * <nonce_str>6cefdb308e1e2e8aabd48cf79e546a02</nonce_str>
     * <out_refund_no>1415701182</out_refund_no>
     * <out_trade_no>1415757673</out_trade_no>
     * <refund_fee>1</refund_fee>
     * <total_fee>1</total_fee>
     * <transaction_id>4006252001201705123297353072</transaction_id>
     * <sign>FE56DD4AA85C0EECA82C35595A69E153</sign>
     * </xml>
     *
     * @param transactionID 是微信系统为每一笔支付交易分配的订单号，通过这个订单号可以标识这笔交易，它由支付订单API支付成功时返回的数据里面获取到。建议优先使用
     * @param outTradeNo    商户系统内部的订单号,transaction_id 、out_trade_no 二选一，如果同时存在优先级：transaction_id>out_trade_no
     * @param outRefundNo   商户系统内部的退款单号，商户系统内部唯一，同一退款单号多次请求只退一笔
     * @param totalFee      订单总金额，单位为分
     * @param refundFee     退款总金额，单位为分
     *                      仅针对老资金流商户使用
     *                      REFUND_SOURCE_UNSETTLED_FUNDS---未结算资金退款（默认使用未结算资金退款）
     *                      REFUND_SOURCE_RECHARGE_FUNDS---可用余额退款
     * @author G/2016年10月19日 上午11:08:56
     */
    public final static String refundData(String sub_mch_id,
                                          String transactionID,
                                          String outTradeNo,
                                          String outRefundNo,
                                          Double totalFee,
                                          Double refundFee) {
        Map<String, String> paramMap = getMchAppHead(WxpayConfig.APPMCHID);
        //子商户号
        paramMap.put("sub_mch_id", sub_mch_id);
        //随机数
        paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
        //transaction_id是微信系统为每一笔支付交易分配的订单号，通过这个订单号可以标识这笔交易，它由支付订单API支付成功时返回的数据里面获取到。
        paramMap.put("transaction_id", transactionID);
        //根据API给的签名规则进行签名
        paramMap.put("out_trade_no", outTradeNo);
        //商户系统内部的退款单号
        paramMap.put("out_refund_no", outRefundNo);
        //金额必须为整数  单位为转换为分
        String tfee = "" + Math.round(totalFee * 100);
        //订单金额
        paramMap.put("total_fee", tfee);
        //退款金额
        String rfee = "" + Math.round(refundFee * 100);
        paramMap.put("refund_fee", rfee);
        //余额退款
        paramMap.put("refund_account", "REFUND_SOURCE_RECHARGE_FUNDS");
        //签名类型
        paramMap.put("sign_type", "HMAC-SHA256");
        //签名
        paramMap.put("sign", SHAUtil.wechatCertficatesSignBySHA256(paramMap, WxpayConfig.APPKEY));
        return XmlUtil.Map2Xml(paramMap);
    }

    //图片上传API,获取media_id
    public final static String doRefund(String data, String certPath, String mchid) {
        String url = WxpayConfig.getPayRefundUrl();
        //提交请求
        String resultXml = PostUtil.doPostWithCert(certPath, mchid, url, data);

        //正常操作返回的结果
        Map<String, String> resultMap = XmlUtil.parse(resultXml);
        System.out.println("result=>" + resultMap.toString());
        if (CheckUtil.isEmpty(resultMap)
                || !resultMap.get("return_code").equals("SUCCESS")
                || !resultMap.get("result_code").equals("SUCCESS")) {
            return "";
        }
        //{sign=9833D0D4FF18FC17D6ADE529AA593CD7, media_id=_qdPgxYeHjr20zZTSLO0ytytIW4WzQGYSjL9keglclA2AQyG_RoQhc87zbvCHC6B_zgcfMm7mi2zlOPEErgzFfy8z-koErfC1n0RAITyOCc, return_msg=OK, result_code=SUCCESS, return_code=SUCCESS}
        //返回applyment_id
        return resultMap.get("applyment_id");
    }


    //统一获取mch头部参数
    public final static Map<String, String> getMchHead(String mchid) {
        Map<String, String> paramMap = new HashMap<String, String>();
        if (mchid.equals(WxpayConfig.GZHMCHID)) {
            //商户号:开通微信支付后分配,必填
            paramMap.put("mch_id", WxpayConfig.GZHMCHID);
        } else if (mchid.equals(WxpayConfig.APPMCHID)) {
            //商户号:开通微信支付后分配,必填
            paramMap.put("mch_id", WxpayConfig.APPMCHID);
        } else {
            return null;
        }

        //如果使用沙箱环境
        if (WxpayConfig.USESANDBOX) {
            paramMap.put("key", WxpayConfig.SANDBOXKEY);
        }

        //随机数
        paramMap.put("nonce_str", WxpayUtil.getNonce("nonce", 16));
        return paramMap;
    }

    public final static Map<String, String> getMchAppHead(String mchid) {
        Map<String, String> paramMap = new HashMap<String, String>();
        if (mchid.equals(WxpayConfig.GZHMCHID)) {
            //商户号:开通微信支付后分配,必填
            paramMap.put("mch_id", WxpayConfig.GZHMCHID);
            //appid:每个公众号都有一个appid,必填
            paramMap.put("appid", WxpayConfig.GZHAPPID);
            //商户号对应key
            paramMap.put("key", WxpayConfig.GZHAPPID);
        } else if (mchid.equals(WxpayConfig.APPMCHID)) {
            //商户号:开通微信支付后分配,必填
            paramMap.put("mch_id", WxpayConfig.APPMCHID);
            //appid:每个公众号都有一个appid,必填
            paramMap.put("appid", WxpayConfig.APPAPPID);
            //商户号对应key
            paramMap.put("key", WxpayConfig.APPKEY);
        } else {
            return null;
        }

        //如果使用沙箱环境
        if (WxpayConfig.USESANDBOX) {
            paramMap.put("key", WxpayConfig.SANDBOXKEY);
        }

        //随机数
        paramMap.put("nonce_str", WxpayUtil.getNonce("nonce", 16));
        return paramMap;
    }

    //小微商户入驻数据
    private final static String getMicroSubmitData(String orderNo, MicroAccount microAccount, String certSn) throws Exception {
        Map<String, String> paramMap = getMchAppHead(WxpayConfig.APPMCHID);
        //接口版本号
        paramMap.put("version", "3.0");
        //平台证书序列号
        paramMap.put("cert_sn", certSn);


        //业务申请编号
        paramMap.put("business_code", orderNo);
        //身份证人像面照片
        paramMap.put("id_card_copy", microAccount.getId_card_copy());
        //身份证国徽面照片
        paramMap.put("id_card_national", microAccount.getId_card_national());
        //身份证姓名
        paramMap.put("id_card_name", RSAEncrypt.rsaEncrypt(microAccount.getId_card_name()));
        //身份证号码
        paramMap.put("id_card_number", RSAEncrypt.rsaEncrypt(microAccount.getId_card_number()));
        //身份证有效期限
        paramMap.put("id_card_valid_time", microAccount.getId_card_valid_time());
        //开户名称
        paramMap.put("account_name", RSAEncrypt.rsaEncrypt(microAccount.getAccount_name()));
        //开户银行
        paramMap.put("account_bank", microAccount.getAccount_bank());
        //开户银行省市编码
        paramMap.put("bank_address_code", microAccount.getBank_address_code());
        //银行账号
        paramMap.put("account_number", RSAEncrypt.rsaEncrypt(microAccount.getAccount_number()));
        //门店名称
        paramMap.put("store_name", microAccount.getStore_name());
        //门店省市编码
        paramMap.put("store_address_code", microAccount.getStore_address_code());
        //门店街道名称
        paramMap.put("store_street", microAccount.getStore_street());
        //门店门口照片
        paramMap.put("store_entrance_pic", microAccount.getStore_entrance_pic());
        //店内环境照片
        paramMap.put("indoor_pic", microAccount.getIndoor_pic());
        //商户简称
        paramMap.put("merchant_shortname", microAccount.getMerchant_shortname());
        //客服电话
        paramMap.put("service_phone", microAccount.getService_phone());
        //售卖商品/提供服务描述
        paramMap.put("product_desc", microAccount.getProduct_desc());
        //费率
        paramMap.put("rate", microAccount.getRate());
        //超级管理员姓名
        paramMap.put("contact", RSAEncrypt.rsaEncrypt(microAccount.getContact()));
        //手机号
        paramMap.put("contact_phone", RSAEncrypt.rsaEncrypt(microAccount.getContact_phone()));
        paramMap.put("contact_email", RSAEncrypt.rsaEncrypt(microAccount.getContact_email()));
        //签名类型
        paramMap.put("sign_type", "HMAC-SHA256");
        //签名
        paramMap.put("sign", SHAUtil.wechatCertficatesSignBySHA256(paramMap, WxpayConfig.APPKEY));

        return XmlUtil.Map2Xml(paramMap);
    }

    //小微商户进件提交,返回applyment_id
    public final static String doMicroSubmit(String data, String syspath, String mchid) {
        String url = WxpayConfig.getMicroSubmitUrl();
        //提交请求
        String resultXml = PostUtil.doPostWithCert(syspath, mchid, url, data);
        ;

        //正常操作返回的结果
        Map<String, String> resultMap = XmlUtil.parse(resultXml);
        System.out.println("result=>" + resultMap.toString());
        if (CheckUtil.isEmpty(resultMap)
                || !resultMap.get("return_code").equals("SUCCESS")
                || !resultMap.get("result_code").equals("SUCCESS")) {
            return "";
        }
        //{sign=9833D0D4FF18FC17D6ADE529AA593CD7, media_id=_qdPgxYeHjr20zZTSLO0ytytIW4WzQGYSjL9keglclA2AQyG_RoQhc87zbvCHC6B_zgcfMm7mi2zlOPEErgzFfy8z-koErfC1n0RAITyOCc, return_msg=OK, result_code=SUCCESS, return_code=SUCCESS}
        //返回applyment_id
        return resultMap.get("applyment_id");

    }

    //图片上传API,获取media_id
    public final static String doUploadImg(String syspath, String mchid, File file) {
        //文件md5hash
        String media_hash = MD5Util.md5HashCode(file);
        //失败直接返回
        if (CheckUtil.isEmpty(media_hash)) {
            return null;
        }

        //上传参数
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("mch_id", mchid);
        paramMap.put("media_hash", media_hash);
        paramMap.put("sign_type", "HMAC-SHA256");
        paramMap.put("sign", SHAUtil.sha256Sign(paramMap, WxpayConfig.APPKEY));
//        System.out.println("=>"+paramMap.toString());

        //获取上传链接
        String url = WxpayConfig.getMicroUploadUrl();

        //获取返回值
        String resultXml = PostUtil.doPostFileDataWithCert(syspath, mchid, url, paramMap, "media", file);

        //正常操作返回的结果
        Map<String, String> resultMap = XmlUtil.parse(resultXml);
        if (CheckUtil.isEmpty(resultMap)
                || !resultMap.get("return_code").equals("SUCCESS")
                || !resultMap.get("result_code").equals("SUCCESS")) {
            return "";
        }
        //{sign=9833D0D4FF18FC17D6ADE529AA593CD7, media_id=_qdPgxYeHjr20zZTSLO0ytytIW4WzQGYSjL9keglclA2AQyG_RoQhc87zbvCHC6B_zgcfMm7mi2zlOPEErgzFfy8z-koErfC1n0RAITyOCc, return_msg=OK, result_code=SUCCESS, return_code=SUCCESS}
        //返回media_id
        return resultMap.get("media_id");
    }

    //查询入驻提交状态
    private final static String getMicroSubmitQueryData(String applyment_id) {
        Map<String, String> paramMap = getMchHead(WxpayConfig.APPMCHID);
        //接口版本号
        paramMap.put("version", "1.0");

        //商户申请单号
        paramMap.put("applyment_id", applyment_id);
        //业务申请编号
//        paramMap.put("business_code",business_code);
        paramMap.put("sign_type", "HMAC-SHA256");
        //签名
        paramMap.put("sign", SHAUtil.wechatCertficatesSignBySHA256(paramMap, WxpayConfig.APPKEY));
        System.out.println("=>" + paramMap.toString());

        return XmlUtil.Map2Xml(paramMap);
    }

    //查询进件状态
    public final static String doMicroSubmitQuery(String syspath, String mchid, String applyment_id) {
        String url = WxpayConfig.getMicroSubmitQueryUrl();
        String data = getMicroSubmitQueryData(applyment_id);
        //提交请求
        String resultXml = PostUtil.doPostWithCert(syspath, mchid, url, data);
        System.out.println("result xml=>" + resultXml);
        //正常操作返回的结果
        Map<String, String> resultMap = XmlUtil.parse(resultXml);
        System.out.println("result map =>" + resultMap.toString());
        if (CheckUtil.isEmpty(resultMap)
                || !resultMap.get("return_code").equals("SUCCESS")
                || !resultMap.get("result_code").equals("SUCCESS")) {
            return "";
        }
        return resultMap.toString();
    }


    /**
     * 获取微信证书
     * 参考微信地址：
     * https://pay.weixin.qq.com/wiki/doc/api/xiaowei.php?chapter=19_11
     * <p>
     * data里面的serial_no，我们申请入驻需要的参数就是这个
     * 其次是encrypt_certificate，主要用来平台证书解密成密钥，为申请入驻接口敏感信息加密处理,后面申请入驻时详细讲解
     * <p>
     * 获取如下三个参数
     * associated_data，
     * nonce，
     * ciphertext
     */
    public String getCertFicates() {
        // 初始化一个HttpClient
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // Post请求
        HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/risk/getcertficates");
        /**
         * 这边需要您提供微信分配的商户号跟API密钥
         */
        Map<String, String> param = new HashMap<>();
        param.put("mch_id", WxpayConfig.APPMCHID);
        param.put("nonce_str", UUID.randomUUID().toString().replace("-", ""));
        // 暂只支持HMAC-SHA256 加密
        param.put("sign_type", "HMAC-SHA256");
        // 对你的参数进行加密处理
        param.put("sign", SHAUtil.wechatCertficatesSignBySHA256(param, WxpayConfig.APPKEY));
        httpPost.setEntity(new StringEntity(XmlUtil.Map2Xml(param), "UTF-8"));
        httpPost.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_XML.getMimeType());
        try {
            HttpResponse httpResponse = httpClient.execute(httpPost);
            System.out.println("获取平台证书响应 =" + httpResponse);
            if (httpResponse != null && httpResponse.getStatusLine().getStatusCode() == 200) {
                String responseEntity = EntityUtils.toString(httpResponse.getEntity());
                Map<String, String> m = XmlUtil.parse(responseEntity);
                System.out.println("获取平台证书响应 =" + m.toString());
                //2019-11-15 调接口返回=>
                // 获取平台证书响应 ={nonce_str=MWQ5hPcwyB4YuJ9Y,
                // certificates={"data":[{"serial_no":"749535475D89DF9ECF413A6CA05F74FF4CEDDCB8","effective_time":"2019-05-06 16:45:20","expire_time":"2024-05-04 16:45:20","encrypt_certificate":{"algorithm":"AEAD_AES_256_GCM","nonce":"c4fce6dde537","associated_data":"certificate","ciphertext":"QY4v5Y2nBEXMmgJDWolonYzH+ML/JQyr+AHZMrLw7DI/J/19V19e1bMQQ6xnah3K/sZbG2p2cTgJGzJHcoesqE7/+IwLqtbA9gup872x6oNBo6ZtPqbdHexy7J5XV99l2jH9ioQaoD+A+I1b2ZLx6LedTW2NPLRj0zMabYYNol24IR7ZiiPUQLOclNJ5uOta4Wvwlc94FuPyRtmih0/bPiOu0AsNySrYLhE5E87jWzsQQR/E9Qozcaul+Z6t3rr01K8oV3z6y0S5GOQoeZqBS8iZhmEO5KMpN/6v+V3Q/URdoXodvU8gxzJZGSQj+AAklPB+X7hx9nHrCXmV9YQsQyer2JglSVp3C1yJPlInEiSosKe31oNdOcM2PHYkm9gtlQQCoku/0RlK9ADbkfePZf8D6IW8x3gXULpkDePVaNcqpJwEg8oGlxCNGZa0jSXHTeclCDugcsPaHvy1QF3PFN/lGKjScRg74WfOywJLS64cpr7VaBxo1jDPdNau/ld6wyyrrDSLcvwKMY6EWVYGZ/0I49KzymERS8QANr6GRY3kTXwEMuJehNOZkoradVimZb5k2SVnFy7qZCbRLTjMI3NnfU7EXXGgl12XCvDu6cYvd+Lkz6SWHqSr7+Utkb1msePUM0dtsi+ArWIJ/0aJqghu7Pja+pgKYJtp5kZGq8QIUZYwT6KrXYbxnruFzuUAgKYSgA01LGsC4nKX7m4A7vQNg/np+kZP1tTibNa5DGrEM+VtZmGgGLjmeRhcCtcHlaNG4RpQQJFpxhO7ooXjQyVLaHjztDdpWbfRfjUEeao1WnnhlqjjAXc70aTbPwMAdkfzqVxb01lbAySVMCH/knUkUNMJ08wPKejKfKeiSeGbhhFuGdHDrLhfN1vFp+W1XvxXo0obWpecIxaJgN/46Uq6qhsSLrkh54yuafrVU4+DEp8a2d6NPZ8MBsDdfSeLqzlWoWdY34SSQ++jLxY+AMYOYK1675IwG+0xp2yo89gLkrl0fF5ei2+w4kDDpbx6rV6HnOpaaMD4rCb0F3+QyCAEqasb3DakDm+EHSd3TFsVViXNpLNaCf7DzoOkgOOPArVAYuiufwYoEcroyMbtHO0sMSNe1l/OHwx9iWiHcKywZ74OMLVxQYCeuBiwUDdnW69r1//DEdv/7ehtFWVUtBL39Yf/Ng6M1AnTCD+Ub3yGW9FjSO3p0W250zpcCgpER0srQulu66kQiwAClCKXklt9TzPukRdBRIwDXUO8OYoZ/KZCPy1aZG90DQBHCMajQicDRG4e6j+DmpkinDMsn3sv8evy2CSdmoYZw5fuN+oUU/TQFQFLflG9hg11o0hmtq81HmC+w7ERPeWHCC1r4qdhxHLdGKOM19XXJXMxZw3FIsT6jr2+HhhBr0LImYlijSM1tLwnVqsKBdc6uDIvFGSMHCXc0GF2pVhxW4APJZP+vCyd/KQ4IUTXPZsu+HtNaTUY3EIn+6KsT3suTCX3QoWN0EDkFVTDN5sxov9rVPmKW0P8zEJIBbcxK2eS4zKRjL6PjlF5vWPPLPLY2INQ59gh20eFsu1QyIh2au7dKTyhbh6FQ6xGmsssIx8XzhnfvOIv/LKEEWBHVx2VWsqskdATP/Vq/xbpmWiLpxrvT32vyUyv1X7T6YKpIancFUoByNGVZKjYepNTPeF8CIh4dTNTt68QhLzgHzeHZAF62p8lMIFA2asDzqsWfX091JnRVJnZ3bCrOA+GojqT2Ao91teH6eGCaifzlAb1S3GmnokyZhziaFb9L9h8QHxZJ7wf1s9wOP4WxoGuQ1aZVoV6vORSlg6qrfLPlAr5/799Y9GdiOc/2QAlpUYw7D0q7B2qCDh0R3KUIsvvQ+Nv8YnfQZ/og3+SnOwxipaMcWYOimLhTb4bAyNfvmzh2rPER9kY9mOh"}}]}, sign=3E11A47A26901129A968A57C0DF6F6A54483A300732DBAB8C89D9E57E345547B, return_msg=OK, result_code=SUCCESS, mch_id=1513549201, return_code=SUCCESS}
                if (m != null
                        && m.get("return_code") != null
                        && "SUCCESS".equals(m.get("return_code"))
                        && m.get("result_code") != null
                        && "SUCCESS".equals(m.get("result_code"))
                ) {
                    return m.get("certificates");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
//            log.error("执行httpclient请求平台证书序号错误 {}", e);
        }
        return null;
    }

    //平台证书解密出pem明文
    public String decryptCertSN(String associatedData, String nonce, String cipherText, String apiv3Key) throws Exception {
        final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "SunJCE");
        SecretKeySpec key = new SecretKeySpec(apiv3Key.getBytes(), "AES");
        GCMParameterSpec spec = new GCMParameterSpec(128, nonce.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, key, spec);
        cipher.updateAAD(associatedData.getBytes());
        return new String(cipher.doFinal(Base64.getDecoder().decode(cipherText)));
    }


    @Test
    public void getCert() throws Exception {
//        System.out.println(getCertFicates());

        String associated_data = "certificate";
        String nonce = "c4fce6dde537";
        String ciphertext = "QY4v5Y2nBEXMmgJDWolonYzH+ML/JQyr+AHZMrLw7DI/J/19V19e1bMQQ6xnah3K/sZbG2p2cTgJGzJHcoesqE7/+IwLqtbA9gup872x6oNBo6ZtPqbdHexy7J5XV99l2jH9ioQaoD+A+I1b2ZLx6LedTW2NPLRj0zMabYYNol24IR7ZiiPUQLOclNJ5uOta4Wvwlc94FuPyRtmih0/bPiOu0AsNySrYLhE5E87jWzsQQR/E9Qozcaul+Z6t3rr01K8oV3z6y0S5GOQoeZqBS8iZhmEO5KMpN/6v+V3Q/URdoXodvU8gxzJZGSQj+AAklPB+X7hx9nHrCXmV9YQsQyer2JglSVp3C1yJPlInEiSosKe31oNdOcM2PHYkm9gtlQQCoku/0RlK9ADbkfePZf8D6IW8x3gXULpkDePVaNcqpJwEg8oGlxCNGZa0jSXHTeclCDugcsPaHvy1QF3PFN/lGKjScRg74WfOywJLS64cpr7VaBxo1jDPdNau/ld6wyyrrDSLcvwKMY6EWVYGZ/0I49KzymERS8QANr6GRY3kTXwEMuJehNOZkoradVimZb5k2SVnFy7qZCbRLTjMI3NnfU7EXXGgl12XCvDu6cYvd+Lkz6SWHqSr7+Utkb1msePUM0dtsi+ArWIJ/0aJqghu7Pja+pgKYJtp5kZGq8QIUZYwT6KrXYbxnruFzuUAgKYSgA01LGsC4nKX7m4A7vQNg/np+kZP1tTibNa5DGrEM+VtZmGgGLjmeRhcCtcHlaNG4RpQQJFpxhO7ooXjQyVLaHjztDdpWbfRfjUEeao1WnnhlqjjAXc70aTbPwMAdkfzqVxb01lbAySVMCH/knUkUNMJ08wPKejKfKeiSeGbhhFuGdHDrLhfN1vFp+W1XvxXo0obWpecIxaJgN/46Uq6qhsSLrkh54yuafrVU4+DEp8a2d6NPZ8MBsDdfSeLqzlWoWdY34SSQ++jLxY+AMYOYK1675IwG+0xp2yo89gLkrl0fF5ei2+w4kDDpbx6rV6HnOpaaMD4rCb0F3+QyCAEqasb3DakDm+EHSd3TFsVViXNpLNaCf7DzoOkgOOPArVAYuiufwYoEcroyMbtHO0sMSNe1l/OHwx9iWiHcKywZ74OMLVxQYCeuBiwUDdnW69r1//DEdv/7ehtFWVUtBL39Yf/Ng6M1AnTCD+Ub3yGW9FjSO3p0W250zpcCgpER0srQulu66kQiwAClCKXklt9TzPukRdBRIwDXUO8OYoZ/KZCPy1aZG90DQBHCMajQicDRG4e6j+DmpkinDMsn3sv8evy2CSdmoYZw5fuN+oUU/TQFQFLflG9hg11o0hmtq81HmC+w7ERPeWHCC1r4qdhxHLdGKOM19XXJXMxZw3FIsT6jr2+HhhBr0LImYlijSM1tLwnVqsKBdc6uDIvFGSMHCXc0GF2pVhxW4APJZP+vCyd/KQ4IUTXPZsu+HtNaTUY3EIn+6KsT3suTCX3QoWN0EDkFVTDN5sxov9rVPmKW0P8zEJIBbcxK2eS4zKRjL6PjlF5vWPPLPLY2INQ59gh20eFsu1QyIh2au7dKTyhbh6FQ6xGmsssIx8XzhnfvOIv/LKEEWBHVx2VWsqskdATP/Vq/xbpmWiLpxrvT32vyUyv1X7T6YKpIancFUoByNGVZKjYepNTPeF8CIh4dTNTt68QhLzgHzeHZAF62p8lMIFA2asDzqsWfX091JnRVJnZ3bCrOA+GojqT2Ao91teH6eGCaifzlAb1S3GmnokyZhziaFb9L9h8QHxZJ7wf1s9wOP4WxoGuQ1aZVoV6vORSlg6qrfLPlAr5/799Y9GdiOc/2QAlpUYw7D0q7B2qCDh0R3KUIsvvQ+Nv8YnfQZ/og3+SnOwxipaMcWYOimLhTb4bAyNfvmzh2rPER9kY9mOh";
        String apiv3Key = "Bcb19e0814494a248d28c591d4bc31e1";
        String content = decryptCertSN(associated_data, nonce, ciphertext, apiv3Key);
        System.out.println("result=>" + content);

        String encrypt = RSAEncrypt.rsaEncryptByCert("我的身份证", content);
    }

    @Test
    public void testMicroUpload() throws Exception {
        String certPath = "D:/workspace/bcb-pay/target/artifacts/bcb_pay_war_exploded";
        String filePath = "C:/Users/Administrator/Desktop/商品信息2/0df05c892726c1cac5062b01fd03fb1.jpg";
        File file = new File(filePath);
        System.out.println("result=" + doUploadImg(certPath, WxpayConfig.APPMCHID, file));
    }

    @Test
    public void testMicroSubmitQuery() throws Exception {
        String certPath = "D:/workspace/bcb-pay/target/artifacts/bcb_pay_war_exploded";
        String applyment_id = "2340280348203";
        String data = doMicroSubmitQuery(certPath, WxpayConfig.APPMCHID, applyment_id);
        System.out.println("result=" + data);
    }

    @Test
    public void testMicroSubmit() throws Exception {
        String certPath = "D:/workspace/bcb-pay/target/artifacts/bcb_pay_war_exploded";
        String orderNo = "1132134";
        MicroAccount microAccount = new MicroAccount();
        microAccount.setAccount_bank("交通银行");
        microAccount.setAccount_name("王志刚");
        microAccount.setAccount_number("6222600620015541041");
        microAccount.setBank_address_code("410102");
        microAccount.setBusiness_code(orderNo);
        microAccount.setContact("王志刚");
        microAccount.setContact_phone("13703957387");
        microAccount.setContact_email("375214167@qq.com");
        microAccount.setId_card_copy("_qdPgxYeHjr20zZTSLO0ytytIW4WzQGYSjL9keglclA2AQyG_RoQhc87zbvCHC6B_zgcfMm7mi2zlOPEErgzFfy8z");
        microAccount.setId_card_name("王志刚");
        microAccount.setId_card_number("320106496903060011");
        microAccount.setId_card_national("W56fsKod1rgFm3W7Xin0HIWYTvWcHWNN2l62P8myGhDeN7dwQ3Xzen1K3EdCEuzRIOHUpEky61GQ8lZQ3JjxDrZ_k23PUCJc9pQathUIR8I");
        microAccount.setId_card_valid_time("[\"1970-01-01\",\"长期\"]");
        microAccount.setIndoor_pic("W56fsKod1rgFm3W7Xin0HIWYTvWcHWNN2l62P8myGhDeN7dwQ3Xzen1K3EdCEuzRIOHUpEky61GQ8lZQ3JjxDrZ_k23PUCJc9pQathUIR8I");
        microAccount.setMerchant_shortname("天天小吃");
        microAccount.setProduct_desc("餐饮");
        microAccount.setRate("0.6%");
        microAccount.setService_phone("13703957387");
        microAccount.setStore_address_code("410101");
        microAccount.setStore_entrance_pic("W56fsKod1rgFm3W7Xin0HIWYTvWcHWNN2l62P8myGhDeN7dwQ3Xzen1K3EdCEuzRIOHUpEky61GQ8lZQ3JjxDrZ_k23PUCJc9pQathUIR8I");
        microAccount.setStore_name("郑州天天小吃");
        microAccount.setStore_street("郑州市工人路100号");

        //添加证书验证初始化
        RSAEncrypt.setCertPath(WxpayConfig.getPublicCertPath(certPath, WxpayConfig.APPMCHID));
        //准备数据
        String data = WxpaySubmitV3.getMicroSubmitData(orderNo, microAccount, WxpayConfig.CERTSN);
        System.out.println("data=" + data);

        //提交
        String applyment_id = WxpaySubmitV3.doMicroSubmit(data, certPath, WxpayConfig.APPMCHID);
        System.out.println("result=" + applyment_id);
    }

    //菠菜包
    String mchId = "1513549201";
    String appId = "wxa574b9142c67f42e";
    String key = "Bcb19e0814494a248d28c591d4bc31e1";
    String sandBoxKey = "ccc6dbfaabfe4d13c01e8666b38c3f3e";

    String subMchId = "1525006091";
    String subAppId = "wxd8de5d37b6976b55"; //

    String certRootPath = "D:/workspace/bcb-pay/target/artifacts/bcb_pay_war_exploded";

    String openid = "olFJwwK3yub_nNNXXOhkTf07nYC0";

    @Test
    public void testRefund() throws Exception {
        String certPath = "D:/workspace/bcb-pay/target/artifacts/bcb_pay_war_exploded";
        String outRefundNo = "BCB234273947298374237491";
        //添加证书验证初始化
        RSAEncrypt.setCertPath(WxpayConfig.getPublicCertPath(certPath, WxpayConfig.APPMCHID));
        //准备数据
        String data = WxpaySubmitV3.refundData(subMchId, "", "10001772509067195318388364", outRefundNo, new Double(0.01), new Double(0.01));
        System.out.println("data=" + data);

        //提交
        String applyment_id = WxpaySubmitV3.doRefund(data, certPath, WxpayConfig.APPMCHID);
        System.out.println("result=" + applyment_id);
    }

}
