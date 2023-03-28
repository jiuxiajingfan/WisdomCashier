package com.li.wisdomcashier.base.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.li.wisdomcashier.base.bean.UserBean;
import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.entity.dto.LoginDTO;
import com.li.wisdomcashier.base.entity.po.AdminUser;
import com.li.wisdomcashier.base.entity.po.JWTToken;
import com.li.wisdomcashier.base.entity.po.Role;
import com.li.wisdomcashier.base.entity.po.User;
import com.li.wisdomcashier.base.mapper.AdminUserMapper;
import com.li.wisdomcashier.base.service.AdminUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.li.wisdomcashier.base.util.JwtUtils;
import com.li.wisdomcashier.base.util.RedisUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lsw
 * @since 2023-03-28
 */
@Service
public class AdminUserServiceImpl extends ServiceImpl<AdminUserMapper, AdminUser> implements AdminUserService {

    @Resource
    private AdminUserMapper adminUserMapper;

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private JwtUtils jwtUtils;

    @Override
    public UserBean getUser(String username) {
        AdminUser user = adminUserMapper.selectOne(Wrappers.lambdaQuery(AdminUser.class)
                .eq(AdminUser::getUserName, username));
        if (user == null) {
            return null;
        }
        List<String> roleList = new ArrayList<>();
        List<String> permissionList = new ArrayList<>();
        UserBean userBean = new UserBean();
        userBean.setId(user.getId());
        userBean.setPassword(user.getUserPwd());
        userBean.setUsername(user.getUserName());
        userBean.setPermission(permissionList);
        userBean.setRole(roleList);
        userBean.setStatus(user.getStatus());
        return userBean;
    }

    @Override
    public R<String> login(LoginDTO loginDto) {
        AdminUser userBean = adminUserMapper.selectOne(Wrappers.lambdaQuery(AdminUser.class)
                .eq(AdminUser::getUserName, loginDto.getUserName()));
        if (null == userBean) {
            return R.error("不存在该用户！");
        }
        if (userBean.getUserPwd().equals(loginDto.getUserPwd())) {
            //封装用户的登录数据
            JWTToken jwtToken = new JWTToken(jwtUtils.generateToken(loginDto.getUserName()),"UserRealm");
            //限制多处登录
            redisUtils.lSet(loginDto.getUserName() + "token", jwtToken.getPrincipal(), 14400);
            if (redisUtils.lGetListSize(loginDto.getUserName() + "token") > 1) {
                redisUtils.lLPop(loginDto.getUserName() + "token");
            }
            return R.ok(jwtToken.getPrincipal().toString());
        }
        return R.error("密码错误！请重试！");
    }
}
