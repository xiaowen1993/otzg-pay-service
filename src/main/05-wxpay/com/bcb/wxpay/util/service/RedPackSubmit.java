package com.bcb.wxpay.util.service;

import com.bcb.util.FuncUtil;
import com.bcb.util.JsonUtil;
import com.bcb.wxpay.dto.WxRedPackDto;
import com.bcb.wxpay.entity.WxRedPack;
import com.bcb.wxpay.util.XmlUtil;
import com.bcb.wxpay.util.sdk.WXPayConstants;
import com.bcb.wxpay.util.sdk.WXPayRequest;
import com.bcb.wxpay.util.sdk.WXPayUtil;
import net.sf.json.JSONObject;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 发红包
 */
public class RedPackSubmit {

    /**
     * 随机字符串	nonce_str	是	5K8264ILTKCH16CQ2502SI8ZNMTM67VS	String(32)	随机字符串，不长于32位
     * 签名	sign	是	C380BEC2BFD727A4B6845133519F3AD6	String(32)	详见签名生成算法
     * 商户订单号	mch_billno	是	10000098201411111234567890	String(28)	商户发放红包的商户订单号
     * 商户号	mch_id	是	10000098	String(32)	微信支付分配的商户号
     * Appid	appid	是	wxe062425f740d30d8	String(32)	微信分配的公众账号ID（企业号corpid即为此appId），接口传入的所有appid应该为公众号的appid（在mp.weixin.qq.com申请的），不能为APP的appid（在open.weixin.qq.com申请的）。
     * 订单类型	bill_type	是	MCHT	String(32)	MCHT:通过商户订单号获取红包信息。
     * @return
     */
    public final static Map<String, String> queryData(String billNo) {

        Map<String, String> paramMap = new HashMap<>();
        //商户订单号
        paramMap.put("mch_billno", billNo);
        //随机数
        paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
        paramMap.put("bill_type", "MCHT");
        return paramMap;
    }


