package com.li.wisdomcashier.base.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @ClassName VipQueryDTO
 * @Description TODO
 * @Author Nine
 * @Date 2023/3/22 16:26
 * @Version 1.0
 */
@Data
public class VipQueryDTO extends PageDTO {
    @NotNull
    String sid;

    List<String> age;

    List<String> sex;

    List<Integer> status;

    List<Integer> level;
}
