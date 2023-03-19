package com.li.wisdomcashier.base.entity.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName UserVo
 * @Description TODO
 * @Author Nine
 * @Date 2022/12/4 21:10
 * @Version 1.0
 */
@Data
public class UserVo {
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;
    private String userName;
    private String userNickname;
    private String image;
    private String phone;
    private String email;
    private Integer roleEnum;
}