    public final static Map<String, String> redPackData(String subMchId, String payRedpackOrderNo, com.bcb.wxpay.dto.WxRedPackDto wxRedPackDto) {

        Map<String, String> paramMap = new HashMap<>();
        //微信支付分配的子商户号，服务商模式下必填
        paramMap.put("sub_mch_id", subMchId);
        //商户订单号
        paramMap.put("mch_billno", payRedpackOrderNo);
        //随机数
        paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
        //微信分配的公众账号ID（企业号corpid即为此appId）。
        // 接口传入的所有appid应该为公众号的appid（在mp.weixin.qq.com申请的），
        // 不能为APP的appid（在open.weixin.qq.com申请的）。
        paramMap.put("wxappid", WXPayConfig.getAppId());
        //服务商模式下触达用户时的appid(可填服务商自己的appid或子商户的appid)，
        // 服务商模式下必填，服务商模式下填入的子商户appid必须在微信支付商户平台中先录入
        // ，否则会校验不过。
        paramMap.put("msgappid", wxRedPackDto.getMsgAppId());
        //红包发送者名称 length=32 注意：敏感词会被转义成字符*
        paramMap.put("send_name", wxRedPackDto.getSendName());
        //接受红包的用户
        //用户在wxappid下的openid，服务商模式下可填入msgappid下的openid。
        paramMap.put("re_openid", wxRedPackDto.getReOpenId());
        //付款金额 单位分
        paramMap.put("total_amount", "" + Math.round(wxRedPackDto.getTotalAmount() * 100));
        //红包发放人数
        paramMap.put("total_num", "" + wxRedPackDto.getTotalNum());
        //红包祝福语
        paramMap.put("wishing", wxRedPackDto.getWishing());
        //调用接口的机器Ip地址(普通红包必填)
        if (null != wxRedPackDto.getClientIp())
            paramMap.put("client_ip", wxRedPackDto.getClientIp());
        //红包分配规则(裂变红包必填)
        if(null != wxRedPackDto.getAmtType())
            paramMap.put("amt_type", wxRedPackDto.getAmtType());
        //活动名称length=32
        paramMap.put("act_name", wxRedPackDto.getActName());
        //备注信息256
        paramMap.put("remark", wxRedPackDto.getRemark());
        //发放红包使用场景，红包金额大于200或者小于1元时必传
        // PRODUCT_1:商品促销
        // PRODUCT_2:抽奖
        // PRODUCT_3:虚拟物品兑奖
        //PRODUCT_4:企业内部福利
        //PRODUCT_5:渠道分润
        //PRODUCT_6:保险回馈
        //PRODUCT_7:彩票派奖
        //PRODUCT_8:税务刮奖
        if(null != wxRedPackDto.getSceneId())
            paramMap.put("scene_id", wxRedPackDto.getSceneId());
        //金额必须为整数  单位为转换为分
        //支付成功后，回调地址
        return paramMap;
    }
    /**
     * 发放普通红包接口
     * <p>
     * 发放规则
     * 1.发送频率限制------默认1800/min
     * 2.发送个数上限------默认1800/min
     * 3.场景金额限制------默认红包金额为1-200元，如有需要，可前往商户平台进行设置和申请
     * 4.其他限制------商户单日出资金额上限--100万元；单用户单日收款金额上限--1000元；单用户可领取红包个数上限--10个；
     * <p>
     * 注意事项：
     * ◆ 红包金额大于200或者小于1元时，请求参数scene_id必传，参数说明见下文。
     * ◆ 根据监管要求，新申请商户号使用现金红包需要满足两个条件：1、入驻时间超过90天 2、连续正常交易30天。
     * ◆ 移动应用的appid无法使用红包接口。
     * ◆ 当返回错误码为“SYSTEMERROR”时，请不要更换商户订单号，一定要使用原商户订单号重试，则可能造成重复发放红包等资金风险。
     * ◆ XML具有可扩展性，因此返回参数可能会有新增，而且顺序可能不完全遵循此文档规范，如果在解析回包的时候发生错误，请商户务必不要换单重试，请商户联系客服确认红包发放情况。如果有新回包字段，会更新到此API文档中。
     * ◆ 因为错误代码字段err_code的值后续可能会增加，所以商户如果遇到回包返回新的错误码，请商户务必不要换单重试，请商户联系客服确认红包发放情况。如果有新的错误码，会更新到此API文档中。
     * ◆ 错误代码描述字段err_code_des只供人工定位问题时做参考，系统实现时请不要依赖这个字段来做自动化处理。
     * ◆ 请商户在自身的系统中合理设置发放频次并做好并发控制，防范错付风险。
     * ◆ 因商户自身系统设置存在问题导致的资金损失，由商户自行承担。
     *
     * @param map
     * @return
     * @throws Exception
     */
    public final static JSONObject redPackPay(Map<String, String> map) throws Exception {
        WXPayConfig wxPayConfig = new WXPayConfig(SignType.MD5.name(), true);

        wxPayConfig.setUrl(WXPayConstants.getRePackPayUrl());
        WXPayRequest wxPayRequest = new WXPayRequest(wxPayConfig);
        Map<String, Object> result = wxPayRequest.requestCertNoAppId(map);
        System.out.println("调用结果=" + result.toString());

        if (result.get("return_code") != null
                && result.get("return_code").equals("SUCCESS")
                && result.get("result_code") != null
                && result.get("result_code").equals("SUCCESS")
        ) {
            System.out.println("调用成功=" + result.toString());
            return JsonUtil.get(true, result.get("result_code").toString(), "调用成功", result);
        } else {
            System.out.println("调用失败");
            return JsonUtil.get(false, result.get("result_code").toString(), "调用失败");
        }
    }


