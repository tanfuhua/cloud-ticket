package org.tanfuhua.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.tanfuhua.dao.UserMapper;
import org.tanfuhua.model.entity.UserDO;
import org.tanfuhua.service.UserService;

/**
 * @author gaofubo
 * @date 2021/7/24
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    @Override
    public UserDO getByUserName(String userName) {
        return getOne(Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUserName, userName));
    }

}
