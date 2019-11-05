package com.loeyae.springboot.demo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.loeyae.springboot.demo.constant.GeneralStatus;
import lombok.Data;

import java.util.Date;

/**
 * demo entity.
 *
 * @date: 2019-09-23
 * @version: 1.0
 * @author: zhangyi07@beyondsoft.com
 */
@Data
public class Demo extends SuperEntity {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String name;

    private GeneralStatus status;

}