    /**
     * 裂变红包
     * <xml>
     * <sign><![CDATA[E1EE61A91C8E90F299DE6AE075D60A2D]]></sign>
     * <mch_billno><![CDATA[0010010404201411170000046545]]></mch_billno>
     * <mch_id><![CDATA[1000888888]]></mch_id>
     * <sub_mch_id><![CDATA[1000888888]]></sub_mch_id>
     * <wxappid><![CDATA[wxcbda96de0b165486]]></wxappid>
     * <send_name><![CDATA[send_name]]></send_name>
     * <re_openid><![CDATA[onqOjjmM1tad-3ROpncN-yUfa6uI]]></re_openid>
     * <total_amount><![CDATA[600]]></total_amount>
     * <amt_type><![CDATA[ALL_RAND]]></amt_type>
     * <total_num><![CDATA[3]]></total_num>
     * <wishing><![CDATA[恭喜发财]]></wishing>
     * <act_name><![CDATA[新年红包]]></act_name>
     * <remark><![CDATA[新年红包]]></remark>
     * <scene_id><![CDATA[PRODUCT_2]]></scene_id>
     * <nonce_str><![CDATA[50780e0cca98c8c8e814883e5caa672e]]></nonce_str>
     * <risk_info>posttime%3d123123412%26clientversion%3d234134%26mobile%3d122344545%26deviceid%3dIOS</risk_info>
     * </xml>
     * @param wxRedPack
     * @return
     */
//    public final static Map<String, String> redPackGroupData(WxRedPack wxRedPack) {
//
//        Map<String, String> paramMap = new HashMap<>();
//        //微信支付分配的子商户号，服务商模式下必填
//        paramMap.put("sub_mch_id", wxRedPack.getSubMchId());
//        //商户订单号
//        paramMap.put("mch_billno", wxRedPack.getMchBillno());
//        //随机数
//        paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
//        //微信分配的公众账号ID（企业号corpid即为此appId）。
//        // 接口传入的所有appid应该为公众号的appid（在mp.weixin.qq.com申请的），
//        // 不能为APP的appid（在open.weixin.qq.com申请的）。
//        paramMap.put("wxappid", wxRedPack.getGzhAppId());
//        //服务商模式下触达用户时的appid(可填服务商自己的appid或子商户的appid)，
//        // 服务商模式下必填，服务商模式下填入的子商户appid必须在微信支付商户平台中先录入
//        // ，否则会校验不过。
//        paramMap.put("msgappid", wxRedPack.getSubAppId());
//
//        //红包发送者名称 length=32 注意：敏感词会被转义成字符*
//        paramMap.put("send_name", wxRedPack.getSendName());
//        //接受红包的用户
//        //用户在wxappid下的openid，服务商模式下可填入msgappid下的openid。
//        paramMap.put("re_openid", wxRedPack.getReOpenId());
//        //付款金额 单位分
//        paramMap.put("total_amount", "" + wxRedPack.getTotalAmount());
//        //红包发放人数
//        paramMap.put("total_num", "" + wxRedPack.getTotalNum());
//        //红包祝福语
//        paramMap.put("wishing", wxRedPack.getWishing());
//        //调用接口的机器Ip地址(普通红包必填)
//        if (null != wxRedPack.getClientIp())
//            paramMap.put("client_ip", wxRedPack.getClientIp());
//        //红包分配规则(裂变红包必填)
//        if(null != wxRedPack.getAmtType())
//            paramMap.put("amt_type", wxRedPack.getAmtType());
//        //活动名称length=32
//        paramMap.put("act_name", wxRedPack.getActName());
//        //备注信息256
//        paramMap.put("remark", wxRedPack.getRemark());
//        //发放红包使用场景，红包金额大于200或者小于1元时必传
//        // PRODUCT_1:商品促销
//        // PRODUCT_2:抽奖
//        // PRODUCT_3:虚拟物品兑奖
//        //PRODUCT_4:企业内部福利
//        //PRODUCT_5:渠道分润
//        //PRODUCT_6:保险回馈
//        //PRODUCT_7:彩票派奖
//        //PRODUCT_8:税务刮奖
//        if(null != wxRedPack.getSceneId())
//            paramMap.put("scene_id", wxRedPack.getSceneId());
//        //金额必须为整数  单位为转换为分
//        //支付成功后，回调地址
//        return paramMap;
//    }

