package com.li.wisdomcashier.entry;

import lombok.Data;


/**
 * @ClassName PageDTO
 * @Description TODO
 * @Author Nine
 * @Date 2023/2/24 16:34
 * @Version 1.0
 */
@Data
public class PageDTO {
    private Integer current = 1;

    private Integer pageSize = 10;
}
