package com.lzs.sskplus.test;

import com.lzs.sskplus.bean.XuanKeTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author 张sir
 * @Company 南邮
 * @Create 2021-04-30-16:01
 **/
public class TestBean {
    @Autowired
    XuanKeTime xuanKeTime;
    @Test
    public void add(){
        System.out.println(xuanKeTime.getEndTime());
    }


}
