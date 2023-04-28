package org.tanfuhua.common.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tanfuhua.common.constant.Constant;
//import org.tanfuhua.model.bo.LowcodeUserBO;
import org.tanfuhua.model.bo.UserBO;
import org.tanfuhua.model.entity.BaseDO;
import org.tanfuhua.util.SessionUtil;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author: gaofubo
 * @date: 2021/2/20
 */
@Configuration
@MapperScan(basePackages = {"org.tanfuhua.dao"})
@Slf4j
public class MybatisPlusConfig {


    /**
     * 插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 分页
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        // 乐观锁
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }

    /**
     * 字段自动填充
     */
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new MetaObjectHandler() {
            @Override
            public void insertFill(MetaObject metaObject) {
                long userId = getUserId(metaObject);

                this.strictInsertFill(metaObject, BaseDO.Fields.createTime, LocalDateTime.class, LocalDateTime.now());
                this.strictInsertFill(metaObject, BaseDO.Fields.updateTime, LocalDateTime.class, LocalDateTime.now());
                this.strictInsertFill(metaObject, BaseDO.Fields.createUserId, Long.class, userId);
                this.strictInsertFill(metaObject, BaseDO.Fields.updateUserId, Long.class, userId);
                this.strictInsertFill(metaObject, BaseDO.Fields.version, Long.class, 1L);

            }

            @Override
            public void updateFill(MetaObject metaObject) {
                long userId = getUserId(metaObject);
                this.strictUpdateFill(metaObject, BaseDO.Fields.updateTime, LocalDateTime.class, LocalDateTime.now());
                this.strictUpdateFill(metaObject, BaseDO.Fields.updateUserId, Long.class, userId);
            }

            private long getUserId(MetaObject metaObject) {
                TableInfo tableInfo = findTableInfo(metaObject);
                //
                long userId = 0L;
                if (tableInfo.getTableName().startsWith("lowcode")) {
//                    Optional<LowcodeUserBO> lowcodeUserBO = SessionUtil.getOptional(Constant.Str.SESSION_LOWCODE_USER, LowcodeUserBO.class);
//                    if (lowcodeUserBO.isPresent()) {
//                        userId = lowcodeUserBO.get().getId();
//                    }
                } else {
                    Optional<UserBO> userBO = SessionUtil.getUserBOOptional();
                    if (userBO.isPresent()) {
                        userId = userBO.get().getId();
                    }
                }
                return userId;
            }


        };
    }
}
