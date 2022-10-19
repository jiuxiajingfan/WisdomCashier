package com.li.wisdomcashier.base.bean;


import lombok.Data;

import java.util.List;

/**
 * @ClassName UserBean
 * @Description TODO
 * @Author Nine
 * @Date 2022/10/17 22:01
 * @Version 1.0
 */
@Data
public class UserBean {
    private String username;

    private String password;

    private List<String> role;

    private List<String> permission;
}
