package com.li.wisdomcashier.base.util;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.common.StatusFailException;
import com.li.wisdomcashier.base.entity.po.Shop;
import com.li.wisdomcashier.base.entity.po.User;
import com.li.wisdomcashier.base.enums.RoleEnum;
import com.li.wisdomcashier.base.mapper.ShopMapper;
import com.li.wisdomcashier.base.mapper.UserMapper;
import io.jsonwebtoken.Claims;
import lombok.SneakyThrows;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.subject.Subject;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.validation.ValidationException;
import java.util.Objects;

/**
 * @ClassName UserUtils
 * @Description TODO
 * @Author Nine
 * @Date 2022/12/2 14:10
 * @Version 1.0
 */
@Component
public class UserUtils {

    @Resource
    private UserMapper userMapper;

    @Resource
    private ShopMapper shopMapper;

    private static UserUtils userUtils;

    @Resource
    private RedisUtils redisUtils;


    @PostConstruct
    public void init(){
        userUtils = this;
        userUtils.userMapper = this.userMapper;
        userUtils.shopMapper = this.shopMapper;
        userUtils.redisUtils = this.redisUtils;
    }

    public static User getUser(){
        JwtUtils jwtUtils = new JwtUtils();
        Subject subject = SecurityUtils.getSubject();
        Claims claimByToken = jwtUtils.getClaimByToken(subject.getPrincipal().toString());
        User user = userUtils.userMapper.selectOne(Wrappers.lambdaQuery(User.class).eq(User::getUserName, claimByToken.getSubject()));
        System.out.println("hello");
        return  user;
    }

    /**
     * 验证用户是否拥有相印权限访问接口
     * @param shopId 店铺ID
     * @param level 用户级别 {@link com.li.wisdomcashier.base.enums.RoleEnum}
     * @return true/false
     */
    @SneakyThrows
    public static Shop hasPermissions(String shopId, Integer level){
        Subject subject = SecurityUtils.getSubject();
        if(!subject.isPermitted(shopId+level)){
            throw new AuthorizationException("无权操作！");
        }
        Integer status;
        Shop shop = new Shop();
        if(userUtils.redisUtils.hasKey(shopId+"status")){
            shop = (Shop)userUtils.redisUtils.get(shopId+"status");
            status = shop.getStatus();
        }else{
            shop = userUtils.shopMapper.selectOne(Wrappers.lambdaQuery(Shop.class)
                    .eq(Shop::getId, Long.parseLong(shopId)));
            if(Objects.isNull(shop))
            {
                throw new StatusFailException("不存在该店铺");
            }
            status = shop.getStatus();
            userUtils.redisUtils.set(shopId+"status",shop);
        }
        if(status == 1)
        {
            throw new StatusFailException("该店铺已被封禁，请联系管理员解封！");
        }
        if(status == 2){
            throw new StatusFailException("该店铺已注销！");
        }
        return shop;
    }
}
