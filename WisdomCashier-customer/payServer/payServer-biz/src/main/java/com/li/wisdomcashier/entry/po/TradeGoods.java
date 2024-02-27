package com.li.wisdomcashier.entry.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 *
 * </p>
 *
 * @author lsw
 * @since 2023-03-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_trade_goods")
@Accessors(chain = true)
public class TradeGoods implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("gid")
    private String gid;

    @TableField("num")
    private Integer num;

    @TableField("name")
    private String name;

    @TableField("trade_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long tradeId;

    @TableId(value = "id", type = IdType.AUTO)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @TableField("price")
    private BigDecimal price;

    @TableField("price_in")
    private BigDecimal priceIn;

    @TableField("type")
    private String type;

    @TableField("price_out_sum")
    private BigDecimal priceOutSum;

    @TableField("price_in_sum")
    private BigDecimal priceInSum;

}
