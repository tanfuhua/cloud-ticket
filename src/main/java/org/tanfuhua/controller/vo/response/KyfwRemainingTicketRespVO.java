package org.tanfuhua.controller.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 火车票
 */
@Data
@ApiModel("火车票余票")
public class KyfwRemainingTicketRespVO {

    // 秘钥
    private String secretStr;
    // 车次
    // station_train_code: "K215"
    // train_no: "240000K2150Z"
    // train_seat_feature: "3"
    @ApiModelProperty("车次")
    private String stationTrainCode;
    private String trainNo;
    private String trainSeatFeature;
    // 出发站
    // from_station_name: "北京"
    // from_station_no: "01"
    // from_station_telecode: "BJP"
    @ApiModelProperty("出发站")
    private String fromStationName;
    private String fromStationNo;
    private String fromStationTelecode;
    // 到达站
    // to_station_name: "唐山北"
    // to_station_no: "02"
    // to_station_telecode: "FUP"
    @ApiModelProperty("到达站")
    private String toStationName;
    private String toStationNo;
    private String toStationTelecode;
    // 始发站
    // start_station_telecode: "BJP"
    @ApiModelProperty("始发站")
    private String startStationTelecode;
    // 结束站
    // end_station_telecode: "YJL"
    @ApiModelProperty("结束站")
    private String endStationTelecode;
    // 出发时间
    // start_time: "16:26"
    // start_train_date: "20210417"
    @ApiModelProperty("出发时间")
    private String startTime;
    private String startTrainDate;
    // 到达时间
    // arrive_time: "18:07"  // 到达时间
    @ApiModelProperty("到达时间")
    private String arriveTime;
    // 历时
    // lishi: "01:41"
    @ApiModelProperty("历时")
    private String liShi;
    // 座位信息
    //    gg_num: "--"
    //    gr_num: "--"
    //    qt_num: "--"
    //    rw_num: "9"
    //    rz_num: "--"
    //    srrb_num: "--"
    //    swz_num: "--"
    //    tz_num: "--"
    //    wz_num: "--"
    //    yb_num: "--"
    //    yw_num: "有"
    //    yz_num: "无"
    //    ze_num: "--"
    //    zy_num: "--"
    @ApiModelProperty("")
    private String ggNum;
    @ApiModelProperty("高级软卧")
    private String grNum;
    @ApiModelProperty("其他")
    private String qtNum;
    @ApiModelProperty("软卧")
    private String rwNum;
    @ApiModelProperty("软座")
    private String rzNum;
    @ApiModelProperty("动卧")
    private String srrbNum;
    @ApiModelProperty("商务")
    private String swzNum;
    @ApiModelProperty("特等")
    private String tzNum;
    @ApiModelProperty("无座")
    private String wzNum;
    @ApiModelProperty("")
    private String ybNum;
    @ApiModelProperty("硬卧")
    private String ywNum;
    @ApiModelProperty("硬座")
    private String yzNum;
    @ApiModelProperty("二等座")
    private String zeNum;
    @ApiModelProperty("一等座")
    private String zyNum;
    // 按钮
    // buttonTextInfo: "预订"
    private String buttonTextInfo;
    // 候补
    // houbu_seat_limit: ""
    // houbu_train_flag: "1"
    private String houbuSeatLimit;
    private String houbuTrainFlag;

    //    canWebBuy: "Y"
    //    controlled_train_flag: "0"
    //    dw_flag: "0#0#0"
    //    exchange_train_flag: "1"
    //    is_support_card: "1"
    //    location_code: "PA"
    //    seat_types: "134"
    //    yp_ex: "103040"
    //    yp_info: "Xeu8fzPhDl8qs%2BcVc2A7wKGZ3Ro7keJpzmLsE7LaDA07MYQq"
    private String canWebBuy;
    private String controlledTrainFlag;
    private String dwFlag;
    private String exchangeTrainFlag;
    private String isSupportCard;
    private String locationCode;
    private String seatTypes;
    private String ypEx;
    private String ypInfo;
}
