package com.li.wisdomcashier.entry;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @TableName t_trade_goods
 */
@TableName(value = "t_trade_goods")
@Data
public class TradeGoods implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     *
     */
    private String gid;

    /**
     *
     */
    private Integer num;

    /**
     *
     */
    private String name;

    /**
     *
     */
    private Long tradeId;

    /**
     *
     */
    private BigDecimal price;

    /**
     *
     */
    private BigDecimal priceIn;

    /**
     *
     */
    private String type;

    /**
     *
     */
    private BigDecimal priceOutSum;

    /**
     *
     */
    private BigDecimal priceInSum;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}