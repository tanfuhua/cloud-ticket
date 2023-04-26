package org.tanfuhua.facade;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tanfuhua.controller.vo.request.KyfwLoginReqVO;
import org.tanfuhua.controller.vo.response.KyfwInfoRespVO;
import org.tanfuhua.convert.BeanConverter;
import org.tanfuhua.enums.BookTypeEnum;
import org.tanfuhua.model.bo.KyfwBrowserBO;
import org.tanfuhua.model.bo.KyfwLoginBO;
import org.tanfuhua.model.bo.UserBO;
import org.tanfuhua.model.entity.UserConfigDO;
import org.tanfuhua.model.entity.UserDO;
import org.tanfuhua.service.UserConfigService;
import org.tanfuhua.service.UserService;
import org.tanfuhua.util.JacksonJsonUtil;
import org.tanfuhua.util.SessionUtil;

import java.util.Date;
import java.util.Objects;

/**
 * @author: gaofubo
 * @date: 2021/1/22
 */
@Service
@AllArgsConstructor
public class LoginFacade {

    /**
     * Service
     */
    private final UserService userService;
    private final UserConfigService userConfigService;
    /**
     * Convert
     */
    private final BeanConverter beanConverter;
    /**
     * Facade
     */
    private final KyfwFacade kyfwFacade;


    /**
     * 12306登录
     */
    @Transactional(rollbackFor = Exception.class)
    public void kyfwLogin(KyfwLoginReqVO reqVO) {
        String account = reqVO.getAccount();
        String password = reqVO.getPassword();

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
            // 新建UserConfigPO
            UserConfigDO userConfigPO = new UserConfigDO();
            userConfigPO.setUserId(userDO.getId());
            userConfigPO.setStartBookInfo(JacksonJsonUtil.DEFAULT_OBJECT_JSON_STRING);
            userConfigPO.setCookieValidStatus(false);
            userConfigPO.setScheduleTime(new Date());
            userConfigPO.setRunStatus(false);
            userConfigPO.setBookType(BookTypeEnum.INITIAL);
            userConfigPO.setUserAgentIndex(1);
            userConfigService.save(userConfigPO);
        } else if (!userDO.getKyfwAccount().equals(account) || !userDO.getKyfwPassword().equals(password)) {
            UserDO updateUser = new UserDO();
            updateUser.setId(userDO.getId());
            updateUser.setKyfwAccount(account);
            updateUser.setKyfwPassword(password);
            userService.updateById(updateUser);
            userDO = userService.getById(userDO.getId());
        }
        UserBO userBO = beanConverter.userDOToBO(userDO);
        userBO.setKyfwBrowserBO(kyfwBrowserBO);
        SessionUtil.setUserBO(userBO);
    }

    /**
     * 12306信息
     */
    public KyfwInfoRespVO kyfwInfo() {
        UserBO userBO = SessionUtil.getUserBO();
        boolean isLogin = userBO.getKyfwBrowserBO().isLogin();
        return beanConverter.userBOToKyfwInfoRespVO(userBO, isLogin);
    }

    public void kyfwLogout() {
        SessionUtil.setUserBO(null);
    }

    public void loginStatus() {
        SessionUtil.getUserBO();
    }
}