    /**
     * 发放裂变红包
     * <p>
     * 发放规则
     * 裂变红包：一次可以发放一组红包。首先领取的用户为种子用户，种子用户领取一组红包当中的一个，并可以通过社交分享将剩下的红包给其他用户。裂变红包充分利用了人际传播的优势。
     * <p>
     * 注意事项：
     * ◆ 当返回错误码为“SYSTEMERROR”时，请不要更换商户订单号，一定要使用原商户订单号重试，否则可能造成重复发放红包等资金风险。
     * ◆ XML具有可扩展性，因此返回参数可能会有新增，而且顺序可能不完全遵循此文档规范，如果在解析回包的时候发生错误，请商户务必不要换单重试，请商户联系客服确认红包发放情况。如果有新回包字段，会更新到此API文档中。
     * ◆ 因为错误代码字段err_code的值后续可能会增加，所以商户如果遇到回包返回新的错误码，请商户务必不要换单重试，请商户联系客服确认红包发放情况。如果有新的错误码，会更新到此API文档中。
     * ◆ 错误代码描述字段err_code_des只供人工定位问题时做参考，系统实现时请不要依赖这个字段来做自动化处理。
     * ◆ 请商户在自身的系统中合理设置发放频次并做好并发控制，防范错付风险。
     * ◆ 因商户自身系统设置存在问题导致的资金损失，由商户自行承担。
     * <p>
     * 消息触达规则
     * 现金红包发放后会以公众号消息的形式触达用户，不同情况下触达消息的形式会有差别，规则如下：
     * <p>
     * 是否关注	关注时间	是否接收消息	触达消息
     * 否	/	/	模版消息
     * 是	<=50小时	是	模版消息
     * 是	<=50小时	否	模版消息
     * 是	>50小时	是	防伪消息
     * 是	>50小时	否	模版消息
     *
     * @param map
     * @return
     * @throws Exception
     */
    public final static JSONObject redPackGroupPay(Map<String, String> map) throws Exception {
        WXPayConfig wxPayConfig = new WXPayConfig(SignType.MD5.name(), true);
        wxPayConfig.setUrl(WXPayConstants.getRePackGroupPayUrl());
        WXPayRequest wxPayRequest = new WXPayRequest(wxPayConfig);
        Map<String, Object> result = wxPayRequest.requestCert(map);
        System.out.println("调用结果=" + result.toString());

        if (result.get("return_code") != null
                && result.get("return_code").equals("SUCCESS")
                && result.get("result_code") != null
                && result.get("result_code").equals("SUCCESS")
        ) {
            System.out.println("调用成功=" + result.toString());
            return JsonUtil.get(true, result.get("result_code").toString(), "调用成功", result);
        } else {
            System.out.println("调用失败");
            return JsonUtil.get(false, result.get("result_code").toString(), "调用失败");
        }
    }

    /**
     * 红包查询
     * @param billNo
     * @return
     * @throws Exception
     */
    public final static JSONObject query(String billNo) throws Exception {
        Map<String, String> payData = queryData(billNo);
        WXPayConfig wxPayConfig = new WXPayConfig(SignType.MD5.name(), true);
        wxPayConfig.setUrl(WXPayConstants.getRePackPayQueryUrl());
        WXPayRequest wxPayRequest = new WXPayRequest(wxPayConfig);
        Map<String, Object> result = wxPayRequest.requestCert(payData);
        System.out.println("调用结果=" + result.toString());

        if (result.get("return_code") != null
                && result.get("return_code").equals("SUCCESS")
                && result.get("result_code") != null
                && result.get("result_code").equals("SUCCESS")
        ) {
            System.out.println("调用成功=" + result.toString());
            return JsonUtil.get(true, result.get("result_code").toString(), "调用成功", result);
        } else {
            System.out.println("调用失败");
            return JsonUtil.get(false, result.get("result_code").toString(), "调用失败");
        }
    }





    //李唐的商户号
    String subMchId = "1525006091";

    String subAppId = "wxd8de5d37b6976b55"; //


//    String openid = "olFJwwK3yub_nNNXXOhkTf07nYC0"; //老顾
    String openid = "olFJwwNOyw5v5OpMq-2Ex959r5is"; //wzg
//    String openid = "olFJwwHszqRbm0XLk_sTy0c20efA"; //李江涛
//   String openid = "olFJwwKShOk8S_lzZigjsPZ5WVSk";  //杨新福

