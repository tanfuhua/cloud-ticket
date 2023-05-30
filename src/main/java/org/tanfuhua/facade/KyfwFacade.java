package org.tanfuhua.facade;


import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.tanfuhua.client.KyfwClient;
import org.tanfuhua.common.config.AppConfig;
import org.tanfuhua.controller.vo.response.KyfwRemainingTicketRespVO;
import org.tanfuhua.enums.KyfwExceptionEnum;
import org.tanfuhua.enums.KyfwPurposeCodeEnum;
import org.tanfuhua.enums.KyfwSeatTypeEnum;
import org.tanfuhua.exception.BadRequestException;
import org.tanfuhua.exception.KyfwException;
import org.tanfuhua.model.bo.*;
import org.tanfuhua.util.GzipUtil;
import org.tanfuhua.util.JacksonJsonUtil;
import org.tanfuhua.util.StringUtil;
import org.tanfuhua.util.ThreadUtil;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author: gaofubo
 * @date: 2021/1/23
 */
@Component
@Slf4j
public class KyfwFacade {

    public static final Pattern REPEAT_SUBMIT_TOKEN_PATTERN = Pattern.compile("var globalRepeatSubmitToken = '([^']{32})'");

    public static final Pattern KEY_CHECK_IS_CHANGE = Pattern.compile("'key_check_isChange':'([^']{56})'");

    private static final Map<String, KyfwBrowserBO> accountAndBrowserMap = new ConcurrentHashMap<>();

    @Resource
    private KyfwClient kyfwClient;

    @Resource
    private SeleniumFacade seleniumFacade;

    @Resource
    private AppConfig appConfig;


    public KyfwBrowserBO createKyfwBrowserBO(String account) {
        return accountAndBrowserMap.computeIfAbsent(account, (k) -> {
            ChromeDriver chromeDriver = seleniumFacade.createChromeDriver();
            WebDriverWait webDriverFastWait = seleniumFacade.createWebDriverFastWait(chromeDriver);
            WebDriverWait webDriverSlowWait = seleniumFacade.createWebDriverSlowWait(chromeDriver);
            return new KyfwBrowserBO(chromeDriver, webDriverFastWait, webDriverSlowWait);
        });
    }


    /**
     * 获取乘客信息
     */
    public List<KyfwPassengerRespBO> getPassengerList() {
        try {
            MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
            ImmutableMap.of("pageIndex", "1", "pageSize", "1000").forEach(formData::add);
            byte[] response = kyfwClient.getPassengers(formData);
            String responseStr = GzipUtil.unGzip(response);
            if (KyfwRespBO.isHtml(responseStr)) {
                throw new KyfwException(KyfwExceptionEnum.BAD_COOKIE);
            }
            KyfwRespBO<?> KyfwRespBO = JacksonJsonUtil.parseObject(responseStr, KyfwRespBO.class);
            KyfwRespBO.checkResponseStatus();
            return JacksonJsonUtil.cast(((LinkedHashMap<?, ?>) KyfwRespBO.getData()).get("datas"),
                    new TypeReference<List<KyfwPassengerRespBO>>() {
                    });
        } catch (KyfwException e) {
            throw new BadRequestException(e);
        }
    }


    /**
     * 获取所有车站信息
     */
    public List<KyfwTrainStationRespBO> getTrainStationList() {
        byte[] response = kyfwClient.getStationName(null);
        String responseStr = GzipUtil.unGzip(response);
        if (KyfwRespBO.isHtml(responseStr)) {
            throw new KyfwException(KyfwExceptionEnum.BAD_COOKIE);
        }
        String str = responseStr.split("'")[1];
        // @bjb|北京北|VAP|beijingbei|bjb|0@bjd|北京东|BOP|beijingdong|bjd|1@bji|北京|BJP|beijing|bj
        String[] name = str.split("\\|");
        List<KyfwTrainStationRespBO> kyfwTrainStationRespBOList = new ArrayList<>(name.length / 9 + 1);
        for (int i = 0; i < name.length; ) {
            KyfwTrainStationRespBO station = new KyfwTrainStationRespBO();
            // @bjb
            String sl = name[i++];
            String[] idAndShortLetter = sl.split("@");
            if (idAndShortLetter.length < 2) {
                break;
            }
            station.setId(idAndShortLetter[0]);
            station.setShortLetter(idAndShortLetter[1]);
            // 北京北
            station.setChineseName(name[i++]);
            // VAP
            station.setEnglishName(name[i++]);
            // beijingbei
            station.setAllLetter(name[i++]);
            // bjb
            station.setFirstLetter(name[i++]);
            //
            i++;
            i++;
            if (i < name.length) {
                station.setCity(name[i++]);
            }
            i++;
            kyfwTrainStationRespBOList.add(station);
        }
        return kyfwTrainStationRespBOList;
    }

