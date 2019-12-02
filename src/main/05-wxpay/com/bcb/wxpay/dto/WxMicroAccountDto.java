package com.bcb.wxpay.dto;

import java.io.Serializable;

/**
 * @Author G.
 * @Date 2019/11/30 0030 上午 11:30
 */
public class WxMicroAccountDto implements Serializable {

    /**
     * 身份证人像面照片
     * 必填
     * length(256)
     */
    String idCardCopy;
    /**
     * 身份证国徽面照片
     * 必填
     * length(256)
     */
    String idCardNational;
    /**
     * 身份证姓名
     * 必填
     * length(64)
     */
    String idCardName;
    /**
     * 身份证号码
     * 必填
     * length(18)
     */
    String idCardNumber;
    /**
     * 身份证有效期限
     * 例子："[\"1970-01-01\",\"长期\"]"
     * 必填
     * length(50)
     */
    String idCardValidTime;
    /**
     * 开户名称
     * 必填
     * length(50)
     */
    String accountName;
    /**
     * 开户银行
     * 必填
     * length(50)
     */
    String accountBank;

    /**
     * 开户银行省市编码
     * 必填
     * length(6)
     */
    String bankAddressCode;
    /**
     * 银行账号
     * 必填
     * length(50)
     */
    String accountNumber;
    /**
     * 门店名称
     * 必填
     * length(128)
     */
    String storeName;
    /**
     * 门店省市编码
     * 例子："110000"
     * 必填
     * length(6)
     */
    String storeAddressCode;
    /**
     * 门店街道名称
     * 必填
     * length(500)
     */
    String storeStreet;
    /**
     * 门店门口照片
     * 必填
     * length(256)
     */
    String storeEntrancePic;
    /**
     * 店内环境照片
     * 必填
     * length(256)
     */
    String indoorPic;
    /**
     * 商户简称
     * 必填
     * length(50)
     */
    String merchantShortName;
    /**
     * 客服电话
     * 必填
     * length(50)
     */
    String servicePhone;
    /**
     * 售卖商品/提供服务描述
     * 必填
     * length(50)
     */
    String productDesc;
    /**
     * 费率
     * 必填
     * length(50)
     */
    String rate;
    /**
     * 超级管理员姓名
     * 例子："0.6%"
     * 必填
     * length(50)
     */
    String contact;
    /**
     * 手机号
     * 必填
     * length(50)
     */
    String contactPhone;

    /**
     * 联系邮箱
     * 非必填
     * length(50)
     */
    String contactEmail;

    /**
     * 开户银行全称（含支行）
     * 非必填
     * length(256)
     */
    String bankName;

    public WxMicroAccountDto() {
    }


    public String getIdCardCopy() {
        return idCardCopy;
    }

    public void setIdCardCopy(String idCardCopy) {
        this.idCardCopy = idCardCopy;
    }

    public String getIdCardNational() {
        return idCardNational;
    }

    public void setIdCardNational(String idCardNational) {
        this.idCardNational = idCardNational;
    }

    public String getIdCardName() {
        return idCardName;
    }

    public void setIdCardName(String idCardName) {
        this.idCardName = idCardName;
    }

    public String getIdCardNumber() {
        return idCardNumber;
    }

    public void setIdCardNumber(String idCardNumber) {
        this.idCardNumber = idCardNumber;
    }

    public String getIdCardValidTime() {
        return idCardValidTime;
    }

    public void setIdCardValidTime(String idCardValidTime) {
        this.idCardValidTime = idCardValidTime;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountBank() {
        return accountBank;
    }

    public void setAccountBank(String accountBank) {
        this.accountBank = accountBank;
    }

    public String getBankAddressCode() {
        return bankAddressCode;
    }

    public void setBankAddressCode(String bankAddressCode) {
        this.bankAddressCode = bankAddressCode;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreAddressCode() {
        return storeAddressCode;
    }

    public void setStoreAddressCode(String storeAddressCode) {
        this.storeAddressCode = storeAddressCode;
    }

    public String getStoreStreet() {
        return storeStreet;
    }

    public void setStoreStreet(String storeStreet) {
        this.storeStreet = storeStreet;
    }

    public String getStoreEntrancePic() {
        return storeEntrancePic;
    }

    public void setStoreEntrancePic(String storeEntrancePic) {
        this.storeEntrancePic = storeEntrancePic;
    }

    public String getIndoorPic() {
        return indoorPic;
    }

    public void setIndoorPic(String indoorPic) {
        this.indoorPic = indoorPic;
    }

    public String getMerchantShortName() {
        return merchantShortName;
    }

    public void setMerchantShortName(String merchantShortName) {
        this.merchantShortName = merchantShortName;
    }

    public String getServicePhone() {
        return servicePhone;
    }

    public void setServicePhone(String servicePhone) {
        this.servicePhone = servicePhone;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
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

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}
