package com.li.wisdomcashier.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.*;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.li.wisdomcashier.entry.dto.AliPayDTO;
import com.li.wisdomcashier.entry.dto.PayDTO;
import com.li.wisdomcashier.entry.dto.RefundDTO;
import com.li.wisdomcashier.entry.po.QueryTrade;
import com.li.wisdomcashier.entry.po.Trade;
import com.li.wisdomcashier.entry.po.TradeRefund;
import com.li.wisdomcashier.mapper.ShopMapper;
import com.li.wisdomcashier.mapper.TradeMapper;
import com.li.wisdomcashier.mapper.TradeRefundMapper;
import com.li.wisdomcashier.service.AlipayService;
import com.li.wisdomcashier.service.TradeRefundService;
import com.li.wisdomcashier.utils.RedisUtils;
import com.li.wisdomcashier.entry.R;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
public class AlipayServiceImpl implements AlipayService {

    //app ID
    @Value("${alipay.appId}")
    private String APP_ID;

    //应用私钥
    @Value("${alipay.privateKey}")
    private String APP_PRIVATE_KEY;

    //字符集
    @Value("${alipay.charset}")
    private String CHARSET;

    // 支付宝公钥
    @Value("${alipay.alipaypublickey}")
    private String ALIPAY_PUBLIC_KEY;

    //接口路径
    @Value("${alipay.serverUrl}")
    private String GATEWAY_URL;

    //返回格式
    @Value("${alipay.format}")
    private String FORMAT;

    //签名方式
    @Value("${alipay.signType}")
    private String SIGN_TYPE;

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private ShopMapper shopMapper;

    @Resource
    private TradeRefundService tradeRefundService;

    @Resource
    private TradeMapper tradeMapper;

    @Resource
    private TradeRefundMapper tradeRefundMapper;

    //支付宝异步通知路径,付款完毕后会异步调用本项目的方法,必须为公网地址
    private String NOTIFY_URL = "http://127.0.0.1/notifyUrl";

    //支付宝同步通知路径,也就是当付款完毕后跳转本项目的页面,可以不是公网地址
    private String RETURN_URL = "http://localhost:8080/returnUrl";

    @Override
    public R<PayDTO> aliPay(AliPayDTO aliPayDTO) {
        UserUtils.hasPermissions(aliPayDTO.getShopName(), RoleEnum.SHOP.getCode());
        Shop shop = shopMapper.selectById(Long.parseLong(aliPayDTO.getShopName()));
        User user = UserUtils.getUser();
        //同一用户同一时间只能产生一笔交易
        if (redisUtils.hasKey("aliPay" + user.getId()))
            return R.error("存在一笔订单未处理，请先处理！");
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);
        AlipayTradePayRequest request = new AlipayTradePayRequest();
        String id = IdUtil.getSnowflake().nextIdStr();
        redisUtils.set("aliPay" + user.getId(), id, 60);
        AlipayTradePayModel model = new AlipayTradePayModel();
        /**
         * 商户订单号，商户自定义，需保证在商户端不重复
         **/
        model.setOutTradeNo(redisUtils.get("aliPay" + user.getId()).toString());
        /**
         * 订单标题
         **/
        model.setSubject(shop.getShopName() + "消费");
        /**
         * 订单金额，精确到小数点后两位
         **/
        model.setTotalAmount(aliPayDTO.getPrice());
        /**
         * 订单描述
         **/
        model.setBody(aliPayDTO.getShopName() + "购物支付");
        model.setAuthCode(aliPayDTO.getUserID());
        model.setOperatorId(aliPayDTO.getOperatorId());
        // 1分钟有效
        model.setTimeoutExpress("1m");
        request.setBizModel(model);
        //代调用商户支付接口
        request.putOtherTextParam("app_auth_token", shop.getAuthZfb());
        /** 异步通知地址，以http或者https开头的，商户外网可以post访问的异步地址，用于接收支付宝返回的支付结果，如果未收到该通知可参考该文档进行确认：			https://opensupport.alipay.com/support/helpcenter/193/201602475759 **/
        request.setNotifyUrl(NOTIFY_URL);
        AlipayTradePayResponse response = null;
        PayDTO payDTO = new PayDTO();
        try {
            response = alipayClient.execute(request);
            payDTO.setRemoteID(response.getTradeNo());
            payDTO.setShopID(id);
            payDTO.setMsg(response.getSubMsg());
            //调用出错解锁
            if (response.getCode().charAt(0) != '1') {
                redisUtils.del("aliPay" + user.getId());
            }
            //成功支付
            if (response.getCode().compareTo("10000") == 0) {
                redisUtils.del("aliPay" + user.getId());
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            redisUtils.del("aliPay" + user.getId());
        }
        return R.ok(payDTO, response.getCode());
    }

