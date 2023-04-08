package org.tanfuhua.model.bo;

import lombok.Data;
import org.tanfuhua.enums.KyfwSeatTypeEnum;

import java.util.List;

/**
 * 预订票
 *
 * @author: gaofubo
 * @date: 2021/4/18
 */
@Data
public class KyfwConfirmTicketOrderBO {
    /**
     * 车票日期，如：Wed Apr 28 2021 00:00:00 GMT+0800 (中国标准时间)
     */
    private String trainDate;
    /**
     * 火车编号，如：240000G10335
     */
    private String trainNo;
    /**
     * 火车code，如：G103
     */
    private String stationTrainCode;
    /**
     * 出发地编号，如：VNP
     */
    private String fromStationTelecode;
    /**
     * 到达地编号，如：AOH
     */
    private String toStationTelecode;
    /**
     * 票，如："ypInfo": "kGECmYuGKIG08ZjPUU3LIVYoYSWm%2F3nJkEy7pj%2BA9gLi%2BIi2"
     */
    private String leftTicket;
    /**
     * 00，如：00
     */
    private String purposeCodes;
    /**
     * 如："locationCode": "P3"
     */
    private String trainLocation;
    /**
     * 加密数据，如：null
     */
    private String encryptedData;
    /**
     * 乘客列表
     */
    private List<KyfwPassengerBO> kyfwPassengerBOList;

    /**
     * 乘客信息
     */
    @Data
    public static class KyfwPassengerBO {
        /**
         * 座位类型，如： +
         * SWZ: "9_商务座",
         * TZ: "P_特等座",
         * ZY: "M_一等座",
         * ZE: "O_二等座",
         * GR: "6_高级软卧",
         * RW: "4_软卧",
         * SRRB: "F_动卧",
         * YW: "3_硬卧",
         * RZ: "2_软座",
         * YZ: "1_硬座",
         * WZ: "1_无座",
         * QT: "H_其他"
         */
        private KyfwSeatTypeEnum seatType;
        /**
         * 票类型：普通=1，儿童=2，学生=3
         */
        private String ticketType;
        /**
         * 乘客姓名，如："passengerName": "高福泊"
         */
        private String passengerName;
        /**
         * 证件类型code，如："passengerIdTypeCode": "1"
         */
        private String passengerIdTypeCode;
        /**
         * 证件号，如："passengerIdNo": "1309***********71X"
         */
        private String passengerIdNo;
        /**
         * 手机号，如："mobileNo": "157****9550"
         */
        private String mobileNo;
        /**
         * 加密数据，如："allEncStr": "806675ff2a6aeeccb478115cfd727ae6406a97ecd1f171862cf1cc5e6b03fe889395b7cb4af8aa2a0fe54b3cb170efc9daf2304392bb9de6baff28ce471997e8161eac95883dd937ed7ce0cfaa185978"
         */
        private String allEncStr;
    }

}
