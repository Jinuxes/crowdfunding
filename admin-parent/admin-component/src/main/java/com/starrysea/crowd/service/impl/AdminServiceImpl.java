package com.starrysea.crowd.service.impl;

import com.starrysea.crowd.entity.Admin;
import com.starrysea.crowd.entity.AdminExample;
import com.starrysea.crowd.mapper.AdminMapper;
import com.starrysea.crowd.service.api.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    public void saveAdmin(Admin admin) {
        adminMapper.insert(admin);
    }

    public List<Admin> getAll() {
        return adminMapper.selectByExample(new AdminExample());
    }
}
