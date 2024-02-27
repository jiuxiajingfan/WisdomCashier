package com.li.wisdomcashier.entry.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <p>
 * 
 * </p>
 *
 * @author lsw
 * @since 2023-02-26
 */
@Data
@TableName("t_goods")
public class Goods implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("name")
    private String name;

    @TableField("gid")
    private String gid;

    @TableField(value = "price_in",numericScale = "2")
    private BigDecimal priceIn;

    @TableField(value ="price_out",numericScale = "2")
    private BigDecimal priceOut;

    @TableField(value = "price_vip",numericScale = "2")
    private BigDecimal priceVip;

    @TableField("sid")
    private Long sid;

    @TableField("deadline")
    private LocalDate deadline;

    @TableField("num")
    private Integer num;

    @TableField("pic_url")
    private String picUrl;

    @TableField(value ="profit",numericScale = "2")
    private BigDecimal profit;

    @TableField("metrology")
    private String metrology;

    @TableField("type")
    private String type;
}
