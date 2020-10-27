package org.zipper.flowable.app.vo;

import org.springframework.beans.BeanUtils;
import org.zipper.flowable.app.entity.Resource;

import java.util.List;

/**
 * 资源节点
 *
 * @author zhuxj
 * @since 2020/10/23
 */
public class ResourceNode extends Resource {


    public ResourceNode() {
    }

    public ResourceNode(Resource resource) {
        BeanUtils.copyProperties(resource,this);
    }

    /**
     * 子资源
     */
    private List<ResourceNode> childNode;
    /**
     * 按钮权限
     */
    private List<Resource> buttons;


    public List<ResourceNode> getChildNode() {
        return childNode;
    }

    public void setChildNode(List<ResourceNode> childNode) {
        this.childNode = childNode;
    }

    public List<Resource> getButtons() {
        return buttons;
    }

    public void setButtons(List<Resource> buttons) {
        this.buttons = buttons;
    }
}
