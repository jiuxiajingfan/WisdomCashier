package com.li.wisdomcashier.controller.goods.dto;

import com.li.wisdomcashier.entry.PageDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


/**
 * @ClassName GoodQueryDTO
 * @Description TODO
 * @Author Nine
 * @Date 2023/2/27 15:54
 * @Version 1.0
 */
@Data
public class GoodQueryDTO extends PageDTO {
    @NotNull
    String sid;

    String gid;
}
