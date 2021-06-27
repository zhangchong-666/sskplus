package com.lzs.sskplus.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author 张sir
 * @Company 南邮
 * @Create 2021-04-29-19:21
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@TableName("users")
public class Users {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    //用户编号
    private String userId;
    //用户名称
    private String userName;
    //用户密码
    private String passWord;
    //专业
    private String userMajor;
    //用户类型
    private String userType;
    //当前学期
    private String currentTerm;
}
