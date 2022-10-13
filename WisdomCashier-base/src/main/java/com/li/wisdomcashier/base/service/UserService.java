package com.li.wisdomcashier.base.service;

import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.entity.dto.SignUpDto;
import com.li.wisdomcashier.base.entity.po.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Nine
 * @since 2022-10-11
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param signUpDto
     * @return
     */
    R<String> signUp(SignUpDto signUpDto);

    R<String> getCode(String email);

    R<User> test();
}
