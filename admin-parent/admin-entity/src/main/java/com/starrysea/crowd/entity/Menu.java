package com.starrysea.crowd.entity;

import java.util.ArrayList;
import java.util.List;

public class Menu {
    private Integer id;  // 主键

    private Integer pid; // 父节点名称

    private String name; // 节点名称

    private String url; // 节点附带的URL地址，是将来点击菜单项时要跳转的地址

    private String icon; //节点图标的样式

    //存储子节点的集合，初始化是为了避免空指针异常
    private List<Menu> children = new ArrayList<>();  //逆向工程创建后，手工加上的属性，还要添加对应的get、set方法

    // 控制节点是否默认为打开，设置为true表示默认打开
    private Boolean open = true;  //逆向工程创建后，手工加上的属性

    public Menu() {
    }

    public Menu(Integer id, Integer pid, String name, String url, String icon, List<Menu> children, Boolean open) {
        this.id = id;
        this.pid = pid;
        this.name = name;
        this.url = url;
        this.icon = icon;
        this.children = children;
        this.open = open;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", pid=" + pid +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", icon='" + icon + '\'' +
                ", children=" + children +
                ", open=" + open +
                '}';
    }

    public List<Menu> getChildren() {
        return children;
    }

    public void setChildren(List<Menu> children) {
        this.children = children;
    }

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon == null ? null : icon.trim();
    }
}