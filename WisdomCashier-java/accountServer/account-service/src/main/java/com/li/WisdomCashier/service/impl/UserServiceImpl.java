package com.li.WisdomCashier.service.impl;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.li.WisdomCashier.controller.OauthFeignClient;
import com.li.WisdomCashier.controller.account.dto.ChangeEmailDTO;
import com.li.WisdomCashier.controller.account.dto.ChangePwdDTO;
import com.li.WisdomCashier.controller.account.vo.UserDetailVO;
import com.li.WisdomCashier.dto.CreateUserDTO;
import com.li.WisdomCashier.enums.EmailEnums;
import com.li.WisdomCashier.mapper.SysMenuMapper;
import com.li.WisdomCashier.mapper.UserMapper;
import com.li.WisdomCashier.po.SysMenu;
import com.li.WisdomCashier.po.User;
import com.li.WisdomCashier.pojo.R;
import com.li.WisdomCashier.service.UserService;
import com.li.WisdomCashier.utils.CommonUtils;
import com.li.WisdomCashier.utils.RedisUtils;
import com.li.WisdomCashier.utils.UserUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static com.li.WisdomCashier.constant.ExceptionConstant.*;
import static com.li.WisdomCashier.constant.RedisPrefix.CHANGE_EMAIL_CODE;
import static com.li.WisdomCashier.constant.RedisPrefix.REGISTER_CODE;


/**
 * @ClassName UserServiceImpl
 * @Description TODO
 * @Author Nine
 * @Date 2023/7/11 18:33
 * @Version 1.0
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisUtils redisUtils;

    @Resource
    OauthFeignClient oauthFeignClient;

    @Resource
    SysMenuMapper sysMenuMapper;

    @Override
    public R<String> createUser(CreateUserDTO createUserDTO) {
        //验证码校验
        String redisCode = (String) redisUtils.get(REGISTER_CODE + createUserDTO.getEmail());
        if (redisCode == null ||
                redisCode.compareTo(createUserDTO.getCode()) != 0) {
            return R.error(CODE_ERROR);
        }
        //账号重复性校验
        List<User> users = userMapper.selectList(Wrappers.lambdaQuery(User.class)
                .eq(User::getEmail, createUserDTO.getEmail())
                .or()
                .eq(User::getUserName,createUserDTO.getUserName())
        );
        if (!users.isEmpty()) {
            for (User user : users) {
                if(StringUtils.equals(createUserDTO.getUserName(), user.getUserName())){
                    return R.error(NAME_BIND_ERROR);
                }
                if(StringUtils.equals(createUserDTO.getEmail(),user.getEmail())){
                    return R.error(EMAIL_BIND_ERROR);
                }
            }
        }
        User copy = CglibUtil.copy(createUserDTO, User.class);
        copy.setUserPwd(new BCryptPasswordEncoder().encode(createUserDTO.getPassword()));
        copy.setUserNickname("用户"+ RandomUtil.randomString(6));
        userMapper.insert(copy);
        redisUtils.del(REGISTER_CODE + createUserDTO.getEmail());
        OAuth2AccessToken oAuth2AccessToken = oauthFeignClient.postAccessToken("password", createUserDTO.getUserName(), createUserDTO.getPassword());
        return R.ok(oAuth2AccessToken.getValue(),"注册成功！");
    }

    @Override
    public R<List<Tree<String>>> getUserCenterMenu() {
        List<SysMenu> userCenterMenu = sysMenuMapper.getUserCenterMenu();
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        treeNodeConfig.setWeightKey("sort");
        treeNodeConfig.setIdKey("menuId");
        treeNodeConfig.setNameKey("name");
        treeNodeConfig.setChildrenKey("children");
        List<Tree<String>> build = TreeUtil.build(userCenterMenu, null, treeNodeConfig, ((sysMenu, tree) -> {
            tree.setId(sysMenu.getMenuId().toString());
            tree.setName(sysMenu.getName());
            tree.setParentId(null == sysMenu.getParentId()?null:sysMenu.getParentId().toString());
            tree.setWeight(sysMenu.getSort());
            tree.putExtra("path", sysMenu.getPath());
            tree.putExtra("component", sysMenu.getComponent());
            tree.putExtra("icon", sysMenu.getIcon());
            tree.putExtra("hidden", sysMenu.getHidden());
            tree.putExtra("status", sysMenu.getStatus());
        }));
        return R.ok(build);
    }

    @Override
    public R<UserDetailVO> getUserDetail() {
        User user = UserUtils.getUser();
        UserDetailVO copy = CglibUtil.copy(user, UserDetailVO.class);
        if (!StringUtils.isBlank(copy.getPhone())) {
            copy.setPhone(DesensitizedUtil.mobilePhone(copy.getPhone()));
        } else
            copy.setPhone("");
        copy.setEmail(DesensitizedUtil.email(copy.getEmail()));
        return R.ok(copy);
    }

    @Override
    public R<String> changeUserNickName(String name) {
        if (StringUtils.isBlank(name)) {
            return R.error("请输入新用户名");
        }
        if (name.length() > 30) {
            return R.error("用户名过长！");
        }
        User user = UserUtils.getUser();
        return userMapper.update(null, new LambdaUpdateWrapper<User>()
                .eq(User::getId, user.getId())
                .set(User::getUserNickname, name)
        )==1?R.ok("修改成功！"):R.error(CHANGE_ERROR);

    }

    @Override
    public R<String> changeUserEmail(ChangeEmailDTO changeEmailDto) {
        User user = UserUtils.getUser();
        if (!changeEmailDto.getCode().equals(redisUtils.get(CHANGE_EMAIL_CODE + user.getEmail()))) {
            return R.error(CODE_ERROR);
        }
        Integer count = userMapper.selectCount(Wrappers.lambdaQuery(User.class)
                .eq(User::getEmail, changeEmailDto.getEmail()));
        if (count > 0) {
            return R.error(EMAIL_BIND_ERROR);
        }
        int update = userMapper.update(null, Wrappers.lambdaUpdate(User.class)
                .eq(User::getId, user.getId())
                .set(User::getEmail, changeEmailDto.getEmail()));
        if(update==1)redisUtils.del(user.getEmail());
        return update==1?R.ok("修改绑定邮箱成功！"):R.error(CHANGE_ERROR);
    }

    @Override
    public R<String> changePwd(ChangePwdDTO changePwdDto) {
        User user = UserUtils.getUser();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if (user.getUserPwd().equals( bCryptPasswordEncoder.encode(changePwdDto.getPwdOriginal()))) {
            if (CommonUtils.compare(changePwdDto.getPwdNew() , changePwdDto.getPwdConfirm())) {
                return userMapper.update(null, Wrappers.lambdaUpdate(User.class)
                        .eq(User::getId, user.getId())
                        .set(User::getUserPwd,bCryptPasswordEncoder.encode(changePwdDto.getPwdNew()))
                )==1?R.ok("修改成功！"):R.error(CHANGE_ERROR);
            } else {
                return R.error(PWD_COMPARE_ERROR);
            }
        }
        return R.error(PWD_ERROR);

    }

}
