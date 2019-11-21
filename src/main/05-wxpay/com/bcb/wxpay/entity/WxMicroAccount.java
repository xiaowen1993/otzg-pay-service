package com.bcb.wxpay.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 小微商户基本信息
 */
@Entity
@Table
public class WxMicroAccount implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 业务申请编号
     * 服务商自定义的商户唯一编号。每个编号对应一个申请单，每个申请单审核通过后会生成一个微信支付商户号。
     */
    @Column(name = "business_code",nullable = false,length = 32)
    String business_code;
    //身份证人像面照片
    @Column(name = "id_card_copy",nullable = false,length = 256)
    String id_card_copy;
    //身份证国徽面照片
    @Column(name = "id_card_national",nullable = false,length = 256)
    String id_card_national;
    //身份证姓名
    @Column(name = "id_card_name",nullable = false,length = 64)
    String id_card_name;
    //身份证号码
    @Column(name = "id_card_number",nullable = false,length = 18)
    String id_card_number;
    //身份证有效期限
    @Column(name = "id_card_valid_time",nullable = false,length = 50)
    String id_card_valid_time;
    //开户名称
    @Column(name = "account_name",nullable = false,length = 50)
    String account_name;
    //开户银行
    @Column(name = "account_bank",nullable = false,length = 50)
    String account_bank;

    //开户银行省市编码
    @Column(name = "bank_address_code",nullable = false,length = 6)
    String bank_address_code;
    //银行账号
    @Column(name = "account_number",nullable = false,length = 50)
    String account_number;
    //门店名称
    @Column(name = "store_name",nullable = false,length = 128)
    String store_name;
    //门店省市编码
    @Column(name = "store_address_code",nullable = false,length = 6)
    String store_address_code;
    //门店街道名称
    @Column(name = "store_address",nullable = false,length = 500)
    String store_street;
    //门店门口照片
    @Column(name = "store_entrance_pic",nullable = false,length = 256)
    String store_entrance_pic;
    //店内环境照片
    @Column(name = "indoor_pic",nullable = false,length = 256)
    String indoor_pic;
    //商户简称
    @Column(name = "merchant_shortname",nullable = false,length = 50)
    String merchant_shortname;
    //客服电话
    @Column(name = "service_phone",nullable = false,length = 50)
    String service_phone;
    //售卖商品/提供服务描述
    @Column(name = "product_desc",nullable = false,length = 50)
    String product_desc;
    //费率
    @Column(name = "rate",nullable = false,length = 50)
    String rate;
    //超级管理员姓名
    @Column(name = "contact",nullable = false,length = 50)
    String contact;
    //手机号
    @Column(name = "contact_phone",nullable = false,length = 50)
    String contact_phone;

    //联系邮箱
    @Column(name = "contact_email",length = 50)
    String contact_email;

    //银行账号
    @Column(name = "bank_name",length = 256)
    String bank_name;

    //创建时间
    @Column(name = "create_time", nullable = false, length = 19)
    Date createTime;
    //修改时间
    @Column(name = "update_time", nullable = false, length = 19)
    Date updateTime;
    //账户状态{-1:删除,0:冻结,1:正常}
    @Column(name = "status",nullable = false,length = 1)
    Integer status=1;

    public WxMicroAccount() {
    }

    public String getBusiness_code() {
        return business_code;
    }

    public void setBusiness_code(String business_code) {
        this.business_code = business_code;
    }

    public String getId_card_copy() {
        return id_card_copy;
    }

    public void setId_card_copy(String id_card_copy) {
        this.id_card_copy = id_card_copy;
    }

    public String getId_card_national() {
        return id_card_national;
    }

    public void setId_card_national(String id_card_national) {
        this.id_card_national = id_card_national;
    }

    public String getId_card_name() {
        return id_card_name;
    }

    public void setId_card_name(String id_card_name) {
        this.id_card_name = id_card_name;
    }

    public String getId_card_number() {
        return id_card_number;
    }

    public void setId_card_number(String id_card_number) {
        this.id_card_number = id_card_number;
    }

    public String getId_card_valid_time() {
        return id_card_valid_time;
    }

    public void setId_card_valid_time(String id_card_valid_time) {
        this.id_card_valid_time = id_card_valid_time;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getAccount_bank() {
        return account_bank;
    }

    public void setAccount_bank(String account_bank) {
        this.account_bank = account_bank;
    }

    public String getBank_address_code() {
        return bank_address_code;
    }

    public void setBank_address_code(String bank_address_code) {
        this.bank_address_code = bank_address_code;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getStore_address_code() {
        return store_address_code;
    }

    public void setStore_address_code(String store_address_code) {
        this.store_address_code = store_address_code;
    }

    public String getStore_street() {
        return store_street;
    }

    public void setStore_street(String store_street) {
        this.store_street = store_street;
    }

    public String getStore_entrance_pic() {
        return store_entrance_pic;
    }

    public void setStore_entrance_pic(String store_entrance_pic) {
        this.store_entrance_pic = store_entrance_pic;
    }

    public String getIndoor_pic() {
        return indoor_pic;
    }

    public void setIndoor_pic(String indoor_pic) {
        this.indoor_pic = indoor_pic;
    }

    public String getMerchant_shortname() {
        return merchant_shortname;
    }

    public void setMerchant_shortname(String merchant_shortname) {
        this.merchant_shortname = merchant_shortname;
    }

    public String getService_phone() {
        return service_phone;
    }

    public void setService_phone(String service_phone) {
        this.service_phone = service_phone;
    }

    public String getProduct_desc() {
        return product_desc;
    }

    public void setProduct_desc(String product_desc) {
        this.product_desc = product_desc;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContact_email() {
        return contact_email;
    }

    public void setContact_email(String contact_email) {
        this.contact_email = contact_email;
    }

    public String getContact_phone() {
        return contact_phone;
    }

    public void setContact_phone(String contact_phone) {
        this.contact_phone = contact_phone;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
