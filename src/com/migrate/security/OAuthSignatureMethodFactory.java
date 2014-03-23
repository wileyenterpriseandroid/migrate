package com.migrate.security;

import java.util.Map;

import javax.crypto.spec.SecretKeySpec;
import org.springframework.security.oauth.common.signature.OAuthSignatureMethod;
import org.springframework.security.oauth.common.OAuthException;
import org.springframework.security.oauth.common.signature.HMAC_SHA1SignatureMethod;
import org.springframework.security.oauth.common.signature.InvalidSignatureException;

public class OAuthSignatureMethodFactory {
    // A mapping from consumer key, userid to secret key (the token?)
    private Map<String, String>oauthKeyMap;

    public void setOauthKeyMap(Map<String, String> oauthKeyMap) {
        this.oauthKeyMap = oauthKeyMap;
    }

    public OAuthSignatureMethod getOAuthSignatureMethod(String consumerKey) {
        String secretKey = oauthKeyMap.get(consumerKey);
        SecretKeySpec k = new SecretKeySpec(secretKey.getBytes(), "AES");
        return new HMAC_SHA1SignatureMethod(k);
    }

    // consumerKey=user_id,signature=
    //
    // Extract values for consumerKey and signatureKey
    //
    // get the oauth method, hmac_sha1, from the consumer key value, this creates
    // the method with an aes secret key spec.
    //
    // Extract the signature value
    //
    // use the method to verify: consumerKey,signatureValue
    //
    // the

    public String validateUserId(String oauth) {
        oauth = oauth.trim();
        String[] temp = oauth.split(",");
        String userIdConsumerKey = temp[1].replace("consumerKey=", "");
        OAuthSignatureMethod oauthMethod = getOAuthSignatureMethod(userIdConsumerKey);

        String secretKeySignature = temp[2].replace("signature=", "");
//        OAuthSignatureMethod oauthMethod = oAuthSignatureMethodFactory.getOAuthSignatureMethod(consumerKey);
        oauthMethod.verify(temp[0] + "," + temp[1], secretKeySignature);
        return userIdConsumerKey;
    }
}
