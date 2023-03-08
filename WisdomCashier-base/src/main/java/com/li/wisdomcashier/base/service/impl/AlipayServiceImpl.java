package com.li.wisdomcashier.base.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeCancelModel;
import com.alipay.api.domain.AlipayTradeCloseModel;
import com.alipay.api.domain.AlipayTradePayModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.request.AlipayTradeCancelRequest;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.entity.dto.AliPayDTO;
import com.li.wisdomcashier.base.entity.dto.PayDTO;
import com.li.wisdomcashier.base.entity.po.Shop;
import com.li.wisdomcashier.base.entity.po.User;
import com.li.wisdomcashier.base.entity.pojo.QueryTrade;
import com.li.wisdomcashier.base.enums.RoleEnum;
import com.li.wisdomcashier.base.mapper.ShopMapper;
import com.li.wisdomcashier.base.service.AlipayService;
import com.li.wisdomcashier.base.service.GoodsService;
import com.li.wisdomcashier.base.util.RedisUtils;
import com.li.wisdomcashier.base.util.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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

    //支付宝异步通知路径,付款完毕后会异步调用本项目的方法,必须为公网地址
    private String NOTIFY_URL = "http://127.0.0.1/notifyUrl";

    //支付宝同步通知路径,也就是当付款完毕后跳转本项目的页面,可以不是公网地址
    private String RETURN_URL = "http://localhost:8080/returnUrl";

    @Override
    public R<PayDTO> aliPay(AliPayDTO aliPayDTO) {
        Shop shop = shopMapper.selectById(aliPayDTO.getShopName());
        if(!UserUtils.hasPermissions(shop.getId(), RoleEnum.SHOP.getCode())){
            throw new AuthorizationException("无权操作！");
        }
        User user = UserUtils.getUser();
        if (redisUtils.hasKey("aliPay" + user.getId()))
            return R.error("存在一笔订单未处理，请先处理！");
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);
        AlipayTradePayRequest request = new AlipayTradePayRequest();
        String id = IdUtil.getSnowflake().nextIdStr();
        redisUtils.set("aliPay" + user.getId(), id, 120);
        AlipayTradePayModel model = new AlipayTradePayModel();
        /** 商户订单号，商户自定义，需保证在商户端不重复，如：20200612000001 **/
        model.setOutTradeNo(redisUtils.get("aliPay" + user.getId()).toString());
        /**订单标题 **/
        model.setSubject(shop.getShopName()+"消费");
        /** 订单金额，精确到小数点后两位 **/
        model.setTotalAmount(String.format("%.2f", aliPayDTO.getPrice()));
        /** 订单描述 **/
        model.setBody(aliPayDTO.getShopName() + "购物支付");
        model.setAuthCode(aliPayDTO.getUserID());
        model.setOperatorId(aliPayDTO.getOperatorId());
        // 1分钟有效
        model.setTimeoutExpress("1m");
        request.setBizModel(model);

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
    public R<String> cancelPay(String tradeNo,Long sid) {
        if(!UserUtils.hasPermissions(sid, RoleEnum.SHOP.getCode())){
            throw new AuthorizationException("无权操作！");
        }
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
    public R<String> closePay(String tradeNo,Long sid) {
        if(!UserUtils.hasPermissions(sid, RoleEnum.SHOP.getCode())){
            throw new AuthorizationException("无权操作！");
        }
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
    public QueryTrade queryPayDetil(String tradeNo) {
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        AlipayTradeQueryModel model = new AlipayTradeQueryModel();
        model.setTradeNo(tradeNo);
        request.setBizModel(model);
        AlipayTradeQueryResponse response = null;
        QueryTrade queryTrade = new QueryTrade();
        try {
            response = alipayClient.execute(request);
            queryTrade.setSum(Double.parseDouble(response.getTotalAmount()));
            queryTrade.setPayUserId(response.getBuyerUserId());
            queryTrade.setTradeNo(response.getOutTradeNo());
            queryTrade.setPayUser(response.getBuyerLogonId());
            queryTrade.setRemoteNo(response.getTradeNo());
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return queryTrade;
    }
}
