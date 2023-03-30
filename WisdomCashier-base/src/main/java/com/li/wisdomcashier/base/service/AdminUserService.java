package com.li.wisdomcashier.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.li.wisdomcashier.base.bean.UserBean;
import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.entity.dto.DeleteDTO;
import com.li.wisdomcashier.base.entity.dto.LoginDTO;
import com.li.wisdomcashier.base.entity.dto.ShopQueryDTO;
import com.li.wisdomcashier.base.entity.po.AdminUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.li.wisdomcashier.base.entity.vo.EChartVO;
import com.li.wisdomcashier.base.entity.vo.UserVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lsw
 * @since 2023-03-28
 */
public interface AdminUserService extends IService<AdminUser> {

    UserBean getUser(String username);

    R<String> login(LoginDTO loginDto);

    R<AdminUser> test();

    public R<UserVo> getUser();

    R<String> loginOut(HttpServletRequest httpServletRequest);

    R<List<List<EChartVO>>>  getSystem();

    R<IPage<UserVo>> getUserPage(ShopQueryDTO queryDTO);

    R<String> changeUserStatus(DeleteDTO deleteDTO);

}
