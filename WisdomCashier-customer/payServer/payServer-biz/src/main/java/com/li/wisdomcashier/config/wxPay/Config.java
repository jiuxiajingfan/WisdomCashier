package com.li.wisdomcashier.config.wxPay;

import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.cert.CertificatesManager;
import com.wechat.pay.contrib.apache.httpclient.exception.HttpCodeException;
import com.wechat.pay.contrib.apache.httpclient.exception.NotFoundException;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @ClassName Config.java
 * @Description 微信的配置文件
 */
@Slf4j
@Data
@Component
public class Config implements Serializable {

    //appId
    String appid;

    //商户号
    String mchId;

    //私钥字符串
    String privateKey;

    //商户证书序列号
    String mchSerialNo;

    //V3密钥
    String apiV3Key;

    //请求地址
    String domain;

    //回调地址
    String notifyUrl;

    String refundNotifyUrl;

    String publicKey;

    String publicKeyId;

    Verifier verifier;

    /**
     * 验证签名
     *
     * @param serialNo  微信平台-证书序列号
     * @param signStr   自己组装的签名串
     * @param signature 微信返回的签名
     */
    public boolean verifiedSign(String serialNo, String signStr, String signature) {
        return verifier.verify(serialNo, signStr.getBytes(StandardCharsets.UTF_8), signature);
    }

    /***
     * @description 构建客户端
     *
     * @param mchId  商户号
     * @param privateKey 私钥字符串
     * @param mchSerialNo 商户证书序列号
     * @param apiV3Key V3密钥
     */
    @Builder
    public Config(String appid,
                  String mchId,
                  String privateKey,
                  String mchSerialNo,
                  String apiV3Key,
                  String domain, String notifyUrl, String refundNotifyUrl,
                  String publicKey, String publicKeyId
    ) {
        this.appid = appid;
        this.mchId = mchId;
        this.privateKey = privateKey;
        this.mchSerialNo = mchSerialNo;
        this.apiV3Key = apiV3Key;
        this.domain=domain;
        this.notifyUrl = notifyUrl;
        this.refundNotifyUrl = refundNotifyUrl;
        this.publicKey = publicKey;
        this.publicKeyId = publicKeyId;
    }

    public Config() {
    }


    public PrivateKey getPrivateKeys() {
        return PemUtil
                .loadPrivateKey(new ByteArrayInputStream(privateKey.getBytes(StandardCharsets.UTF_8)));
    }

    public Verifier save() throws IOException {
        Verifier verifier;
        if(StringUtils.isBlank(publicKeyId)) {
        PrivateKey merchantPrivateKey = getPrivateKeys();
        // 获取证书管理器实例
        CertificatesManager certificatesManager = CertificatesManager.getInstance();
        try {
            certificatesManager.putMerchant(mchId, new WechatPay2Credentials(mchId,
                    new PrivateKeySigner(mchSerialNo, merchantPrivateKey)), apiV3Key.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            log.error("流处理异常：{}", e.getMessage());
            return null;
        } catch (GeneralSecurityException e) {
            log.error("微信支付权限异常：{}",  e.getMessage());
            return null;
        } catch (HttpCodeException e) {
            log.error("微信支付请求异常：{}",  e.getMessage());
            return null;
        }
        try {
            verifier = certificatesManager.getVerifier(mchId);
        } catch (NotFoundException e) {
            log.error("微信支付验证者创建异常：{}",  e.getMessage());
            return null;
        }
        }
        else{
            //公钥模式
            verifier = new PubKeyVerifier(publicKeyId,publicKey);
        }
        return verifier;
    }

    /**
     * 生成带签名支付信息
     *
     * @param timestamp 时间戳
     * @param nonceStr 随机字符串
     * @param prepayId 预付单
     * @return 支付信息
     */
    public String appPaySign(String timestamp, String nonceStr, String prepayId)throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        PrivateKey privateKey = getPrivateKeys();
        String signatureStr = Stream.of(appid, timestamp, nonceStr, prepayId)
                .collect(Collectors.joining("\n", "", "\n"));
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initSign(privateKey);
        sign.update(signatureStr.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(sign.sign());
    }
}
