package org.tanfuhua.facade;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.tanfuhua.common.constant.Constant;
import org.tanfuhua.controller.vo.request.KyfwBookTrainTicketReqVO;
import org.tanfuhua.controller.vo.request.KyfwQueryTicketReqVO;
import org.tanfuhua.controller.vo.response.KyfwPassengerRespVO;
import org.tanfuhua.controller.vo.response.KyfwRemainingTicketRespVO;
import org.tanfuhua.controller.vo.response.KyfwTrainStationRespVO;
import org.tanfuhua.convert.BeanConverter;
import org.tanfuhua.enums.KyfwSeatTypeEnum;
import org.tanfuhua.exception.BadRequestException;
import org.tanfuhua.model.bo.*;
import org.tanfuhua.util.DateUtil;
import org.tanfuhua.util.FunctionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author: gaofubo
 * @date: 2021/4/18
 */
@Service
@AllArgsConstructor
@Slf4j
public class TrainTicketFacade {

    private final BeanConverter beanConverter;
    private final KyfwFacade kyfwFacade;

    /**
     * 获取乘客信息
     */
    public List<KyfwPassengerRespVO> getPassengerList() {
        List<KyfwPassengerRespBO> kyfwPassengerRespBOList = kyfwFacade.getPassengerList();
        return kyfwPassengerRespBOList.stream().map(beanConverter::kyfwPassengerRespBOToVO).collect(Collectors.toList());
    }

    /**
     * 获取所有车站信息
     */
    public List<KyfwTrainStationRespVO> getStationList() {
        List<KyfwTrainStationRespBO> trainStationList = kyfwFacade.getTrainStationList();
        return trainStationList.stream().map(beanConverter::kyfwTrainStationRespBOToVO).collect(Collectors.toList());
    }

    /**
     * 查询火车票余票
     */
    public List<KyfwRemainingTicketRespVO> queryRemainingTicketList(KyfwQueryTicketReqVO reqVO) {
        return kyfwFacade.queryRemainingTicketList(reqVO.getTrainDate(), reqVO.getFromStation(),
                reqVO.getToStation(), reqVO.getPurposeCode());
    }


    /**
     * 订火车票
     */
    public void bookTrainTicket(KyfwBookTrainTicketReqVO reqVO) {
        // 获取乘客信息
        List<KyfwPassengerRespBO> passengerList = kyfwFacade.getPassengerList();
        Map<String, KyfwPassengerRespBO> passengerUUIDMap = FunctionUtil.convertCollToMap(passengerList, KyfwPassengerRespBO::getPassengerUuid, Function.identity());

        // 获取火车票余票信息
        KyfwQueryTicketReqVO queryTicketReqVO = reqVO.getQueryTicketReqVO();
        List<KyfwRemainingTicketRespVO> remainingTicketList = kyfwFacade.queryRemainingTicketList(queryTicketReqVO.getTrainDate(), queryTicketReqVO.getFromStation(), queryTicketReqVO.getToStation(), queryTicketReqVO.getPurposeCode());
        Map<String, KyfwRemainingTicketRespVO> remainingTicketMap = FunctionUtil.convertCollToMap(remainingTicketList, kyfwRemainingTicketRespVO -> kyfwRemainingTicketRespVO.getStationTrainCode() + "_" + kyfwRemainingTicketRespVO.getTrainNo(), Function.identity());
        KyfwRemainingTicketRespVO remainingTicket = remainingTicketMap.get(reqVO.getStationTrainCode() + "_" + reqVO.getTrainNo());

        // 提交订单请求
        KyfwSubmitTicketOrderBO submitTicketOrderBO = new KyfwSubmitTicketOrderBO();
        submitTicketOrderBO.setTrainDate(queryTicketReqVO.getTrainDate());
        submitTicketOrderBO.setBackTrainDate(submitTicketOrderBO.getTrainDate());
        submitTicketOrderBO.setSecretStr(remainingTicket.getSecretStr());
        submitTicketOrderBO.setPurposeCodes(queryTicketReqVO.getPurposeCode());
        submitTicketOrderBO.setTourFlag("dc");
        submitTicketOrderBO.setQueryFromStationName(remainingTicket.getFromStationName());
        submitTicketOrderBO.setQueryToStationName(remainingTicket.getToStationName());
        submitTicketOrder(submitTicketOrderBO);

        // 确认订单
        KyfwConfirmTicketOrderBO confirmTicketOrderBO = new KyfwConfirmTicketOrderBO();
        String trainDate = DateUtil.dateToString(
                DateUtil.stringToDate(submitTicketOrderBO.getTrainDate(), Constant.Str.DATE_FORMAT),
                Constant.Str.DATE_TIME_KYFW_FORMAT, Locale.ENGLISH, Constant.Time.GMT_PLUS_8);
        confirmTicketOrderBO.setTrainDate(trainDate.replace("08:00", "0800"));
        confirmTicketOrderBO.setTrainNo(reqVO.getTrainNo());
        confirmTicketOrderBO.setStationTrainCode(reqVO.getStationTrainCode());
        confirmTicketOrderBO.setFromStationTelecode(remainingTicket.getFromStationTelecode());
        confirmTicketOrderBO.setToStationTelecode(remainingTicket.getToStationTelecode());
        confirmTicketOrderBO.setLeftTicket(remainingTicket.getYpInfo());
        confirmTicketOrderBO.setPurposeCodes("00");
        confirmTicketOrderBO.setTrainLocation(remainingTicket.getLocationCode());
        confirmTicketOrderBO.setEncryptedData(null);
        confirmTicketOrderBO.setKyfwPassengerBOList(new ArrayList<>(reqVO.getPassengerReqVOList().size()));
        for (KyfwBookTrainTicketReqVO.KyfwBookPassengerReqVO passengerReqVO : reqVO.getPassengerReqVOList()) {
            KyfwPassengerRespBO passengerRespBO = passengerUUIDMap.get(passengerReqVO.getPassengerUuid());
            KyfwConfirmTicketOrderBO.KyfwPassengerBO kyfwPassengerBO = new KyfwConfirmTicketOrderBO.KyfwPassengerBO();
            kyfwPassengerBO.setSeatType(passengerReqVO.getSeatType());
            kyfwPassengerBO.setTicketType(passengerRespBO.getPassengerType());
            kyfwPassengerBO.setPassengerIdNo(passengerRespBO.getPassengerIdNo());
            kyfwPassengerBO.setPassengerName(passengerRespBO.getPassengerName());
            kyfwPassengerBO.setPassengerIdTypeCode(passengerRespBO.getPassengerIdTypeCode());
            kyfwPassengerBO.setMobileNo(passengerRespBO.getMobileNo());
            kyfwPassengerBO.setAllEncStr(passengerRespBO.getAllEncStr());
            confirmTicketOrderBO.getKyfwPassengerBOList().add(kyfwPassengerBO);
        }
        confirmTicketOrder(confirmTicketOrderBO);

    }

