package org.zipper.flowable.app.mapper;

import org.zipper.flowable.app.entity.Resource;

import java.util.List;

/**
 * @author zhuxj
 * @since 2020/10/23
 */
public interface ResourceMapper {
    /**
     * 查询某个用户所有的资源权限
     *
     * @param userId 用户标号
     * @return 资源列表
     */
    public List<Resource> selectByUserId(int userId);
}
