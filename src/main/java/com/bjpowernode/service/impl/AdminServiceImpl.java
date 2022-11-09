package com.bjpowernode.service.impl;

import com.bjpowernode.mapper.AdminMapper;
import com.bjpowernode.pojo.Admin;
import com.bjpowernode.pojo.AdminExample;
import com.bjpowernode.service.AdminService;
import com.bjpowernode.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    //在业务逻辑层中，一定会有数据访问层的对象
    @Autowired
    AdminMapper adminMapper;

    @Override
    public Admin login(String name, String pwd) {
        AdminExample example = new AdminExample();
        example.createCriteria().andANameEqualTo(name);
        List<Admin> adminList = adminMapper.selectByExample(example);
        if (adminList.size() > 0) {
            Admin admin = adminList.get(0);
            String miPwd = MD5Util.getMD5(pwd);
            if (miPwd.equals(admin.getaPass())) {
                return admin;
            }
        }
        return null;
    }
}
