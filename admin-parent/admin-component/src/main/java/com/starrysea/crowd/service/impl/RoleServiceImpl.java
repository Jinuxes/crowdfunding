package com.starrysea.crowd.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.starrysea.crowd.entity.Role;
import com.starrysea.crowd.mapper.RoleMapper;
import com.starrysea.crowd.service.api.RoleService;
import com.starrysea.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public PageInfo<Role> getPageInfo(Integer pageNum, Integer pageSize, String keyword){
        PageHelper.startPage(pageNum,pageSize);
        List<Role> roleList = roleMapper.selectRoleByKeyword(keyword);

        PageInfo<Role> pageInfo = new PageInfo<>(roleList);

        return pageInfo;
    }

    @Override
    public void saveRole(Role role) {
        roleMapper.insert(role);
    }

    @Override
    public void updateRole(Role role) {
        roleMapper.updateByPrimaryKey(role);
    }
}
