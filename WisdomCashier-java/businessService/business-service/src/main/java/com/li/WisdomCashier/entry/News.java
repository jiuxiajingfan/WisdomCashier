package com.li.WisdomCashier.entry;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 新闻表
 * @TableName t_news
 */
@TableName(value ="t_news")
@Data
public class News implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 
     */
    private String detail;

    /**
     * 
     */
    private String label;

    /**
     * 
     */
    @TableField(value = "gmt_create", fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    /**
     * 
     */
    @TableField(value = "gmt_update", fill = FieldFill.UPDATE)
    private LocalDateTime gmtUpdate;

    private Integer type;

    private Integer status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}