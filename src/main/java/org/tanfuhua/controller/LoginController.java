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
import org.tanfuhua.controller.vo.request.KyfwLoginReqVO;
import org.tanfuhua.controller.vo.response.KyfwInfoRespVO;
import org.tanfuhua.facade.LoginFacade;

/**
 * @author: gaofubo
 * @date: 2021/2/8
 */
@Api(tags = "登录相关")
@RestController
@RequestMapping("/v1/login")
@Validated
@Slf4j
@AllArgsConstructor
public class LoginController {

    private final LoginFacade loginFacade;

    /**
     * 登录状态
     */
    @ApiOperation("登录状态")
    @GetMapping(value = "/loginStatus", produces = Constant.Str.APPLICATION_JSON_UTF8)
    public ResponseEntity<ServerResp<Void>> loginStatus() {
        loginFacade.loginStatus();
        return ServerResp.createRespEntity(HttpStatus.OK);
    }

    /**
     * 12306登录
     */
    @ApiOperation("12306登录")
    @PostMapping(value = "/kyfwLogin", produces = Constant.Str.APPLICATION_JSON_UTF8)
    public ResponseEntity<ServerResp<Void>> kyfwLogin(@RequestBody KyfwLoginReqVO reqVO) {
        loginFacade.kyfwLogin(reqVO);
        return ServerResp.createRespEntity(HttpStatus.OK);
    }

    /**
     * 12306注销
     */
    @ApiOperation("12306注销")
    @GetMapping(value = "/kyfwLogout", produces = Constant.Str.APPLICATION_JSON_UTF8)
    public ResponseEntity<ServerResp<Void>> kyfwLogout() {
        loginFacade.kyfwLogout();
        return ServerResp.createRespEntity(HttpStatus.OK);
    }

    /**
     * 12306信息
     */
    @ApiOperation("12306信息")
    @GetMapping(value = "/kyfwInfo", produces = Constant.Str.APPLICATION_JSON_UTF8)
    public ResponseEntity<ServerResp<KyfwInfoRespVO>> kyfwInfo() {
        KyfwInfoRespVO responseVo = loginFacade.kyfwInfo();
        return ServerResp.createRespEntity(responseVo, HttpStatus.OK);
    }


    @ApiOperation("测试")
    @GetMapping(value = "/test", produces = Constant.Str.APPLICATION_JSON_UTF8)
    public ResponseEntity<ServerResp<Void>> test() {
        return ServerResp.createRespEntity(HttpStatus.OK);
    }


}
