package com.li.wisdomcashier.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.*;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.li.wisdomcashier.annotation.RedissonLock;
import com.li.wisdomcashier.entry.R;
import com.li.wisdomcashier.entry.dto.AliPayDTO;
import com.li.wisdomcashier.entry.dto.PayDTO;
import com.li.wisdomcashier.entry.dto.RefundDTO;
import com.li.wisdomcashier.entry.po.QueryTrade;
import com.li.wisdomcashier.entry.po.Shop;
import com.li.wisdomcashier.entry.po.SysPay;
import com.li.wisdomcashier.mapper.ShopMapper;
import com.li.wisdomcashier.mapper.SysPayMapper;
import com.li.wisdomcashier.service.AlipayService;
import com.li.wisdomcashier.utils.CommonUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName PayServiceImpl
 * @Description TODO
 * @Author Nine
 * @Date 2023/3/4 16:32
 * @Version 1.0
 */
@Service
@Slf4j
@DubboService
public class AlipayServiceImpl implements AlipayService {
    @Resource
    private SysPayMapper sysPayMapper;

    @Resource
    private ShopMapper shopMapper;

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
    @RedissonLock(keyPrefix = "ALIPAY:", key = "#aliPayDTO.operatorId", time = 3000)
    public R<PayDTO> aliPay(AliPayDTO aliPayDTO) {
        Shop shop = shopMapper.selectOne(Wrappers.lambdaQuery(Shop.class).eq(Shop::getId, aliPayDTO.getShopId()));
        AlipayTradePayRequest request = new AlipayTradePayRequest();
        AlipayTradePayModel model = new AlipayTradePayModel();
        model.setOutTradeNo(aliPayDTO.getId());
        model.setSubject(shop.getShopName() + "消费");
        //订单金额，精确到小数点后两位
        model.setTotalAmount(aliPayDTO.getPrice());
        //订单描述
        model.setBody(shop.getShopName() + "购物支付");
        //付款用户
        model.setAuthCode(aliPayDTO.getUserID());
        //操作员
        model.setOperatorId(aliPayDTO.getOperatorId());
        // 1分钟有效
        model.setTimeoutExpress("1m");
        request.setBizModel(model);
        //代调用商户支付接口
        request.putOtherTextParam("app_auth_token", shop.getAuthZfb());
        /** 异步通知地址，以http或者https开头的，商户外网可以post访问的异步地址，用于接收支付宝返回的支付结果，如果未收到该通知可参考该文档进行确认：			https://opensupport.alipay.com/support/helpcenter/193/201602475759 **/
        request.setNotifyUrl(payInfo.getNotifyUrl());
        AlipayTradePayResponse response = null;
        PayDTO payDTO = new PayDTO();
        try {
            response = alipayClient.execute(request);
            payDTO.setRemoteID(response.getTradeNo());
            payDTO.setShopID(shop.getId().toString());
            payDTO.setMsg(response.getSubMsg());
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return R.ok(payDTO, response.getCode());
    }

    @Override
    public R<String> queryAliPay(String tradeNo) {
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        AlipayTradeQueryModel model = new AlipayTradeQueryModel();
        model.setTradeNo(tradeNo);
        request.setBizModel(model);
        AlipayTradeQueryResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        log.info("{}支付状态：{}", tradeNo, response.getTradeStatus());
        return R.ok(response.getTradeStatus(), response.getCode());
    }

    @Override
    public R<String> cancelPay(String tradeNo, Long sid) {
        AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();
        AlipayTradeCancelModel model = new AlipayTradeCancelModel();
        model.setTradeNo(tradeNo);
        request.setBizModel(model);
        AlipayTradeCancelResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            log.error("交易撤销失败！撤销单号{},返回值{}", tradeNo, response.toString());
            return R.error("撤销失败！请重试！");
        }
        return R.ok("撤销成功！");
    }

