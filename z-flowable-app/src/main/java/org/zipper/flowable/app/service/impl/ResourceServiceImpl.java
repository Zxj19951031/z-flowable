package org.zipper.flowable.app.service.impl;

import org.springframework.stereotype.Service;
import org.zipper.flowable.app.constant.enums.ResourceType;
import org.zipper.flowable.app.vo.ResourceNode;
import org.zipper.flowable.app.entity.Resource;
import org.zipper.flowable.app.mapper.ResourceMapper;
import org.zipper.flowable.app.service.ResourceService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhuxj
 * @since 2020/10/23
 */
@Service
public class ResourceServiceImpl implements ResourceService {

    @javax.annotation.Resource
    private ResourceMapper resourceMapper;

    @Override
    public List<Resource> getResourceByUserId(int userId) {
        return this.resourceMapper.selectByUserId(userId);
    }

    @Override
    public List<ResourceNode> getResourceNodeByUserId(int userId) {
        List<Resource> resources = getResourceByUserId(userId);

        List<ResourceNode> temps = resources.stream().map(r -> {
            ResourceNode node = new ResourceNode(r);
            node.setButtons(resources.stream()
                    .filter(n -> n.getPid().equals(node.getId()) && ResourceType.FUNCTION.equals(n.getType()))
                    .collect(Collectors.toList()));
            return node;
        }).collect(Collectors.toList());
        List<ResourceNode> nodes = new ArrayList<>();

        for (ResourceNode resource : temps) {
            if (ResourceType.FUNCTION.equals(resource.getType())) {
                continue;
            }
            if (resource.getPid() == 0) {
                nodes.add(resource);
            }

            for (ResourceNode temp : temps) {
                if (ResourceType.FUNCTION.equals(temp.getType())) {
                    continue;
                }
                if (temp.getPid().equals(resource.getId())) {
                    if (resource.getChildNode() == null) {
                        resource.setChildNode(new ArrayList<>());
                    }
                    resource.getChildNode().add(temp);
                }
            }
        }

        return nodes;
    }
}
