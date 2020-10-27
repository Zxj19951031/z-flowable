package org.zipper.flowable.app.dto.parameter;


/**
 * @author zhuxj
 * @since 2020/10/12
 */
public class ProcessSaveParameter {
    /**
     * 记录ID
     */
    private Integer id;
    /**
     * 流程名称
     */
    private String name;
    /**
     * 流程定义bpmn20规范内容
     */
    private String xml;

    /**
     * 流程关联表单编号
     */
    private Integer formId;


    public Integer getFormId() {
        return formId;
    }

    public void setFormId(Integer formId) {
        this.formId = formId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }
}
