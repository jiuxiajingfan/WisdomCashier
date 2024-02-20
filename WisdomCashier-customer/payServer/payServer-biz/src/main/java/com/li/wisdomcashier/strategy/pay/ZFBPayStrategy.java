package com.li.wisdomcashier.strategy.pay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeCancelModel;
import com.alipay.api.domain.AlipayTradePayModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.request.AlipayTradeCancelRequest;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.li.wisdomcashier.entry.R;
import com.li.wisdomcashier.entry.dto.PayDTO;
import com.li.wisdomcashier.entry.dto.PayInfo;
import com.li.wisdomcashier.entry.dto.PayVO;
import com.li.wisdomcashier.entry.dto.RefundDTO;
import com.li.wisdomcashier.entry.po.SysPay;
import com.li.wisdomcashier.entry.vo.StatusVO;
import com.li.wisdomcashier.enums.pay.PayEnums;
import com.li.wisdomcashier.mapper.SysPayMapper;
import com.li.wisdomcashier.utils.CommonUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ZFBPayStrategy extends AbstractPayStrategy {

    @Resource
    private SysPayMapper sysPayMapper;

    @Resource
    private Redisson redisson;

    @Value("${pay.ali.active}")
    private String activeName;

    private AlipayClient alipayClient;

    private SysPay payInfo;


    @PostConstruct
    void payInit() {
        List<SysPay> sysPays = sysPayMapper.selectList(null);
        sysPays.forEach(e -> {
            RBucket<SysPay> bucket = redisson.getBucket(e.getName());
            bucket.set(e);
            if (CommonUtils.compare(e.getName(), activeName)) {
                payInfo = e;
                alipayClient = new DefaultAlipayClient(
                        e.getAppUrl(),
                        e.getAppId(),
                        e.getAppPrivateKey(),
                        "json",
                        "utf-8",
                        e.getAppPublicKey(),
                        e.getSignType()
                );
            }
        });
    }

    @Override
    protected PayEnums getTypeEnum() {
        return PayEnums.ZFB;
    }

    @Override
    public PayVO pay(PayDTO dto) {
        AlipayTradePayRequest request = new AlipayTradePayRequest();
        AlipayTradePayModel model = new AlipayTradePayModel();
        model.setOutTradeNo(dto.getId());
        model.setSubject(dto.getShopName());
        //订单金额，精确到小数点后两位
        model.setTotalAmount(dto.getPrice());
        //订单描述
        model.setBody(dto.getShopName() + "购物支付");
        //付款用户
        model.setAuthCode(dto.getUserID());
        //操作员
        model.setOperatorId(dto.getOperatorId());
        // 1分钟有效
        model.setTimeoutExpress("1m");
        request.setBizModel(model);
        //代调用商户支付接口
        request.putOtherTextParam("app_auth_token", dto.getAuthToken());
        /** 异步通知地址，以http或者https开头的，商户外网可以post访问的异步地址，用于接收支付宝返回的支付结果，如果未收到该通知可参考该文档进行确认：			https://opensupport.alipay.com/support/helpcenter/193/201602475759 **/
        request.setNotifyUrl(payInfo.getNotifyUrl());
        AlipayTradePayResponse response;
        try {
            response = alipayClient.execute(request);
            return PayVO.builder()
                    .remoteID(response.getTradeNo())
                    .shopID(dto.getShopId())
                    .msg(response.getSubMsg())
                    .build();
        } catch (AlipayApiException e) {
            log.info("支付宝支付接口调用异常{}", e.getErrMsg());
        }
        return null;
    }

    @Override
    public StatusVO status(String tradeNo) {
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        AlipayTradeQueryModel model = new AlipayTradeQueryModel();
        model.setTradeNo(tradeNo);
        request.setBizModel(model);
        AlipayTradeQueryResponse response;
        try {
            response = alipayClient.execute(request);
            return StatusVO.builder()
                    .code(response.getCode())
                    .status(response.getTradeStatus())
                    .build();
        } catch (AlipayApiException e) {
            log.info("支付宝订单查询失败{}", e.getErrMsg());
        }
        return null;

    }

    @Override
    public String cancel(String tradeNo) {
        AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();
        AlipayTradeCancelModel model = new AlipayTradeCancelModel();
        model.setTradeNo(tradeNo);
        request.setBizModel(model);
        AlipayTradeCancelResponse response;
        try {
            response = alipayClient.execute(request);
            return response.getMsg();
        } catch (AlipayApiException e) {
            log.error("交易撤销失败！撤销单号{},返回值{}", tradeNo);
        }
        return null;
    }

    @Override
    public String close(String tradeNo) {
        return null;
    }

    @Override
    public PayInfo detail(String tradeNo) {
        return null;
    }

    @Override
    public String refund(RefundDTO refundDTO) {
        return null;
    }
}
