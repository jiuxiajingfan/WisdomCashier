package com.li.WisdomCashier.entry;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author lsw
 * @since 2023-03-30
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
    //redis序列化需要
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime gmtCreate;

    @TableField(value = "gmt_update", fill = FieldFill.UPDATE)
    //redis序列化需要
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime gmtUpdate;

    @ApiModelProperty(value = "介绍")
    @TableField("tip")
    private String tip;

    @ApiModelProperty(value = "店铺状态 0正常 1封禁 2注销")
    @TableField("status")
    private Integer status;

    @ApiModelProperty(value = "支付宝付款商家授权码")
    @TableField("auth_zfb")
    private String authZfb;

    @ApiModelProperty(value = "微信付款商家授权码")
    @TableField("auth_wx")
    private String authWx;

    @TableField("zfb_status")
    private Integer zfbStatus;

    @TableField("wx_status")
    private Integer wxStatus;

    @TableField("img_idCard")
    private String imgIdcard;

    @TableField("img_idCard2")
    private String imgIdcard2;

    @TableField("img_shop")
    private String imgShop;
}
