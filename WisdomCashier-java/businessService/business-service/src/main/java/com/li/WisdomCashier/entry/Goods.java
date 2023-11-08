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
 * @TableName t_goods
 */
@TableName(value ="t_goods")
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
    private BigDecimal price_in;

    /**
     * 
     */
    private BigDecimal price_out;

    /**
     * 
     */
    private Long sid;

    /**
     * 
     */
    private Date deadline;

    /**
     * 
     */
    private Long num;

    /**
     * 
     */
    private String pic_url;

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
    private BigDecimal price_vip;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}