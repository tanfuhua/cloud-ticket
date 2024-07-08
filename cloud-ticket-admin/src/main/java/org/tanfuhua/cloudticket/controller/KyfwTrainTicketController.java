package org.tanfuhua.cloudticket.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tanfuhua.cloudticket.facade.TrainTicketFacade;
import org.tanfuhua.common.constant.Constant;
import org.tanfuhua.common.response.ListRespVO;
import org.tanfuhua.common.response.ServerResp;
import org.tanfuhua.controller.vo.request.KyfwBookTrainTicketReqVO;
import org.tanfuhua.controller.vo.request.KyfwLoginReqVO;
import org.tanfuhua.controller.vo.request.KyfwQueryTicketReqVO;
import org.tanfuhua.controller.vo.response.KyfwInfoRespVO;
import org.tanfuhua.controller.vo.response.KyfwPassengerRespVO;
import org.tanfuhua.controller.vo.response.KyfwRemainingTicketRespVO;
import org.tanfuhua.controller.vo.response.KyfwTrainStationRespVO;
import org.tanfuhua.job.SampleXxlJob;

import javax.validation.Valid;
import java.util.List;

/**
 * @author: gaofubo
 * @date: 2021/4/18
 */
@Api(tags = "订票相关")
@RestController
@RequestMapping("/v1/trainTicket")
@Validated
@Slf4j
@AllArgsConstructor
public class KyfwTrainTicketController {

    private final TrainTicketFacade trainTicketFacade;

    private final SampleXxlJob sampleXxlJob;

    /**
     * 12306信息
     */
    @ApiOperation("12306信息")
    @GetMapping(value = "/kyfwInfo", produces = Constant.Str.APPLICATION_JSON_UTF8)
    public ResponseEntity<ServerResp<KyfwInfoRespVO>> kyfwInfo() {
        KyfwInfoRespVO responseVo = trainTicketFacade.kyfwInfo();
        return ServerResp.createRespEntity(responseVo, HttpStatus.OK);
    }

    /**
     * 12306绑定
     */
    @ApiOperation("12306绑定")
    @PostMapping(value = "/kyfwBind", produces = Constant.Str.APPLICATION_JSON_UTF8)
    public ResponseEntity<ServerResp<Void>> kyfwBind(@RequestBody KyfwLoginReqVO reqVO) {
        trainTicketFacade.kyfwLogin();
        return ServerResp.createRespEntity(HttpStatus.OK);
    }

    /**
     * 12306登录
     */
    @ApiOperation("12306登录")
    @PostMapping(value = "/kyfwLogin", produces = Constant.Str.APPLICATION_JSON_UTF8)
    public ResponseEntity<ServerResp<Void>> kyfwLogin() {
        trainTicketFacade.kyfwLogin();
        return ServerResp.createRespEntity(HttpStatus.OK);
    }

    /**
     * 12306注销
     */
    @ApiOperation("12306注销")
    @GetMapping(value = "/kyfwLogout", produces = Constant.Str.APPLICATION_JSON_UTF8)
    public ResponseEntity<ServerResp<Void>> kyfwLogout() {
        return ServerResp.createRespEntity(HttpStatus.OK);
    }

    /**
     * 获取乘客信息
     */
    @ApiOperation("获取乘客信息")
    @GetMapping(value = "/passengerList", produces = Constant.Str.APPLICATION_JSON_UTF8)
    public ResponseEntity<ServerResp<List<KyfwPassengerRespVO>>> getPassengerList() {
        List<KyfwPassengerRespVO> passengerRespBOList = trainTicketFacade.getPassengerList();
        return ServerResp.createRespEntity(passengerRespBOList, HttpStatus.OK);
    }

    /**
     * 获取所有车站信息
     */
    @ApiOperation("获取所有车站信息")
    @GetMapping(value = "/stationList", produces = Constant.Str.APPLICATION_JSON_UTF8)
    public ResponseEntity<ServerResp<ListRespVO<KyfwTrainStationRespVO>>> getStationList() {
        List<KyfwTrainStationRespVO> kyfwTrainStationRespBOList = trainTicketFacade.getStationList();
        return ServerResp.createRespEntity(new ListRespVO<>(kyfwTrainStationRespBOList), HttpStatus.OK);
    }

    /**
     * 查询火车票余票
     */
    @ApiOperation("查询火车票余票")
    @PostMapping(value = "/ticketList", produces = Constant.Str.APPLICATION_JSON_UTF8)
    public ResponseEntity<ServerResp<KyfwRemainingTicketRespVO>> queryRemainingTicketList(@Valid @RequestBody KyfwQueryTicketReqVO reqVO) {
        KyfwRemainingTicketRespVO ticketRespVO = trainTicketFacade.queryRemainingTicketList(reqVO);
        return ServerResp.createRespEntity(ticketRespVO, HttpStatus.OK);
    }

    /**
     * 订火车票
     */
    @ApiOperation("订火车票")
    @PostMapping(value = "/bookTrainTicket", produces = Constant.Str.APPLICATION_JSON_UTF8)
    public ResponseEntity<ServerResp<Void>> bookTrainTicket(@Valid @RequestBody KyfwBookTrainTicketReqVO reqVO) {
        trainTicketFacade.bookTrainTicket(reqVO);
        return ServerResp.createRespEntity(HttpStatus.OK);
    }

    /**
     * 手动触发保持session
     */
    @ApiOperation("手动触发保持session")
    @PostMapping(value = "/sessionRefresh", produces = Constant.Str.APPLICATION_JSON_UTF8)
    public ResponseEntity<ServerResp<Void>> sessionRefresh() {
        sampleXxlJob.kyfwSessionRefresh();
        return ServerResp.createRespEntity(HttpStatus.OK);
    }

}
