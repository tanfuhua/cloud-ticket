package org.tanfuhua.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.tanfuhua.model.entity.UserConfigDO;

/**
 * @author gaofubo
 * @date 2021/7/25
 */
public interface UserConfigService extends IService<UserConfigDO> {

    UserConfigDO getByUserId(Long userId);

    void updateCookie(Long userId, Boolean cookieStatus, String cookie);

}
