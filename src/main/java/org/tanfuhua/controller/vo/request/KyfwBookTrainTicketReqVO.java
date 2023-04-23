package org.tanfuhua.controller.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.tanfuhua.enums.KyfwSeatTypeEnum;

import java.io.Serializable;
import java.util.List;

/**
 * @author gaofubo
 * @date 2023/3/26
 */
@Data
@ApiModel("订票VO")
public class KyfwBookTrainTicketReqVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("火车编号，如：240000G10335")
    private String trainNo;
    @ApiModelProperty("火车code，如：G103")
    private String stationTrainCode;
    @ApiModelProperty("查询火车票VO")
    private KyfwQueryTicketReqVO queryTicketReqVO;
    @ApiModelProperty("订票乘客VO集合")
    private List<KyfwBookPassengerReqVO> passengerReqVOList;

    @Data
    @ApiModel("订票乘客VO")
    public static class KyfwBookPassengerReqVO implements Serializable {
        private static final long serialVersionUID = 1L;
        @ApiModelProperty("座位类型，如：" +
                "SWZ: \"9_商务座\"," +
                "TZ: \"P_特等座\"," +
                "ZY: \"M_一等座\"," +
                "ZE: \"O_二等座\"," +
                "GR: \"6_高级软卧\"," +
                "RW: \"4_软卧\"," +
                "SRRB: \"F_动卧\"," +
                "YW: \"3_硬卧\"," +
                "RZ: \"2_软座\"," +
                "YZ: \"1_硬座\"," +
                "WZ: \"1_无座\"," +
                "QT: \"H_其他\"")
        private KyfwSeatTypeEnum seatType;
        @ApiModelProperty("乘客UUID，如：3dfa4085d9a1e5dee2d332fa611b9e407f48d71d1daed8cc39df45005cd4ad3b")
        private String passengerUuid;
    }

}
