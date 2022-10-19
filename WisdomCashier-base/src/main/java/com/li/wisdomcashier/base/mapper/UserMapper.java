package com.li.wisdomcashier.base.mapper;

import com.li.wisdomcashier.base.bean.UserBean;
import com.li.wisdomcashier.base.entity.po.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Nine
 * @since 2022-10-11
 */
public interface UserMapper extends BaseMapper<User> {
    /**
     * 根据账号查询用户
     * @param name
     * @return
     */
   UserBean selectUserByname(String name);
}
