package org.tanfuhua.convert;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.tanfuhua.common.constant.Constant;
import org.tanfuhua.controller.vo.request.LowcodeScenarioCreateReqVO;
import org.tanfuhua.controller.vo.response.KyfwInfoRespVO;
import org.tanfuhua.controller.vo.response.KyfwPassengerRespVO;
import org.tanfuhua.controller.vo.response.KyfwTrainStationRespVO;
import org.tanfuhua.controller.vo.response.LowcodeScenarioRespVO;
import org.tanfuhua.model.bo.KyfwPassengerRespBO;
import org.tanfuhua.model.bo.KyfwTrainStationRespBO;
import org.tanfuhua.model.entity.LowcodeScenarioDO;
import org.tanfuhua.model.entity.UserDO;

/**
 * @author gaofubo
 * @date 2021/7/25
 */
@Mapper(componentModel = Constant.Str.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BeanConverter {

    /**
     * UserBO -> KyfwInfoRespVO
     */
    @Mapping(source = "isLogin", target = KyfwInfoRespVO.Fields.isLogin)
    KyfwInfoRespVO userDOToKyfwInfoRespVO(UserDO userDO, boolean isLogin);

    /**
     * KyfwTrainStationRespBO -> KyfwTrainStationRespVO
     */
    KyfwTrainStationRespVO kyfwTrainStationRespBOToVO(KyfwTrainStationRespBO kyfwTrainStationRespBO);

    /**
     * KyfwTrainStationRespBO -> KyfwPassengerRespVO
     */
    KyfwPassengerRespVO kyfwPassengerRespBOToVO(KyfwPassengerRespBO kyfwPassengerRespBO);

    /**
     * SchemaCreateReqVO -> LowcodeScenarioDO
     */
    LowcodeScenarioDO scenarioReqVOToDO(LowcodeScenarioCreateReqVO reqVO);

    /**
     * LowcodeScenarioDO -> LowcodeScenarioRespVO
     */
    LowcodeScenarioRespVO scenarioDOToRespVO(LowcodeScenarioDO lowcodeScenarioDO);
}
