package com.bcb.wxpay.util.service;

import com.bcb.util.CheckUtil;
import com.bcb.wxpay.entity.WxMicroAccount;
import com.bcb.wxpay.util.*;
import com.bcb.wxpay.util.company.WxpayConfig;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 子商户入驻
 */
public class MicroSubmit {

    //菠菜包
    String mchId = "1513549201";
    String appId = "wxa574b9142c67f42e";
    String key = "Bcb19e0814494a248d28c591d4bc31e1";
    String sandBoxKey = "ccc6dbfaabfe4d13c01e8666b38c3f3e";

    String subMchId = "1525006091";
    String subAppId = "wxd8de5d37b6976b55"; //子商户号绑定的appid

    String certRootPath = "D:/workspace/bcb-pay/target/artifacts/bcb_pay_war_exploded";

    String openid = "olFJwwK3yub_nNNXXOhkTf07nYC0";


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
    private final static String getMicroSubmitData(String orderNo, WxMicroAccount wxMicroAccount, String certSn) throws Exception {
        Map<String, String> paramMap = getMchAppHead(WxpayConfig.APPMCHID);
        //接口版本号
        paramMap.put("version", "3.0");
        //平台证书序列号
        paramMap.put("cert_sn", certSn);


        //业务申请编号
        paramMap.put("business_code", orderNo);
        //身份证人像面照片
        paramMap.put("id_card_copy", wxMicroAccount.getId_card_copy());
        //身份证国徽面照片
        paramMap.put("id_card_national", wxMicroAccount.getId_card_national());
        //身份证姓名
        paramMap.put("id_card_name", RSAUtil.rsaEncrypt(wxMicroAccount.getId_card_name()));
        //身份证号码
        paramMap.put("id_card_number", RSAUtil.rsaEncrypt(wxMicroAccount.getId_card_number()));
        //身份证有效期限
        paramMap.put("id_card_valid_time", wxMicroAccount.getId_card_valid_time());
        //开户名称
        paramMap.put("account_name", RSAUtil.rsaEncrypt(wxMicroAccount.getAccount_name()));
        //开户银行
        paramMap.put("account_bank", wxMicroAccount.getAccount_bank());
        //开户银行省市编码
        paramMap.put("bank_address_code", wxMicroAccount.getBank_address_code());
        //银行账号
        paramMap.put("account_number", RSAUtil.rsaEncrypt(wxMicroAccount.getAccount_number()));
        //门店名称
        paramMap.put("store_name", wxMicroAccount.getStore_name());
        //门店省市编码
        paramMap.put("store_address_code", wxMicroAccount.getStore_address_code());
        //门店街道名称
        paramMap.put("store_street", wxMicroAccount.getStore_street());
        //门店门口照片
        paramMap.put("store_entrance_pic", wxMicroAccount.getStore_entrance_pic());
        //店内环境照片
        paramMap.put("indoor_pic", wxMicroAccount.getIndoor_pic());
        //商户简称
        paramMap.put("merchant_shortname", wxMicroAccount.getMerchant_shortname());
        //客服电话
        paramMap.put("service_phone", wxMicroAccount.getService_phone());
        //售卖商品/提供服务描述
        paramMap.put("product_desc", wxMicroAccount.getProduct_desc());
        //费率
        paramMap.put("rate", wxMicroAccount.getRate());
        //超级管理员姓名
        paramMap.put("contact", RSAUtil.rsaEncrypt(wxMicroAccount.getContact()));
        //手机号
        paramMap.put("contact_phone", RSAUtil.rsaEncrypt(wxMicroAccount.getContact_phone()));
        paramMap.put("contact_email", RSAUtil.rsaEncrypt(wxMicroAccount.getContact_email()));
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

        //正常操作返回的结果
        Map<String, String> resultMap = XmlUtil.parse(resultXml);
        System.out.println("result=>" + resultMap.toString());
        if (CheckUtil.isEmpty(resultMap)
                || !resultMap.get("return_code").equals("SUCCESS")
                || !resultMap.get("result_code").equals("SUCCESS")) {
            return "";
        }
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
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("mch_id", mchid);
        paramMap.put("media_hash", media_hash);
        paramMap.put("sign_type", "HMAC-SHA256");
        paramMap.put("sign", SHAUtil.sha256Sign(paramMap, WxpayConfig.APPKEY));
        System.out.println("Sign=>"+XmlUtil.Map2Xml(paramMap));

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
     * 下载证书
     */
    @Test
    public void downLoadCert(){
        System.out.println(AesGcmUtil.getCertFicates());
    }

    /**
     * 证书解密出公钥
     * @throws Exception
     */
    @Test
    public void getCert() throws Exception {
        String associated_data = "certificate";
        String nonce = "c4fce6dde537";
        String ciphertext = "QY4v5Y2nBEXMmgJDWolonYzH+ML/JQyr+AHZMrLw7DI/J/19V19e1bMQQ6xnah3K/sZbG2p2cTgJGzJHcoesqE7/+IwLqtbA9gup872x6oNBo6ZtPqbdHexy7J5XV99l2jH9ioQaoD+A+I1b2ZLx6LedTW2NPLRj0zMabYYNol24IR7ZiiPUQLOclNJ5uOta4Wvwlc94FuPyRtmih0/bPiOu0AsNySrYLhE5E87jWzsQQR/E9Qozcaul+Z6t3rr01K8oV3z6y0S5GOQoeZqBS8iZhmEO5KMpN/6v+V3Q/URdoXodvU8gxzJZGSQj+AAklPB+X7hx9nHrCXmV9YQsQyer2JglSVp3C1yJPlInEiSosKe31oNdOcM2PHYkm9gtlQQCoku/0RlK9ADbkfePZf8D6IW8x3gXULpkDePVaNcqpJwEg8oGlxCNGZa0jSXHTeclCDugcsPaHvy1QF3PFN/lGKjScRg74WfOywJLS64cpr7VaBxo1jDPdNau/ld6wyyrrDSLcvwKMY6EWVYGZ/0I49KzymERS8QANr6GRY3kTXwEMuJehNOZkoradVimZb5k2SVnFy7qZCbRLTjMI3NnfU7EXXGgl12XCvDu6cYvd+Lkz6SWHqSr7+Utkb1msePUM0dtsi+ArWIJ/0aJqghu7Pja+pgKYJtp5kZGq8QIUZYwT6KrXYbxnruFzuUAgKYSgA01LGsC4nKX7m4A7vQNg/np+kZP1tTibNa5DGrEM+VtZmGgGLjmeRhcCtcHlaNG4RpQQJFpxhO7ooXjQyVLaHjztDdpWbfRfjUEeao1WnnhlqjjAXc70aTbPwMAdkfzqVxb01lbAySVMCH/knUkUNMJ08wPKejKfKeiSeGbhhFuGdHDrLhfN1vFp+W1XvxXo0obWpecIxaJgN/46Uq6qhsSLrkh54yuafrVU4+DEp8a2d6NPZ8MBsDdfSeLqzlWoWdY34SSQ++jLxY+AMYOYK1675IwG+0xp2yo89gLkrl0fF5ei2+w4kDDpbx6rV6HnOpaaMD4rCb0F3+QyCAEqasb3DakDm+EHSd3TFsVViXNpLNaCf7DzoOkgOOPArVAYuiufwYoEcroyMbtHO0sMSNe1l/OHwx9iWiHcKywZ74OMLVxQYCeuBiwUDdnW69r1//DEdv/7ehtFWVUtBL39Yf/Ng6M1AnTCD+Ub3yGW9FjSO3p0W250zpcCgpER0srQulu66kQiwAClCKXklt9TzPukRdBRIwDXUO8OYoZ/KZCPy1aZG90DQBHCMajQicDRG4e6j+DmpkinDMsn3sv8evy2CSdmoYZw5fuN+oUU/TQFQFLflG9hg11o0hmtq81HmC+w7ERPeWHCC1r4qdhxHLdGKOM19XXJXMxZw3FIsT6jr2+HhhBr0LImYlijSM1tLwnVqsKBdc6uDIvFGSMHCXc0GF2pVhxW4APJZP+vCyd/KQ4IUTXPZsu+HtNaTUY3EIn+6KsT3suTCX3QoWN0EDkFVTDN5sxov9rVPmKW0P8zEJIBbcxK2eS4zKRjL6PjlF5vWPPLPLY2INQ59gh20eFsu1QyIh2au7dKTyhbh6FQ6xGmsssIx8XzhnfvOIv/LKEEWBHVx2VWsqskdATP/Vq/xbpmWiLpxrvT32vyUyv1X7T6YKpIancFUoByNGVZKjYepNTPeF8CIh4dTNTt68QhLzgHzeHZAF62p8lMIFA2asDzqsWfX091JnRVJnZ3bCrOA+GojqT2Ao91teH6eGCaifzlAb1S3GmnokyZhziaFb9L9h8QHxZJ7wf1s9wOP4WxoGuQ1aZVoV6vORSlg6qrfLPlAr5/799Y9GdiOc/2QAlpUYw7D0q7B2qCDh0R3KUIsvvQ+Nv8YnfQZ/og3+SnOwxipaMcWYOimLhTb4bAyNfvmzh2rPER9kY9mOh";
        String apiv3Key = "Bcb19e0814494a248d28c591d4bc31e1";
        String content = AesGcmUtil.decryptCertSN(associated_data, nonce, ciphertext, apiv3Key);
        System.out.println("result=>" + content);
        String encrypt = RSAUtil.rsaEncryptByCert("我的身份证", content);
        System.out.println("result=>" + encrypt);
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
        WxMicroAccount wxMicroAccount = new WxMicroAccount();
        wxMicroAccount.setAccount_bank("交通银行");
        wxMicroAccount.setAccount_name("王志刚");
        wxMicroAccount.setAccount_number("6222600620015541041");
        wxMicroAccount.setBank_address_code("410102");
        wxMicroAccount.setBusiness_code(orderNo);
        wxMicroAccount.setContact("王志刚");
        wxMicroAccount.setContact_phone("13703957387");
        wxMicroAccount.setContact_email("375214167@qq.com");
        wxMicroAccount.setId_card_copy("_qdPgxYeHjr20zZTSLO0ytytIW4WzQGYSjL9keglclA2AQyG_RoQhc87zbvCHC6B_zgcfMm7mi2zlOPEErgzFfy8z");
        wxMicroAccount.setId_card_name("王志刚");
        wxMicroAccount.setId_card_number("320106496903060011");
        wxMicroAccount.setId_card_national("W56fsKod1rgFm3W7Xin0HIWYTvWcHWNN2l62P8myGhDeN7dwQ3Xzen1K3EdCEuzRIOHUpEky61GQ8lZQ3JjxDrZ_k23PUCJc9pQathUIR8I");
        wxMicroAccount.setId_card_valid_time("[\"1970-01-01\",\"长期\"]");
        wxMicroAccount.setIndoor_pic("W56fsKod1rgFm3W7Xin0HIWYTvWcHWNN2l62P8myGhDeN7dwQ3Xzen1K3EdCEuzRIOHUpEky61GQ8lZQ3JjxDrZ_k23PUCJc9pQathUIR8I");
        wxMicroAccount.setMerchant_shortname("天天小吃");
        wxMicroAccount.setProduct_desc("餐饮");
        wxMicroAccount.setRate("0.6%");
        wxMicroAccount.setService_phone("13703957387");
        wxMicroAccount.setStore_address_code("410101");
        wxMicroAccount.setStore_entrance_pic("W56fsKod1rgFm3W7Xin0HIWYTvWcHWNN2l62P8myGhDeN7dwQ3Xzen1K3EdCEuzRIOHUpEky61GQ8lZQ3JjxDrZ_k23PUCJc9pQathUIR8I");
        wxMicroAccount.setStore_name("郑州天天小吃");
        wxMicroAccount.setStore_street("郑州市工人路100号");

        //添加证书验证初始化
        RSAUtil.setCertPath(WxpayConfig.getPublicCertPath(certPath, WxpayConfig.APPMCHID));
        //准备数据
        String data = MicroSubmit.getMicroSubmitData(orderNo, wxMicroAccount, WxpayConfig.CERTSN);
        System.out.println("data=" + data);

        //提交
        String applyment_id = MicroSubmit.doMicroSubmit(data, certPath, WxpayConfig.APPMCHID);
        System.out.println("result=" + applyment_id);
    }

}
