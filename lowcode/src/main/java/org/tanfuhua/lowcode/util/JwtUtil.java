package org.tanfuhua.lowcode.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.tanfuhua.common.constant.Constant;
import org.tanfuhua.exception.LoginExpireException;

import java.util.Collections;
import java.util.Date;

/**
 * JWT工具类
 **/
@Slf4j
public class JwtUtil {

    public static DecodedJWT decodeAuthorizationWithVerify(String token, String secret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            return verifier.verify(token);
        } catch (SignatureVerificationException e) {
            log.error("签名不一致:token={},secret:{}", token, secret);
            throw new LoginExpireException();
        } catch (TokenExpiredException e) {
            log.error("Token过期:token={},secret:{}", token, secret);
            throw new LoginExpireException();
        } catch (AlgorithmMismatchException e) {
            log.error("算法不一致:token={},secret:{}", token, secret);
            throw new LoginExpireException();
        } catch (InvalidClaimException e) {
            log.error("Payload非法:token={},secret:{}", token, secret);
            throw new LoginExpireException();
        } catch (Exception e) {
            log.error(String.format("Token失败:token=%s,secret:%s,error:%s", token, secret, e.getMessage()), e);
            throw new LoginExpireException();
        }
    }

    public static DecodedJWT decodeAuthorization(String token) {
        try {
            return JWT.decode(token);
        } catch (Exception e) {
            log.error(String.format("Token失败:token=%s,error:%s", token, e.getMessage()), e);
            throw new LoginExpireException();
        }
    }


    public static String createAuthorization(Long userId, String secret, Date expireDate) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
                // Header: 默认有alg：表示签名的算法类型，比如HMAC SHA256 或者 RSA。typ：代表这个token令牌的类型，比如JWT。
                .withHeader(Collections.emptyMap())
                // Payload
                .withClaim(Constant.Str.LOWCODE_TOKEN_USER_ID, userId)
                .withExpiresAt(expireDate)
                // Signature
                .sign(algorithm);

    }

}