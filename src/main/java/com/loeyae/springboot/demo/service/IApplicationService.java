package com.loeyae.springboot.demo.service;

import com.loeyae.springboot.demo.entity.Application;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 应用信息表 服务类
 * </p>
 *
 * @author zhang yi
 * @since 2019-10-31
 */
public interface IApplicationService extends IService<Application> {

    String getSecret(String appId);
}
