package com.li.WisdomCashier.controller;

import cn.hutool.core.lang.tree.Tree;
import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.li.WisdomCashier.dto.CreateUserDTO;
import com.li.WisdomCashier.entry.dto.LoginDTO;
import com.li.WisdomCashier.entry.dto.TokenDTO;
import com.li.WisdomCashier.pojo.R;
import com.li.WisdomCashier.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
    R<String> createUser(@RequestBody @Validated CreateUserDTO createUserDTO){
        return userService.createUser(createUserDTO);
    };
    @PostMapping("/login")
    R<TokenDTO> postAccessToken(@RequestBody @Validated LoginDTO loginDTO) {
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
            TokenDTO token = TokenDTO.builder()
                    .token(accessToken.getValue())
                    .refresh(String.valueOf(accessToken.getRefreshToken()))
                    .build();
            return R.ok(token, "登录成功！");
        }
    }

    @PostMapping("/checkToken")
    R<Map<String, ?>> checkToken(@RequestParam("token") String value) {
        Map<String, ?> stringMap = oauthFeignClient.checkToken(value);
        return R.ok(stringMap);
    }

    @PostMapping("/getCaptcha")
    public ResponseModel get(@RequestBody CaptchaVO captchaVO){
        return captchaService.get(captchaVO);
    }

    @PostMapping("/checkCaptcha")
    public ResponseModel check(@RequestBody CaptchaVO captchaVO){
        return captchaService.check(captchaVO);
    }

    @GetMapping("/test")
    @PreAuthorize("hasPermission(#id,#no)")
    public String test(@RequestParam("id") int id,@RequestParam("no")int no){
        return "ok!";
    }

    @GetMapping("/getUserCenterMenu")
    public  R<List<Tree<String>>> getUserCenterMenu(){
        return userService.getUserCenterMenu();
    }
}
