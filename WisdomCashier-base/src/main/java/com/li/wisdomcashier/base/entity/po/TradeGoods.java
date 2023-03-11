package com.li.wisdomcashier.base.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@ApiModel(value = "TradeGoods对象", description = "")
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
    private Long id;

    @TableField("price")
    private Double price;


}
