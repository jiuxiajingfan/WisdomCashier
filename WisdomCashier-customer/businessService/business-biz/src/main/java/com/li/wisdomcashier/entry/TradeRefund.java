package com.li.wisdomcashier.entry;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @TableName t_trade_refund
 */
@TableName(value = "t_trade_refund")
@Data
public class TradeRefund implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     *
     */
    private Long sid;

    /**
     *
     */
    private BigDecimal money;

    /**
     *
     */
    private String msg;

    /**
     *
     */
    private String no;

    /**
     *
     */
    private Date createTime;

    /**
     *
     */
    private Integer status;

    /**
     *
     */
    private Long operator;

    /**
     *
     */
    private String errMsg;

    /**
     *
     */
    private Integer type;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}