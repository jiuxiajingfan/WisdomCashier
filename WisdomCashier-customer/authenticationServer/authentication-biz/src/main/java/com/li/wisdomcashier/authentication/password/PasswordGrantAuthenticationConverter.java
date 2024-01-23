package com.li.wisdomcashier.authentication.password;

import com.li.wisdomcashier.authentication.constant.OAuth2Constant;
import com.li.wisdomcashier.exception.MyAuthenticationException;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class PasswordGrantAuthenticationConverter implements AuthenticationConverter {

    @Nullable
    @Override
    public Authentication convert(HttpServletRequest request) {
        String granType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
        if (!OAuth2Constant.GRANT_TYPE_PASSWORD.equals(granType)) {
            return null;
        }
        Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();

        //从request中提取请求参数
        MultiValueMap<String, String> parameters = getParameters(request);

        //username
        String username = parameters.getFirst(OAuth2ParameterNames.USERNAME);
        if (!StringUtils.hasText(username) ||
                parameters.get(OAuth2ParameterNames.USERNAME).size() != 1) {
            throw new MyAuthenticationException("用户名不能为空！");
        }
        String password = parameters.getFirst(OAuth2ParameterNames.PASSWORD);
        if (!StringUtils.hasText(password) ||
                parameters.get(OAuth2ParameterNames.PASSWORD).size() != 1) {
            throw new MyAuthenticationException("密码不能为空！");
        }

        //收集要传入PasswordGrantAuthenticationToken构造方法的参数
        //该参数接下来在PasswordGrantAuthenticationProvider中使用
        HashMap<String, Object> additionalParameters = new HashMap<>();
        //遍历从request中提取的参数，排除掉grant_type,client_id,code等字段参数
        parameters.forEach((key, value) -> {
            if (!key.equals(OAuth2ParameterNames.GRANT_TYPE) &&
                    !key.equals(OAuth2ParameterNames.CLIENT_ID) &&
                    !key.equals(OAuth2ParameterNames.CODE)) {
                additionalParameters.put(key, value.get(0));
            }
        });

        //返回自定义的PasswordGrantAuthenticationToken
        return new PasswordGrantAuthenticationToken(clientPrincipal, additionalParameters);
    }

    /**
     * 提取请求参数
     *
     * @param request
     * @return
     */
    private MultiValueMap<String, String> getParameters(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        LinkedMultiValueMap<String, String> parameters = new LinkedMultiValueMap<>(parameterMap.size());
        parameterMap.forEach((key, values) -> {
            for (String value : values) {
                parameters.add(key, value);
            }
        });
        return parameters;
    }
}
