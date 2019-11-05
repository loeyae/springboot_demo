package com.loeyae.springboot.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 应用信息表
 * </p>
 *
 * @author zhang yi
 * @since 2019-10-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Application implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 应用id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 应用名称
     */
    private String name;

    /**
     * 应用简介
     */
    private String introduction;

    /**
     * 应用提供者
     */
    private String provider;

    /**
     * 应用类型
     */
    private Integer appType;

    /**
     * 由系统生成，用来验证应用合法性的加密串
     */
    private String appKey;

    /**
     * 由系统生成，用来验证应用合法性的加密串
     */
    private String appSecret;

    /**
     * 0:启用,1:停用
     */
    private Integer status;

    /**
     * 删除标识 0未删除 1删除
     */
    private Integer deleted;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建者
     */
    private Integer createUserId;

    /**
     * 修改者
     */
    private Integer updateUserId;


}
