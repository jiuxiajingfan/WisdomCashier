package com.li.wisdomcashier.controller.account.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * @ClassName UserDetailVo
 * @Description TODO
 * @Author Nine
 * @Date 2023/10/22 13:34
 * @Version 1.0
 */
@Data
public class UserDetailVO {
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;
    private String userName;
    private String userNickname;
    private String image;
    private String phone;
    private String email;
}
