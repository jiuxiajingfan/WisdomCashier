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
    @Select("select * from t_sys_menu\n" +
            "where type = 0 order by menu_id,sort")
    List<SysMenu> getUserCenterMenu();
}
