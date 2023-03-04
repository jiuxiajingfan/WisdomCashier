package com.li.wisdomcashier.base.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePayModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.entity.dto.AliPayDTO;
import com.li.wisdomcashier.base.service.AlipayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @ClassName PayServiceImpl
 * @Description TODO
 * @Author Nine
 * @Date 2023/3/4 16:32
 * @Version 1.0
 */
@Service
@Slf4j
public class AlipayServiceImpl implements AlipayService {

    //app ID
    @Value("${alipay.appId}")
    private  String APP_ID;

    //应用私钥
    @Value("${alipay.privateKey}")
    private  String APP_PRIVATE_KEY;

    //字符集
    @Value("${alipay.charset}")
    private  String CHARSET;

    // 支付宝公钥
    @Value("${alipay.alipaypublickey}")
    private  String ALIPAY_PUBLIC_KEY;

    //接口路径
    @Value("${alipay.serverUrl}")
    private String GATEWAY_URL;

    //返回格式
    @Value("${alipay.format}")
    private String FORMAT;

    //签名方式
    @Value("${alipay.signType}")
    private String SIGN_TYPE ;

    //支付宝异步通知路径,付款完毕后会异步调用本项目的方法,必须为公网地址
    private String NOTIFY_URL = "http://127.0.0.1/notifyUrl";

    //支付宝同步通知路径,也就是当付款完毕后跳转本项目的页面,可以不是公网地址
    private String RETURN_URL = "http://localhost:8080/returnUrl";

    @Override
    public R<String> aliPay(AliPayDTO aliPayDTO) {
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL,APP_ID,APP_PRIVATE_KEY,FORMAT,CHARSET,ALIPAY_PUBLIC_KEY,SIGN_TYPE);
        AlipayTradePayRequest request = new AlipayTradePayRequest();
//        AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
        AlipayTradePayModel model = new AlipayTradePayModel();
        /** 商户订单号，商户自定义，需保证在商户端不重复，如：20200612000001 **/
        model.setOutTradeNo(aliPayDTO.getId().toString());
        /**订单标题 **/
        model.setSubject(aliPayDTO.getShopName());
        /** 订单金额，精确到小数点后两位 **/
        model.setTotalAmount(String.format("%.2f",aliPayDTO.getPrice()));
        /** 订单描述 **/
        model.setBody(aliPayDTO.getShopName()+"购物支付");
        model.setAuthCode(aliPayDTO.getUserID());
        model.setOperatorId(aliPayDTO.getOperatorId());
        // 5分钟有效
        model.setTimeoutExpress("5m");
        request.setBizModel(model);

        /** 异步通知地址，以http或者https开头的，商户外网可以post访问的异步地址，用于接收支付宝返回的支付结果，如果未收到该通知可参考该文档进行确认：			https://opensupport.alipay.com/support/helpcenter/193/201602475759 **/
        request.setNotifyUrl(NOTIFY_URL);
        AlipayTradePayResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return R.ok(response.getTradeNo(),response.getCode());
    }

    @Override
    public R<String> queryAliPay(String tradeNo) {
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL,APP_ID,APP_PRIVATE_KEY,FORMAT,CHARSET,ALIPAY_PUBLIC_KEY,SIGN_TYPE);
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        AlipayTradeQueryModel model = new AlipayTradeQueryModel();
        model.setTradeNo(tradeNo);
        request.setBizModel(model);
        AlipayTradeQueryResponse response = null;
        try{
           response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return R.ok(response.getCode());
    }
}
