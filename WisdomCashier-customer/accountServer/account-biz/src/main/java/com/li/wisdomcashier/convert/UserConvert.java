package com.li.wisdomcashier.convert;

import com.li.wisdomcashier.controller.account.vo.UserDetailVO;
import com.li.wisdomcashier.entry.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserConvert {
    UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);

    UserDetailVO toUserDetailVO(User user);
}
