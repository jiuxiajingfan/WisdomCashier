package com.li.wisdomcashier.entry;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName t_shop_category
 */
@TableName(value = "t_shop_category")
@Data
public class ShopCategory implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     *
     */
    private Long shopId;

    /**
     *
     */
    private String category;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}