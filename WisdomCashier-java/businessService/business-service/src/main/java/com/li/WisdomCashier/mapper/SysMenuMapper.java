package com.li.WisdomCashier.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.li.WisdomCashier.po.SysMenu;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @ClassName SystemMapper
 * @Description TODO
 * @Author Nine
 * @Date 2023/10/18 20:43
 * @Version 1.0
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    @Select("select * from t_sys_menu where menu_id in(\n" +
            "select menu_id from t_sys_role_menu where role_id >= #{id}\n" +
            "and type = 2);")
    List<SysMenu> getShopMenu(long id);
}
