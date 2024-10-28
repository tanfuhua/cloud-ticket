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
import org.tanfuhua.common.response.IdRespVO;
import org.tanfuhua.common.response.Page;
import org.tanfuhua.common.response.ServerResp;
import org.tanfuhua.controller.vo.request.LowcodeLoginReqVO;
import org.tanfuhua.controller.vo.request.LowcodeScenarioCreateReqVO;
import org.tanfuhua.controller.vo.request.LowcodeScenarioUpdateReqVO;
import org.tanfuhua.controller.vo.response.LowcodeAuthorizationRespVO;
import org.tanfuhua.controller.vo.response.LowcodeScenarioRespVO;
import org.tanfuhua.facade.LowcodeFacade;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: gaofubo
 * @date: 2023/4/4
 */
@Api(tags = "低代码模块相关")
@RestController
@RequestMapping("/v1/lowcode")
@Validated
@Slf4j
@AllArgsConstructor
public class LowcodeController {

    private final LowcodeFacade lowcodeFacade;

    /**
     * 登录
     */
    @ApiOperation("登录")
    @PostMapping(value = "/login", produces = Constant.Str.APPLICATION_JSON_UTF8)
    public ResponseEntity<ServerResp<LowcodeAuthorizationRespVO>> login(@RequestBody @Valid LowcodeLoginReqVO reqVO) {
        String authorization = lowcodeFacade.login(reqVO);
        LowcodeAuthorizationRespVO respVO = new LowcodeAuthorizationRespVO();
        respVO.setAuthorization(authorization);
        return ServerResp.createRespEntity(respVO, HttpStatus.OK);
    }

    /**
     * 登录状态
     */
    @ApiOperation("登录状态")
    @GetMapping(value = "/loginStatus", produces = Constant.Str.APPLICATION_JSON_UTF8)
    public ResponseEntity<ServerResp<Void>> loginStatus() {
        return ServerResp.createRespEntity(HttpStatus.OK);
    }

    /**
     * schema
     */
    @ApiOperation("schema")
    @PostMapping(value = "/scenario/swap", produces = Constant.Str.APPLICATION_JSON_UTF8)
    public ResponseEntity<ServerResp<Object>> swap(@RequestBody String obj) {
        return ServerResp.createRespEntity(obj, HttpStatus.OK);
    }

    /**
     * 测试
     */
    @ApiOperation("测试")
    @GetMapping(value = "/test", produces = Constant.Str.APPLICATION_JSON_UTF8)
    public ResponseEntity<ServerResp<Map<String, Object>>> test() {
        Map<String, Object> map = new HashMap<>();
        map.put("integer", 1);
        map.put("string", "字符串");
        throw new RuntimeException();
//        return ServerResp.createRespEntity(map, HttpStatus.OK);
    }

    /**
     * 分页查询所有scenario
     */
    @ApiOperation("分页查询所有scenario")
    @GetMapping(value = {"/scenario/page"}, produces = Constant.Str.APPLICATION_JSON_UTF8)
    public ResponseEntity<ServerResp<Page<LowcodeScenarioRespVO>>> getScenarioPage(@RequestParam("page") Integer page,
                                                                                   @RequestParam("perPage") Integer perPage) {
        Page<LowcodeScenarioRespVO> voPage = lowcodeFacade.getScenarioPage(page, perPage);
        return ServerResp.createRespEntity(voPage, HttpStatus.OK);

    }

    /**
     * 查询scenario
     */
    @ApiOperation("查询scenario")
    @GetMapping(value = {"/scenario/encrypt"}, produces = Constant.Str.APPLICATION_JSON_UTF8)
    public ResponseEntity<ServerResp<LowcodeScenarioRespVO>> getScenario(@RequestParam(value = "id", required = false) Long id,
                                                                         @RequestParam(value = "path", required = false) String path) {
        LowcodeScenarioRespVO respVO = lowcodeFacade.getScenario(id, path);
        return ServerResp.createRespEntity(respVO, HttpStatus.OK);
    }


    /**
     * 删除scenario
     */
    @ApiOperation("删除scenario")
    @DeleteMapping(value = {"/scenario/{id}"}, produces = Constant.Str.APPLICATION_JSON_UTF8)
    public ResponseEntity<ServerResp<Void>> deleteScenario(@PathVariable Long id) {
        lowcodeFacade.deleteScenario(id);
        return ServerResp.createRespEntity(HttpStatus.OK);
    }

    /**
     * 修改scenario
     */
    @ApiOperation("修改scenario")
    @PutMapping(value = {"/scenario/{id}"}, produces = Constant.Str.APPLICATION_JSON_UTF8)
    public ResponseEntity<ServerResp<Void>> updateScenario(@PathVariable Long id,
                                                           @RequestBody LowcodeScenarioUpdateReqVO reqVO) {
        lowcodeFacade.updateScenario(id, reqVO);
        return ServerResp.createRespEntity(HttpStatus.OK);
    }


    /**
     * 保存scenario
     */
    @ApiOperation("保存scenario")
    @PostMapping(value = {"/scenario"}, produces = Constant.Str.APPLICATION_JSON_UTF8)
    public ResponseEntity<ServerResp<IdRespVO>> saveScenario(@Valid @RequestBody LowcodeScenarioCreateReqVO reqVO) {
        Long id = lowcodeFacade.saveScenario(reqVO);
        IdRespVO idRespVO = new IdRespVO();
        idRespVO.setId(id);
        return ServerResp.createRespEntity(idRespVO, HttpStatus.OK);
    }

    /**
     * 保存scenario
     */
    @ApiOperation("保存scenario")
    @PostMapping(value = {"/scenario/copy/{id}"}, produces = Constant.Str.APPLICATION_JSON_UTF8)
    public ResponseEntity<ServerResp<IdRespVO>> saveScenario(@PathVariable Long id,
                                                             @Valid @RequestBody LowcodeScenarioCreateReqVO reqVO) {
        id = lowcodeFacade.copyScenario(id, reqVO);
        IdRespVO idRespVO = new IdRespVO();
        idRespVO.setId(id);
        return ServerResp.createRespEntity(idRespVO, HttpStatus.OK);
    }


}
