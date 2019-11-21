package com.bcb.wxpay.entity;

import javax.persistence.*;
import java.io.Serializable;


/**
 * 微信红包
 * 1.红包分两种 a.普通红包，b.裂变红包
 * 2.普通红包 client_ip 为必填项
 * 3.裂变红包 amtType 为必填项 表示红包分配规则
 */
@Entity
@Table
public class WxRedPack implements Serializable {
    @Id
    String id;
    //子商户号
    @Column(name = "sub_mch_id",nullable = false,length = 32)
    String subMchId;

    //商户号
    @Column(name = "mch_id",nullable = false,length = 32)
    String mchId;

    //公众号appid
    @Column(name = "gzh_app_id",nullable = false,length = 32)
    String gzhAppId;
    //子商户appid
    @Column(name = "sub_app_id",nullable = false,length = 32)
    String subAppId;

    /**
     * 商户订单号
     * 微信规则：
     * 商户订单号（每个订单号必须唯一）
     * 组成：mch_id+yyyymmdd+10位一天内不能重复的数字。
     * 接口根据商户订单号支持重入，如出现超时可再调用。
     */
    @Column(name = "mch_billno",nullable = false,length = 28)
    String mchBillno;

    //商户名称
    @Column(name = "send_name",nullable = false,length = 32)
    String sendName;

    //用户openid
    @Column(name = "re_open_id",nullable = false,length = 32)
    String reOpenId;

    //红包祝福语
    @Column(name = "wishing",nullable = false,length = 128)
    String wishing;


    //发送者ip(普通红包必填项)
    @Column(name = "client_ip",length = 15)
    String clientIp;

    /**
     * 红包分配方式(裂变红包必填)
     *
     * 微信规则
     * 红包金额设置方式
     * ALL_RAND—全部随机,商户指定总金额和红包发放总人数，由微信支付随机计算出各红包金额
     */
    @Column(name = "amt_type",length = 32)
    String amtType;




    //活动名称
    @Column(name = "act_name",nullable = false,length = 32)
    String actName;
    //备注
    @Column(name = "remark",nullable = false,length = 256)
    String remark;
    //发放金额单位为分
    @Column(name = "total_amount",nullable = false,length = 8)
    Integer totalAmount;
    //红包发放总人数
    @Column(name = "total_num",nullable = false,length = 8)
    Integer totalNum;

    /**
     *  场景id发
     *      放红包使用场景，红包金额大于200或者小于1元时必传
     *     PRODUCT_1:商品促销
     *     PRODUCT_2:抽奖
     *     PRODUCT_3:虚拟物品兑奖
     *     PRODUCT_4:企业内部福利
     *     PRODUCT_5:渠道分润
     *     PRODUCT_6:保险回馈
     *     PRODUCT_7:彩票派奖
     *     PRODUCT_8:税务刮奖
     */
    @Column(name = "scene_id",nullable = false,length = 32)
    String sceneId;

    public WxRedPack() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubMchId() {
        return subMchId;
    }

    public void setSubMchId(String subMchId) {
        this.subMchId = subMchId;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getGzhAppId() {
        return gzhAppId;
    }

    public void setGzhAppId(String gzhAppId) {
        this.gzhAppId = gzhAppId;
    }

    public String getSubAppId() {
        return subAppId;
    }

    public void setSubAppId(String subAppId) {
        this.subAppId = subAppId;
    }

    public String getMchBillno() {
        return mchBillno;
    }

    public void setMchBillno(String mchBillno) {
        this.mchBillno = mchBillno;
    }

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    public String getReOpenId() {
        return reOpenId;
    }

    public void setReOpenId(String reOpenId) {
        this.reOpenId = reOpenId;
    }

    public String getWishing() {
        return wishing;
    }

    public void setWishing(String wishing) {
        this.wishing = wishing;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getActName() {
        return actName;
    }

    public void setActName(String actName) {
        this.actName = actName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public String getSceneId() {
        return sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }

    public String getAmtType() {
        return amtType;
    }

    public void setAmtType(String amtType) {
        this.amtType = amtType;
    }
}