    /**
     * 查询火车票余票
     */
    public KyfwRemainingTicketRespVO queryRemainingTicket(String trainDate,
                                                          String fromStation,
                                                          String toStation,
                                                          KyfwPurposeCodeEnum purposeCode) {
        try {
            // 查询
            byte[] bytes = kyfwClient.leftTicketQuery(trainDate, fromStation, toStation, purposeCode.getCode());
            if (ArrayUtils.isEmpty(bytes)) {
                throw new KyfwException(KyfwExceptionEnum.BAD_COOKIE);
            }
            bytes = GzipUtil.unGzip(new ByteArrayInputStream(bytes));
            String response = new String(bytes, StandardCharsets.UTF_8);
            if (KyfwRespBO.isHtml(response)) {
                throw new KyfwException(KyfwExceptionEnum.BAD_COOKIE);
            }
            KyfwRespBO<KyfwRemainingTicketRespBO> KyfwRespBO =
                    JacksonJsonUtil.parseObject(response, new TypeReference<KyfwRespBO<KyfwRemainingTicketRespBO>>() {
                    });
            KyfwRespBO.checkResponseStatus();
            // 解析
            KyfwRemainingTicketRespBO ticketBO = KyfwRespBO.getData();
            return parseRemainingTicket(ticketBO);
        } catch (KyfwException e) {
            throw new BadRequestException(e);
        }
    }


    /**
     * 解析12306车票信息
     */
    private KyfwRemainingTicketRespVO parseRemainingTicket(KyfwRemainingTicketRespBO ticketRespBO) {
        if (!ticketRespBO.getFlag()) {
            throw new KyfwException(KyfwExceptionEnum.BAD_COOKIE);
        }
        Map<String, String> map = ticketRespBO.getMap();
        List<String> ticketStrList = ticketRespBO.getResult();
        List<KyfwRemainingTicketRespVO.Ticket> ticketList = new ArrayList<>(ticketStrList.size());
        List<KyfwRemainingTicketRespVO.Station> fromStationList = new ArrayList<>(ticketStrList.size());
        List<KyfwRemainingTicketRespVO.Station> toStationList = new ArrayList<>(ticketStrList.size());
        for (String ticketStr : ticketStrList) {
            KyfwRemainingTicketRespVO.Ticket ticket = new KyfwRemainingTicketRespVO.Ticket();
            String[] splitArr = ticketStr.split("\\|");
            ticket.setSecretStr(splitArr[0]);
            ticket.setButtonTextInfo(splitArr[1]);
            ticket.setTrainNo(splitArr[2]);
            ticket.setStationTrainCode(splitArr[3]);
            ticket.setStartStationTelecode(splitArr[4]);
            ticket.setEndStationTelecode(splitArr[5]);
            ticket.setFromStationTelecode(splitArr[6]);
            ticket.setToStationTelecode(splitArr[7]);
            ticket.setStartTime(splitArr[8]);
            ticket.setArriveTime(splitArr[9]);
            ticket.setLiShi(splitArr[10]);
            ticket.setCanWebBuy(splitArr[11]);
            ticket.setYpInfo(splitArr[12]);
            ticket.setStartTrainDate(splitArr[13]);
            ticket.setTrainSeatFeature(splitArr[14]);
            ticket.setLocationCode(splitArr[15]);
            ticket.setFromStationNo(splitArr[16]);
            ticket.setToStationNo(splitArr[17]);
            ticket.setIsSupportCard(splitArr[18]);
            ticket.setControlledTrainFlag(splitArr[19]);
            ticket.setGgNum(StringUtils.isNotBlank(splitArr[20]) ? splitArr[20] : "--");
            ticket.setGrNum(StringUtils.isNotBlank(splitArr[21]) ? splitArr[21] : "--");
            ticket.setQtNum(StringUtils.isNotBlank(splitArr[22]) ? splitArr[22] : "--");
            ticket.setRwNum(StringUtils.isNotBlank(splitArr[23]) ? splitArr[23] : "--");
            ticket.setRzNum(StringUtils.isNotBlank(splitArr[24]) ? splitArr[24] : "--");
            ticket.setTzNum(StringUtils.isNotBlank(splitArr[25]) ? splitArr[25] : "--");
            ticket.setWzNum(StringUtils.isNotBlank(splitArr[26]) ? splitArr[26] : "--");
            ticket.setYbNum(StringUtils.isNotBlank(splitArr[27]) ? splitArr[27] : "--");
            ticket.setYwNum(StringUtils.isNotBlank(splitArr[28]) ? splitArr[28] : "--");
            ticket.setYzNum(StringUtils.isNotBlank(splitArr[29]) ? splitArr[29] : "--");
            ticket.setZeNum(StringUtils.isNotBlank(splitArr[30]) ? splitArr[30] : "--");
            ticket.setZyNum(StringUtils.isNotBlank(splitArr[31]) ? splitArr[31] : "--");
            ticket.setSwzNum(StringUtils.isNotBlank(splitArr[32]) ? splitArr[32] : "--");
            ticket.setSrrbNum(StringUtils.isNotBlank(splitArr[33]) ? splitArr[33] : "--");
            ticket.setYpEx(splitArr[34]);
            ticket.setSeatTypes(splitArr[35]);
            ticket.setExchangeTrainFlag(splitArr[36]);
            ticket.setHoubuTrainFlag(splitArr[37]);
            ticket.setHoubuSeatLimit(splitArr[38]);
            if (splitArr.length > 46) {
                ticket.setDwFlag(splitArr[46]);
            }
            ticket.setFromStationName(map.get(splitArr[6]));
            ticket.setToStationName(map.get(splitArr[7]));
            ticketList.add(ticket);

            KyfwRemainingTicketRespVO.Station fromStation = new KyfwRemainingTicketRespVO.Station();
            fromStation.setStationName(ticket.getFromStationName());
            fromStation.setStationNameTelecode(ticket.getFromStationTelecode());
            fromStationList.add(fromStation);

            KyfwRemainingTicketRespVO.Station toStation = new KyfwRemainingTicketRespVO.Station();
            toStation.setStationName(ticket.getToStationName());
            toStation.setStationNameTelecode(ticket.getToStationTelecode());
            toStationList.add(toStation);
        }

        KyfwRemainingTicketRespVO respVO = new KyfwRemainingTicketRespVO();
        respVO.setTicketList(ticketList);
        respVO.setFromStationList(fromStationList.stream().distinct().collect(Collectors.toList()));
        respVO.setToStationList(toStationList.stream().distinct().collect(Collectors.toList()));
        return respVO;
    }


