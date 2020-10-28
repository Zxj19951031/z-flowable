package org.zipper.flowable.app.mapper;

import org.zipper.flowable.app.entity.Role;

import java.util.List;

/**
 * @author zhuxj
 * @since 2020/10/28
 */
public interface RoleMapper {
    /**
     * 查询某个用户所拥有的所有角色信息
     * @param userId 用户编号
     * @return list of roles
     */
    List<Role> selectByUserId(String userId);
}
