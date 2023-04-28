package org.tanfuhua.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.tanfuhua.common.constant.Constant;
import org.tanfuhua.common.response.ServerResp;
import org.tanfuhua.controller.vo.request.KyfwBookTrainTicketReqVO;
import org.tanfuhua.controller.vo.request.KyfwQueryTicketReqVO;
import org.tanfuhua.controller.vo.response.KyfwPassengerRespVO;
import org.tanfuhua.controller.vo.response.KyfwTrainStationRespVO;
import org.tanfuhua.controller.vo.response.KyfwRemainingTicketRespVO;
import org.tanfuhua.facade.TrainTicketFacade;

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
    public ResponseEntity<ServerResp<List<KyfwTrainStationRespVO>>> getStationList() {
        List<KyfwTrainStationRespVO> kyfwTrainStationRespBOList = trainTicketFacade.getStationList();
        return ServerResp.createRespEntity(kyfwTrainStationRespBOList, HttpStatus.OK);
    }

    /**
     * 查询火车票余票
     */
    @ApiOperation("查询火车票余票")
    @PostMapping(value = "/ticketList", produces = Constant.Str.APPLICATION_JSON_UTF8)
    public ResponseEntity<ServerResp<List<KyfwRemainingTicketRespVO>>> queryRemainingTicketList(@Valid @RequestBody KyfwQueryTicketReqVO reqVO) {
        List<KyfwRemainingTicketRespVO> ticketList = trainTicketFacade.queryRemainingTicketList(reqVO);
        return ServerResp.createRespEntity(ticketList, HttpStatus.OK);
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

}