    /**
     * 获取12306用户信息
     */
    public KyfwUserBO getUserInfo() {
        try {
            String response = kyfwClient.conf();
            if (KyfwRespBO.isHtml(response)) {
                throw new KyfwException(KyfwExceptionEnum.BAD_COOKIE);
            }
            KyfwRespBO<KyfwUserBO> KyfwRespBO =
                    JacksonJsonUtil.parseObject(response, new TypeReference<KyfwRespBO<KyfwUserBO>>() {
                    });
            KyfwRespBO.checkResponseStatus();
            if (!KyfwRespBO.getData().getIsLogin()) {
                throw new KyfwException(KyfwExceptionEnum.BAD_COOKIE);
            }
            return KyfwRespBO.getData();
        } catch (KyfwException e) {
            throw new BadRequestException(e);
        }
    }


    public void checkUser() {
        try {
            MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
            map.add("_json_att", "");
            String response = kyfwClient.checkUser(map);
            KyfwRespBO<Map<String, Object>> KyfwRespBO =
                    JacksonJsonUtil.parseObject(response, new TypeReference<KyfwRespBO<Map<String, Object>>>() {
                    });
            boolean status = KyfwRespBO.checkResponseStatusAndReturn();
            if (!status) {
                throw new KyfwException(KyfwExceptionEnum.BAD_COOKIE);
            }
            Map<String, Object> data = KyfwRespBO.getData();
            boolean flag = Boolean.parseBoolean(String.valueOf(data.get("flag")));
            if (!flag) {
                throw new KyfwException(KyfwExceptionEnum.BAD_COOKIE);
            }
        } catch (KyfwException e) {
            throw new BadRequestException(e);
        }
    }

