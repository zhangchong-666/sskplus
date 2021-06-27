package com.lzs.sskplus.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Author 张sir
 * @Company 南邮
 * @Create 2021-04-30-14:51
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@TableName("major_course")
public class MajorCourse implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    //专业名称
    private String major;
    //学期
    private String schoolTerm;
    //课程名称
    private String courseName;
}
