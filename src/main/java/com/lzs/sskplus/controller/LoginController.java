package com.lzs.sskplus.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lzs.sskplus.bean.Users;
import com.lzs.sskplus.bean.XuanKeTime;
import com.lzs.sskplus.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpSession;

/**
 * @Author 张sir
 * @Company 南邮
 * @Create 2021-04-20-18:18
 * 登入
 **/
@Controller
public class LoginController {
    @Autowired
    UsersService usersService;
    @Autowired
    XuanKeTime xuanKeTime;

    /**
     * 返回一个登入页面
     * @return
     */
    @RequestMapping(value = {"/","/login"})
    public String loginPage(){
        return "login";
    }



    @PostMapping("/login")
    public String doLogin(Users users, HttpSession session, Model model){
        QueryWrapper<Users> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",users.getUserId());
        Users u = usersService.getOne(wrapper);
        if (StringUtils.isEmpty(users.getUserId())){
            model.addAttribute("umsg","账号不能为空");
            return "login";
        }else if(StringUtils.isEmpty(users.getPassWord())){
            model.addAttribute("pmsg","密码不能为空");
            return "login";
        }else if(StringUtils.isEmpty(users.getUserType())){
            model.addAttribute("tmsg","用户角色必须勾选");
            return "login";
        }else if(u==null) {
            model.addAttribute("nmsg","用户不存在");
            return "login";
        }else if(!u.getPassWord().equals(users.getPassWord())) {
            model.addAttribute("fmsg","密码错误");
            return "login";
        }else {
            if(users.getUserType().equals(u.getUserType())){
                if("2".equals(u.getUserType())){
                    session.setAttribute("loginUser",users);
                    //重定向去管理员主页，重定向解决表单重交问题
                    return "redirect:/adminindex.html";
                }else if("3".equals(u.getUserType())){
                    session.setAttribute("loginUser",users);
                    //重定向去老师页面
                    return "redirect:/teacherindex.html";
                }else if("1".equals(u.getUserType())){
                    session.setAttribute("loginUser",u);
                    return "redirect:/studentindex.html";
                }
            }else{
                model.addAttribute("gmsg","角色错误");
                return "login";
            }
        }
        return "login";
    }

    /**
     * 返回一个管理员主页
     * @return
     */
    @RequestMapping("/adminindex.html")
    public String adminIndex(){

        return "admin/index";
    }



    @RequestMapping("/studentindex.html")
    public String studentIndex(Model model) {
        String startTime = xuanKeTime.getStartTime();
        String endTime = xuanKeTime.getEndTime();
        String tuikeTime = xuanKeTime.getTuikeTime();
        model.addAttribute("starttime",startTime);
        model.addAttribute("endtime",endTime);
        model.addAttribute("tuiketime",tuikeTime);
        return "student/index";
    }
}