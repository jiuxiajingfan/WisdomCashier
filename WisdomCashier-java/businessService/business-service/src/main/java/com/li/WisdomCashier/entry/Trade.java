package com.li.WisdomCashier.entry;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName t_trade
 */
@TableName(value ="t_trade")
@Data
public class Trade implements Serializable {
    /**
     * 
     */
    @TableId
    private Long id;

    /**
     * 
     */
    private Date create_time;

    /**
     * 
     */
    private Long sid;

    /**
     * 
     */
    private BigDecimal income;

    /**
     * 
     */
    private Integer type;

    /**
     * 
     */
    private String remote_no;

    /**
     * 
     */
    private Integer status;

    /**
     * 
     */
    private String msg;

    /**
     * 
     */
    private String payer;

    /**
     * 
     */
    private String operater;

    /**
     * 
     */
    private Integer refund_no;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}