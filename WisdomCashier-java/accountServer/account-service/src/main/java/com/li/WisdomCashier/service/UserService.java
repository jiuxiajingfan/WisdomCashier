package com.li.WisdomCashier.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.li.WisdomCashier.dto.CreateUserDTO;
import com.li.WisdomCashier.po.User;
import com.li.WisdomCashier.pojo.R;

public interface UserService extends IService<User> {
    R<String> createUser(CreateUserDTO createUserDTO);

}
