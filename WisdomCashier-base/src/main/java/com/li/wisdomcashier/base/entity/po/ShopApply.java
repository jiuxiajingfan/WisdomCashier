package com.li.wisdomcashier.base.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.time.LocalDateTime;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
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
 * @since 2023-03-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_shop_apply")
@ApiModel(value="ShopApply对象", description="")
public class ShopApply implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

      @TableField(value = "gmt_create", fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @TableField("status")
    private Integer status;

    @TableField("name")
    private String name;

    @TableField("descript")
    private String descript;

    @TableField("img_shop")
    private String imgShop;

    @TableField("img_idCard1")
    private String imgIdcard1;

    @TableField("img_idCard2")
    private String imgIdcard2;

    @TableField("code")
    private String code;

    @TableField("apply_id")
    private Long applyId;

    @TableField("tips")
    private String tips;

}
