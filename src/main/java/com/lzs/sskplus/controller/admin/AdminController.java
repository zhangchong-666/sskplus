package com.lzs.sskplus.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lzs.sskplus.bean.Course;
import com.lzs.sskplus.bean.StudentCourse;
import com.lzs.sskplus.bean.Users;
import com.lzs.sskplus.service.CourseService;
import com.lzs.sskplus.service.StudentCourseService;
import com.lzs.sskplus.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sun.security.provider.MD2;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Author 张sir
 * @Company 南邮
 * @Create 2021-05-03-15:06
 **/
@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    StudentCourseService studentCourseService;
    @Autowired
    CourseService courseService;
    @Autowired
    UsersService usersService;

    /**
     * 返回一个审核列表
     * @param schoolTerm
     * @param courseName
     * @param studentId
     * @param model
     * @return
     */
    @RequestMapping("/checkCourse")
    public String checkCourse(String schoolTerm, String courseName, String studentId, Model model){
        QueryWrapper<StudentCourse> wrapper = new QueryWrapper<>();
        if (schoolTerm == null || schoolTerm.equals("")){

        }else {
            wrapper.eq("current_term",schoolTerm);
        }
        if (courseName ==null || courseName.equals("")){

        }else {
            wrapper.eq("course_name",courseName);
        }
        if (studentId == null || studentId.equals("")){

        }else {
            wrapper.eq("student_id",studentId);
        }
        wrapper.eq("statu",0).or().eq("statu",3);
        List<StudentCourse> list = studentCourseService.list(wrapper);
        if (list.size() == 0){
            model.addAttribute("msg","没有数据");
            return "admin/checkCourse";
        }
        model.addAttribute("list",list);
        return "admin/checkCourse";
    }


    /**
     * 审核
     * @param id
     * @param r
     * @param statu
     * @return
     */
    @RequestMapping("/goCheck/{id}")
    public String goCheck(@PathVariable("id")Integer id, RedirectAttributes r, @RequestParam("statu")Integer statu){
        StudentCourse course = studentCourseService.getById(id);
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_name",course.getCourseName()).eq("teacher_name",course.getTeacherName());
        Course one = courseService.getOne(queryWrapper);
        if(statu == 1){
            if (one.getClassroomSize()==0){
                r.addAttribute("msg","课程人数已满，不能批准");
                return "redirect:/admin/checkCourse";
            }else {
                course.setStatu(1);
                studentCourseService.updateById(course);
                one.setClassroomSize(one.getClassroomSize()-1);
                courseService.updateById(one);
                r.addAttribute("msg","批准成功");
                return "redirect:/admin/checkCourse";
            }
        }else if(statu == 2) {
            course.setStatu(2);
            studentCourseService.updateById(course);
            r.addAttribute("msg","驳回成功");
            return "redirect:/admin/checkCourse";
        }else if(statu == 3){
            studentCourseService.removeById(id);
            one.setClassroomSize(one.getClassroomSize()+1);
            courseService.updateById(one);
            r.addAttribute("msg","退课成功");
        }
        return "redirect:/admin/checkCourse";
    }


    /**
     * 返回一个审核通过课程列表
     * @param schoolTerm
     * @param courseName
     * @param studentId
     * @param model
     * @return
     */
    @RequestMapping("/overCheck")
    public String overCheck(String schoolTerm, String courseName, String studentId, Model model){
        QueryWrapper<StudentCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("statu",1);
        if (schoolTerm == null || schoolTerm.equals("")){

        }else {
            wrapper.eq("current_term",schoolTerm);
        }
        if (courseName ==null || courseName.equals("")){

        }else {
            wrapper.eq("course_name",courseName);
        }
        if (studentId == null || studentId.equals("")){

        }else {
            wrapper.eq("student_id",studentId);
        }
        List<StudentCourse> list = studentCourseService.list(wrapper);
        if (list.size() == 0){
            model.addAttribute("msg","没有数据");
            return "admin/overCheck";
        }
        model.addAttribute("list",list);
        return "admin/overCheck";
    }

    /**
     * 管理员退课
     * @param id
     * @return
     */
    @RequestMapping("/tuiXuan/{id}")
    public String tuiXuan(@PathVariable("id")String id,RedirectAttributes r){
        StudentCourse one = studentCourseService.getById(id);
        QueryWrapper<Course> wrapper = new QueryWrapper<>();
        wrapper.eq("teacher_name",one.getTeacherName()).eq("course_name",one.getCourseName());
        Course one1 = courseService.getOne(wrapper);
        studentCourseService.removeById(id);
        one1.setClassroomSize(one1.getClassroomSize()+1);
        courseService.updateById(one1);
        r.addAttribute("msg","退课成功");
        return "redirect:/admin/overCheck";
    }

    /**
     * 修改密码
     * @param studentId
     * @param password
     * @param model
     * @return
     */
    @RequestMapping("/changePassword")
    public String changePassword(String studentId,String password,Model model){
        if (studentId == null && password == null){
            return "admin/changPassword";
        }else {
            if ("".equals(studentId)){
                model.addAttribute("msg","请输入编号");
                return "admin/changPassword";
            }
            if ("".equals(password)){
                model.addAttribute("msg","请输入新密码");
                return "admin/changPassword";
            }
            QueryWrapper<Users> usersQueryWrapper = new QueryWrapper<>();
            usersQueryWrapper.eq("user_id", studentId);
            Users one = usersService.getOne(usersQueryWrapper);
            /*if("".equals(studentId)){
                model.addAttribute("msg","账号不能为空，请重新输入");
                return "admin/changPassword";
            }
            if("".equals(password)){
                model.addAttribute("msg","密码不能为空，请重新输入");
                return "admin/changPassword";
            }*/
            if(one == null){
                model.addAttribute("msg","没有这个学生，请重新输入");
                return "admin/changPassword";
            }
            one.setPassWord(password);
            usersService.updateById(one);
            model.addAttribute("msg","修改成功新密码："+password);
            return "admin/changPassword";
        }
    }


    @RequestMapping("/logOut")
    public String logOut(HttpSession session){
        session.removeAttribute("loginUser");
        return "login";
    }
}
