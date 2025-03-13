package com.li.wisdomcashier.config.wxPay;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.cert.CertificatesManager;
import com.wechat.pay.contrib.apache.httpclient.exception.HttpCodeException;
import com.wechat.pay.contrib.apache.httpclient.exception.NotFoundException;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;

/**
 * @ClassName WechatPayHttpClient.java
 * @Description 微信支付远程调用对象
 */
@Slf4j
@Data
@NoArgsConstructor
public class WechatPayHttpClient {

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

    //商户公钥
    String publicKey;

    //商户公钥id
    String publicKeyId;


    @Builder
    public WechatPayHttpClient(String mchId,
                               String privateKey,
                               String mchSerialNo,
                               String apiV3Key,
                               String domain,
                               String publicKey,
                               String publicKeyId) {
        this.mchId = mchId;
        this.privateKey = privateKey;
        this.mchSerialNo = mchSerialNo;
        this.apiV3Key = apiV3Key;
        this.domain = domain;
            this.publicKey = publicKey;
            this.publicKeyId = publicKeyId;
    }


    /***
     * @description 构建CloseableHttpClient远程请求对象
     * @return: org.apache.http.impl.client.CloseableHttpClient
     */
    private CloseableHttpClient createHttpClient() throws UnsupportedEncodingException {
            Verifier verifier = null;
        // 加载商户私钥（privateKey：私钥字符串）
        PrivateKey merchantPrivateKey = PemUtil
                .loadPrivateKey(new ByteArrayInputStream(privateKey.getBytes("utf-8")));
            if(StringUtils.isBlank(publicKeyId)) {
        // 获取证书管理器实例
        CertificatesManager certificatesManager = CertificatesManager.getInstance();
        // 向证书管理器增加需要自动更新平台证书的商户信息
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
            log.error("微信支付请求异常：{}", e.getMessage());
            return null;
        }
        try {
            verifier = certificatesManager.getVerifier(mchId);
        } catch (NotFoundException e) {
            log.error("微信支付验证者创建异常：{}",  e.getMessage());
            return null;
        }
            }else{
                //公钥模式验签
                verifier = new PubKeyVerifier(publicKeyId,publicKey);
            }
        // 初始化httpClient
        return com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder.create()
                .withMerchant(mchId, mchSerialNo, merchantPrivateKey)
                .withValidator(new WechatPay2Validator(verifier))
                .build();
    }

    /***
     * @description 支持post请求的远程调用
     * @param params 携带请求参数
     * @return  返回字符串
     */
    public String doPost(ObjectNode params) throws IOException {
        HttpPost httpPost = new HttpPost("https://"+domain);
        httpPost.addHeader("Accept", "application/json");
        httpPost.addHeader("Content-type","application/json; charset=utf-8");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(bos, params);

        httpPost.setEntity(new StringEntity(bos.toString("UTF-8"), "UTF-8"));
        CloseableHttpResponse response = this.createHttpClient().execute(httpPost);
        return EntityUtils.toString(response.getEntity());
    }

    /***
     * @description 支持Patch请求的远程调用
     * @param params 携带请求参数
     * @return  返回字符串
     */
    public String doPatch(ObjectNode params) throws IOException {
        HttpPatch httpPatch = new HttpPatch("https://"+domain);
        httpPatch.addHeader("Accept", "application/json");
        httpPatch.addHeader("Content-type","application/json; charset=utf-8");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(bos, params);

        httpPatch.setEntity(new StringEntity(bos.toString("UTF-8"), "UTF-8"));
        CloseableHttpResponse response = this.createHttpClient().execute(httpPatch);
        return EntityUtils.toString(response.getEntity());
    }

    /***
     * @description 支持Patch请求的远程调用
     * @param params 携带请求参数
     * @return  返回字符串
     */
    public String doPut(ObjectNode params) throws IOException {
        HttpPut httpPut = new HttpPut("https://"+domain);
        httpPut.addHeader("Accept", "application/json");
        httpPut.addHeader("Content-type","application/json; charset=utf-8");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(bos, params);

        httpPut.setEntity(new StringEntity(bos.toString("UTF-8"), "UTF-8"));
        CloseableHttpResponse response = this.createHttpClient().execute(httpPut);
        return EntityUtils.toString(response.getEntity());
    }


    /***
     * @description 支持get请求的远程调用
     * @param param 在路径中请求的参数
     * @return
     * @return: 返回字符串
     */
    public String doDelete(String param) throws URISyntaxException, IOException {
        URIBuilder uriBuilder = new URIBuilder("https://"+domain+param);
        HttpDelete httpDelete = new HttpDelete(uriBuilder.build());
        CloseableHttpResponse response = this.createHttpClient().execute(httpDelete);
        return EntityUtils.toString(response.getEntity());
    }

    /***
     * @description 支持get请求的远程调用
     * @param param 在路径中请求的参数
     * @return
     * @return: 返回字符串
     */
    public String doGet(String param) throws URISyntaxException, IOException {
        URIBuilder uriBuilder = new URIBuilder("https://"+domain+param);
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        httpGet.addHeader("Accept", "application/json");
        CloseableHttpResponse response = this.createHttpClient().execute(httpGet);
        return EntityUtils.toString(response.getEntity());
    }
}