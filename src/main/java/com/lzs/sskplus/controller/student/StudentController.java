package com.lzs.sskplus.controller.student;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzs.sskplus.bean.*;
import com.lzs.sskplus.service.CourseService;
import com.lzs.sskplus.service.MajorCourseService;
import com.lzs.sskplus.service.StudentCourseService;
import com.lzs.sskplus.service.UsersService;
import com.lzs.sskplus.util.DateTimeUtil;
import com.lzs.sskplus.util.Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author 张sir
 * @Company 南邮
 * @Create 2021-04-30-16:39
 **/
@Controller
@RequestMapping("/student")
public class StudentController {
    @Autowired
    MajorCourseService majorCourseService;
    @Autowired
    XuanKeTime xuanKeTime;
    @Autowired
    UsersService usersService;
    @Autowired
    CourseService courseService;
    @Autowired
    StudentCourseService studentCourseService;

    /**
     * 返回一个专业课表
     * @param userId
     * @param pn
     * @param model
     * @return
     */
    @RequestMapping(value = "/xuanKe/{userId}")
    public String xuanKe(@PathVariable("userId")String userId, String courseName,@RequestParam(value = "pn",defaultValue = "1")Integer pn, Model model){
        QueryWrapper<Users> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("user_id",userId);
        Users user = usersService.getOne(wrapper1);
        QueryWrapper<MajorCourse> wrapper2 = new QueryWrapper<>();
        if(courseName == null || courseName.equals("") ){
            wrapper2.eq("major",user.getUserMajor()).eq("school_term",user.getCurrentTerm());
        }else{
            wrapper2.eq("major",user.getUserMajor()).eq("school_term",user.getCurrentTerm()).like("course_name",courseName);
        }
        Page<MajorCourse> majorCoursePage = new Page<>(pn,5);
        Page<MajorCourse> page = majorCourseService.page(majorCoursePage,wrapper2);
        model.addAttribute("page",page);
        return "student/course_list";
    }

    /**
     * 返回具体课程用来选择
     * @param courseName
     * @param pn
     * @return
     */
    @RequestMapping("/xuanCourse/{courseName}")
    @ResponseBody
    public Msg xuanCourse(@PathVariable("courseName")String courseName,@RequestParam(value = "pn",defaultValue = "1")Integer pn){
        QueryWrapper<Course> wrapper = new QueryWrapper<>();
        wrapper.eq("course_name",courseName);
        Page<Course> coursePage = new Page<>(pn,3);
        Page<Course> page = courseService.page(coursePage,wrapper);
        return Msg.success().add("page",page);
    }

    /**
     * 具体选课
     * @param id
     * @param studentId
     * @return
     */
    @RequestMapping("/confirmCourse/{id}")
    @ResponseBody
    public Msg confirmCourse(@PathVariable("id")Integer id,@RequestParam("studentId")String studentId){
        Course course = courseService.getById(id);
        //查询已经在审核的课程的信息，给出具体的提示
        QueryWrapper<StudentCourse> studentCourseQueryWrapper = new QueryWrapper<>();
        studentCourseQueryWrapper.eq("course_name",course.getCourseName()).eq("student_id",studentId).eq("statu",0);
        StudentCourse studentCourse = studentCourseService.getOne(studentCourseQueryWrapper);
        //
        QueryWrapper<Users> usersQueryWrapper = new QueryWrapper<>();
        usersQueryWrapper.eq("user_id",studentId);
        Users user = usersService.getOne(usersQueryWrapper);
        //选课开始时间start
        String startTime = xuanKeTime.getStartTime();
        Date start = DateTimeUtil.parseDate(startTime, "yyyy-MM-dd");
        //选课结束时间
        String endTime = xuanKeTime.getEndTime();
        Date end = DateTimeUtil.parseDate(endTime, "yyyy-MM-dd");
        //判断是否再选课时间内选课
        Date now = new Date();
        if(now.getTime()<start.getTime() || now.getTime()>end.getTime()){
            return Msg.fail().add("msg","不在选课时间内");
        }
        //判断数据库中为0的待审核的时间是否冲突
        QueryWrapper<StudentCourse> wrapper5 = new QueryWrapper<>();
        wrapper5.eq("go_time",course.getGoTime()).eq("student_id",studentId).eq("statu",0);
        List<StudentCourse> list1 = studentCourseService.list(wrapper5);
        for (StudentCourse s:
                list1) {
            if(course.getGoTime().equals(s.getGoTime())){
                return Msg.fail().add("msg","这门课程的时间与你选的正在审核"+s.getCourseName()+"这门课程冲突");
            }
        }
        //判断是否选了这门课，在待审核中
        QueryWrapper<StudentCourse> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("course_name",course.getCourseName()).eq("student_id",studentId).eq("statu",0);
        StudentCourse one1 = studentCourseService.getOne(wrapper1);
        //判断这门课是否通过审核
        QueryWrapper<StudentCourse> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("course_name",course.getCourseName()).eq("student_id",studentId).eq("statu",1);
        StudentCourse  one2 = studentCourseService.getOne(wrapper2);
        //判断是否审核驳回
        /*QueryWrapper<StudentCourse> wrapper3 = new QueryWrapper<>();
        wrapper3.eq("course_name",course.getCourseName()).eq("student_id",studentId).eq("statu",2);
        StudentCourse  one3 = studentCourseService.getOne(wrapper3);*/
        //判断容量
        if(course.getClassroomSize()==0){
            return Msg.fail().add("msg","该课程已达最大容量，请跟换其他老师的课程");
        }
        //判断这门课程是否与其他课程的上课时间冲突
        QueryWrapper<StudentCourse> wrapper4 = new QueryWrapper<>();
        wrapper4.eq("go_time",course.getGoTime()).eq("statu",1).eq("student_id",studentId);
        List<StudentCourse> list = studentCourseService.list(wrapper4);
        for (StudentCourse s:
             list) {
            if(course.getGoTime().equals(s.getGoTime())){
                return Msg.fail().add("msg","这门课程的时间与你选的已经通过审核"+s.getCourseName()+"这门课程冲突");
            }
        }
        /*if(one3 != null){
            return Msg.fail().add("msg","管理员驳回你的选课，具体原因请咨询辅导员");
        }*/
        if( one2 != null){
            return Msg.fail().add("msg","你已经选了这门课，请勿再选");
        }
        if(one1 == null){
            studentCourseService.save(new StudentCourse(null,studentId,user.getCurrentTerm(),course.getCourseName(),course.getTeacherName(),course.getCourseCredit(),course.getCourseHours(),course.getGoTime(),course.getGoPlace(),0));
            return Msg.success().add("msg","选课成功管理员正在审核");
        }else {
            return Msg.fail().add("msg", "你已经选了"+studentCourse.getTeacherName()+"教的这门课程，管理员正在审核中，请耐心等待");
        }

    }

