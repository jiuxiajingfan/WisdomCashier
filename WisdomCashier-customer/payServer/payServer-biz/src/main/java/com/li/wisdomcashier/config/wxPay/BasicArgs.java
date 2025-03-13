package com.li.wisdomcashier.config.wxPay;


import lombok.Data;

/**
 * @ClassName BasicArgs.java
 * @Description 基础请求参数
 */
@Data
public class BasicArgs {

    //appId
    public String appid;

    //商户号
    public String mchId;

    //私钥字符串
    public String privateKey;

    //商户证书序列号
    public String mchSerialNo;

    //V3密钥
    public String apiV3Key;

    //请求地址
    public String domain;

    //回调地址
    public String notifyUrl;

    //退款回调地址
    public String refundNotifyUrl;

    public String publicKey;

    public String publicKeyId;


    public BasicArgs(Config config){
        this.appid = config.getAppid();
        this.mchId = config.getMchId();
        this.privateKey = config.getPrivateKey();
        this.mchSerialNo = config.getMchSerialNo();
        this.apiV3Key = config.getApiV3Key();
        this.domain = config.getDomain();
        this.notifyUrl = config.getNotifyUrl();
        this.refundNotifyUrl = config.getRefundNotifyUrl();
        this.publicKey = config.getPublicKey();
        this.publicKeyId = config.getPublicKeyId();
    }
}
