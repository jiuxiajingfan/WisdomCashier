package com.li.wisdomcashier.base.service;

import com.li.wisdomcashier.base.bean.UserBean;
import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.entity.dto.LoginDTO;
import com.li.wisdomcashier.base.entity.po.AdminUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.li.wisdomcashier.base.entity.po.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lsw
 * @since 2023-03-28
 */
public interface AdminUserService extends IService<AdminUser> {

    UserBean getUser(String username);

    R<String> login(LoginDTO loginDto);

    R<AdminUser> test();
}
