package org.tanfuhua.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.tanfuhua.model.entity.UserDO;

/**
 * @author gaofubo
 * @date 2021/7/24
 */
public interface UserService extends IService<UserDO> {

    UserDO getByUserName(String userName);

}
