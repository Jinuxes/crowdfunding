package com.starrysea.crowd.mvc.handler;

import com.starrysea.crowd.entity.Menu;
import com.starrysea.crowd.service.api.MenuService;
import com.starrysea.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MenuHandler {

    @Autowired
    private MenuService menuService;

    @RequestMapping("/menu/get/tree.json")
    @ResponseBody
    public ResultEntity<Menu> getWholeTree(){

        // 1.查询全部的Menu对象
        List<Menu> menuList = menuService.getAll();

        // 2.声明一个变量来存储找到的根节点
        Menu root = null;

        // 3.创建Map对象用来存储id和Menu对象的对应关系便于查找父节点
        Map<Integer,Menu> menuMap = new HashMap<>();

        // 4.遍历menuList，以(id,menu)的方式填充menuMap
        for(Menu menu:menuList){
            menuMap.put(menu.getId(),menu);
        }

        // 5.再次遍历menuList查找根节点、组装父子节点
        for(Menu menu:menuList){
            // 6.获取当前menu对象的pid属性值
            Integer pid = menu.getPid();

            // 7.找跟节点
            if(pid == null){
                root = menu;
                continue;
            }

            // 8.组装父子节点
            // 获取父节点
            Menu father = menuMap.get(pid);
            // 将当前节点存入父节点的children集合
            father.getChildren().add(menu);
        }

        System.out.println(root);
        return ResultEntity.successWithData(root);
    }

    @RequestMapping("/menu/save.json")
    @ResponseBody
    public ResultEntity<Menu> saveMenu(Menu menu){
        menuService.saveMenu(menu);

        return ResultEntity.successWithoutData();
    }

    @RequestMapping("/menu/update.json")
    @ResponseBody
    public ResultEntity<Menu> updateMenu(Menu menu){
        menuService.updateMenu(menu);
        return ResultEntity.successWithoutData();
    }

    @RequestMapping("/menu/remove.json")
    @ResponseBody
    public ResultEntity<Menu> removeMenu(Integer id){
        menuService.removeMenu(id);
        return ResultEntity.successWithoutData();
    }
}
