package com.li.wisdomcashier.base.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.li.wisdomcashier.base.bean.UserBean;
import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.entity.dto.LoginDto;
import com.li.wisdomcashier.base.entity.dto.SignUpDto;
import com.li.wisdomcashier.base.entity.po.JWTToken;
import com.li.wisdomcashier.base.entity.po.Role;
import com.li.wisdomcashier.base.entity.po.User;
import com.li.wisdomcashier.base.mapper.PermissionMapper;
import com.li.wisdomcashier.base.mapper.RoleMapper;
import com.li.wisdomcashier.base.mapper.UserMapper;
import com.li.wisdomcashier.base.service.EmailService;
import com.li.wisdomcashier.base.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.li.wisdomcashier.base.util.CodeUtils;
import com.li.wisdomcashier.base.util.JwtUtils;
import com.li.wisdomcashier.base.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
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
        if(!signUpDto.getCode().equals(redisUtils.get(signUpDto.getEmail()))) {
           return R.error("验证码错误！");
        }
        List<User> users = userMapper.selectList(Wrappers.lambdaQuery(User.class).eq(User::getEmail, signUpDto.getEmail()));
        if(!users.isEmpty()){
            return R.error("该邮箱已被注册！请勿重复注册。");
        }
        List<User> users2 = userMapper.selectList(Wrappers.lambdaQuery(User.class).eq(User::getUserName, signUpDto.getUserName()));
        if(!users2.isEmpty()){
            return R.error("该账号已被注册！请勿重复注册。");
        }
        redisUtils.del(signUpDto.getEmail());
        User convert = CglibUtil.copy(signUpDto, User.class);
        convert.setUserNickname("用户"+ RandomUtil.randomString(6));
        convert.setStatus(0);
        userMapper.insert(convert);
        return R.ok("注册成功！");
    }

    @Override
    public R<String> getCode(String email) {
        log.info("调用邮箱服务：{}",email);
        if (redisUtils.hasKey(email)) {
            return R.error("请勿重复调用~");
        } else {
            String code = CodeUtils.getCode();
            //两分钟有效
            redisUtils.set(email, code,120);
            emailService.sendSimpleMail(email, code);
        }
        return R.ok("验证码已发送，请耐心等待~");
    }

    @Override
    public R<User> test() {
        User user = userMapper.selectOne(Wrappers.lambdaQuery(User.class).eq(User::getEmail, "1475549985@qq.com"));
        return R.ok(user);

    }

    @Override
    public UserBean getUser(String username) {
        User user = userMapper.selectOne(Wrappers.lambdaQuery(User.class).eq(User::getUserName, username));
        if(user == null) {
            return null;
        }
        List<String> roleList = new ArrayList<>();
        List<String> permissionList = new ArrayList<>();
        UserBean userBean = new UserBean();
        userBean.setPassword(user.getUserPwd());
        userBean.setUsername(user.getUserName());
        List<Role> roles = roleMapper.selectList(Wrappers.lambdaQuery(Role.class).eq(Role::getUserId, user.getId()));
        for (Role role : roles) {
            //超级管理员
            if(role.getRole()==1){
               roleList.add("admin");
               permissionList.add("all");
            }
            //店铺管理员
            else if(role.getRole()==2){
                roleList.add("shopadmin");
                permissionList.add(role.getShopId().toString()+"shopadmin");
            }
            //店员
            else if(role.getRole()==3){
                roleList.add("shop");
                permissionList.add(role.getShopId().toString()+"shop");
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
        //图形验证码验证
        if(verification.getRepCode().compareTo(ResponseModel.success().getRepCode())!=0){
            return R.error(verification.getRepMsg());
        }
        //获取当前用户
        Subject subject = SecurityUtils.getSubject();
        //封装用户的登录数据
        JWTToken jwtToken = new JWTToken(jwtUtils.generateToken(loginDto.getUserName()));
        try{
            subject.login(jwtToken);
            //限制多处登录
            redisUtils.lSet(loginDto.getUserName()+"token",jwtToken.getPrincipal(),14400);
            if(redisUtils.lGetListSize(loginDto.getUserName()+"token")>1){
                redisUtils.lLPop(loginDto.getUserName()+"token");
            }
            return R.ok(jwtToken.getPrincipal().toString());
        } catch (AuthenticationException e) {
            log.info(e.getMessage());
            return R.error("账号或密码错误！登录失败");
        }
    }

   public R<String> login2(LoginDto loginDto){
       Subject subject = SecurityUtils.getSubject();
       //封装用户的登录数据
       JWTToken jwtToken = new JWTToken(jwtUtils.generateToken(loginDto.getUserName()));
       try{
           subject.login(jwtToken);
           //限制多处登录
           redisUtils.lSet(loginDto.getUserName()+"token",jwtToken.getPrincipal(),14400);
           if(redisUtils.lGetListSize(loginDto.getUserName()+"token")>1){
               redisUtils.lLPop(loginDto.getUserName()+"token");
           }
           return R.ok(jwtToken.getPrincipal().toString());
       } catch (AuthenticationException e) {
           log.info(e.getMessage());
           return R.error("账号或密码错误！登录失败");
       }
   }


}
