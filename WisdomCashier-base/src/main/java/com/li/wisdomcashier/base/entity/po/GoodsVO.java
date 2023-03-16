package com.li.wisdomcashier.base.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * @ClassName GoodsVO
 * @Description TODO
 * @Author Nine
 * @Date 2023/2/26 16:22
 * @Version 1.0
 */
@Data
public class GoodsVO {
    private String name;

    private String gid;

    private String priceOut;

    private String picUrl;

    private String metrology;

    private String priceIn;

    private String type;

}
