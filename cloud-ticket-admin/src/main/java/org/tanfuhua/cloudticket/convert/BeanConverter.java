package org.tanfuhua.cloudticket.convert;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.tanfuhua.cloudticket.common.constant.Constant;
import org.tanfuhua.cloudticket.controller.vo.response.KyfwInfoRespVO;
import org.tanfuhua.cloudticket.controller.vo.response.KyfwPassengerRespVO;
import org.tanfuhua.cloudticket.controller.vo.response.KyfwTrainStationRespVO;
import org.tanfuhua.cloudticket.model.bo.KyfwPassengerRespBO;
import org.tanfuhua.cloudticket.model.bo.KyfwTrainStationRespBO;
import org.tanfuhua.cloudticket.model.entity.UserDO;

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

}
