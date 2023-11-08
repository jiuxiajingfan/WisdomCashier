package com.li.WisdomCashier.entry;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName t_vip
 */
@TableName(value ="t_vip")
@Data
public class Vip implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private Long shop_id;

    /**
     * 
     */
    private String phone;

    /**
     * 
     */
    private Integer status;

    /**
     * 
     */
    private Integer level;

    /**
     * 
     */
    private Date gmt_limit;

    /**
     * 
     */
    private Integer integration;

    /**
     * 
     */
    private String sex;

    /**
     * 
     */
    private String age;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}