    public void submitOrderRequest(KyfwSubmitTicketOrderBO submitTicketOrderBO) {
        try {
            submitTicketOrderBO.setSecretStr(StringUtil.urlDecode(submitTicketOrderBO.getSecretStr()));
            Map<String, Object> map = JacksonJsonUtil.parseToHashMap(JacksonJsonUtil.toJsonString(submitTicketOrderBO));
            MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
            map.forEach(formData::add);
            formData.add("undefined", null);
            String response = kyfwClient.submitOrderRequest(formData);
            KyfwRespBO<String> KyfwRespBO =
                    JacksonJsonUtil.parseObject(response, new TypeReference<KyfwRespBO<String>>() {
                    });
            if (!KyfwRespBO.getStatus()) {
                throw new KyfwException(KyfwExceptionEnum.BAD_COOKIE);
            }
            if (!Objects.equals(KyfwRespBO.getData(), "0")) {
                throw new KyfwException(KyfwExceptionEnum.BAD_COOKIE);
            }
        } catch (KyfwException e) {
            throw new BadRequestException(e);
        }
    }

    public String initDc() {
        byte[] response = kyfwClient.initDc();
        response = GzipUtil.unGzip(new ByteArrayInputStream(response));
        return new String(response, StandardCharsets.UTF_8);
    }

    public String getRegexString(Pattern pattern, String str) {
        Matcher matcher = pattern.matcher(str);
        if (!matcher.find()) {
            return null;
        }
        String repeatSubmitToken = matcher.group(1);
        if (StringUtils.isBlank(repeatSubmitToken)) {
            return null;
        }
        return repeatSubmitToken;
    }

    public List<KyfwPassengerRespBO> getPassengerList(String repeatSubmitToken) {
        try {
            MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
            ImmutableMap.of("_json_att", "", "REPEAT_SUBMIT_TOKEN", repeatSubmitToken).forEach(formData::add);
            byte[] response = kyfwClient.getPassengerDTOs(formData);
            String responseStr = GzipUtil.unGzip(response);
            KyfwRespBO<?> KyfwRespBO = JacksonJsonUtil.parseObject(responseStr, KyfwRespBO.class);
            KyfwRespBO.checkResponseStatus();
            List<KyfwPassengerRespBO> kyfwPassengerRespBOList =
                    JacksonJsonUtil.cast(
                            ((LinkedHashMap) KyfwRespBO.getData()).get("normal_passengers"),
                            new TypeReference<List<KyfwPassengerRespBO>>() {
                            });
            return kyfwPassengerRespBOList;
        } catch (KyfwException e) {
            throw new BadRequestException(e);
        }
    }


    public String createPassengerTicketStr(List<KyfwConfirmTicketOrderBO.KyfwPassengerBO> passengerBOList) {
        if (CollectionUtils.isEmpty(passengerBOList)) {
            throw new BadRequestException("乘客信息不能为空！");
        }
        StringBuilder passengerTicketStr = new StringBuilder();
        for (int i = 0, requestVOListSize = passengerBOList.size(); i < requestVOListSize; i++) {
            KyfwConfirmTicketOrderBO.KyfwPassengerBO vo = passengerBOList.get(i);
            if (i != 0) {
                passengerTicketStr.append("_");
            }
            passengerTicketStr.append(vo.getSeatType())
                    .append(",0,").append(vo.getTicketType())
                    .append(",").append(vo.getPassengerName())
                    .append(",").append(vo.getPassengerIdTypeCode())
                    .append(",").append(vo.getPassengerIdNo())
                    .append(",").append(vo.getMobileNo())
                    .append(",N,").append(vo.getAllEncStr());
        }
        return passengerTicketStr.toString();
    }

    public String createOldPassengerStr(List<KyfwConfirmTicketOrderBO.KyfwPassengerBO> passengerBOList) {
        StringBuilder oldPassengerStr = new StringBuilder();
        for (KyfwConfirmTicketOrderBO.KyfwPassengerBO vo : passengerBOList) {
            oldPassengerStr.append(vo.getPassengerName())
                    .append(",").append(vo.getPassengerIdTypeCode())
                    .append(",").append(vo.getPassengerIdNo())
                    .append(",").append(vo.getTicketType())
                    .append("_");
        }
        return oldPassengerStr.toString();
    }

