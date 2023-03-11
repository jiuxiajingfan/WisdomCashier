package com.li.wisdomcashier.base.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
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
@TableName("t_trade")
@ApiModel(value="Trade对象", description="")
public class Trade implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("sid")
    private Long sid;

    @TableField("income")
    private Double income;


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

    @TableField("operater")
    private Long operater;

    @TableField("refund_no")
    private Integer refundNo;

}
