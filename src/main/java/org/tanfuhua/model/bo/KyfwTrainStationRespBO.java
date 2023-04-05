package org.tanfuhua.model.bo;

import lombok.Data;

/**
 * 火车站
 */
@Data
public class KyfwTrainStationRespBO {
    private Integer id;
    private String shortLetter;
    private String chineseName;
    private String englishName;
    private String allLetter;
    private String firstLetter;
}
