package com.li.wisdomcashier.service.impl;

import com.li.wisdomcashier.annotation.RedissonLock;
import com.li.wisdomcashier.entry.R;
import com.li.wisdomcashier.entry.dto.PayDTO;
import com.li.wisdomcashier.entry.dto.PayVO;
import com.li.wisdomcashier.entry.dto.RefundDTO;
import com.li.wisdomcashier.entry.vo.StatusVO;
import com.li.wisdomcashier.exception.BusinessException;
import com.li.wisdomcashier.service.DubboPayService;
import com.li.wisdomcashier.service.PayService;
import com.li.wisdomcashier.utils.UserUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.li.wisdomcashier.constant.MQConstant.ROUTING_EXCHANGE_ORDER;
import static com.li.wisdomcashier.constant.MQConstant.ROUTING_KEY_ORDER_CYCLE;

@Service
public class PayServiceImpl implements PayService {

    @DubboReference(version = "1.0", check = false, timeout = 5000, retries = 0)
    public DubboPayService dubboPayService;

    @Resource
    public RabbitTemplate rabbitTemplate;

    @Override
    @RedissonLock(keyPrefix = "PAY_PAY",key = "#payDTO.id+#payDTO.type",expire = -1)
    public R<PayVO> pay(PayDTO payDTO) {
        payDTO.setOperatorId(UserUtils.getUser().getId().toString());
        PayVO payVO = dubboPayService.pay(payDTO);
        if(null == payVO)
            throw new BusinessException("支付发起失败！请检查付款类型");
        if(!payVO.getSuccess()){
            throw new BusinessException(payVO.getMsg());
        }
        //mq
        payDTO.setRemoteId(payVO.getRemoteID());
        rabbitTemplate.convertAndSend(ROUTING_EXCHANGE_ORDER,ROUTING_KEY_ORDER_CYCLE,payDTO);
        return R.ok(payVO);
    }

    @Override
    @RedissonLock(keyPrefix = "PAY_STATUS",key = "#tradeNo+#type",expire = -1)
    public R<StatusVO> status(Integer type, String tradeNo) {
      return R.ok(dubboPayService.status(type, tradeNo));
    }

    @Override
    @RedissonLock(keyPrefix = "PAY_REFUND",key = "#dto.sid+#dto.tradeNo+#dto.type",expire = -1)
    public R<String> refundPay(RefundDTO dto) {
        String refund = dubboPayService.refund(dto);
        if(null == refund){
            throw new BusinessException("发起退款失败");
        }
        return R.ok(refund);
    }
}
