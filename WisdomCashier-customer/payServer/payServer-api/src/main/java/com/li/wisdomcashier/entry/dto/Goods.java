package com.li.wisdomcashier.entry.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @TableName t_goods
 */
@Data
public class Goods implements Serializable {
    /**
     *
     */
    private Long id;

    /**
     *
     */
    private String name;

    /**
     *
     */
    private String gid;

    /**
     *
     */
    private BigDecimal priceIn;

    /**
     *
     */
    private BigDecimal priceOut;

    /**
     *
     */
    private Long sid;

    /**
     *
     */
    private LocalDate deadline;

    /**
     *
     */
    private Long num;

    /**
     *
     */
    private String picUrl;

    /**
     *
     */
    private BigDecimal profit;

    /**
     *
     */
    private String metrology;

    /**
     *
     */
    private String type;

    /**
     *
     */
    private BigDecimal priceVip;
}