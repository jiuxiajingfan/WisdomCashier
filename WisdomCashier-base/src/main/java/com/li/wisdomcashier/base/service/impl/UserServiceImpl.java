package com.li.wisdomcashier.base.service.impl;

import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.li.wisdomcashier.base.bean.UserBean;
import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.entity.dto.EmailDto;
import com.li.wisdomcashier.base.entity.dto.LoginDto;
import com.li.wisdomcashier.base.entity.dto.SignUpDto;
import com.li.wisdomcashier.base.entity.po.JWTToken;
import com.li.wisdomcashier.base.entity.po.Role;
import com.li.wisdomcashier.base.entity.po.User;
import com.li.wisdomcashier.base.entity.vo.UserVo;
import com.li.wisdomcashier.base.mapper.PermissionMapper;
import com.li.wisdomcashier.base.mapper.RoleMapper;
import com.li.wisdomcashier.base.mapper.UserMapper;
import com.li.wisdomcashier.base.service.EmailService;
import com.li.wisdomcashier.base.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.li.wisdomcashier.base.util.CodeUtils;
import com.li.wisdomcashier.base.util.JwtUtils;
import com.li.wisdomcashier.base.util.RedisUtils;
import com.li.wisdomcashier.base.util.UserUtils;
import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * ???????????????
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

    @Value("${tokenTime}")
    private Long tokenTime;

    @Override
    public R<String> signUp(SignUpDto signUpDto) {
        if (!signUpDto.getCode().equals(redisUtils.get(signUpDto.getEmail()))) {
            return R.error("??????????????????");
        }
        List<User> users = userMapper.selectList(Wrappers.lambdaQuery(User.class).eq(User::getEmail, signUpDto.getEmail()));
        if (!users.isEmpty()) {
            return R.error("?????????????????????????????????????????????");
        }
        List<User> users2 = userMapper.selectList(Wrappers.lambdaQuery(User.class).eq(User::getUserName, signUpDto.getUserName()));
        if (!users2.isEmpty()) {
            return R.error("?????????????????????????????????????????????");
        }
        redisUtils.del(signUpDto.getEmail());
        User convert = CglibUtil.copy(signUpDto, User.class);
        convert.setUserNickname("??????" + RandomUtil.randomString(6));
        convert.setStatus(0);
        userMapper.insert(convert);
        return R.ok("???????????????");
    }

    @Override
    public R<String> getCode(String email,int type) {
        log.info("?????????????????????{}", email);
        if (redisUtils.hasKey(email)) {
            return R.error("??????????????????~");
        } else {
            String code = CodeUtils.getCode();
            //???????????????
            redisUtils.set(email, code, 120);
            if (EmailTypeEnum.signUp.getValue() == type) {
                emailService.sendSimpleMail(email, code, EmailTypeEnum.signUp.getLabel());
            } else if (EmailTypeEnum.findPassword.getValue() == type) {
                emailService.sendSimpleMail(email, code, EmailTypeEnum.findPassword.getLabel());
            }
        }
        return R.ok("????????????????????????????????????~");
    }

    @Override
    public R<String> getCodeAuth(int type) {
        String email = UserUtils.getUser().getEmail();
        log.info("??????????????????,??????:{},??????:{}",type,email);
        if (redisUtils.hasKey(email)) {
            return R.error("??????????????????~");
        } else {
            String code = CodeUtils.getCode();
            //???????????????
            redisUtils.set(email, code, 120);
            if (EmailTypeEnum.changePassword.getValue() == type) {
                email = UserUtils.getUser().getEmail();
                emailService.sendSimpleMail(email, code, EmailTypeEnum.changePassword.getLabel());
            } else if (EmailTypeEnum.changeEmail.getValue() == type) {
                email = UserUtils.getUser().getEmail();
                emailService.sendSimpleMail(email, code, EmailTypeEnum.changeEmail.getLabel());
            }
        }
        return R.ok("??????????????????????????????????????????????????????~");
    }

    @Override
    public R<User> test() {
        User user = userMapper.selectOne(Wrappers.lambdaQuery(User.class).eq(User::getEmail, "1475549985@qq.com"));
        return R.ok(user);

    }

    @Override
    public UserBean getUser(String username) {
        User user = userMapper.selectOne(Wrappers.lambdaQuery(User.class).eq(User::getUserName, username));
        if (user == null) {
            return null;
        }
        List<String> roleList = new ArrayList<>();
        List<String> permissionList = new ArrayList<>();
        UserBean userBean = new UserBean();
        userBean.setPassword(user.getUserPwd());
        userBean.setUsername(user.getUserName());
        List<Role> roles = roleMapper.selectList(Wrappers.lambdaQuery(Role.class).eq(Role::getUserId, user.getId()));
        for (Role role : roles) {
            //???????????????
            if (role.getRole() == 1) {
                roleList.add("admin");
                permissionList.add("all");
            }
            //???????????????
            else if (role.getRole() == 2) {
                roleList.add("shopadmin");
                permissionList.add(role.getShopId().toString() + "shopadmin");
            }
            //??????
            else if (role.getRole() == 3) {
                roleList.add("shop");
                permissionList.add(role.getShopId().toString() + "shop");
            }
        }
        userBean.setPermission(permissionList);
        userBean.setRole(roleList);
        return userBean;
    }

    @Override
    public R<String> login(LoginDto loginDto) {
        CaptchaVO captchaVO = new CaptchaVO();
        captchaVO.setCaptchaVerification(loginDto.getVerification());
        ResponseModel verification = captchaService.verification(captchaVO);
        //?????????????????????
        if (verification.getRepCode().compareTo(ResponseModel.success().getRepCode()) != 0) {
            return R.error(verification.getRepMsg());
        }
        //??????????????????
        Subject subject = SecurityUtils.getSubject();
        //???????????????????????????
        JWTToken jwtToken = new JWTToken(jwtUtils.generateToken(loginDto.getUserName()));
        try {
            subject.login(jwtToken);
            //??????????????????
            redisUtils.lSet(loginDto.getUserName() + "token", jwtToken.getPrincipal(), 14400);
            if (redisUtils.lGetListSize(loginDto.getUserName() + "token") > 1) {
                redisUtils.lLPop(loginDto.getUserName() + "token");
            }
            return R.ok(jwtToken.getPrincipal().toString());
        } catch (AuthenticationException e) {
            log.info(e.getMessage());
            return R.error("????????????????????????????????????");
        }
    }

    public R<String> login2(LoginDto loginDto) {
        Subject subject = SecurityUtils.getSubject();
        //???????????????????????????
        JWTToken jwtToken = new JWTToken(jwtUtils.generateToken(loginDto.getUserName()));
        try {
            subject.login(jwtToken);
            //??????????????????
            redisUtils.lSet(loginDto.getUserName() + "token", jwtToken.getPrincipal(), 14400);
            if (redisUtils.lGetListSize(loginDto.getUserName() + "token") > 1) {
                redisUtils.lLPop(loginDto.getUserName() + "token");
            }
            return R.ok(jwtToken.getPrincipal().toString());
        } catch (AuthenticationException e) {
            log.info(e.getMessage());
            return R.error("????????????????????????????????????");
        }
    }

    @Override
    public R<String> loginOut(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (StrUtil.isBlank(token))
            return R.error("????????????");
        Claims claimByToken = jwtUtils.getClaimByToken(token);
        if (claimByToken == null)
            return R.error("????????????");
        else {
            if (redisUtils.lGetListSize(claimByToken.getSubject() + "token") > 0) {
                redisUtils.lLPop(claimByToken.getSubject() + "token");
            }
        }
        return R.ok("??????????????????");
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
        User user = UserUtils.getUser();
        int update = userMapper.update(null, new LambdaUpdateWrapper<User>().eq(User::getId, user.getId()).set(User::getUserNickname, name));
        return R.ok("???????????????");
    }

    @Override
    public R<String> changeUserEmail(EmailDto emailDto) {

        User user = UserUtils.getUser();
        if (!emailDto.getCode().equals(redisUtils.get(user.getEmail()))) {
            return R.error("??????????????????");
        }
        Integer count = userMapper.selectCount(Wrappers.lambdaQuery(User.class).eq(User::getEmail, emailDto.getEmail()));
        if (count>0) {
            return R.error("????????????????????????");
        }
        userMapper.update(null,Wrappers.lambdaUpdate(User.class).eq(User::getId,user.getId()).set(User::getEmail,emailDto.getEmail()));
        redisUtils.del(user.getEmail());
        return R.ok("???????????????????????????");
    }


}
