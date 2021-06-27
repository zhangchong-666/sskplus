package com.lzs.sskplus.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @Author 张sir
 * @Company 南邮
 * @Create 2021-04-21-2:00
 **/
public class LoginInterceptor implements HandlerInterceptor {
    /**
     * 目标执行之前
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //登入检查
        HttpSession session = request.getSession();
        Object loginUser = session.getAttribute("loginUser");
        if(loginUser != null){
            return true;
        }else{
            request.setAttribute("msg","请先登入");
            request.getRequestDispatcher("/").forward(request,response);
        }
        return false;
    }
}
