package com.li.wisdomcashier.constant;

public class MQConstant {
    private MQConstant(){}
    /**
     * 邮件
     */
    public static final String ROUTING_EXCHANGE_EMAIL = "routing.exchange.email";
    public static final String ROUTING_MSG_EMAIL = "routing.msg.email";
    public static final String ROUTING_KEY_EMAIL = "email";

    /**
     * 订单
     */
    public static final String ROUTING_EXCHANGE_ORDER = "routing.exchange.order";
    public static final String ROUTING_MSG_ORDER_CYCLE = "routing.msg.order.cycle";
    public static final String ROUTING_MSG_ORDER_FINISH = "routing.msg.order.finish";
    public static final String ROUTING_KEY_ORDER_CYCLE = "cycle";
    public static final String ROUTING_KEY_ORDER_FINISH = "finish";

}