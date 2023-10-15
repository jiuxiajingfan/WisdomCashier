package com.li.WisdomCashier.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 菜单
 * </p>
 *
 * @author lsw
 * @since 2023-02-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_sys_menu")
public class SysMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "menu_id", type = IdType.AUTO)
    private Integer menuId;

    @TableField("name")
    private String name;

    @TableField("path")
    private String path;

    @TableField("parent_id")
    private Integer parentId;

    @TableField("icon")
    private String icon;

    @TableField("component")
    private String component;

    @TableField("hidden")
    private Integer hidden;

    @TableField("sort")
    private Integer sort;

    @TableField("status")
    private Integer status;

    private List<SysMenu> children;

}
