package com.li.wisdomcashier.service.impl;

import com.li.wisdomcashier.entry.R;
import com.li.wisdomcashier.entry.dto.PayDTO;
import com.li.wisdomcashier.entry.dto.PayVO;
import com.li.wisdomcashier.exception.BusinessException;
import com.li.wisdomcashier.service.DubboPayService;
import com.li.wisdomcashier.service.PayService;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static com.li.wisdomcashier.constant.MQConstant.ROUTING_EXCHANGE_ORDER;
import static com.li.wisdomcashier.constant.MQConstant.ROUTING_KEY_ORDER_CYCLE;

@Service
public class PayServiceImpl implements PayService {
    @DubboReference(version = "1.0", check = false, timeout = 5000, retries = 0)
    public DubboPayService dubboPayService;

    @Resource
    public RabbitTemplate rabbitTemplate;

    @Override
    public R<PayVO> pay(PayDTO payDTO) {
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
}
