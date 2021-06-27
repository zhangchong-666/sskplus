package com.lzs.sskplus.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzs.sskplus.bean.Users;
import com.lzs.sskplus.mapper.UsersMapper;
import com.lzs.sskplus.service.UsersService;
import org.springframework.stereotype.Service;

/**
 * @Author 张sir
 * @Company 南邮
 * @Create 2021-04-29-19:30
 **/
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {
}
