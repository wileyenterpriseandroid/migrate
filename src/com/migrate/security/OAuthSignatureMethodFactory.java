package com.migrate.security;

import java.util.Map;

import javax.crypto.spec.SecretKeySpec;
import org.springframework.security.oauth.common.signature.OAuthSignatureMethod;
import org.springframework.security.oauth.common.OAuthException;
import org.springframework.security.oauth.common.signature.HMAC_SHA1SignatureMethod;
import org.springframework.security.oauth.common.signature.InvalidSignatureException;

public class OAuthSignatureMethodFactory {
    private Map<String, String>oauthKeyMap;

    public void setOauthKeyMap(Map<String, String> oauthKeyMap) {
        this.oauthKeyMap = oauthKeyMap;
    }

    public OAuthSignatureMethod getOAuthSignatureMethod(String consumerKey) {
        String serectkey = oauthKeyMap.get(consumerKey);
        SecretKeySpec k = new SecretKeySpec(serectkey.getBytes(), "AES");
        return new HMAC_SHA1SignatureMethod(k);
    }
}
