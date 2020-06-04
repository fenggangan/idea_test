package com.atguigu.mybatisplus.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;


@Data
public class User {
    @TableId(type = IdType.INPUT)
    private Long id;
    private String name;
    private Integer age;
    private String email;
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private int deleted;


    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @Version
    //@TableField(value="version", fill = FieldFill.INSERT, update="%s+1")
    private int version;
}
