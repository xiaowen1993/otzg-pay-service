package com.bcb.util;


import com.bcb.log.util.LogUtil;
import net.sf.json.JSONObject;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

public class SignUtil{
    /**
     * 系统启动传入当前应用密钥
     * @author G/2018/6/20 14:24
     */
    private static String appid;
    public static void setAppid(String key){
        appid = key;
    }
    public static String getAppid(){return appid;}

    /**
     * 传入对称加密密钥
     */
    private static String secret;
    public static void setSecret(String value){
        secret = value;
    }

    private static String appPrivateKey;
    public static void setAppPrivateKey(String value){
        appPrivateKey = value;
    }

    /**
     * 传入用户中心公钥
     */
    private static String memberServPublicKey;
    public static void setMemberServPublicKey(String value){
        memberServPublicKey = value;
    }


    /**
     * 根据json参数 加上应用appid及签名sign
     * appid 及 secret 由系统启动后自动传入
     * 返回spring restTemplate 的 post 方法需要 的 MultiValueMap格式
     */
    public final static MultiValueMap getSignedByJson(JSONObject params) {
        MultiValueMap multiValueMap = new LinkedMultiValueMap<>();
        Map<String,Object> maps = JSONObject.fromObject(params);
        //对称加密
//        maps = SignUtil.sign(maps,secret,"md5");
        //非对称加密
        maps = SignUtil.sign(maps,appPrivateKey,"rsa");
        for(String key:maps.keySet()){
            multiValueMap.add(key,maps.get(key));
        }
        return multiValueMap;
    }

    public static Object getSignedByMap(Map<String,Object> maps) {
        MultiValueMap multiValueMap = new LinkedMultiValueMap<>();
        //对称加密
//        maps = SignUtil.sign(maps,secret,"md5");
        //非对称加密
        maps = SignUtil.sign(maps,appPrivateKey,"rsa");
        for(String key:maps.keySet()){
            multiValueMap.add(key,maps.get(key));
        }
        return multiValueMap;
    }

    /**
     * 采用非加密的方式组织参数
     * @author G/2018/6/23 16:09
     */
    public final static MultiValueMap getUnSignedByMap(Map<String,Object> maps) {
        MultiValueMap multiValueMap = new LinkedMultiValueMap<>();
        for(String key:maps.keySet()){
            multiValueMap.add(key,maps.get(key));
        }
        return multiValueMap;
    }

    /**
     * 添加appid标识(验签一方据此获取解密密钥)
     * 结果加入签名
     * @author G/2016年11月11日 上午11:32:22
     * @return
     */
    public final static Map<String,Object> sign(Map<String,Object> paramMap, String secret,String signType) {
        if(paramMap!=null && signType!=null){
            paramMap.put("APP_ID",appid);
            if(signType.equalsIgnoreCase("MD5")){
                paramMap.put("sign",MD5Sign(paramMap,secret));
            }else if(signType.equalsIgnoreCase("RSA")){
                paramMap.put("sign",RSASign(paramMap,secret));
            }
        }
        return paramMap;
    }

    /**
     * 校验签名
     * @param paramMap
     * @param secret
     * @param signType
     * @return
     */
    public final static boolean verify(Map<String,Object> paramMap,String secret,String signType){
        boolean f=false;
        if(paramMap!=null && paramMap.get("sign")!=null && signType!=null){
            if(signType.equalsIgnoreCase("MD5")){
                f=MD5Verify(paramMap,secret);
            }else if(signType.equalsIgnoreCase("RSA")){
                f=RSAVerify(paramMap,secret);
            }
        }
        return f;
    }

