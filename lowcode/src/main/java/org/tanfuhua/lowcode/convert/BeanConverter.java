package org.tanfuhua.lowcode.convert;

import org.mapstruct.Mapper;
import org.tanfuhua.lowcode.constant.Constant;
import org.tanfuhua.lowcode.controller.request.LowcodeScenarioCreateReqVO;
import org.tanfuhua.lowcode.controller.response.LowcodeScenarioRespVO;
import org.tanfuhua.lowcode.model.LowcodeScenarioDO;

/**
 * @author gaofubo
 * @date 2021/7/25
 */
@Mapper(componentModel = Constant.Str.SPRING)
public interface BeanConverter {

    /**
     * SchemaCreateReqVO -> LowcodeScenarioDO
     */
    LowcodeScenarioDO scenarioReqVOToDO(LowcodeScenarioCreateReqVO reqVO);

    /**
     * LowcodeScenarioDO -> LowcodeScenarioRespVO
     */
    LowcodeScenarioRespVO scenarioDOToRespVO(LowcodeScenarioDO lowcodeScenarioDO);
}
