package com.loeyae.springboot.demo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

/**
 * 实体父类.
 *
 * @date: 2019-09-24
 * @version: 1.0
 * @author: zhangyi07@beyondsoft.com
 */
@Data
public class SuperEntity {

    @TableField(value = "create_time")
    private Date createTime;
}