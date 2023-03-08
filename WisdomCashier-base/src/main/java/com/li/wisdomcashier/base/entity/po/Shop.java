package com.li.wisdomcashier.base.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author Nine
 * @since 2022-10-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_shop")
@ApiModel(value="Shop对象", description="")
public class Shop implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id主键")
      @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "店铺名")
    @TableField("shop_name")
    private String shopName;

    @TableField(value = "gmt_create", fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @TableField(value = "gmt_update", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtUpdate;


    @ApiModelProperty(value = "介绍")
    @TableField("tip")
    private String tip;

    @ApiModelProperty(value = "店铺状态 0正常 1封禁 2注销")
    @TableField("status")
    private Integer status;

    @ApiModelProperty(value = "支付宝商家授权码")
    @TableField("auth_zfb")
    private String authZfb;

    @ApiModelProperty(value = "微信商家授权码")
    @TableField("auth_wx")
    private String authWx;

}
