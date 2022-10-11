package com.li.wisdomcashier.base.service.impl;

import com.li.wisdomcashier.base.entity.dto.SignUpDto;
import com.li.wisdomcashier.base.entity.po.R;
import com.li.wisdomcashier.base.entity.po.User;
import com.li.wisdomcashier.base.mapper.UserMapper;
import com.li.wisdomcashier.base.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.li.wisdomcashier.base.util.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Nine
 * @since 2022-10-11
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private UserMapper userMapper;
    @Override
    public R<String> signUp(SignUpDto signUpDto) {
       if(redisTemplate.opsForValue().get(signUpDto.getEmail()).toString().compareTo(signUpDto.getCode())!=0)
           return R.error("验证码错误！");
        User convert = BeanUtils.convert(signUpDto, User.class);
        return R.ok();
    }
}
