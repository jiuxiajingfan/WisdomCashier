package com.li.WisdomCashier.entry;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName t_shop
 */
@TableName(value ="t_shop")
@Data
public class Shop implements Serializable {
    /**
     * id主键
     */
    @TableId
    private Long id;

    /**
     * 店铺名
     */
    private String shop_name;

    /**
     * 
     */
    private Date gmt_create;

    /**
     * 
     */
    private Date gmt_update;

    /**
     * 介绍
     */
    private String tip;

    /**
     * 店铺状态 0正常 1封禁 2注销
     */
    private Integer status;

    /**
     * 支付宝付款商家授权码
     */
    private String auth_zfb;

    /**
     * 微信付款商家授权码
     */
    private String auth_wx;

    /**
     * 
     */
    private Integer zfb_status;

    /**
     * 
     */
    private Integer wx_status;

    /**
     * 
     */
    private String img_idCard;

    /**
     * 
     */
    private String img_idCard2;

    /**
     * 
     */
    private String img_shop;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}