    /**
     * 非对称私钥加密
     * @param paramMap
     * @param privateKey
     * @return
     */
    private final static String RSASign(Map<String,Object> paramMap,String privateKey){
        //所有的参数重新排序
        String strSignTemp= UrlUtil.getLinkParamString(paramMap);
        //签名
        String sign=null;
        try {
            sign = RsaUtil.sign(strSignTemp.getBytes(),privateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sign;
    }
    /**
     * 非对称公钥校验签名
     * @author G/2016年10月19日 上午10:01:19
     * @param paramMap
     * @return
     */
    private final static boolean RSAVerify(Map<String,Object> paramMap,String pubicKey){
        boolean f=false;
        if(paramMap!=null && paramMap.get("sign")!=null){
            Object sign=paramMap.get("sign");
            try {
                String strSignTemp= UrlUtil.getLinkParamString(paramMap);
                f= RsaUtil.verify(strSignTemp.getBytes(),pubicKey,sign.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return f;
    }


    /**
     * MD5参数签名
     * 1、所有的参数按照字母顺序重新排序
     * 2、尾部加key = 秘钥
     * 3、MD5并转换为大写
     * @author G/2018年06月19日 上午10:28:05
     * @param paramMap
     * @return
     */
    private final static String MD5Sign(Map<String,Object> paramMap,String secret){
        //所有的参数重新排序
        String strSignTemp= UrlUtil.getLinkParamString(paramMap);
        //拼接API密钥：
        strSignTemp=strSignTemp+"&key="+secret;
        //签名
        return AesUtil.MD5(strSignTemp).toUpperCase();
    }

    /**
     * MD5校验签名
     * @author G/2016年10月19日 上午10:01:19
     * @param paramMap
     * @return
     */
    private final static boolean MD5Verify(Map<String,Object> paramMap,String secret){
        boolean f=false;
        if(paramMap!=null && paramMap.get("sign")!=null){
            Object sign=paramMap.get("sign");
            if(MD5Sign(paramMap,secret).equals(sign)){
                f=true;
            }
        }
        return f;
    }



    public static void main(String[] args) throws Exception {
//        String privateKey ="MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKYoiGrhZKwBnAjWlNhrf+DFUfVkSKH8YFYPVUurYE1dmLEFrJnNb6/7HKV+SiBkQBr+WZoOHf4hWXHkXrMpPJAWGVMvJc+UL2m1ZW+njIcA096ElrQdvdXPQ50yacOY4Js70fomQI9VCmE/Mt3qXWhGp3Pp2o/sWaaa7chjZcfBAgMBAAECgYBYUDjZnfzVE3HsnKi2MsTgIeCC7g9Q0YQemb27H1ZrKHEsvZhUkwVm4rACIhKiDsan0kKriA9W8EDLMRdIYIWIMIMhN0MObRCvfu5pkepWaeOA+F+T+SL29ijc7GhmS5i1XXznLLKOHAb2tcNkoLTcKxU40sbITSXAuFIZpfvzxQJBANFAJBVDvvAVVRpynUzp7GQe7xmUlODFdAS1Q8HEa5hpdtiWIMWLrVZITfl3C+51vWmG6eQRutwmgmVvk3snn98CQQDLR8dvhAZlJ2LTxj6DFjEw/OiRviFwDlk2sDjQcxz1B5+TdzKYMFaw9RMKCp38kfXQGedDi0QkVEm6E56GLQxfAkEAp3jSaEKEOtqX9kbtJnXCQI+RhcOpNAxUQsBgrmBqTN17xPTC3dhgrsHHxnVFE2Ega6kS4Ppft3sKueyG+PZJuQJAW18uQ+/iOAGWKH7Jhn6pKc3kc+40dXvdmfln8Dpt363Hiq9fbIz9ypi+MBtJnEe3aAzcqL2mqXXBlgRPkxwYnQJBAMOTGdbzd16Rl8yRW+CEj5OSSRzdYPuuiFv0LW/mheITfsm7X/gm3IJ/v6/MyUP2xyAEXDh6vufNlcQUzCPgLUM=";
//        String publicKey ="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCmKIhq4WSsAZwI1pTYa3/gxVH1ZEih/GBWD1VLq2BNXZixBayZzW+v+xylfkogZEAa/lmaDh3+IVlx5F6zKTyQFhlTLyXPlC9ptWVvp4yHANPehJa0Hb3Vz0OdMmnDmOCbO9H6JkCPVQphPzLd6l1oRqdz6dqP7Fmmmu3IY2XHwQIDAQAB";
//        Map<String,Object> jo = new HashMap<>();
//        jo.put("aaa","xxxxxxxxxxx");
//        jo.put("bbb","qqwefqwefsdf1212");
//
//        jo= SignUtil.sign(jo,"123","md5");
//        LogUtil.print("sign==>"+jo.toString());
//        boolean b = SignUtil.verify(jo,"123","md5");
//        LogUtil.print("result="+b);
//
//        jo= SignUtil.sign(jo,privateKey,"RSA");
//        LogUtil.print("sign==>"+jo.toString());
//        boolean flag = SignUtil.verify(jo,publicKey,"RSA");
//        LogUtil.print("result="+flag);


        String appid ="2022bba77291445d80ac";
        String secret ="054e9796dea4a350";
        String account ="13703957387";
        JSONObject param = new JSONObject();
        param.put("appid",appid);
        param.put("account",account);
        Map<String,Object> maps = sign(param,secret,"md5");


        boolean flag = verify(maps,secret,"MD5");
        LogUtil.print("result="+flag);

    }
}