    //测试普通红包
    @Test
    public void redPackTest() throws Exception {
        WxRedPackDto wxRedPackDto = new WxRedPackDto();
        wxRedPackDto.setActName("redbag");
        wxRedPackDto.setSendName("菠菜包");
        wxRedPackDto.setClientIp(FuncUtil.getLocalIp());
        wxRedPackDto.setRemark("恭喜发财");
        wxRedPackDto.setWishing("恭喜发财");
        wxRedPackDto.setReOpenId(openid);
        wxRedPackDto.setMsgAppId(WXPayConfig.getAppId());
        wxRedPackDto.setTotalAmount(new Double(30));
        wxRedPackDto.setTotalNum(1);

        String payRedpackOrderNo="2019121216320001";

//        wxRedPack.setSceneId("PRODUCT_2");
        Map<String, String> payData = redPackData(subMchId,payRedpackOrderNo,wxRedPackDto);
        System.out.println("result=" + XmlUtil.Map2Xml(payData));
        Map<String, String> result = redPackPay(payData);
        System.out.println("result=" + result.toString());
        //2019-11-21 09:55
        //{re_openid=olFJwwK3yub_nNNXXOhkTf07nYC0, total_amount=1000, total_num=10, err_code=PARAM_ERROR, return_msg=参数错误:total_num必须等于1, result_code=FAIL, err_code_des=参数错误:total_num必须等于1, mch_id=1513549201, return_code=SUCCESS, wxappid=wxd8de5d37b6976b55, mch_billno=1332313233233213}
        //{re_openid=olFJwwK3yub_nNNXXOhkTf07nYC0, total_amount=1000, err_code=CA_ERROR, return_msg=证书已过期, result_code=FAIL, err_code_des=证书已过期, mch_id=1513549201, return_code=SUCCESS, wxappid=wxd8de5d37b6976b55, mch_billno=1332313233233213}
        //调用结果={re_openid=olFJwwK3yub_nNNXXOhkTf07nYC0, total_amount=1000, err_code=SIGN_ERROR, return_msg=签名错误, result_code=FAIL, err_code_des=签名错误, mch_id=1513549201, return_code=SUCCESS, wxappid=wxd8de5d37b6976b55, mch_billno=1332313233233213}
        //{re_openid=olFJwwK3yub_nNNXXOhkTf07nYC0, total_amount=1000, err_code=SIGN_ERROR, return_msg=签名错误, result_code=FAIL, err_code_des=签名错误, mch_id=1513549201, return_code=SUCCESS, wxappid=wxa574b9142c67f42e, mch_billno=1332313233233213}
        //2019-12-07 10:26
        //{re_openid=olFJwwK3yub_nNNXXOhkTf07nYC0, total_amount=1000, err_code=PRODUCT_AUTHORITY_UNOPEN, return_msg=你的商户号未开通该产品权限，请联系管理员到产品中心开通。开通路径：产品中心-产品大全-现金红包-申请开通, result_code=FAIL, err_code_des=你的商户号未开通该产品权限，请联系管理员到产品中心开通。开通路径：产品中心-产品大全-现金红包-申请开通, mch_id=1513549201, return_code=SUCCESS, wxappid=wxa574b9142c67f42e, mch_billno=1332313233233213}
        //{re_openid=olFJwwK3yub_nNNXXOhkTf07nYC0, total_amount=1000, err_code=NO_AUTH, return_msg=IP地址非你在商户平台设置的可用IP地址, result_code=FAIL, err_code_des=IP地址非你在商户平台设置的可用IP地址, mch_id=1513549201, return_code=SUCCESS, wxappid=wxa574b9142c67f42e, mch_billno=1332313233233213}

        //2019-12-12 16:28
        //调用结果={re_openid=olFJwwK3yub_nNNXXOhkTf07nYC0, total_amount=1000, err_code=SUCCESS, return_msg=发放成功, result_code=SUCCESS, err_code_des=发放成功, mch_id=1513549201, send_listid=1000041701201912123000082277434, return_code=SUCCESS, wxappid=wxa574b9142c67f42e, mch_billno=1332313233233213}
        //调用结果={re_openid=olFJwwNOyw5v5OpMq-2Ex959r5is, total_amount=1000, err_code=FATAL_ERROR, return_msg=更换了openid，但商户单号未更新, result_code=FAIL, err_code_des=更换了openid，但商户单号未更新, mch_id=1513549201, return_code=SUCCESS, wxappid=wxa574b9142c67f42e, mch_billno=1332313233233213}
        //result={"success":true,"code":"SUCCESS","msg":"调用成功","data":{"re_openid":"olFJwwNOyw5v5OpMq-2Ex959r5is","total_amount":"1000","err_code":"SUCCESS","return_msg":"发放成功","result_code":"SUCCESS","err_code_des":"发放成功","mch_id":"1513549201","send_listid":"1000041701201912123000085246410","return_code":"SUCCESS","wxappid":"wxa574b9142c67f42e","mch_billno":"88888888"}}
        //result={"success":true,"code":"SUCCESS","msg":"调用成功","data":{"re_openid":"olFJwwNOyw5v5OpMq-2Ex959r5is","total_amount":"100","err_code":"SUCCESS","return_msg":"发放成功","result_code":"SUCCESS","err_code_des":"发放成功","mch_id":"1513549201","send_listid":"1000041701201912123000085230450","return_code":"SUCCESS","wxappid":"wxa574b9142c67f42e","mch_billno":"2019121216320001"}}
        //{re_openid=olFJwwKShOk8S_lzZigjsPZ5WVSk, total_amount=100, err_code=FATAL_ERROR, return_msg=更换了openid，但商户单号未更新, result_code=FAIL, err_code_des=更换了openid，但商户单号未更新, mch_id=1513549201, return_code=SUCCESS, wxappid=wxa574b9142c67f42e, mch_billno=2019121216320001}

        //2019-12-13 17:00
        //调用结果={re_openid=olFJwwNOyw5v5OpMq-2Ex959r5is, total_amount=3000, err_code=NO_AUTH, return_msg=IP地址非你在商户平台设置的可用IP地址, result_code=FAIL, err_code_des=IP地址非你在商户平台设置的可用IP地址, mch_id=1513549201, return_code=SUCCESS, wxappid=wxa574b9142c67f42e, mch_billno=2019121216320001}
    }

