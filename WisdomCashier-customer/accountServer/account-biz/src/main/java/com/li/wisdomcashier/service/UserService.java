package com.li.wisdomcashier.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.service.IService;
import com.li.wisdomcashier.controller.account.dto.ChangeEmailDTO;
import com.li.wisdomcashier.controller.account.dto.ChangePwdDTO;
import com.li.wisdomcashier.controller.account.vo.UserDetailVO;
import com.li.wisdomcashier.dto.CreateUserDTO;
import com.li.wisdomcashier.entry.R;
import com.li.wisdomcashier.entry.User;

import java.util.List;

public interface UserService extends IService<User> {
    R<String> createUser(CreateUserDTO createUserDTO);

    R<List<Tree<String>>> getUserCenterMenu();

    R<UserDetailVO> getUserDetail();

    R<String> changeUserNickName(String name);

    R<String> changeUserEmail(ChangeEmailDTO changeEmailDto);

    R<String> changePwd(ChangePwdDTO changePwdDto);
}
