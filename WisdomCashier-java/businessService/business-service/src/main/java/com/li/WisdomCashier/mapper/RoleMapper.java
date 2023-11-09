package com.li.WisdomCashier.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.li.WisdomCashier.po.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Nine
 * @since 2022-10-11
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

}