    //裂变红包
    @Test
    public void redPackGroupTest() throws Exception {
        WxRedPackDto wxRedPackDto = new WxRedPackDto();
        wxRedPackDto.setActName("redbag");
        wxRedPackDto.setSendName("菠菜包");
        wxRedPackDto.setAmtType("ALL_RAND");
        wxRedPackDto.setRemark("恭喜发财");
        wxRedPackDto.setWishing("恭喜发财");
        wxRedPackDto.setReOpenId(openid);
        wxRedPackDto.setMsgAppId(WXPayConfig.getAppId());
        //Integer.parseInt(""+Math.round(10 * 100))
        wxRedPackDto.setTotalAmount(new Double(0.3));
        wxRedPackDto.setTotalNum(3);
//        wxRedPack.setSceneId("PRODUCT_2");
        String payRedpackOrderNo="2019121216320002";
        Map<String, String> payData = redPackData(subMchId,payRedpackOrderNo,wxRedPackDto);

        Map<String, String> result = redPackGroupPay(payData);
        System.out.println("result=" + result.toString());
    }



    @Test
    public void query() throws Exception {

//        String billNo="1332313233233213";
//        String billNo="88888888";
        String billNo="2019121216320001";

//        Map<String,Object> result = query(billNo);
//        System.out.println("result=" + result.toString());

        //2019-12-12 17:14
        //调用结果={send_type=API, err_code=SUCCESS, return_msg=OK,
        // err_code_des=OK, hb_type=NORMAL, mch_id=1513549201,
        // detail_id=1000041701201912123000082277434, mch_billno=1332313233233213,
        // send_time=2019-12-12 16:29:44, total_amount=1000, total_num=1, hblist=
        //, result_code=SUCCESS, return_code=SUCCESS, status=RECEIVED}

        //result={"success":true,"code":"SUCCESS","msg":"调用成功","data":{"send_type":"API","err_code":"SUCCESS","return_msg":"OK","err_code_des":"OK","hb_type":"NORMAL","mch_id":"1513549201","detail_id":"1000041701201912123000085246410","mch_billno":"88888888","send_time":"2019-12-12 16:32:19","total_amount":"1000","total_num":"1","hblist":"\n","result_code":"SUCCESS","return_code":"SUCCESS","status":"RECEIVED"}}
        //result={"success":true,"code":"SUCCESS","msg":"调用成功","data":{"send_type":"API","err_code":"SUCCESS","return_msg":"OK","err_code_des":"OK","hb_type":"NORMAL","mch_id":"1513549201","detail_id":"1000041701201912123000085230450","mch_billno":"2019121216320001","send_time":"2019-12-12 16:35:59","total_amount":"100","total_num":"1","result_code":"SUCCESS","return_code":"SUCCESS","status":"SENT"}}

        //2019-12-13 16:56
        //调用结果={send_type=API, err_code=SUCCESS, return_msg=OK, err_code_des=OK, hb_type=NORMAL,
        // mch_id=1513549201, detail_id=1000041701201912123000085230450, mch_billno=2019121216320001,
        // send_time=2019-12-12 16:35:59, total_amount=100, refund_time=2019-12-13 16:51:38,
        // total_num=1, refund_amount=100, result_code=SUCCESS, return_code=SUCCESS, status=REFUND}

        //result={"success":true,"code":"SUCCESS","msg":"调用成功",
        // "data":{"send_type":"API","err_code":"SUCCESS","return_msg":"OK","err_code_des":"OK",
        // "hb_type":"NORMAL","mch_id":"1513549201","detail_id":"1000041701201912123000085230450",
        // "mch_billno":"2019121216320001","send_time":"2019-12-12 16:35:59","total_amount":"100",
        // "refund_time":"2019-12-13 16:51:38","total_num":"1","refund_amount":"100","result_code":"SUCCESS",
        // "return_code":"SUCCESS","status":"REFUND"}}
    }

}
