package com.starrysea.crowd.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.starrysea.crowd.entity.Admin;
import com.starrysea.crowd.entity.AdminExample;
import com.starrysea.crowd.exception.LoginFailedException;
import com.starrysea.crowd.mapper.AdminMapper;
import com.starrysea.crowd.service.api.AdminService;
import com.starrysea.crowd.util.CrowdConstant;
import com.starrysea.crowd.util.CrowdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public void saveAdmin(Admin admin) {
        adminMapper.insert(admin);
    }

    @Override
    public List<Admin> getAll() {
        return adminMapper.selectByExample(new AdminExample());
    }

    @Override
    public Admin getAdminByLoginAcct(String loginAcct, String userPswd) {

        // 1.根据登录账号使用QBC查询Admin对象
        AdminExample adminExample = new AdminExample();
        AdminExample.Criteria criteria = adminExample.createCriteria();
        criteria.andLoginAcctEqualTo(loginAcct);
        List<Admin> adminList = adminMapper.selectByExample(adminExample);

        if(adminList == null || adminList.size() == 0){
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }

        // 这个判断根据账号查询结果是否返回多个，如果是则抛出运行时异常，这是数据有问题
        // 其实这里最好不这样写，直接在数据库对应字段加入unique去现在账号字段即可，然后注册的时候对账号进行去重
        // 但是由于是后台管理系统，注册账号一般不留对外接口，一般是程序员直接创建账号。
        if(adminList.size() > 1){
            throw new RuntimeException(CrowdConstant.MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE);
        }

        // 2.判断Admin对象是否为null
        Admin admin = adminList.get(0);

        // 3.如果Admin对象为null则抛出异常
        // 这个判断有点多余，因为数据库查询出来之后，如果返回的列表结果没有数据，在上面就已经抛出异常了。所以这里一般取出来不可能为null
        if(admin == null){
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }

        // 4.如果Admin对象不为null，则将数据库中密码从Admin对象中取出
        String userPswdFromDB = admin.getUserPswd();

        // 5.将表单提交的明文密码进行加密
        String md5 = CrowdUtil.md5(userPswd);

        // 6.对密码进行比较，如果比较结果是不一致则抛出异常
        if(!Objects.equals(userPswdFromDB, md5)){
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }

        // 8.如果一致则返回Admin对象
        return admin;
    }

    @Override
    public PageInfo<Admin> getPageInfo(String keyword, Integer pageNum, Integer pageSize) {

        // 1.调用PageHelper的静态方法开启分页功能。设置分页查询的页码和每一页显示的数量，这个设置好后，调用mapper层访问数据库前会调用拦截器拦截查询语句，然后加入在语句中加入limit xx xx内容
        PageHelper.startPage(pageNum, pageSize);
        // 2.执行查询
        List<Admin> adminList = adminMapper.selectAdminByKeyword(keyword);
        // 3.封装到PageInfo对象中
        return new PageInfo<>(adminList);
    }
}
