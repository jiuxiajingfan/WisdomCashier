package com.li.WisdomCashier.controller.account;

import cn.hutool.core.lang.tree.Tree;
import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.li.WisdomCashier.controller.OauthFeignClient;
import com.li.WisdomCashier.controller.account.vo.TokenVO;
import com.li.WisdomCashier.controller.account.vo.UserDetailVO;
import com.li.WisdomCashier.dto.CreateUserDTO;
import com.li.WisdomCashier.controller.account.dto.LoginDTO;
import com.li.WisdomCashier.pojo.R;
import com.li.WisdomCashier.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import java.util.List;
import java.util.Map;

/**
 * @ClassName UserController
 * @Description TODO
 * @Author Nine
 * @Date 2023/9/21 16:26
 * @Version 1.0
 */
@RestController
@RequestMapping("/account")
@Api(tags = "账户相关")
public class AccountController {

    /**
     * 验证码开关
     */
    @Value("${captcha:true}")
    private Boolean chaptcha;

    @Resource
    private UserService userService;

    @Resource
    private CaptchaService captchaService;

    @Resource
    OauthFeignClient oauthFeignClient;

    @PostMapping("/createUser")
    @ApiOperation("创建用户")
    @PermitAll
    R<String> createUser(@RequestBody @Validated CreateUserDTO createUserDTO){
        return userService.createUser(createUserDTO);
    };
    @PostMapping("/login")
    @ApiOperation("登录")
    @PermitAll
    R<TokenVO> postAccessToken(@RequestBody @Validated LoginDTO loginDTO) {
        if(chaptcha) {
            //图像验证码校验
            CaptchaVO captchaVO = new CaptchaVO();
            captchaVO.setCaptchaVerification(loginDTO.getVerify());
            ResponseModel verification = captchaService.verification(captchaVO);
            if (verification.getRepCode().compareTo(ResponseModel.success().getRepCode()) != 0) {
                return R.error(verification.getRepMsg());
            }
        }
        //登录逻辑
        OAuth2AccessToken accessToken = oauthFeignClient.postAccessToken("password",
                loginDTO.getUsername(),
                loginDTO.getPassword());
        if (accessToken.getValue() == null) {
            return R.error((String) accessToken.getAdditionalInformation().get("msg"), 401);
        } else {
            return R.ok(
                    TokenVO.builder()
                    .token(accessToken.getValue())
                    .refresh(String.valueOf(accessToken.getRefreshToken()))
                    .build(),
                    "登录成功！"
            );
        }
    }

    @PostMapping("/getCaptcha")
    @ApiOperation("获取图形验证码")
    @PermitAll
    public ResponseModel get(@RequestBody CaptchaVO captchaVO){
        return captchaService.get(captchaVO);
    }

    @PostMapping("/checkCaptcha")
    @ApiOperation("图形验证码检查")
    @PermitAll
    public ResponseModel check(@RequestBody CaptchaVO captchaVO){
        return captchaService.check(captchaVO);
    }

    @GetMapping("/test")
    @PreAuthorize("hasPermission(#id,#no)")
    public String test(@RequestParam("id") int id,@RequestParam("no")int no){
        return "ok!";
    }

    @GetMapping("/getUserCenterMenu")
    @ApiOperation("用户中心菜单")
    public  R<List<Tree<String>>> getUserCenterMenu(){
        return userService.getUserCenterMenu();
    }

    @GetMapping("/getUserDetail")
    @ApiOperation("获取用户信息")
    public R<UserDetailVO> getUserDetail(){
        return userService.getUserDetail();
    }
}
