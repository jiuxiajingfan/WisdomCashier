package com.li.wisdomcashier.base.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.math.BigDecimal;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import lombok.Data;

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
@ApiModel(value="Goods对象", description="")
public class Goods implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("name")
    private String name;

    @TableField("gid")
    private String gid;

    @TableField("price_in")
    private BigDecimal priceIn;

    @TableField("price_out")
    private BigDecimal priceOut;

    @TableField("sid")
    private Long sid;

    @TableField("deadline")
    private LocalDate deadline;

    @TableField("num")
    private Integer num;

    @TableField("pic_url")
    private String picUrl;

    @TableField("profit")
    private BigDecimal profit;

    @TableField("metrology")
    private String metrology;


}
