package org.tanfuhua.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.tanfuhua.common.config.FeignConfig;
import org.tanfuhua.common.constant.Constant;


/**
 * @author: gaofubo
 * @date: 2021/1/22
 */
@FeignClient(value = "12306", url = "https://kyfw.12306.cn", configuration = FeignConfig.class)
public interface KyfwClient {

    /**
     * 基本信息or是否已登录
     */
    @PostMapping(value = "/otn/login/conf")
    String conf();

    /**
     * 获取站名
     */
    @GetMapping("/otn/resources/js/framework/station_name.js")
    byte[] getStationName(@RequestParam(value = "station_version", defaultValue = "1.9188") Double stationVersion);

    /**
     * 查询火车票
     */
    @GetMapping("/otn/leftTicket/query")
    byte[] leftTicketQuery(@RequestParam("leftTicketDTO.train_date") String trainDate,
                           @RequestParam("leftTicketDTO.from_station") String fromStation,
                           @RequestParam("leftTicketDTO.to_station") String toStation,
                           @RequestParam("purpose_codes") String purposeCode);

    /**
     * 检查用户状态
     */
    @PostMapping(value = "/otn/login/checkUser", consumes = Constant.Str.APPLICATION_FORM_UTF8)
    String checkUser(MultiValueMap<String, Object> formData);

    /**
     * 提交订单请求
     */
    @PostMapping(value = "/otn/leftTicket/submitOrderRequest", consumes = Constant.Str.APPLICATION_FORM_UTF8)
    String submitOrderRequest(MultiValueMap<String, Object> formData);

    /**
     * 初始化DC，以获取repeat token
     */
    @GetMapping("/otn/confirmPassenger/initDc")
    byte[] initDc();

    /**
     * 获取乘客信息
     */
    @PostMapping(value = "/otn/confirmPassenger/getPassengerDTOs", consumes = Constant.Str.APPLICATION_FORM_UTF8)
    byte[] getPassengerDTOs(MultiValueMap<String, Object> formData);

    /**
     * 获取乘客信息
     */
    @PostMapping(value = "/otn/passengers/query", consumes = Constant.Str.APPLICATION_FORM_UTF8)
    byte[] getPassengers(MultiValueMap<String, Object> formData);

    /**
     * 检查订单信息
     */
    @PostMapping(value = "/otn/confirmPassenger/checkOrderInfo", consumes = Constant.Str.APPLICATION_FORM_UTF8)
    String checkOrderInfo(MultiValueMap<String, Object> formData);

    /**
     * 获取余票和排队信息
     */
    @PostMapping(value = "/otn/confirmPassenger/getQueueCount", consumes = Constant.Str.APPLICATION_FORM_UTF8)
    String getQueueCount(MultiValueMap<String, Object> formData);

    /**
     * 确认单程订单
     */
    @PostMapping(value = "/otn/confirmPassenger/confirmSingleForQueue", consumes = Constant.Str.APPLICATION_FORM_UTF8)
    String confirmSingleForQueue(MultiValueMap<String, Object> formData);

    /**
     * 查询订单等待时间
     */
    @GetMapping(value = "/otn/confirmPassenger/queryOrderWaitTime")
    String queryOrderWaitTime(@RequestParam("random") Long random,
                              @RequestParam("tourFlag") String tourFlag,
                              @RequestParam(value = "_json_att", defaultValue = "") String jsonAtt,
                              @RequestParam("REPEAT_SUBMIT_TOKEN") String repeatSubmitToken);

    /**
     * 单程订单结果查询
     */
    @PostMapping(value = "/otn/confirmPassenger/resultOrderForDcQueue")
    String resultOrderForDcQueue(MultiValueMap<String, Object> formData);

    //    @PostMapping(value = "/otn/queryOrder/initNoCompleteQueueApi")
    //    byte[] initNoCompleteQueueApi(MultiValueMap<String, Object> formData);
    //
    //    @PostMapping(value = "/otn/queryOrder/queryMyOrderNoComplete")
    //    byte[] queryMyOrderNoComplete(MultiValueMap<String, Object> formData);
    //
    //    /**
    //     * 用户登录
    //     */
    //    @GetMapping(value = "/otn/login/userLogin")
    //    byte[] userLogin();

}
