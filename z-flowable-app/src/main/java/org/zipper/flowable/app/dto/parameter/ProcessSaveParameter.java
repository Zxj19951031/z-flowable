package org.zipper.flowable.app.dto.parameter;


import java.util.List;

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
    /**
     * 是否任何人可以发起流程
     */
    private Boolean allowAnybody;
    /**
     * 哪些角色可以发起此流程
     */
    private List<String> allowRole;
    /**
     * 哪些人员可以发起此流程
     */
    private List<String> allowMember;
    /**
     * 哪些部门可以发起此流程
     */
    private List<String> allowDept;


    public Boolean getAllowAnybody() {
        if(allowAnybody==null){
            allowAnybody = false;
        }
        return allowAnybody;
    }

    public void setAllowAnybody(Boolean allowAnybody) {
        this.allowAnybody = allowAnybody;
    }

    public List<String> getAllowRole() {
        return allowRole;
    }

    public void setAllowRole(List<String> allowRole) {
        this.allowRole = allowRole;
    }

    public List<String> getAllowMember() {
        return allowMember;
    }

    public void setAllowMember(List<String> allowMember) {
        this.allowMember = allowMember;
    }

    public List<String> getAllowDept() {
        return allowDept;
    }

    public void setAllowDept(List<String> allowDept) {
        this.allowDept = allowDept;
    }

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
