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
import org.tanfuhua.model.bo.UserBO;
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
                this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
                this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
                Optional<UserBO> userSO = SessionUtil.getUserBOOptional();
                this.strictInsertFill(metaObject, "createUserId", Long.class, userSO.isPresent() ? userSO.get().getId() : 0L);
                this.strictInsertFill(metaObject, "updateUserId", Long.class, userSO.isPresent() ? userSO.get().getId() : 0L);
                this.strictInsertFill(metaObject, "version", Long.class, 1L);

            }

            @Override
            public void updateFill(MetaObject metaObject) {
                Optional<UserBO> userSO = SessionUtil.getUserBOOptional();
                this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
                this.strictUpdateFill(metaObject, "updateUserId", Long.class, userSO.isPresent() ? userSO.get().getId() : 0L);
            }
        };
    }
}
