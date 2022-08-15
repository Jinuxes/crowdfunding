package com.starrysea.crowd.service.api;

import com.github.pagehelper.PageInfo;
import com.starrysea.crowd.entity.Role;
import com.starrysea.crowd.util.ResultEntity;

import java.util.List;

public interface RoleService {

    PageInfo<Role> getPageInfo(Integer pageNum, Integer pageSize, String keyword);

    public void saveRole(Role role);
}
