package com.li.WisdomCashier.service.impl;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.li.WisdomCashier.controller.OauthFeignClient;
import com.li.WisdomCashier.dto.CreateUserDTO;
import com.li.WisdomCashier.mapper.SysMenuMapper;
import com.li.WisdomCashier.mapper.UserMapper;
import com.li.WisdomCashier.po.SysMenu;
import com.li.WisdomCashier.po.User;
import com.li.WisdomCashier.pojo.R;
import com.li.WisdomCashier.service.UserService;
import com.li.WisdomCashier.utils.RedisUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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
            return R.error("验证码错误！");
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
                    return R.error("该用户名已被注册，请勿重复注册！");
                }
                if(StringUtils.equals(createUserDTO.getEmail(),user.getEmail())){
                    return R.error("该邮箱已被注册，请勿重复注册！");
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
        treeNodeConfig.setParentIdKey("parent_id");
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
}
