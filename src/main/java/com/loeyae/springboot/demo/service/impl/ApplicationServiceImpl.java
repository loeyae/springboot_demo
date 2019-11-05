package com.loeyae.springboot.demo.service.impl;

import com.loeyae.springboot.demo.entity.Application;
import com.loeyae.springboot.demo.mapper.ApplicationMapper;
import com.loeyae.springboot.demo.service.IApplicationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 应用信息表 服务实现类
 * </p>
 *
 * @author zhang yi
 * @since 2019-10-31
 */
@Service
public class ApplicationServiceImpl extends ServiceImpl<ApplicationMapper, Application> implements IApplicationService {

    @Override
    public String getSecret(String appId) {
        Application application = super.getBaseMapper().selectById(appId);
        return application.getAppSecret();
    }
}
