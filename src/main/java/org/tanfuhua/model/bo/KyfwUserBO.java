package org.tanfuhua.model.bo;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.tanfuhua.common.json.jsondeserialize.BooleanJsonDeserialize;

import java.io.Serializable;
import java.util.List;

/**
 * 用户信息
 *
 * @author: gaofubo
 * @date: 2021/1/30
 */
@Data
public class KyfwUserBO implements Serializable {
    private static final long serialVersionUID = 7488300053040617381L;
    @JsonAlias("isstudentDate")
    private Boolean isStudentDate;
    @JsonAlias("studentDate")
    private List<String> studentDate;
    @JsonAlias("user_name")
    private String userName;
    @JsonAlias("name")
    private String name;
    @JsonAlias("is_login")
    @JsonDeserialize(using = BooleanJsonDeserialize.class)
    private Boolean isLogin;
}
