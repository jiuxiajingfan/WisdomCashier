package com.li.wisdomcashier.base.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;
import java.time.LocalDateTime;

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
 * @since 2023-03-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_trade_refund")
@ApiModel(value = "TradeRefund对象", description = "")
public class TradeRefund implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("sid")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long sid;

    @TableField("money")
    private Double money;

    @TableField("msg")
    private String msg;

    @TableField("no")
    private String no;

    @TableField("create_time")
    private LocalDateTime ctrateTime;

    @TableField("status")
    private Integer status;

    @TableField("operater")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long operater;

    @TableField("err_msg")
    private String errMsg;

    @TableField("type")
    private Integer type;

}
