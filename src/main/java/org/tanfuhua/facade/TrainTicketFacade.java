package org.tanfuhua.facade;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tanfuhua.common.constant.Constant;
import org.tanfuhua.controller.vo.request.KyfwBookTrainTicketReqVO;
import org.tanfuhua.controller.vo.request.KyfwQueryTicketReqVO;
import org.tanfuhua.controller.vo.response.KyfwInfoRespVO;
import org.tanfuhua.controller.vo.response.KyfwPassengerRespVO;
import org.tanfuhua.controller.vo.response.KyfwRemainingTicketRespVO;
import org.tanfuhua.controller.vo.response.KyfwTrainStationRespVO;
import org.tanfuhua.convert.BeanConverter;
import org.tanfuhua.enums.BookTypeEnum;
import org.tanfuhua.enums.KyfwSeatTypeEnum;
import org.tanfuhua.exception.BadRequestException;
import org.tanfuhua.model.bo.*;
import org.tanfuhua.model.entity.UserConfigDO;
import org.tanfuhua.model.entity.UserDO;
import org.tanfuhua.service.UserConfigService;
import org.tanfuhua.service.UserService;
import org.tanfuhua.util.*;

import java.util.*;
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
    private final UserService userService;
    private final UserConfigService userConfigService;


    /**
     * 12306登录
     */
    @Transactional(rollbackFor = Exception.class)
    public void kyfwLogin() {

        KyfwInfoRespVO kyfwInfoRespVO = kyfwInfo();
        if (kyfwInfoRespVO.getIsLogin()) {
            return;
        }

        UserDO userDOCache = ContextUtil.UserHolder.getUserDOCache();

        String account = userDOCache.getKyfwAccount();
        String password = userDOCache.getKyfwPassword();

        KyfwBrowserBO kyfwBrowserBO = kyfwFacade.createKyfwBrowserBO(account);

        kyfwBrowserBO.login(new KyfwLoginBO(account, password));

        UserDO userDO = userService.getByUserName(kyfwBrowserBO.getUserName());
        if (Objects.isNull(userDO)) {
            // 新建UserPO
            userDO = new UserDO();
            userDO.setUserName(kyfwBrowserBO.getUserName());
            userDO.setRealName(kyfwBrowserBO.getRealName());
            userDO.setKyfwAccount(account);
            userDO.setKyfwPassword(password);
            userService.save(userDO);
        } else if (!userDO.getKyfwAccount().equals(account) || !userDO.getKyfwPassword().equals(password)) {
            UserDO updateUser = new UserDO();
            updateUser.setId(userDO.getId());
            updateUser.setKyfwAccount(account);
            updateUser.setKyfwPassword(password);
            userService.updateById(updateUser);
        }

        UserConfigDO userConfigDO = userConfigService.getByUserId(userDO.getId());
        if (Objects.isNull(userConfigDO)) {
            // 新建UserConfigPO
            userConfigDO = new UserConfigDO();
            userConfigDO.setUserId(userDO.getId());
            userConfigDO.setStartBookInfo(JacksonJsonUtil.DEFAULT_OBJECT_JSON_STRING);
            userConfigDO.setCookieValidStatus(true);
            userConfigDO.setCookie(StringUtil.cookieListToKVString(kyfwBrowserBO.getCookieList()));
            userConfigDO.setScheduleTime(new Date());
            userConfigDO.setRunStatus(false);
            userConfigDO.setBookType(BookTypeEnum.INITIAL);
            userConfigDO.setUserAgentIndex(1);
            userConfigService.save(userConfigDO);
        } else {
            UserConfigDO updateDO = new UserConfigDO();
            updateDO.setId(userConfigDO.getId());
            updateDO.setCookieValidStatus(true);
            updateDO.setCookie(StringUtil.cookieListToKVString(kyfwBrowserBO.getCookieList()));
            userConfigService.updateById(updateDO);
        }

        kyfwFacade.stopKyfwBrowserBO(account);

    }

    /**
     * 12306信息
     */
    public KyfwInfoRespVO kyfwInfo() {
        UserDO userDO = ContextUtil.UserHolder.getUserDOCache();
        UserConfigDO userConfigDO = userConfigService.getByUserId(userDO.getId());
        boolean cookieValidStatus = false;
        if (Objects.nonNull(userConfigDO)) {
            cookieValidStatus = userConfigDO.getCookieValidStatus();
        }
        return beanConverter.userDOToKyfwInfoRespVO(userDO, cookieValidStatus);
    }

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
    public KyfwRemainingTicketRespVO queryRemainingTicketList(KyfwQueryTicketReqVO reqVO) {
        return kyfwFacade.queryRemainingTicket(reqVO.getTrainDate(), reqVO.getFromStation(),
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
        List<KyfwRemainingTicketRespVO.Ticket> remainingTicketList = kyfwFacade.queryRemainingTicket(queryTicketReqVO.getTrainDate(), queryTicketReqVO.getFromStation(), queryTicketReqVO.getToStation(), queryTicketReqVO.getPurposeCode()).getTicketList();
        Map<String, KyfwRemainingTicketRespVO.Ticket> remainingTicketMap = FunctionUtil.convertCollToMap(remainingTicketList, ticket -> ticket.getStationTrainCode() + "_" + ticket.getTrainNo(), Function.identity());
        KyfwRemainingTicketRespVO.Ticket remainingTicket = remainingTicketMap.get(reqVO.getStationTrainCode() + "_" + reqVO.getTrainNo());

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
