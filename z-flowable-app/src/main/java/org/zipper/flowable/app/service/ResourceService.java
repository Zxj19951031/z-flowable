package org.zipper.flowable.app.service;

import org.zipper.flowable.app.vo.ResourceNode;
import org.zipper.flowable.app.entity.Resource;

import java.util.List;

/**
 * 资源服务
 *
 * @author zhuxj
 * @since 2020/10/23
 */
public interface ResourceService {

    /**
     * 查询某个用户所拥有的所有资源权限
     *
     * @param userId 用户编号
     * @return 资源权限列表
     */
    List<Resource> getResourceByUserId(int userId);

    /**
     * 查询某个用户的资源权限树
     * @param userId 用户编号
     * @return 资源权限树
     */
    List<ResourceNode> getResourceNodeByUserId(int userId);
}
