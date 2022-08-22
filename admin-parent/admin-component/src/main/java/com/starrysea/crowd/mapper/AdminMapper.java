package com.starrysea.crowd.mapper;

import com.starrysea.crowd.entity.Admin;
import com.starrysea.crowd.entity.AdminExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AdminMapper {
    int countByExample(AdminExample example);

    int deleteByExample(AdminExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Admin record);

    int insertSelective(Admin record);

    List<Admin> selectByExample(AdminExample example);

    Admin selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Admin record, @Param("example") AdminExample example);

    int updateByExample(@Param("record") Admin record, @Param("example") AdminExample example);

    int updateByPrimaryKeySelective(Admin record);

    int updateByPrimaryKey(Admin record);

    List<Admin> selectAdminByKeyword(String keyword);

    void deleteRelationship(Integer adminId);

    // 注意这个参数，如果roleIdList没有用@Param("roleIdList")去修饰，那么
    // AdminMapper.xml中的sql语句中的<foreach>标签的collection属性就需要这样写<foreach collection="list"> 指明collection的类型
    // 如果使用@Param注解修饰后，可以使用@Param注解中的value指定collection，如<foreach collection="roleIdList">
    void insertNewRelationship(@Param("adminId") Integer adminId, @Param("roleIdList") List<Integer> roleIdList);
}