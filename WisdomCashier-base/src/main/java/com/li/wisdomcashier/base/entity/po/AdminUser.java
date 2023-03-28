package com.li.wisdomcashier.base.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
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
 * @since 2023-03-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_admin_user")
@ApiModel(value="AdminUser对象", description="")
public class AdminUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id主键")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户登录账户")
    @TableField("user_name")
    private String userName;

    @ApiModelProperty(value = "用户登录密码md5")
    @TableField("user_pwd")
    private String userPwd;

    @ApiModelProperty(value = "用户名")
    @TableField("user_nickname")
    private String userNickname;

    @ApiModelProperty(value = "邮箱")
    @TableField("email")
    private String email;

      @TableField(value = "gmt_create", fill = FieldFill.INSERT)
    private Date gmtCreate;

      @TableField(value = "gmt_update", fill = FieldFill.UPDATE)
    private Date gmtUpdate;

    @ApiModelProperty(value = "账户状态 0正常 1封禁 2注销")
    @TableField("status")
    private Integer status;

    @ApiModelProperty(value = "11位手机号")
    @TableField("phone")
    private String phone;

    @ApiModelProperty(value = "头像链接")
    @TableField("image")
    private String image;


}
