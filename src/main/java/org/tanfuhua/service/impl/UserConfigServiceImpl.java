package org.tanfuhua.service.impl;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.tanfuhua.dao.UserConfigMapper;
import org.tanfuhua.model.entity.UserConfigDO;
import org.tanfuhua.service.UserConfigService;

/**
 * @author gaofubo
 * @date 2021/7/25
 */
@Service
public class UserConfigServiceImpl extends ServiceImpl<UserConfigMapper, UserConfigDO> implements UserConfigService {

    @Override
    public UserConfigDO getByUserId(Long userId) {
        return getOne(Wrappers.lambdaQuery(UserConfigDO.class)
                .eq(UserConfigDO::getUserId, userId));
    }

    @Override
    public void updateCookie(Long userId, Boolean cookieStatus, String cookie) {
        UserConfigDO configDO = getByUserId(userId);
        UserConfigDO updateDO = new UserConfigDO();
        updateDO.setId(configDO.getId());
        updateDO.setCookieValidStatus(cookieStatus);
        updateDO.setCookie(cookie);
        updateById(updateDO);
    }

}
