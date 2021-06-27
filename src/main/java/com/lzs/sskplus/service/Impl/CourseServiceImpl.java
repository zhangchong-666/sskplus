package com.lzs.sskplus.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzs.sskplus.bean.Course;
import com.lzs.sskplus.mapper.CourseMapper;
import com.lzs.sskplus.service.CourseService;
import org.springframework.stereotype.Service;

/**
 * @Author 张sir
 * @Company 南邮
 * @Create 2021-04-30-15:16
 **/
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {
}
