package com.li.wisdomcashier.base.mapper;

import com.li.wisdomcashier.base.entity.po.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.li.wisdomcashier.base.entity.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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

    List<UserVo> selectPerson(@Param("sid") Long sid,@Param("current") Integer current,@Param("pageSize") Integer pageSize);

    Integer selectPersonCount(@Param("sid") String sid);
}
