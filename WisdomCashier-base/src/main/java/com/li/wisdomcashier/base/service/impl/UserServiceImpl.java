package com.li.wisdomcashier.base.service.impl;

import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.li.wisdomcashier.base.bean.UserBean;
import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.entity.dto.ChangeEmailDTO;
import com.li.wisdomcashier.base.entity.dto.ChangePwdDTO;
import com.li.wisdomcashier.base.entity.dto.LoginDTO;
import com.li.wisdomcashier.base.entity.dto.SignUpDTO;
import com.li.wisdomcashier.base.entity.po.JWTToken;
import com.li.wisdomcashier.base.entity.po.Role;
import com.li.wisdomcashier.base.entity.po.SysMenu;
import com.li.wisdomcashier.base.entity.po.User;
import com.li.wisdomcashier.base.entity.vo.UserVo;
import com.li.wisdomcashier.base.enums.MenuEnum;
import com.li.wisdomcashier.base.enums.RoleEnum;
import com.li.wisdomcashier.base.mapper.RoleMapper;
import com.li.wisdomcashier.base.mapper.UserMapper;
import com.li.wisdomcashier.base.mapper.SysMenuMapper;
import com.li.wisdomcashier.base.service.EmailService;
import com.li.wisdomcashier.base.service.TradeRefundService;
import com.li.wisdomcashier.base.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.li.wisdomcashier.base.util.*;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Nine
 * @since 2022-10-11
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private UserMapper userMapper;

    @Resource
    private EmailService emailService;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private CaptchaService captchaService;

    @Resource
    private MinioUtils minioUtils;

    @Value("${tokenTime}")
    private Long tokenTime;

    @Resource
    private SysMenuMapper sysMenuMapper;

    @Value("${minio.endpoint}")
    private String address;

    @Value("${minio.bucketName}")
    private String bucketName;
    @Override
    public R<String> signUp(SignUpDTO signUpDto) {
        if (!signUpDto.getCode().equals(redisUtils.get(signUpDto.getEmail()))) {
            return R.error("验证码错误！");
        }
        List<User> users = userMapper.selectList(Wrappers.lambdaQuery(User.class)
                .eq(User::getEmail, signUpDto.getEmail()));
        if (!users.isEmpty()) {
            return R.error("该邮箱已被注册！请勿重复注册。");
        }
        List<User> users2 = userMapper.selectList(Wrappers.lambdaQuery(User.class)
                .eq(User::getUserName, signUpDto.getUserName()));
        if (!users2.isEmpty()) {
            return R.error("该账号已被注册！请勿重复注册。");
        }
        redisUtils.del(signUpDto.getEmail());
        User convert = CglibUtil.copy(signUpDto, User.class);
        convert.setUserNickname("用户" + RandomUtil.randomString(6));
        convert.setStatus(0);
        userMapper.insert(convert);
        return userMapper.insert(convert)==1?R.ok("注册成功！"):R.error("注册失败，请联系管理员！");
    }

    @Override
    public R<String> getCode(String email, int type) {
        log.info("调用邮箱服务：{}", email);
        if (redisUtils.hasKey(email)) {
            return R.error("请勿重复调用~");
        } else {
            String code = CodeUtils.getCode();
            //两分钟有效
            redisUtils.set(email, code, 120);
            if (EmailTypeEnum.signUp.getValue() == type) {
                emailService.sendSimpleMail(email, code, EmailTypeEnum.signUp.getLabel());
            } else if (EmailTypeEnum.findPassword.getValue() == type) {
                emailService.sendSimpleMail(email, code, EmailTypeEnum.findPassword.getLabel());
            }
        }
        return R.ok("验证码已发送，请耐心等待~");
    }

    @Override
    public R<String> getCodeAuth(int type) {
        String email = UserUtils.getUser().getEmail();
        log.info("调用邮箱服务,类型:{},邮箱:{}", type, email);
        if (redisUtils.hasKey(email)) {
            return R.error("请勿重复调用~");
        } else {
            String code = CodeUtils.getCode();
            //两分钟有效
            redisUtils.set(email, code, 120);
            if (EmailTypeEnum.changePassword.getValue() == type) {
                email = UserUtils.getUser().getEmail();
                emailService.sendSimpleMail(email, code, EmailTypeEnum.changePassword.getLabel());
            } else if (EmailTypeEnum.changeEmail.getValue() == type) {
                email = UserUtils.getUser().getEmail();
                emailService.sendSimpleMail(email, code, EmailTypeEnum.changeEmail.getLabel());
            }
        }
        return R.ok("已向您的原邮箱发送验证码，请耐心等待~");
    }

    @Override
    public R<User> test() {
        User user = userMapper.selectOne(Wrappers.lambdaQuery(User.class)
                .eq(User::getEmail, "1475549985@qq.com"));
        return R.ok(user);

    }

    @Override
    public UserBean getUser(String username) {
        User user = userMapper.selectOne(Wrappers.lambdaQuery(User.class)
                .eq(User::getUserName, username));
        if (user == null) {
            return null;
        }
        List<String> roleList = new ArrayList<>();
        List<String> permissionList = new ArrayList<>();
        UserBean userBean = new UserBean();
        userBean.setId(user.getId());
        userBean.setPassword(user.getUserPwd());
        userBean.setUsername(user.getUserName());
        List<Role> roles = roleMapper.selectList(Wrappers.lambdaQuery(Role.class)
                .eq(Role::getUserId, user.getId()));
        for (Role role : roles) {
            //店主
            if (role.getRole() == 1) {
                roleList.addAll(Arrays.asList(role.getShopId().toString()+"shopmaster",role.getShopId().toString()+"shopadmin",role.getShopId().toString()+"shop"));
                permissionList.addAll(Arrays.asList(role.getShopId().toString() + "1",role.getShopId().toString() + "2",role.getShopId().toString() + "3"));
            }
            //店铺管理员
            else if (role.getRole() == 2) {
                roleList.addAll(Arrays.asList(role.getShopId().toString()+"shopadmin",role.getShopId().toString()+"shop"));
                permissionList.addAll(Arrays.asList(role.getShopId().toString() + "2",role.getShopId().toString() + "3"));
            }
            //店员
            else if (role.getRole() == 3) {
                roleList.addAll(Arrays.asList(role.getShopId().toString()+"shop"));
                permissionList.addAll(Arrays.asList(role.getShopId().toString() + "3"));
            }
        }
        userBean.setPermission(permissionList);
        userBean.setRole(roleList);
        return userBean;
    }

    @Override
    public R<String> login(LoginDTO loginDto) {
        CaptchaVO captchaVO = new CaptchaVO();
        captchaVO.setCaptchaVerification(loginDto.getVerification());
        ResponseModel verification = captchaService.verification(captchaVO);
        //图形验证码验证
        if (verification.getRepCode().compareTo(ResponseModel.success().getRepCode()) != 0) {
            return R.error(verification.getRepMsg());
        }
        User userBean = userMapper.selectOne(Wrappers.lambdaQuery(User.class)
                .eq(User::getUserName, loginDto.getUserName()));
        if (null == userBean) {
            return R.error("不存在该用户！");
        }
        if (userBean.getUserPwd().equals(loginDto.getUserPwd())) {
            //封装用户的登录数据
            JWTToken jwtToken = new JWTToken(jwtUtils.generateToken(loginDto.getUserName()));
            //限制多处登录
            redisUtils.lSet(loginDto.getUserName() + "token", jwtToken.getPrincipal(), 14400);
            if (redisUtils.lGetListSize(loginDto.getUserName() + "token") > 1) {
                redisUtils.lLPop(loginDto.getUserName() + "token");
            }
            return R.ok(jwtToken.getPrincipal().toString());
        }
        return R.error("密码错误！请重试！");
    }

    public R<String> login2(LoginDTO loginDto) {
        User userBean = userMapper.selectOne(Wrappers.lambdaQuery(User.class).eq(User::getUserName, loginDto.getUserName()));
        if (null == userBean) {
            return R.error("不存在该用户！");
        }
        if (userBean.getUserPwd().equals(loginDto.getUserPwd())) {
            //封装用户的登录数据
            JWTToken jwtToken = new JWTToken(jwtUtils.generateToken(loginDto.getUserName()));
            //限制多处登录
            redisUtils.lSet(loginDto.getUserName() + "token", jwtToken.getPrincipal(), 14400);
            if (redisUtils.lGetListSize(loginDto.getUserName() + "token") > 1) {
                redisUtils.lLPop(loginDto.getUserName() + "token");
            }
            return R.ok(jwtToken.getPrincipal().toString());
        }
        return R.error("密码错误！请重试！");
    }

    @Override
    public R<String> loginOut(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (StrUtil.isBlank(token))
            return R.error("请先登录");
        Claims claimByToken = jwtUtils.getClaimByToken(token);
        if (claimByToken == null)
            return R.error("请先登录");
        else {
            if (redisUtils.lGetListSize(claimByToken.getSubject() + "token") > 0) {
                redisUtils.lLPop(claimByToken.getSubject() + "token");
            }
        }
        return R.ok("账户退出成功");
    }

    @Override
    public R<UserVo> getUser() {
        UserVo copy = CglibUtil.copy(UserUtils.getUser(), UserVo.class);
        if (!StrUtil.isBlank(copy.getPhone())) {
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
                .eq(User::getId, user.getId()).set(User::getUserNickname, name))==1?R.ok("修改成功！"):R.error("修改失败，请联系管理员！");
    }

    @Override
    public R<String> changeUserEmail(ChangeEmailDTO changeEmailDto) {

        User user = UserUtils.getUser();
        if (!changeEmailDto.getCode().equals(redisUtils.get(user.getEmail()))) {
            return R.error("验证码错误！");
        }
        Integer count = userMapper.selectCount(Wrappers.lambdaQuery(User.class)
                .eq(User::getEmail, changeEmailDto.getEmail()));
        if (count > 0) {
            return R.error("该邮箱已被绑定！");
        }
        int update = userMapper.update(null, Wrappers.lambdaUpdate(User.class)
                .eq(User::getId, user.getId())
                .set(User::getEmail, changeEmailDto.getEmail()));
        if(update==1)redisUtils.del(user.getEmail());
        return update==1?R.ok("修改绑定邮箱成功！"):R.error("修改失败，请联系管理员！");
    }

    @Override
    public R<String> changePwd(ChangePwdDTO changePwdDto) {
        Subject subject = SecurityUtils.getSubject();
        Claims claimByToken = jwtUtils.getClaimByToken(subject.getPrincipal().toString());
        User userBean = userMapper.selectOne(Wrappers.lambdaQuery(User.class)
                .eq(User::getUserName, claimByToken.getSubject()));
        if (userBean == null)
            return R.error("不存在该用户！");
        if (userBean.getUserPwd().equals(changePwdDto.getPwdOriginal())) {
            if (changePwdDto.getPwdNew().equals(changePwdDto.getPwdConfirm())) {
                return userMapper.update(null, Wrappers.lambdaUpdate(User.class)
                        .eq(User::getId, userBean.getId())
                        .set(User::getUserPwd, changePwdDto.getPwdNew()))==1?R.ok("修改成功！"):R.error("修改失败，请联系管理员！");
            } else {
                return R.error("两次密码不一致，请重新确认！");
            }
        }
        return R.error("原密码错误！请重试！");
    }

    @Override
    public R<List<SysMenu>> getUserCenterMenu() {
        Subject subject = SecurityUtils.getSubject();
        List<SysMenu> userCenterMenu = new ArrayList<>();
        Integer role = RoleEnum.SHOPMASTER.getCode();
        userCenterMenu = sysMenuMapper.getUserCenterMenu(role, MenuEnum.USERCENTER.getCode());
        for (SysMenu centerMenu : userCenterMenu) {
            centerMenu.setChildren(sysMenuMapper.getChildrens(role, centerMenu.getMenuId(), MenuEnum.USERCENTER.getCode()));
        }
        return R.ok(userCenterMenu);
    }

    @Override
    public R<String> changeIcon(String url) {
        try {
            String imageUrl = minioUtils.base64ConvertPNG(url);
            userMapper.update(null, Wrappers.lambdaUpdate(User.class)
                    .eq(User::getId, UserUtils.getUser().getId())
                    .set(User::getImage, address+"/"+bucketName+"/"+imageUrl));
            return R.ok(address+"/"+bucketName+"/"+imageUrl);
        } catch (Exception e) {
            return R.error("上传失败！");
        }
    }


}
