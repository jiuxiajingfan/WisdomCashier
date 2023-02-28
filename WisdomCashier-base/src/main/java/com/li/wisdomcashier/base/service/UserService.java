package com.li.wisdomcashier.base.service;

import com.li.wisdomcashier.base.bean.UserBean;
import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.entity.dto.ChangeEmailDTO;
import com.li.wisdomcashier.base.entity.dto.ChangePwdDTO;
import com.li.wisdomcashier.base.entity.dto.LoginDTO;
import com.li.wisdomcashier.base.entity.dto.SignUpDTO;
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
    R<String> signUp(SignUpDTO signUpDto);

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
    R<String> login(LoginDTO loginDto);

    R<String> login2(LoginDTO loginDto);

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
    R<String> changeUserEmail(ChangeEmailDTO changeEmailDto);

    /**
     * 用户密码修改
     * @param changePwdDto
     * @return
     */
    R<String> changePwd(ChangePwdDTO changePwdDto);

    /**
     * 获取用户中心菜单
     * @return
     */
    R<List<SysMenu>> getUserCenterMenu();

    /**
     * 更改用户头像
     * @param url base64图片
     * @return
     */
    R<String> changeIcon(String url);
}
