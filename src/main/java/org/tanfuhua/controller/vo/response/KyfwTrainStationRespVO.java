package org.tanfuhua.controller.vo.response;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 火车站
 */
@Data
@ApiModel("火车站")
public class KyfwTrainStationRespVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String shortLetter;
    private String chineseName;
    private String englishName;
    private String allLetter;
    private String firstLetter;
}
