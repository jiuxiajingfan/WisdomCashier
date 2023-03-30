package com.li.wisdomcashier.base.service.impl;

import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.li.wisdomcashier.base.bean.UserBean;
import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.entity.dto.DeleteDTO;
import com.li.wisdomcashier.base.entity.dto.LoginDTO;
import com.li.wisdomcashier.base.entity.dto.ShopQueryDTO;
import com.li.wisdomcashier.base.entity.po.AdminUser;
import com.li.wisdomcashier.base.entity.po.JWTToken;
import com.li.wisdomcashier.base.entity.po.User;
import com.li.wisdomcashier.base.mapper.UserMapper;

import com.sun.management.OperatingSystemMXBean;
import com.li.wisdomcashier.base.entity.vo.EChartVO;
import com.li.wisdomcashier.base.entity.vo.UserVo;
import com.li.wisdomcashier.base.mapper.AdminUserMapper;
import com.li.wisdomcashier.base.service.AdminUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.li.wisdomcashier.base.util.JwtUtils;
import com.li.wisdomcashier.base.util.RedisUtils;
import com.li.wisdomcashier.base.util.UserUtils;
import io.jsonwebtoken.Claims;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import oshi.hardware.CentralProcessor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lsw
 * @since 2023-03-28
 */
@Service
@Slf4j
public class AdminUserServiceImpl extends ServiceImpl<AdminUserMapper, AdminUser> implements AdminUserService {

    @Resource
    private AdminUserMapper adminUserMapper;

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private UserMapper userMapper;

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
            JWTToken jwtToken = new JWTToken(jwtUtils.generateToken(loginDto.getUserName(),"AdminRealm"));
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
    public R<AdminUser> test() {
        AdminUser user = adminUserMapper.selectOne(Wrappers.lambdaQuery(AdminUser.class)
                .eq(AdminUser::getEmail, "1475549985@qq.com"));
        return R.ok(user);

    }

    @Override
    public R<UserVo> getUser() {
        UserVo copy = CglibUtil.copy(UserUtils.getAdminUser(), UserVo.class);
        if (!StrUtil.isBlank(copy.getPhone())) {
            copy.setPhone(DesensitizedUtil.mobilePhone(copy.getPhone()));
        } else
            copy.setPhone("");
        copy.setEmail(DesensitizedUtil.email(copy.getEmail()));
        return R.ok(copy);
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

    @SneakyThrows
    @Override
    public R<List<List<EChartVO>>> getSystem() {
        List<List<EChartVO>> ans = new ArrayList<>();
        List<EChartVO> a0 = new ArrayList<>();
        List<EChartVO> a1 = new ArrayList<>();
        OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        // 总的物理内存
        String totalMemorySize = new DecimalFormat("#.##").format(osmxb.getTotalPhysicalMemorySize() / 1024.0 / 1024 / 1024) + "G";
        a0.add(new EChartVO("总内存",totalMemorySize));
        a1.add(new  EChartVO("内存使用率",new DecimalFormat("#.##").format(((osmxb.getTotalPhysicalMemorySize() - osmxb.getFreePhysicalMemorySize()) / 1024.0 / 1024 / 1024)/((osmxb.getTotalPhysicalMemorySize()) / 1024.0 / 1024 / 1024))));
        oshi.SystemInfo si = new oshi.SystemInfo();
        CentralProcessor processor = si.getHardware().getProcessor();
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        // 睡眠1s
        TimeUnit.SECONDS.sleep(1);
        long[] ticks = processor.getSystemCpuLoadTicks();
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        long cSys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;
        a0.add(new EChartVO("总核心数",Integer.toString(processor.getLogicalProcessorCount())));
        a1.add(new EChartVO("cpu使用率",new DecimalFormat("#.##").format((1-idle * 1.0 / totalCpu))));
        a0.add(new EChartVO("Cpu信息",processor.toString()));
        ans.add(a0);
        ans.add(a1);
        return R.ok(ans);
    }

    @Override
    public R<IPage<UserVo>> getUserPage(ShopQueryDTO queryDTO) {
        Page<User> page = new Page(queryDTO.getCurrent(),queryDTO.getPageSize());
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery(User.class)
                .eq(!StringUtils.isBlank(queryDTO.getName()), User::getId, queryDTO.getName())
                .orderByDesc(User::getGmtCreate);
        IPage<UserVo> convert = userMapper.selectPage(page, wrapper).convert(e -> {
            UserVo copy = CglibUtil.copy(e, UserVo.class);
            copy.setRoleEnum(e.getStatus());
            copy.setPhone(DesensitizedUtil.mobilePhone(copy.getPhone()));
            return copy;
        });
        return R.ok(convert);
    }

    @Override
    public R<String> changeUserStatus(DeleteDTO deleteDTO) {
        User user = userMapper.selectOne(Wrappers.lambdaQuery(User.class).
                eq(User::getId, deleteDTO.getId())
        );
        if(Objects.isNull(user)){
            return R.error("无该用户！");
        }
        user.setStatus(Integer.parseInt(deleteDTO.getSid()));
        if(user.getStatus()!=0) {
            redisUtils.del(user.getUserName() + "token");
        }
       return R.ok(userMapper.updateById(user)==1?"修改成功！":"修改失败！");
    }
}
