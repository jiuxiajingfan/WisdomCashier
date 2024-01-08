package com.li.wisdomcashier.utils;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import com.li.wisdomcashier.exception.MyAuthenticationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;

import javax.crypto.BadPaddingException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class AccessTokenUtils {

    private final static Log logger = LogFactory.getLog(AccessTokenUtils.class);

    /**
     * 随机数分隔符
     */
    public static final String RDM_SEP = "#";

    /**
     *  AccessToken 签名
     */
    public static JwtClaimsSet.Builder signAccessToken(JwtClaimsSet.Builder claims, String privateKey){

        String uuIdStr = UUID.randomUUID().toString();
        long currentTimeMillis = System.currentTimeMillis();

        String rdmSource = uuIdStr + RDM_SEP + claims.build().getExpiresAt().getEpochSecond() + RDM_SEP + currentTimeMillis;
        String rdmTarget = RSAUtils.encryptByPriKey(rdmSource,privateKey);
        String signSource = uuIdStr + RDM_SEP + currentTimeMillis;
        String signature;
        try {
            signature = RSAUtils.sign(signSource.getBytes(), Base64Decoder.decode(privateKey));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        claims.claim("rdm",rdmTarget);
        claims.claim("sign",signature);
        
        return claims;
    }

    /**
     * 验签
     */
    public static boolean verifyAccessToken(String authorizationToken,String publicKey){

        String accessToken = authorizationToken.replace("Bearer ","");
        try {
            JWT jwtToken = JWTUtil.parseToken(accessToken);
            JWTPayload jwtPayload = jwtToken.getPayload();
            String rdm = jwtPayload.getClaim("rdm")+ "";
            String sign = jwtPayload.getClaim("sign") +"";
            //解密
            String rdmSource;
            rdmSource = RSAUtils.decryptByPubKey(rdm,publicKey);
            String[] rdmData =rdmSource.split(RDM_SEP);
            long exp = Long.parseLong(rdmData[1]);
            long now = Instant.now().getEpochSecond();
            if(exp<now){
                throw new MyAuthenticationException("账户已过期，请重新登录!");
            }
            String signSource = rdmData[0] + RDM_SEP + rdmData[2];
            //验签
            boolean verifyResult;
            verifyResult = RSAUtils.verify(signSource.getBytes(), Base64Decoder.decode(sign),Base64Decoder.decode(publicKey));
            if(!verifyResult){
                return false;
            }
            JSONArray authorities = (JSONArray) jwtPayload.getClaim("authorities");
            List<SimpleGrantedAuthority> collect = authorities.stream().map(e -> {
                JSONObject jsonObject = JSONUtil.parseObj(e);
                return new SimpleGrantedAuthority(jsonObject.get("role").toString());
            }).toList();
            UsernamePasswordAuthenticationToken authenticated;
            authenticated = UsernamePasswordAuthenticationToken.authenticated(
                    jwtPayload.getClaim("sub"), null, collect);
            SecurityContextHolder.getContext().setAuthentication(authenticated);
        }catch (JSONException e){
            throw new MyAuthenticationException("无效Token!");
        } catch (BadPaddingException e) {
            throw new MyAuthenticationException("无效Token！");
        } catch (Exception e) {
            logger.info("accessToken验签异常，accessToken="+accessToken,e);
            throw new RuntimeException(e);
        }
        return true;
    }
}