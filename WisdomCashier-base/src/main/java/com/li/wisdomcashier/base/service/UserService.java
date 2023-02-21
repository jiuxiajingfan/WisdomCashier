package com.li.wisdomcashier.base.service;

import com.li.wisdomcashier.base.bean.UserBean;
import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.entity.dto.ChangeEmailDto;
import com.li.wisdomcashier.base.entity.dto.ChangePwdDto;
import com.li.wisdomcashier.base.entity.dto.LoginDto;
import com.li.wisdomcashier.base.entity.dto.SignUpDto;
import com.li.wisdomcashier.base.entity.po.SysMenu;
import com.li.wisdomcashier.base.entity.po.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.li.wisdomcashier.base.entity.vo.UserVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    /**
     * 邮箱服务
     * @param email 邮箱
     * @param type 邮件类型 {@link com.li.wisdomcashier.base.service.impl.EmailTypeEnum}
     * @return
     */
    R<String> getCode(String email,int type);

    /**
     * 邮箱服务
     * @param type 邮件类型 {@link com.li.wisdomcashier.base.service.impl.EmailTypeEnum}
     * @return
     */
    R<String> getCodeAuth(int type);
    R<User> test();

    /**
     * 根据用户账号获取用户
     * @param username
     * @return
     */
    UserBean getUser(String username);

    /**
     * 登录
     * @param loginDto
     * @return
     */
    R<String> login(LoginDto loginDto);

    R<String> login2(LoginDto loginDto);

    /**
     * 注销登录
     * @return
     */
    R<String> loginOut(HttpServletRequest httpServletRequest);

    /**
     * 获取用户信息
     * @return
     */
    R<UserVo> getUser();

    /**
     * 用户名修改
     * @return
     */
    R<String> changeUserNickName(String name);


    /**
     * 用户邮箱修改
     * @param changeEmailDto
     * @return
     */
    R<String> changeUserEmail(ChangeEmailDto changeEmailDto);

    /**
     * 用户密码修改
     * @param changePwdDto
     * @return
     */
    R<String> changePwd(ChangePwdDto changePwdDto);

    /**
     * 获取用户中心菜单
     * @return
     */
    R<List<SysMenu>> getUserCenterMenu();
}
