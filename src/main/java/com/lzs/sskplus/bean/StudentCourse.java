package com.lzs.sskplus.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.util.pattern.PathPattern;

/**
 * @Author 张sir
 * @Company 南邮
 * @Create 2021-04-30-15:08
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@TableName("student_course")
public class StudentCourse {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    //学生编号
    private String studentId;
    //当前学期
    private String currentTerm;
    //课程名称
    private String courseName;
    //教师名称
    private String teacherName;
    //课程学分
    private Double courseCredit;
    //课程学时
    private Integer courseHours;
    //上课时间
    private String goTime;
    //上课地点
    private String goPlace;
    //选课状态:0--待审核 1--审核通过  2--审核驳回  3--退课
    private Integer statu;
}
