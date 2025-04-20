package org.tanfuhua.lowcode.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author gaofubo
 * @date 2023/3/26
 */
@UtilityClass
@Slf4j
public class SSLUtil {

    public static HostnameVerifier createTrustHostnameVerifier() {
        return (s, sslSession) -> {
            log.info("HostnameVerifier:host={}SSL信任", s);
            return true;
        };
    }

    @SneakyThrows
    public static SSLSocketFactory createSSLSocketFactory() {
        TrustManager[] trustAllCerts = new TrustManager[1];
        TrustManager tm = new MiTM();
        trustAllCerts[0] = tm;
        javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL", "SunJSSE");
        sc.init(null, trustAllCerts, null);
        return sc.getSocketFactory();
    }

    public static class MiTM implements TrustManager, X509TrustManager {

        public boolean isServerTrusted(X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(X509Certificate[] certs) {
            return true;
        }

        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

}
