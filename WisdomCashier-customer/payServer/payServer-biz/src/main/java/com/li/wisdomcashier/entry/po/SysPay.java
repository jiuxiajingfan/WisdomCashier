package com.li.wisdomcashier.entry.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("t_sys_pay")
public class SysPay implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Integer id;

    private String name;

    private String description;

    private String appId;

    private String appPublicKey;

    private String appPrivateKey;

    private String signType;

    private String appUrl;

    private String notifyUrl;
}
