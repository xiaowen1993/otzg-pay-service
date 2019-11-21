package com.bcb.wxpay.util.service;

import com.bcb.util.JsonUtil;
import com.bcb.wxpay.entity.WxRedPack;
import com.bcb.wxpay.util.WxpayUtil;
import com.bcb.wxpay.util.sdk.WXPayConstants;
import com.bcb.wxpay.util.sdk.WXPayRequest;
import com.bcb.wxpay.util.sdk.WXPayUtil;
import net.sf.json.JSONObject;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 发红包
 */
public class RedPackSubmit {

    public final static Map<String, String> redPackData(WxRedPack wxRedPack) {

        Map<String, String> paramMap = new HashMap<>();
        //微信支付分配的子商户号，服务商模式下必填
        paramMap.put("sub_mch_id", wxRedPack.getSubMchId());
        //商户订单号
        paramMap.put("mch_billno", wxRedPack.getMchBillno());
        //随机数
        paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
        //微信分配的公众账号ID（企业号corpid即为此appId）。接口传入的所有appid应该为公众号的appid（在mp.weixin.qq.com申请的），不能为APP的appid（在open.weixin.qq.com申请的）。
        paramMap.put("wxappid", wxRedPack.getGzhAppId());
        //服务商模式下触达用户时的appid(可填服务商自己的appid或子商户的appid)，服务商模式下必填，服务商模式下填入的子商户appid必须在微信支付商户平台中先录入，否则会校验不过。
        paramMap.put("msgappid", wxRedPack.getSubAppId());
        //红包发送者名称 length=32 注意：敏感词会被转义成字符*
        paramMap.put("send_name", wxRedPack.getSendName());
        //接受红包的用户
        //用户在wxappid下的openid，服务商模式下可填入msgappid下的openid。
        paramMap.put("re_openid", wxRedPack.getReOpenId());
        //付款金额 单位分
        paramMap.put("total_amount", "" + wxRedPack.getTotalAmount());
        //红包发放人数
        paramMap.put("total_num", "" + wxRedPack.getTotalNum());
        //红包祝福语
        paramMap.put("wishing", wxRedPack.getWishing());
        //调用接口的机器Ip地址(普通红包必填)
        if (null != wxRedPack.getClientIp())
            paramMap.put("client_ip", wxRedPack.getClientIp());
        //红包分配规则(裂变红包必填)
        if(null != wxRedPack.getAmtType())
            paramMap.put("amt_type", wxRedPack.getAmtType());
        //活动名称length=32
        paramMap.put("act_name", wxRedPack.getActName());
        //备注信息256
        paramMap.put("remark", wxRedPack.getRemark());
        //场景id 放红包使用场景，红包金额大于200或者小于1元时必传
        paramMap.put("scene_id", wxRedPack.getSceneId());
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
        String certRootPath = "D:/workspace/bcb-pay/target/artifacts/bcb_pay_war_exploded";
        WXPayConfig wxPayConfig = new WxPayConfigImpl(SignType.MD5.name(), certRootPath);

        wxPayConfig.setUrl(WXPayConstants.getRePackPayUrl());
        WXPayRequest wxPayRequest = new WXPayRequest(wxPayConfig);
        Map<String, String> result = wxPayRequest.requestCert(map);
        System.out.println("调用成功=" + result.toString());

        if (result.get("return_code") != null
                && result.get("return_code").equals("SUCCESS")
                && result.get("result_code") != null
                && result.get("result_code").equals("SUCCESS")
        ) {
            System.out.println("调用成功=" + result.toString());
            return JsonUtil.get(true, result.get("result_code"), "调用成功", result);
        } else {
            System.out.println("调用失败");
            return JsonUtil.get(false, result.get("result_code"), "调用失败");
        }
    }

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
        String certRootPath = "D:/workspace/bcb-pay/target/artifacts/bcb_pay_war_exploded";
        WXPayConfig wxPayConfig = new WxPayConfigImpl(SignType.MD5.name(), certRootPath);
        wxPayConfig.setUrl(WXPayConstants.getRePackGroupPayUrl());
        WXPayRequest wxPayRequest = new WXPayRequest(wxPayConfig);
        Map<String, String> result = wxPayRequest.requestCert(map);
        System.out.println("调用成功=" + result.toString());

