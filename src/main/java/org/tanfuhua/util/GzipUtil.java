package org.tanfuhua.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

/**
 * @author: gaofubo
 * @date: 2021/4/18
 */
@Slf4j
@UtilityClass
public class GzipUtil {

    public static String unGzip(byte[] bytes) {
        try {
            return new String(unGzip(new ByteArrayInputStream(bytes)), StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public static byte[] unGzip(InputStream inputStream) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream)) {
            byte[] buf = new byte[4096];
            int len = -1;
            while ((len = gzipInputStream.read(buf, 0, buf.length)) != -1) {
                byteArrayOutputStream.write(buf, 0, len);
            }
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            try {
                inputStream.reset();
                return IOUtils.toByteArray(inputStream);
            } catch (IOException ee) {
                throw new RuntimeException(ee.getMessage(), ee);
            }
        }
    }
}
