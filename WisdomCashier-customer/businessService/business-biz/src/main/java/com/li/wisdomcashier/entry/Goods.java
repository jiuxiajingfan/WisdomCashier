package com.li.wisdomcashier.entry;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @TableName t_goods
 */
@TableName(value = "t_goods")
@Data
public class Goods implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     *
     */
    private String name;

    /**
     *
     */
    private String gid;

    /**
     *
     */
    private BigDecimal priceIn;

    /**
     *
     */
    private BigDecimal priceOut;

    /**
     *
     */
    private Long sid;

    /**
     *
     */
    private LocalDate deadline;

    /**
     *
     */
    private Long num;

    /**
     *
     */
    private String picUrl;

    /**
     *
     */
    private BigDecimal profit;

    /**
     *
     */
    private String metrology;

    /**
     *
     */
    private String type;

    /**
     *
     */
    private BigDecimal priceVip;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}