package com.li.wisdomcashier.base.mapper;

import com.li.wisdomcashier.base.entity.po.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 菜单 Mapper 接口
 * </p>
 *
 * @author lsw
 * @since 2023-02-20
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    @Select("select *\n" +
            "from t_sys_menu\n" +
            "where menu_id in (select menu_id\n" +
            "                  from t_sys_role_menu\n" +
            "                  where role_id = #{role}\n" +
            "                   and type = 1 order by menu_id asc) and parent_id is null order by menu_id asc ;")
    List<SysMenu> getUserCenterMenu(Integer role);

    @Select("\n" +
            "select *\n" +
            "from t_sys_menu\n" +
            "where menu_id in (select menu_id\n" +
            "                  from t_sys_role_menu\n" +
            "                  where role_id = #{role}\n" +
            "                  and type = 1 order by menu_id asc) and parent_id = #{menuId} order by sort asc;")
    List<SysMenu> getChildrens(@Param("role") Integer role,@Param("menuId") Integer menuId);
}
