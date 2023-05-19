package org.tanfuhua.common.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tanfuhua.model.entity.BaseDO;
import org.tanfuhua.util.ContextUtil;

import java.time.LocalDateTime;
import java.util.Objects;

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
                Long userId = ContextUtil.UserHolder.getUserId();

                this.strictInsertFill(metaObject, BaseDO.Fields.createTime, LocalDateTime.class, LocalDateTime.now());
                this.strictInsertFill(metaObject, BaseDO.Fields.updateTime, LocalDateTime.class, LocalDateTime.now());
                this.strictInsertFill(metaObject, BaseDO.Fields.createUserId, Long.class, Objects.isNull(userId) ? 0 : userId);
                this.strictInsertFill(metaObject, BaseDO.Fields.updateUserId, Long.class, Objects.isNull(userId) ? 0 : userId);
                this.strictInsertFill(metaObject, BaseDO.Fields.version, Long.class, 1L);

            }

            @Override
            public void updateFill(MetaObject metaObject) {
                Long userId = ContextUtil.UserHolder.getUserId();
                this.strictUpdateFill(metaObject, BaseDO.Fields.updateTime, LocalDateTime.class, LocalDateTime.now());
                this.strictUpdateFill(metaObject, BaseDO.Fields.updateUserId, Long.class, Objects.isNull(userId) ? 0 : userId);
            }

        };
    }
}
