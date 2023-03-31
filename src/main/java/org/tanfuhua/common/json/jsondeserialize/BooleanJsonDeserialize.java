package org.tanfuhua.common.json.jsondeserialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Objects;

/**
 * @author: gaofubo
 * @date: 2021/4/18
 */
public class BooleanJsonDeserialize extends JsonDeserializer<Boolean> {
    @Override
    public Boolean deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (p == null) {
            return false;
        }
        if (Objects.equals(p.getText(), "Y")) {
            return true;
        }
        if (StringUtils.isNumeric(p.getText())) {
            return Integer.parseInt(p.getText()) > 0;
        }
        return false;
    }
}
