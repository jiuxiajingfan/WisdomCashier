package com.li.wisdomcashier.base.service.impl;

import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.entity.dto.SignUpDto;
import com.li.wisdomcashier.base.entity.po.User;
import com.li.wisdomcashier.base.mapper.UserMapper;
import com.li.wisdomcashier.base.service.EmailService;
import com.li.wisdomcashier.base.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.li.wisdomcashier.base.util.CodeUtils;
import com.li.wisdomcashier.base.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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

    @Override
    public R<String> signUp(SignUpDto signUpDto) {
        if(!signUpDto.getCode().equals(redisUtils.get(signUpDto.getEmail()))) {
           return R.error("验证码错误！");
        }
        List<User> users = userMapper.selectList(Wrappers.lambdaQuery(User.class).eq(User::getEmail, signUpDto.getEmail()));
        if(!users.isEmpty()){
            return R.error("该邮箱已被注册！请勿重复注册。");
        }
        redisUtils.del(signUpDto.getEmail());
        User convert = CglibUtil.copy(signUpDto, User.class);
        convert.setStatus(0);
        userMapper.insert(convert);
        return R.ok();
    }

    @Override
    public R<String> getCode(String email) {
        log.info("调用邮箱服务：{}",email);
        if (redisUtils.hasKey(email)) {
            return R.error("请勿重复调用~");
        } else {
            String code = CodeUtils.getCode();
            redisUtils.set(email, code,90);
            emailService.sendSimpleMail(email, code);
        }
        return R.ok("验证码已发送，请耐心等待~");
    }

    @Override
    public R<User> test() {
        User user = userMapper.selectOne(Wrappers.lambdaQuery(User.class).eq(User::getEmail, "1475549985@qq.com"));
        return R.ok(user);

    }

}