    public KyfwCheckOrderInfoRespBO checkOrderInfo(String passengerTicketStr,
                                                   String oldPassengerStr,
                                                   String repeatSubmitToken) {
        try {
            // 参数
            MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
            formData.add("cancel_flag", "2");
            formData.add("bed_level_order_num", "000000000000000000000000000000");
            formData.add("passengerTicketStr", passengerTicketStr);
            formData.add("oldPassengerStr", oldPassengerStr);
            formData.add("tour_flag", "dc");
            formData.add("randCode", "");
            formData.add("whatsSelect", "1");
            formData.add("sessionId", "");
            formData.add("sig", "");
            formData.add("_json_att", "");
            formData.add("scene", "nc_login");
            formData.add("REPEAT_SUBMIT_TOKEN", repeatSubmitToken);
            // 请求
            String response = kyfwClient.checkOrderInfo(formData);
            KyfwRespBO<KyfwCheckOrderInfoRespBO> KyfwRespBO = JacksonJsonUtil.parseObject(
                    response,
                    new TypeReference<KyfwRespBO<KyfwCheckOrderInfoRespBO>>() {
                    });
            KyfwRespBO.checkResponseStatus();
            KyfwCheckOrderInfoRespBO data = KyfwRespBO.getData();
            if (!data.getSubmitStatus()) {
                throw new KyfwException(data.getErrMsg());
            }
            return data;
        } catch (KyfwException e) {
            throw new BadRequestException(e);
        }
    }

    public void getQueueCount(String trainDate,
                              String trainNo,
                              String stationTrainCode,
                              KyfwSeatTypeEnum seatType,
                              String fromStationTelecode,
                              String toStationTelecode,
                              String leftTicket,
                              String purposeCodes,
                              String trainLocation,
                              String repeatSubmitToken) {

        try {
            // 参数
            MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();

            formData.add("train_date", trainDate);
            formData.add("train_no", trainNo);
            formData.add("stationTrainCode", stationTrainCode);
            // 取第一个乘客的座位类型即可
            formData.add("seatType", seatType.getCode());
            formData.add("fromStationTelecode", fromStationTelecode);
            formData.add("toStationTelecode", toStationTelecode);
            formData.add("leftTicket", leftTicket);
            // 恒为00
            formData.add("purpose_codes", purposeCodes);
            formData.add("train_location", trainLocation);
            formData.add("_json_att", "");
            formData.add("REPEAT_SUBMIT_TOKEN", repeatSubmitToken);
            // 请求
            String response = kyfwClient.getQueueCount(formData);
            KyfwRespBO<Map<String, Object>> KyfwRespBO = JacksonJsonUtil.parseObject(
                    response,
                    new TypeReference<KyfwRespBO<Map<String, Object>>>() {
                    });
            KyfwRespBO.checkResponseStatus();
            // {"count":"0","ticket":"85,0","op_2":"false","countT":"0","op_1":"false"}
            // count:当前排队人数，ticket:剩余票数
            Map<String, Object> data = KyfwRespBO.getData();
        } catch (KyfwException e) {
            throw new BadRequestException(e);
        }
    }

    public void confirmSingleForQueue(String passengerTicketStr,
                                      String oldPassengerStr,
                                      String keyCheckIsChange,
                                      String leftTicket,
                                      String purposeCodes,
                                      String trainLocation,
                                      String repeatSubmitToken) {
        leftTicket = StringUtil.urlDecode(leftTicket);
        try {
            // 参数
            MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
            formData.add("passengerTicketStr", passengerTicketStr);
            formData.add("oldPassengerStr", oldPassengerStr);
            formData.add("randCode", "");
            formData.add("purpose_codes", purposeCodes);
            formData.add("key_check_isChange", keyCheckIsChange);
            formData.add("leftTicketStr", leftTicket);
            formData.add("train_location", trainLocation);
            formData.add("choose_seats", "");
            formData.add("seatDetailType", "000");
            // 请优先为我分配“静音车厢”席位
            formData.add("is_jy", "N");
            // 请优先为我分配残疾人专用席位
            formData.add("is_cj", "N");
            // formData.add("encryptedData", encryptedData);
            formData.add("whatsSelect", "1");
            formData.add("roomType", "00");
            formData.add("dwAll", "N");
            formData.add("_json_att", "");
            formData.add("REPEAT_SUBMIT_TOKEN", repeatSubmitToken);
            // 请求
            String response = kyfwClient.confirmSingleForQueue(formData);
            //            {
            //                "isAsync": "1",
            //                "submitStatus": true
            //            }
            KyfwRespBO<?> KyfwRespBO = JacksonJsonUtil.parseObject(
                    response,
                    KyfwRespBO.class);
            if (KyfwRespBO.getHttpStatus() != HttpStatus.OK.value()) {
                throw new KyfwException(String.valueOf(KyfwRespBO.getMessages()));
            }
            if (!KyfwRespBO.getStatus()) {
                throw new BadRequestException(String.valueOf(KyfwRespBO.getData()));
            }
            Map<String, Object> data = (Map<String, Object>) KyfwRespBO.getData();
            Object submitStatus = data.get("submitStatus");
            if (!(submitStatus instanceof Boolean) || !(Boolean) submitStatus) {
                throw new KyfwException(String.valueOf(data.get("errMsg")));
            }
        } catch (KyfwException e) {
            throw new BadRequestException(e);
        }
    }

