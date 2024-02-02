package com.li.wisdomcashier.controller.account;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.anji.captcha.model.vo.CaptchaVO;
import com.li.wisdomcashier.controller.OauthFeignClient;
import com.li.wisdomcashier.controller.account.dto.ChangeEmailDTO;
import com.li.wisdomcashier.controller.account.dto.ChangePwdDTO;
import com.li.wisdomcashier.controller.account.dto.CreateUserDTO;
import com.li.wisdomcashier.controller.account.dto.LoginDTO;
import com.li.wisdomcashier.controller.account.vo.TokenVO;
import com.li.wisdomcashier.controller.account.vo.UserDetailVO;
import com.li.wisdomcashier.entry.R;
import com.li.wisdomcashier.exception.MyAuthenticationException;
import com.li.wisdomcashier.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * @ClassName UserController
 * @Description TODO
 * @Author Nine
 * @Date 2023/9/21 16:26
 * @Version 1.0
 */
@RestController
@RequestMapping("/account")
@Tag(name = "账户相关")
public class AccountController {

    /**
     * 验证码开关
     */
    @Value("${captcha:true}")
    private Boolean chaptcha;

    @Resource
    private UserService userService;

//    @Resource
//    private CaptchaService captchaService;

    @Resource
    OauthFeignClient oauthFeignClient;

    @PostMapping("/createUser")
    @Operation(summary = "创建用户")
    @PermitAll
    R<String> createUser(@RequestBody @Validated CreateUserDTO createUserDTO) {
        return userService.createUser(createUserDTO);
    }

    @PostMapping("/login")
    @Operation(summary = "登录")
    @PermitAll
    R<TokenVO> postAccessToken(@RequestBody @Validated LoginDTO loginDTO) {
        if (Boolean.TRUE.equals(chaptcha)) {
            //图像验证码校验
            CaptchaVO captchaVO = new CaptchaVO();
            captchaVO.setCaptchaVerification(loginDTO.getVerify());
//            ResponseModel verification = captchaService.verification(captchaVO);
//            if (verification.getRepCode().compareTo(ResponseModel.success().getRepCode()) != 0) {
//                return R.error(verification.getRepMsg());
//            }
        }
        //登录逻辑
        String accessToken = oauthFeignClient.postAccessToken("authorization_password",
                loginDTO.getUsername(),
                loginDTO.getPassword(),
                "profile");
        JSONObject parseObj = JSONUtil.parseObj(accessToken);
        String code = parseObj.getStr("code");
        if (Objects.isNull(code)) {
            return R.ok(
                    TokenVO.builder()
                            .token(parseObj.getStr("access_token"))
                            .refresh(parseObj.getStr("refresh_token"))
                            .build()
                    , "登录成功！"
            );
        }
        throw new MyAuthenticationException(parseObj.getStr("msg"));
    }

//    @PostMapping("/getCaptcha")
////    @Operation(summary = "获取图形验证码")
//    @PermitAll
//    public ResponseModel get(@RequestBody CaptchaVO captchaVO) {
//        return captchaService.get(captchaVO);
//    }

//    @PostMapping("/checkCaptcha")
////    @Operation(summary = "图形验证码检查")
//    @PermitAll
//    public ResponseModel check(@RequestBody CaptchaVO captchaVO) {
//        return captchaService.check(captchaVO);
//    }

    @GetMapping("/test")
    @PreAuthorize("@ss.hasPermission(#id,#no)")
    public String test(@RequestParam("id") int id, @RequestParam("no") int no) {
        return "ok!";
    }

    @GetMapping("/getUserCenterMenu")
    @Operation(summary = "用户中心菜单")
    public R<List<Tree<String>>> getUserCenterMenu() {
        return userService.getUserCenterMenu();
    }

    @GetMapping("/getUserDetail")
    @Operation(summary = "获取用户信息")
    public R<UserDetailVO> getUserDetail() {
        return userService.getUserDetail();
    }

    @GetMapping("/changeUserNickName")
    @Operation(summary = "修改用户名")
    public R<String> changeUserNickName(String name) {
        return userService.changeUserNickName(name);
    }

    @Operation(summary = "修改绑定邮箱")
    @PostMapping("/changeUserEmail")
    public R<String> changeUserEmail(@RequestBody @Validated ChangeEmailDTO changeEmailDto) {
        return userService.changeUserEmail(changeEmailDto);
    }

    @Operation(summary = "修改用户密码")
    @PostMapping("/changePwd")
    public R<String> changePwd(@RequestBody @Validated ChangePwdDTO changePwdDto) {
        return userService.changePwd(changePwdDto);
    }

}
