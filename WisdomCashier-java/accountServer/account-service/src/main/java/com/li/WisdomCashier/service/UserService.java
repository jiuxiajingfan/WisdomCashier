package com.li.WisdomCashier.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.service.IService;
import com.li.WisdomCashier.controller.account.dto.ChangeEmailDTO;
import com.li.WisdomCashier.controller.account.vo.UserDetailVO;
import com.li.WisdomCashier.dto.CreateUserDTO;
import com.li.WisdomCashier.po.User;
import com.li.WisdomCashier.pojo.R;

import java.util.List;

public interface UserService extends IService<User> {
    R<String> createUser(CreateUserDTO createUserDTO);

    R<List<Tree<String>>> getUserCenterMenu();

    R<UserDetailVO> getUserDetail();

    R<String> changeUserNickName(String name);

    R<String> changeUserEmail(ChangeEmailDTO changeEmailDto);
}