    public void checkSubmitSuccess(String repeatSubmitToken) {
        try {
            while (true) {
                ThreadUtil.sleep(appConfig.getKyfw().getCheckSubmitOrderWaitSecond() * 1000L);
                String response = kyfwClient.queryOrderWaitTime(System.currentTimeMillis(), "dc", "", repeatSubmitToken);
                KyfwRespBO<Map<String, Object>> KyfwRespBO = JacksonJsonUtil.parseObject(
                        response,
                        new TypeReference<KyfwRespBO<Map<String, Object>>>() {
                        });
                KyfwRespBO.checkResponseStatus();
                Map<String, Object> data = KyfwRespBO.getData();
                Object orderId = data.get("orderId");
                if (Objects.equals(data.get("waitTime").toString(), "-2")) {
                    throw new KyfwException(String.valueOf(data.get("msg")));
                }
                if (orderId == null) {
                    continue;
                }
                checkOrderResult(String.valueOf(orderId), repeatSubmitToken);
                break;
            }
        } catch (KyfwException e) {
            throw new BadRequestException(e);
        }
    }

    public void checkOrderResult(String orderId, String repeatSubmitToken) {
        try {
            MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
            formData.add("orderSequence_no", orderId);
            formData.add("_json_att", "");
            formData.add("REPEAT_SUBMIT_TOKEN", repeatSubmitToken);
            String response = kyfwClient.resultOrderForDcQueue(formData);
            KyfwRespBO<Map<String, Object>> KyfwRespBO = JacksonJsonUtil.parseObject(
                    response,
                    new TypeReference<KyfwRespBO<Map<String, Object>>>() {
                    });
            KyfwRespBO.checkResponseStatus();
            Map<String, Object> data = KyfwRespBO.getData();
            Object submitStatus = data.get("submitStatus");
            if (!(submitStatus instanceof Boolean) || !(Boolean) submitStatus) {
                throw new KyfwException("提交订单失败");
            }
        } catch (KyfwException e) {
            throw new BadRequestException(e);
        }
    }


    /**
     * 识别图片验证码
     */
    @SuppressWarnings({"unchecked", "rawtype"})
    private String getImageCode(String str) {
        List<String> list = Lists.newArrayList(
                "<div randcode=\"44,32\" class=\"lgcode-active\" style=\"top: 48px; left: 31px;\"></div>",
                "<div randcode=\"99,45\" class=\"lgcode-active\" style=\"top: 61px; left: 86px;\"></div>",
                "<div randcode=\"175,41\" class=\"lgcode-active\" style=\"top: 57px; left: 162px;\"></div>",
                "<div randcode=\"272,50\" class=\"lgcode-active\" style=\"top: 66px; left: 259px;\"></div>",
                "<div randcode=\"47,112\" class=\"lgcode-active\" style=\"top: 128px; left: 34px;\"></div>",
                "<div randcode=\"101,114\" class=\"lgcode-active\" style=\"top: 130px; left: 88px;\"></div>",
                "<div randcode=\"179,111\" class=\"lgcode-active\" style=\"top: 127px; left: 166px;\"></div>",
                "<div randcode=\"247,120\" class=\"lgcode-active\" style=\"top: 136px; left: 234px;\"></div>"
        );

        String url = "http://10.211.55.5:8080/verify/base64/";
        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
        postParameters.add("imageFile", str);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        HttpEntity<MultiValueMap<String, Object>> r = new HttpEntity<>(postParameters, headers);
        String s = new RestTemplate().postForObject(url, r, String.class);
        List<String> answerList = (List<String>) JacksonJsonUtil.parseToHashMap(s).get("data");
        return StringUtils.join(answerList.stream().map(Integer::valueOf).map(i -> i - 1).map(list::get)
                .collect(Collectors.joining()));
    }
}
