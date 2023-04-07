package org.tanfuhua.model.bo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.tanfuhua.common.json.jsondeserialize.BooleanJsonDeserialize;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author: gaofubo
 * @date: 2021/4/18
 */
@Data
public class KyfwRemainingTicketRespBO implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonDeserialize(using = BooleanJsonDeserialize.class)
    private Boolean flag;
    private Map<String, String> map;
    private List<String> result;
}
