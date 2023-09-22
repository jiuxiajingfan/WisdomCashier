package com.li.WisdomCashier.service.impl;

import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.li.WisdomCashier.dto.CreateUserDTO;
import com.li.WisdomCashier.mapper.UserMapper;
import com.li.WisdomCashier.po.User;
import com.li.WisdomCashier.pojo.R;
import com.li.WisdomCashier.service.UserService;
import com.li.WisdomCashier.utils.RedisUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private RabbitTemplate rabbitTemplate;

    @Resource
    private RedisUtils redisUtils;

    @Override
    public R<String> createUser(CreateUserDTO createUserDTO) {
        List<User> users = userMapper.selectList(Wrappers.lambdaQuery(User.class)
                .eq(User::getEmail, createUserDTO.getEmail())
                .or()
                .eq(User::getUserName,createUserDTO.getUserName())
        );
        if (!users.isEmpty()) {
            return R.error("该邮箱已被注册，请勿重复注册！");
        }
        String redisCode = (String) redisUtils.get(REGISTER_CODE + createUserDTO.getEmail());
        if (redisCode == null ||
                redisCode.compareTo(createUserDTO.getCode()) != 0) {
            return R.error("验证码错误！");
        }
        User copy = CglibUtil.copy(createUserDTO, User.class);
        copy.setUserPwd(new BCryptPasswordEncoder().encode(createUserDTO.getPassword()));
        userMapper.insert(copy);
        redisUtils.del(REGISTER_CODE + createUserDTO.getEmail());
        return R.ok("注册成功！");
    }
}
