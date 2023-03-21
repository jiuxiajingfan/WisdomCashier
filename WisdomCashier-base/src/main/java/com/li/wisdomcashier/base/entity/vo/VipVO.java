package com.li.wisdomcashier.base.entity.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName VipVO
 * @Description TODO
 * @Author Nine
 * @Date 2023/3/21 13:30
 * @Version 1.0
 */
@Data
public class VipVO {
   private String id;
   private Integer status;
   private Integer integration;
   private LocalDateTime limit;
   private String age;
   private String sex;
   private Integer level;
}
