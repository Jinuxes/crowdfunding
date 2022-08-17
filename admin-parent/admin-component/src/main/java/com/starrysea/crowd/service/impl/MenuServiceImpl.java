package com.starrysea.crowd.service.impl;

import com.starrysea.crowd.entity.Menu;
import com.starrysea.crowd.entity.MenuExample;
import com.starrysea.crowd.mapper.MenuMapper;
import com.starrysea.crowd.service.api.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<Menu> getAll() {
        return menuMapper.selectByExample(new MenuExample());
    }
}