        if (result.get("return_code") != null
                && result.get("return_code").equals("SUCCESS")
                && result.get("result_code") != null
                && result.get("result_code").equals("SUCCESS")
        ) {
            System.out.println("调用成功=" + result.toString());
            return JsonUtil.get(true, result.get("result_code"), "调用成功", result);
        } else {
            System.out.println("调用失败");
            return JsonUtil.get(false, result.get("result_code"), "调用失败");
        }
    }






    String subMchId = "1525006091";
    //    String subMchId = "1521225291";
    String subAppId = "wxd8de5d37b6976b55"; //


    String openid = "olFJwwK3yub_nNNXXOhkTf07nYC0";
//    String openid = "olFJwwNOyw5v5OpMq-2Ex959r5is"; //wzg


    //商户号下的公众号appid
    String gzhAppId = "wxd8de5d37b6976b55";

    //测试普通红包
    @Test
    public void redPackTest() throws Exception {
        WxRedPack wxRedPack = new WxRedPack();
        wxRedPack.setActName("新年红包");
        wxRedPack.setSendName("菠菜包");
        wxRedPack.setClientIp(WxpayUtil.localIp());
        wxRedPack.setGzhAppId(gzhAppId);
        wxRedPack.setMchBillno("1332313233233213");
        wxRedPack.setRemark("恭喜发财");
        wxRedPack.setWishing("恭喜发财");
        wxRedPack.setReOpenId(openid);
        wxRedPack.setSubAppId(subAppId);
        wxRedPack.setSubMchId(subMchId);
        wxRedPack.setTotalAmount(1000);
        wxRedPack.setTotalNum(1);
        wxRedPack.setSceneId("PRODUCT_2");
        Map<String, String> payData = redPackData(wxRedPack);

        Map<String, String> result = redPackPay(payData);
        System.out.println("result=" + result.toString());
        //2019-11-21 09:55
        //{re_openid=olFJwwK3yub_nNNXXOhkTf07nYC0, total_amount=1000, total_num=10, err_code=PARAM_ERROR, return_msg=参数错误:total_num必须等于1, result_code=FAIL, err_code_des=参数错误:total_num必须等于1, mch_id=1513549201, return_code=SUCCESS, wxappid=wxd8de5d37b6976b55, mch_billno=1332313233233213}
        //{re_openid=olFJwwK3yub_nNNXXOhkTf07nYC0, total_amount=1000, err_code=CA_ERROR, return_msg=证书已过期, result_code=FAIL, err_code_des=证书已过期, mch_id=1513549201, return_code=SUCCESS, wxappid=wxd8de5d37b6976b55, mch_billno=1332313233233213}
    }

    //裂变红包
    @Test
    public void redPackGroupTest() throws Exception {
        WxRedPack wxRedPack = new WxRedPack();
        wxRedPack.setActName("新年红包");
        wxRedPack.setSendName("菠菜包");
        wxRedPack.setAmtType("ALL_RAND");
        wxRedPack.setGzhAppId(gzhAppId);
        wxRedPack.setMchBillno("1332313233233213");
        wxRedPack.setRemark("恭喜发财");
        wxRedPack.setWishing("恭喜发财");
        wxRedPack.setReOpenId(openid);
        wxRedPack.setSubAppId(subAppId);
        wxRedPack.setSubMchId(subMchId);
        //Integer.parseInt(""+Math.round(10 * 100))
        wxRedPack.setTotalAmount(1000);
        wxRedPack.setTotalNum(3);
        wxRedPack.setSceneId("PRODUCT_2");
        Map<String, String> payData = redPackData(wxRedPack);

        Map<String, String> result = redPackGroupPay(payData);
        System.out.println("result=" + result.toString());
        //2019-11-21 10:34
        //{re_openid=olFJwwK3yub_nNNXXOhkTf07nYC0, total_amount=1000, err_code=PARAM_ERROR, return_msg=参数错误:total_num必须介于(包括)3到20之间., result_code=FAIL, err_code_des=参数错误:total_num必须介于(包括)3到20之间., mch_id=1513549201, return_code=SUCCESS, wxappid=wxd8de5d37b6976b55, mch_billno=1332313233233213}
        //2019-11-21 10:36
        //{re_openid=olFJwwK3yub_nNNXXOhkTf07nYC0, total_amount=1000, total_num=3, err_code=CA_ERROR, return_msg=证书已过期, result_code=FAIL, err_code_des=证书已过期, mch_id=1513549201, return_code=SUCCESS, wxappid=wxd8de5d37b6976b55, mch_billno=1332313233233213}
    }

}
