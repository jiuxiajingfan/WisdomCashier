package com.li.wisdomcashier.config.wxPay;

import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;

import java.security.*;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


/**
 * 公钥模式
 */
public class PubKeyVerifier implements Verifier {

    private PublicKey publicKey;

    private String publicKeyId;

    public PubKeyVerifier(String publicKeyId, String publicKey) {
        this.publicKeyId = publicKeyId;
        try {
            String replace = publicKey.replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "").replace("\n", "");
            byte[] keyBytes = Base64.getDecoder().decode(replace);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            this.publicKey = keyFactory.generatePublic(spec);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean verify(String serialNumber, byte[] message, String signature) {
        if (!serialNumber.isEmpty() && message.length != 0 && !signature.isEmpty()) {
            if (!this.publicKeyId.equals(serialNumber)) {
                throw new IllegalArgumentException("pub_key_id error");
            }
            try {
                Signature sign = Signature.getInstance("SHA256withRSA");
                sign.initVerify(this.publicKey);
                sign.update(message);
                return sign.verify(Base64.getDecoder().decode(signature));
            } catch (NoSuchAlgorithmException var8) {
                throw new RuntimeException("当前Java环境不支持SHA256withRSA", var8);
            } catch (SignatureException var9) {
                throw new RuntimeException("公钥模式签名验证过程发生了错误", var9);
            } catch (InvalidKeyException var10) {
                throw new RuntimeException("公钥模式无效的证书", var10);
            }
        } else {
            throw new IllegalArgumentException("serialNumber或message或signature为空");
        }
    }

    @Override
    public X509Certificate getValidCertificate() {
        return null;
    }
}
