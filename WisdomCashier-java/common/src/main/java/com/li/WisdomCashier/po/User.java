package com.li.WisdomCashier.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName User
 * @Description TODO
 * @Author Nine
 * @Date 2023/9/19 15:33
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("user_name")
    private String userName;

    @TableField("user_pwd")
    private String userPwd;

    @TableField("user_nickname")
    private String userNickname;

    @TableField("email")
    private String email;

    @TableField(value = "gmt_create", fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @TableField(value = "gmt_update", fill = FieldFill.UPDATE)
    private LocalDateTime gmtUpdate;

    @TableField("status")
    private Integer status;

    @TableField("phone")
    private String phone;


}
