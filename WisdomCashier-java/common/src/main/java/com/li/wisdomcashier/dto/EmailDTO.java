package com.li.wisdomcashier.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName EmailDto
 * @Description TODO
 * @Author Nine
 * @Date 2023/9/22 17:42
 * @Version 1.0
 */
@Data
@Builder
public class EmailDTO implements Serializable {
    String email;
    String type;
    String desc;
}
