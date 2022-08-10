package com.starrysea.crowd.service.api;

import com.starrysea.crowd.entity.Admin;

import java.util.List;

public interface AdminService {
    void saveAdmin(Admin admin);

    List<Admin> getAll();
}
