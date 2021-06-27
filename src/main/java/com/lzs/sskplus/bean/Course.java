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
 * @Create 2021-04-30-14:54
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@TableName("course")
public class Course {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    //课程名称
    private String courseName;
    //教师名称
    private String teacherName;
    //课程学分
    private Double courseCredit;
    //课程学时
    private Integer courseHours;
    //教室容量
    private Integer classroomSize;
    //上课时间
    private String goTime;
    //上课地点
    private String goPlace;
}
