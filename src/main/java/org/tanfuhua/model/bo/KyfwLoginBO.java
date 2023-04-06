package org.tanfuhua.model.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author gaofubo
 * @date 2021/7/25
 */
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class KyfwLoginBO {

    private String kyfwAccount;

    private String kyfwPassword;

}
