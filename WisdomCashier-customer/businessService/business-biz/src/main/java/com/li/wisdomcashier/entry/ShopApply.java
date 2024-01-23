package com.li.wisdomcashier.entry;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @TableName t_shop_apply
 */
@TableName(value = "t_shop_apply")
@Data
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

    @TableField("operator")
    private Long operator;
}