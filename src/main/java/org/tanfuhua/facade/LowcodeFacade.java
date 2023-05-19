package org.tanfuhua.facade;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tanfuhua.common.config.LowcodeConfig;
import org.tanfuhua.common.response.Page;
import org.tanfuhua.controller.vo.request.LowcodeLoginReqVO;
import org.tanfuhua.controller.vo.request.LowcodeScenarioCreateReqVO;
import org.tanfuhua.controller.vo.request.LowcodeScenarioUpdateReqVO;
import org.tanfuhua.controller.vo.response.LowcodeScenarioRespVO;
import org.tanfuhua.convert.BeanConverter;
import org.tanfuhua.exception.BadRequestException;
import org.tanfuhua.model.entity.LowcodeScenarioDO;
import org.tanfuhua.service.LowcodeScenarioService;
import org.tanfuhua.util.ContextUtil;
import org.tanfuhua.util.JwtUtil;
import org.tanfuhua.util.PageUtil;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Objects;

/**
 * 低代码模块逻辑
 *
 * @author gaofubo
 * @date 2023/4/4
 */
@Service
@RequiredArgsConstructor
public class LowcodeFacade {

    private final LowcodeScenarioService lowcodeScenarioService;

    private final BeanConverter beanConverter;

    private final LowcodeConfig lowcodeConfig;

    @Value("${token.secret}")
    private String secret;
    @Value("${token.timeout-second}")
    private Long timeoutSecond;

    public String login(LowcodeLoginReqVO reqVO) {
        if (!lowcodeConfig.getUsername().equals(reqVO.getUsername())) {
            throw new BadRequestException("账号不正确");
        }
        if (!lowcodeConfig.getPassword().equals(reqVO.getPassword())) {
            throw new BadRequestException("密码不正确");
        }
        String authorization = JwtUtil.createAuthorization(lowcodeConfig.getId(), secret, new Date(System.currentTimeMillis() + timeoutSecond * 1000));
        HttpServletResponse response = ContextUtil.RequestResponseHolder.getHttpServletResponse();
        response.addHeader(HttpHeaders.AUTHORIZATION, authorization);
        return authorization;
    }

    @Transactional(rollbackFor = Exception.class)
    public Long saveScenario(LowcodeScenarioCreateReqVO reqVO) {
        int countByName = lowcodeScenarioService.count(Wrappers
                .lambdaQuery(LowcodeScenarioDO.class).eq(LowcodeScenarioDO::getName, reqVO.getName()));
        if (countByName > 0) {
            throw new BadRequestException("名称已存在:%s", reqVO.getName());
        }
        int countByPath = lowcodeScenarioService.count(Wrappers
                .lambdaQuery(LowcodeScenarioDO.class).eq(LowcodeScenarioDO::getPath, reqVO.getPath()));
        if (countByPath > 0) {
            throw new BadRequestException("路径已存在:%s", reqVO.getPath());
        }

        LowcodeScenarioDO lowcodeScenarioDO = beanConverter.scenarioReqVOToDO(reqVO);
        lowcodeScenarioDO.setConfig(lowcodeConfig.getConfig());
        lowcodeScenarioDO.setSchema(lowcodeConfig.getSchema());

        lowcodeScenarioService.save(lowcodeScenarioDO);
        return lowcodeScenarioDO.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateScenario(Long id, LowcodeScenarioUpdateReqVO reqVO) {
        LowcodeScenarioDO scenarioDO = lowcodeScenarioService.getById(id);
        if (Objects.isNull(scenarioDO)) {
            throw new BadRequestException("schema的name=%s不存在", reqVO.getName());
        }
        LowcodeScenarioDO updateDO = new LowcodeScenarioDO();
        // set
        updateDO.setPath(reqVO.getPath());
        updateDO.setIcon(reqVO.getIcon());
        updateDO.setConfig(reqVO.getConfig());
        updateDO.setSchema(reqVO.getSchema());
        // where
        updateDO.setId(scenarioDO.getId());
        updateDO.setVersion(scenarioDO.getVersion());
        boolean update = lowcodeScenarioService.updateById(updateDO);
        if (!update) {
            throw new BadRequestException("修改失败，请重试");
        }
    }

    public LowcodeScenarioRespVO getScenario(Long id, String path) {
        LambdaQueryWrapper<LowcodeScenarioDO> queryWrapper = Wrappers
                .lambdaQuery(LowcodeScenarioDO.class);
        if (Objects.nonNull(id)) {
            queryWrapper.eq(LowcodeScenarioDO::getId, id);
        } else if (Objects.nonNull(path)) {
            queryWrapper.eq(LowcodeScenarioDO::getPath, path);
        } else {
            return null;
        }
        queryWrapper.last("LIMIT 1");
        LowcodeScenarioDO scenarioDO = lowcodeScenarioService.getOne(queryWrapper);
        if (Objects.isNull(scenarioDO)) {
            return null;
        }
        return beanConverter.scenarioDOToRespVO(scenarioDO);
    }

    public void deleteScenario(Long id) {
        if (Objects.equals(id, 1L)) {
            throw new BadRequestException("系统管理页不能删除");
        }
        LowcodeScenarioDO scenarioDO = lowcodeScenarioService.getById(id);
        if (Objects.isNull(scenarioDO)) {
            return;
        }
        lowcodeScenarioService.removeById(id);
    }

    public Page<LowcodeScenarioRespVO> getScenarioPage(Integer page, Integer perPage) {
        IPage<LowcodeScenarioDO> iPage = lowcodeScenarioService.page(PageUtil.createIPage(page, perPage));
        return PageUtil.iPageToPage(iPage, beanConverter::scenarioDOToRespVO);
    }


    /**
     * 复制
     */
    public Long copyScenario(Long id, LowcodeScenarioCreateReqVO reqVO) {
        LowcodeScenarioDO scenarioDO = lowcodeScenarioService.getById(id);
        if (Objects.isNull(scenarioDO)) {
            throw new BadRequestException("复制的页面不存在");
        }
        int countByName = lowcodeScenarioService.count(Wrappers
                .lambdaQuery(LowcodeScenarioDO.class).eq(LowcodeScenarioDO::getName, reqVO.getName()));
        if (countByName > 0) {
            throw new BadRequestException("名称已存在:%s", reqVO.getName());
        }
        int countByPath = lowcodeScenarioService.count(Wrappers
                .lambdaQuery(LowcodeScenarioDO.class).eq(LowcodeScenarioDO::getPath, reqVO.getPath()));
        if (countByPath > 0) {
            throw new BadRequestException("路径已存在:%s", reqVO.getPath());
        }

        LowcodeScenarioDO lowcodeScenarioDO = beanConverter.scenarioReqVOToDO(reqVO);
        lowcodeScenarioDO.setConfig(scenarioDO.getConfig());
        lowcodeScenarioDO.setSchema(scenarioDO.getSchema());

        lowcodeScenarioService.save(lowcodeScenarioDO);
        return lowcodeScenarioDO.getId();
    }
}
