package com.li.wisdomcashier.entry.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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
@TableName("t_trade")
public class Trade implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("sid")
    private Long sid;

    @TableField("income")
    private BigDecimal income;


    @TableField("type")
    private Integer type;

    @TableField("remote_no")
    private String remoteNo;

    @TableField("status")
    private Integer status;

    @TableField("msg")
    private String msg;

    @TableField("payer")
    private String payer;

    @TableField("operator")
    private Long operator;

    @TableField("refund_no")
    private Integer refundNo;

}
