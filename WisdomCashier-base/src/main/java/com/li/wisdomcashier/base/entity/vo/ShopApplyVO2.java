package com.li.wisdomcashier.base.entity.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName ShopApplyVO2
 * @Description TODO
 * @Author Nine
 * @Date 2023/3/30 16:20
 * @Version 1.0
 */
@Data
public class ShopApplyVO2 {
    @JsonSerialize(using = ToStringSerializer.class)
    Long id;

    String name;

    String code;

    LocalDateTime gmtCreate;

    Integer status;

    List<String> img;

    @JsonSerialize(using = ToStringSerializer.class)
    Long operator;

    String tips;


}