    /**
     * 返回选课状态
     * @param studentId
     * @param pn
     * @param model
     * @return
     */
    @RequestMapping("/courseStatue/{studentId}")
    public String courseStatue(@PathVariable("studentId")String studentId,@RequestParam(value = "pn",defaultValue = "1")Integer pn,Model model){
        QueryWrapper<Users> usersQueryWrapper = new QueryWrapper<>();
        usersQueryWrapper.eq("user_id",studentId);
        Users user = usersService.getOne(usersQueryWrapper);
        QueryWrapper<StudentCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("student_id",studentId).eq("current_term",user.getCurrentTerm()).ne("statu",5);
        Page<StudentCourse> studentCoursePage = new Page<>(pn,5);
        Page<StudentCourse> page = studentCourseService.page(studentCoursePage,wrapper);
        model.addAttribute("page",page);
        return "student/courseStatue";
    }

    /**
     * 返回选课已经通过的课表
     * @param studentId
     * @param pn
     * @param model
     * @return
     */
    @RequestMapping("/successCourse/{studentId}")
    public String successCourse(@PathVariable("studentId")String studentId,@RequestParam(value = "pn",defaultValue = "1")Integer pn,Model model){
        QueryWrapper<Users> usersQueryWrapper = new QueryWrapper<>();
        usersQueryWrapper.eq("user_id",studentId);
        Users user = usersService.getOne(usersQueryWrapper);
        QueryWrapper<StudentCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("student_id",studentId).eq("current_term",user.getCurrentTerm()).eq("statu",1).or().eq("statu",5);
        Page<StudentCourse> studentCoursePage = new Page<>(pn,5);
        Page<StudentCourse> page = studentCourseService.page(studentCoursePage,wrapper);
        model.addAttribute("page",page);
        return "student/successCourse";
    }


    /**
     * 返回一个修改密码页面
     * @return
     */
    @RequestMapping("/changePassword/{studentId}")
    public String changePassword(@PathVariable("studentId")String studentId, Model model){
        QueryWrapper<Users> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",studentId);
        Users users = usersService.getOne(wrapper);
        model.addAttribute("password",users.getPassWord());
        return "student/repassword";
    }

    /**
     * 修改密码
     * @param studentId
     * @param password
     * @param model
     * @return
     */
    @RequestMapping("/rePassWord/{studentId}")
    public String rePassWord(@PathVariable("studentId")String studentId,String password,Model model){
        QueryWrapper<Users> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",studentId);
        Users users = usersService.getOne(wrapper);
        model.addAttribute("password",users.getPassWord());
        users.setPassWord(password);
        usersService.updateById(users);
        if("".equals(password)){
            model.addAttribute("onemsg","请输入密码");
            return "student/repassword";
        }
        model.addAttribute("onemsg","密码修改成功,你的新密码"+password);
        return "student/repassword";
    }

    @RequestMapping("/tuiKe/{studentId}")
    public String tuiKe(@PathVariable("studentId")String studentId,Model model, @RequestParam("id")Integer id){
        //选课开始时间start
        String startTime = xuanKeTime.getStartTime();
        Date start = DateTimeUtil.parseDate(startTime, "yyyy-MM-dd");
        System.out.println(start);
        System.out.println(start.getTime());
        //选课结束时间
        String endTime = xuanKeTime.getTuikeTime();
        Date end = DateTimeUtil.parseDate(endTime, "yyyy-MM-dd");
        System.out.println(end);
        System.out.println(end.getTime());
        //判断是否再选课时间内选课
        Date now = new Date();
        if(now.getTime()<start.getTime() || now.getTime()>end.getTime()){
            model.addAttribute("msg","不在退课时间内，退课失败");
            return "student/successCourse";
        }else {
            QueryWrapper<StudentCourse> wrapper = new QueryWrapper<>();
            wrapper.eq("student_id",studentId).eq("id",id);
            StudentCourse one = studentCourseService.getOne(wrapper);
            one.setStatu(3);
            studentCourseService.updateById(one);
            model.addAttribute("msg","正在退课中");
            return "student/successCourse";
        }
    }


    /**
     * 退出登入
     * @param studentId
     * @param session
     * @return
     */
    @RequestMapping("/logOut/{studentId}")
    public String logOut(@PathVariable("studentId")String studentId, HttpSession session){
        session.removeAttribute("loginUser");
        return "login";
    }

}
