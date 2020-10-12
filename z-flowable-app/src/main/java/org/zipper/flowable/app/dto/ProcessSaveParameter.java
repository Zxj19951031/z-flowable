package org.zipper.flowable.app.dto;

/**
 * @author zhuxj
 * @since 2020/10/12
 */
public class ProcessSaveParameter {
    private Long id;
    private String name;
    private String xml;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