    @Override
    public R<String> closePay(String tradeNo, Long sid) {
        AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
        AlipayTradeCloseModel model = new AlipayTradeCloseModel();
        model.setTradeNo(tradeNo);
        request.setBizModel(model);
        AlipayTradeCloseResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            log.error("交易停止失败！停止单号{},返回值{}", tradeNo, response.toString());
            return R.error("停止失败！请重试！");
        }
        return R.ok("停止成功！");
    }

    @Override
    public QueryTrade queryPayDetail(String tradeNo) {
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        AlipayTradeQueryModel model = new AlipayTradeQueryModel();
        model.setTradeNo(tradeNo);
        request.setBizModel(model);
        AlipayTradeQueryResponse response = null;
        QueryTrade queryTrade = new QueryTrade();
        try {
            response = alipayClient.execute(request);
            queryTrade.setSum(response.getTotalAmount());
            queryTrade.setPayUserId(response.getBuyerUserId());
            queryTrade.setTradeNo(response.getOutTradeNo());
            queryTrade.setPayUser(response.getBuyerLogonId());
            queryTrade.setRemoteNo(response.getTradeNo());
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return queryTrade;
    }

    @Override
    public R<String> refundPay(RefundDTO refundDTO) {
//        //检查是否有现金退款
//        TradeRefund tradeRefund = new TradeRefund();
//        User user = UserUtils.getUser();
//        Trade trade = tradeMapper.selectById(Long.parseLong(refundDTO.getTradeNo()));
//        List<TradeRefund> tradeRefunds = tradeRefundMapper.selectList(Wrappers.lambdaQuery(TradeRefund.class).eq(TradeRefund::getSid, Long.parseLong(refundDTO.getTradeNo())));
//        BigDecimal reduce = tradeRefunds.stream().filter(e ->
//                e.getStatus() == 1
//        ).map(TradeRefund::getMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
//        tradeRefund.setMsg(refundDTO.getMsg());
//        tradeRefund.setSid(Long.parseLong(refundDTO.getTradeNo()));
//        tradeRefund.setNo(refundDTO.getNo());
//        tradeRefund.setMoney(new BigDecimal(refundDTO.getMoney()));
//        tradeRefund.setCtrateTime(LocalDateTime.now());
//        tradeRefund.setOperater(user.getId());
//        tradeRefund.setType(2);
//        tradeRefund.setCtrateTime(LocalDateTime.now());
//        if (reduce.add(new BigDecimal(refundDTO.getMoney())).compareTo(trade.getIncome()) > 0) {
//            tradeRefund.setStatus(0);
//            tradeRefund.setErrMsg("总退款金额大于付款！");
//            //退款失败记录
//            tradeRefundService.TradeRefundRecord(tradeRefund);
//            return R.error("总退款金额大于付款！");
//        }
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        AlipayTradeRefundModel model = new AlipayTradeRefundModel();
        model.setOutTradeNo(refundDTO.getTradeNo());
        model.setRefundAmount(refundDTO.getMoney());
        model.setRefundReason(refundDTO.getMsg());
        model.setOutRequestNo(refundDTO.getNo());
        request.setBizModel(model);
        AlipayTradeRefundResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            log.error("交易退款失败，订单号：{},Err:{}", refundDTO.getTradeNo(), e.getErrMsg());
        }
        return R.ok(response.getCode(), response.getFundChange());
    }

    @Override
    public R<String> queryRefund(RefundDTO refundDTO) {
        AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
        AlipayTradeFastpayRefundQueryModel model = new AlipayTradeFastpayRefundQueryModel();
        model.setOutTradeNo(refundDTO.getTradeNo());
        model.setOutRequestNo(refundDTO.getNo());
        request.setBizModel(model);
        AlipayTradeFastpayRefundQueryResponse response = null;
        try {
            response = alipayClient.execute(request);
            if (response.getRefundStatus().compareTo("REFUND_SUCCESS") != 0) {
                return R.error("退款失败！");
            }
        } catch (AlipayApiException e) {
            log.info("退款查询失败，订单号：{}，errMsg:{}", refundDTO.getTradeNo(), response.getSubMsg());
        }
        return R.ok("退款成功！");
    }
}