    @Override
    public R<String> queryAliPay(String tradeNo) {
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);
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
        UserUtils.hasPermissions(sid.toString(), RoleEnum.SHOP.getCode());
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);
        User user = UserUtils.getUser();
        redisUtils.del("aliPay" + user.getId());
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
        UserUtils.hasPermissions(sid.toString(), RoleEnum.SHOP.getCode());
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);
        User user = UserUtils.getUser();
        redisUtils.del("aliPay" + user.getId());
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
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);
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
        //店铺管理员才能退款
        UserUtils.hasPermissions(refundDTO.getSid(), RoleEnum.SHOPADMIN.getCode());
        //检查是否有现金退款
        TradeRefund tradeRefund = new TradeRefund();
        User user = UserUtils.getUser();
        Trade trade = tradeMapper.selectById(Long.parseLong(refundDTO.getTradeNo()));
        List<TradeRefund> tradeRefunds = tradeRefundMapper.selectList(Wrappers.lambdaQuery(TradeRefund.class).eq(TradeRefund::getSid, Long.parseLong(refundDTO.getTradeNo())));
        BigDecimal reduce = tradeRefunds.stream().filter(e ->
                e.getStatus() == 1
        ).map(TradeRefund::getMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
        tradeRefund.setMsg(refundDTO.getMsg());
        tradeRefund.setSid(Long.parseLong(refundDTO.getTradeNo()));
        tradeRefund.setNo(refundDTO.getNo());
        tradeRefund.setMoney(new BigDecimal(refundDTO.getMoney()));
        tradeRefund.setCtrateTime(LocalDateTime.now());
        tradeRefund.setOperater(user.getId());
        tradeRefund.setType(2);
        tradeRefund.setCtrateTime(LocalDateTime.now());
        if (reduce.add(new BigDecimal(refundDTO.getMoney())).compareTo(trade.getIncome()) > 0) {
            tradeRefund.setStatus(0);
            tradeRefund.setErrMsg("总退款金额大于付款！");
            //退款失败记录
            tradeRefundService.TradeRefundRecord(tradeRefund);
            return R.error("总退款金额大于付款！");
        }
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);
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
            if (response.getCode().compareTo("10000") == 0) {

                if (response.getFundChange().compareTo("Y") == 0) {
                    tradeRefund.setStatus(1);
                } else {
                    Thread.sleep(10000);
                    R<String> stringR = this.queryRefund(refundDTO);
                    if (stringR.getCode() == 200) {
                        tradeRefund.setStatus(1);
                    } else {
                        tradeRefund.setStatus(0);
                    }
                }
            } else {
                tradeRefund.setStatus(0);
                tradeRefund.setErrMsg(response.getSubMsg());
                tradeRefundService.TradeRefundRecord(tradeRefund);
                return R.error(response.getSubMsg());
            }
            tradeRefundService.TradeRefundRecord(tradeRefund);
        } catch (AlipayApiException e) {
            log.error("交易退款失败，订单号：{},Err:{}", refundDTO.getTradeNo(), e.getErrMsg());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return R.ok(response.getCode(), response.getFundChange());
    }

    @Override
    public R<String> queryRefund(RefundDTO refundDTO) {
        UserUtils.hasPermissions(refundDTO.getSid(), RoleEnum.SHOP.getCode());
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);
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