    /**
     * 提交订单
     */
    public void submitTicketOrder(KyfwSubmitTicketOrderBO submitTicketOrderBO) {
        kyfwFacade.getUserInfo();
        kyfwFacade.checkUser();
        kyfwFacade.submitOrderRequest(submitTicketOrderBO);
    }


    /**
     * 确认订单
     */
    public void confirmTicketOrder(KyfwConfirmTicketOrderBO confirmTicketOrderBO) {
        String passengerTicketStr = kyfwFacade.createPassengerTicketStr(confirmTicketOrderBO.getKyfwPassengerBOList());
        String oldPassengerStr = kyfwFacade.createOldPassengerStr(confirmTicketOrderBO.getKyfwPassengerBOList());
        // RepeatSubmitToken
        // initDc
        String initDc = kyfwFacade.initDc();
        // 获取token
        String repeatSubmitToken = kyfwFacade.getRegexString(KyfwFacade.REPEAT_SUBMIT_TOKEN_PATTERN, initDc);
        if (StringUtils.isBlank(repeatSubmitToken)) {
            throw new BadRequestException("RepeatSubmitToken为空！");
        }
        String keyCheckIsChange = kyfwFacade.getRegexString(KyfwFacade.KEY_CHECK_IS_CHANGE, initDc);
        // checkOrderInfo
        KyfwCheckOrderInfoRespBO kyfwCheckOrderInfoRespBO = kyfwFacade.checkOrderInfo(passengerTicketStr, oldPassengerStr, repeatSubmitToken);
        // seatType
        KyfwSeatTypeEnum seatType = confirmTicketOrderBO.getKyfwPassengerBOList().get(0).getSeatType();
        if (!kyfwCheckOrderInfoRespBO.getChooseSeats().contains(seatType.getCode())) {
            throw new BadRequestException("当前已无票:" + seatType.getDesc());
        }
        // getQueueCount
        kyfwFacade.getQueueCount(confirmTicketOrderBO.getTrainDate(),
                confirmTicketOrderBO.getTrainNo(),
                confirmTicketOrderBO.getStationTrainCode(),
                seatType,
                confirmTicketOrderBO.getFromStationTelecode(),
                confirmTicketOrderBO.getToStationTelecode(),
                confirmTicketOrderBO.getLeftTicket(),
                confirmTicketOrderBO.getPurposeCodes(),
                confirmTicketOrderBO.getTrainLocation(),
                repeatSubmitToken);
        // confirmSingleForQueue
        kyfwFacade.confirmSingleForQueue(
                passengerTicketStr,
                oldPassengerStr,
                keyCheckIsChange,
                confirmTicketOrderBO.getLeftTicket(),
                confirmTicketOrderBO.getPurposeCodes(),
                confirmTicketOrderBO.getTrainLocation(),
                repeatSubmitToken);
        // checkSubmitSuccess
        kyfwFacade.checkSubmitSuccess(repeatSubmitToken);
    }
}
